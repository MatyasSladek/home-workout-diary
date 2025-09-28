package cz.cvut.fel.dcgi.zan.zan_sladema8.ui.navigation

import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.WorkoutType
import org.threeten.bp.DayOfWeek
import kotlinx.serialization.Serializable

@Serializable
sealed class Routes(val route: String) {
    @Serializable
    data object Permissions : Routes("Permissions")
    @Serializable
    data object Dashboard : Routes("Dashboard")
    @Serializable
    data object UserProfile : Routes("UserProfile")
    @Serializable
    data object Splashscreen : Routes("Splashscreen")
    @Serializable
    data object Calendar : Routes("Calendar")
    @Serializable
    data class UserProfileEditor(
        val name: String,
        val surname: String,
        val exerciseDaysString: String = "",
        val workoutTypesString: String = ""
    ) : Routes("UserProfileEditor")
}

fun <T : Enum<T>> Set<T>.toSerializableString(): String {
    return this.joinToString(",") { it.name }
}

fun String.toDayOfWeekSet(): Set<DayOfWeek> {
    return if (this.isEmpty()) {
        emptySet()
    } else {
        this.split(",").mapNotNull { dayName ->
            try {
                DayOfWeek.valueOf(dayName.trim())
            } catch (_: IllegalArgumentException) {
                null
            }
        }.toSet()
    }
}

fun String.toWorkoutTypeSet(): Set<WorkoutType> {
    return if (this.isEmpty()) {
        emptySet()
    } else {
        this.split(",").mapNotNull { typeName ->
            try {
                WorkoutType.valueOf(typeName.trim())
            } catch (_: IllegalArgumentException) {
                null
            }
        }.toSet()
    }
}