package cz.cvut.fel.dcgi.zan.zan_sladema8.data.repository

import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.CompletedWorkout
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.WorkoutPreferences
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.WorkoutSuggestion
import org.threeten.bp.LocalDate

interface WorkoutRepository {
    suspend fun getWorkoutPreferences(): WorkoutPreferences
    suspend fun saveWorkoutPreferences(preferences: WorkoutPreferences)
    suspend fun getCompletedWorkouts(): List<CompletedWorkout>
    suspend fun getCompletedWorkoutsForDate(date: LocalDate): List<CompletedWorkout>
    suspend fun addCompletedWorkout(workout: CompletedWorkout)
    suspend fun updateCompletedWorkout(workout: CompletedWorkout)
    suspend fun deleteCompletedWorkout(id: String)
    suspend fun getWorkoutSuggestionsForWeek(weekStart: LocalDate): List<WorkoutSuggestion>
}