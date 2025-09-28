package cz.cvut.fel.dcgi.zan.zan_sladema8.data.local

import org.threeten.bp.LocalDate

data class WorkoutSuggestion(
    val date: LocalDate,
    val workoutType: WorkoutType,
    val isCompleted: Boolean = false,
    val completedWorkout: CompletedWorkout? = null
)