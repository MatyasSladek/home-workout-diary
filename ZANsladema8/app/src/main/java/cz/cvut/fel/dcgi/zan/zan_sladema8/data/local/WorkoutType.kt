package cz.cvut.fel.dcgi.zan.zan_sladema8.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDate
import org.threeten.bp.DayOfWeek

enum class WorkoutType(val displayName: String, val exercises: List<String>) {
    LEGS("Legs", listOf("Squats", "Lunges", "Leg Press", "Calf Raises")),
    BELLY("Belly", listOf("Sit-ups", "Crunches", "Plank", "Mountain Climbers")),
    BACK("Back", listOf("Superman", "Pull-ups", "Rows", "Deadlifts")),
    ARMS("Arms", listOf("Push-ups", "Bicep Curls", "Tricep Dips", "Shoulder Press"))
}

@Entity(tableName = "workout_preferences")
data class WorkoutPreferences(
    @PrimaryKey
    val id: Int = 1,
    val selectedDays: String = "",
    val workoutTypes: String = "",
    val lastWorkoutRotation: Int = 0
)

@Entity(tableName = "completed_workouts")
data class CompletedWorkout(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val date: LocalDate,
    val workoutType: WorkoutType,
    val exercises: String,
    val duration: Int = 0,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

fun String.toDayOfWeekList(): List<DayOfWeek> =
    split(",").mapNotNull { runCatching { DayOfWeek.valueOf(it.trim()) }.getOrNull() }

fun String.toWorkoutTypeList(): List<WorkoutType> =
    split(",").mapNotNull { runCatching { WorkoutType.valueOf(it.trim()) }.getOrNull() }