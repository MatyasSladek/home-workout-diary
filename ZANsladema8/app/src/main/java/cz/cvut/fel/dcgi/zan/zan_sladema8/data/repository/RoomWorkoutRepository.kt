package cz.cvut.fel.dcgi.zan.zan_sladema8.data.repository

import cz.cvut.fel.dcgi.zan.zan_sladema8.data.db.WorkoutDao
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.CompletedWorkout
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.WorkoutPreferences
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.WorkoutSuggestion
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.toDayOfWeekList
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.toWorkoutTypeList
import org.threeten.bp.LocalDate

class RoomWorkoutRepository(
    private val workoutDao: WorkoutDao
) : WorkoutRepository {

    override suspend fun getWorkoutPreferences(): WorkoutPreferences {
        return workoutDao.getWorkoutPreferences() ?: WorkoutPreferences()
    }

    override suspend fun saveWorkoutPreferences(preferences: WorkoutPreferences) {
        workoutDao.insertWorkoutPreferences(preferences)
    }

    override suspend fun getCompletedWorkouts(): List<CompletedWorkout> {
        return workoutDao.getAllCompletedWorkouts()
    }

    override suspend fun getCompletedWorkoutsForDate(date: LocalDate): List<CompletedWorkout> {
        return workoutDao.getCompletedWorkoutsForDate(date)
    }

    override suspend fun addCompletedWorkout(workout: CompletedWorkout) {
        workoutDao.insertCompletedWorkout(workout)
    }

    override suspend fun updateCompletedWorkout(workout: CompletedWorkout) {
        workoutDao.updateCompletedWorkout(workout)
    }

    override suspend fun deleteCompletedWorkout(id: String) {
        workoutDao.deleteCompletedWorkout(id)
    }

    override suspend fun getWorkoutSuggestionsForWeek(weekStart: LocalDate): List<WorkoutSuggestion> {
        val preferences = getWorkoutPreferences()

        if (preferences.selectedDays.isBlank() || preferences.workoutTypes.isBlank()) {
            return emptyList()
        }

        val selectedDays = preferences.selectedDays.toDayOfWeekList()
        val workoutTypes = preferences.workoutTypes.toWorkoutTypeList()

        if (selectedDays.isEmpty() || workoutTypes.isEmpty()) return emptyList()

        val allDatesInWeek = (0..6).map { weekStart.plusDays(it.toLong()) }
        val targetDates = allDatesInWeek.filter { it.dayOfWeek in selectedDays }

        val completedWorkouts = workoutDao.getCompletedWorkoutsInRange(
            weekStart,
            weekStart.plusDays(6)
        )

        val suggestions = mutableListOf<WorkoutSuggestion>()
        var rotationIndex = preferences.lastWorkoutRotation

        for (date in targetDates) {
            val completed = completedWorkouts.find { it.date == date }
            val type = completed?.workoutType ?: workoutTypes[rotationIndex % workoutTypes.size]

            suggestions.add(
                WorkoutSuggestion(
                    date = date,
                    workoutType = type,
                    isCompleted = completed != null,
                    completedWorkout = completed
                )
            )

            if (completed == null) {
                rotationIndex++
            }
        }

        val updatedPrefs = preferences.copy(lastWorkoutRotation = rotationIndex % workoutTypes.size)
        workoutDao.updateWorkoutPreferences(updatedPrefs)

        return suggestions
    }
}