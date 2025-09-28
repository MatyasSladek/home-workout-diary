package cz.cvut.fel.dcgi.zan.zan_sladema8.data.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import org.threeten.bp.DayOfWeek

@Parcelize
@Serializable
data class UserProfile(
    val name: String = "",
    val surname: String = "",
    val exerciseDays: Set<DayOfWeek> = emptySet(),
    val workoutTypes: Set<WorkoutType> = emptySet()
) : Parcelable