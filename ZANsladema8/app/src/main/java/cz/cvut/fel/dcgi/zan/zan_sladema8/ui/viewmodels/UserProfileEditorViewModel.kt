package cz.cvut.fel.dcgi.zan.zan_sladema8.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.WorkoutType
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.navigation.Routes
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.navigation.toDayOfWeekSet
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.navigation.toWorkoutTypeSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.threeten.bp.DayOfWeek

data class UserProfileUiState(
    val name: String,
    val surname: String,
    val exerciseDays: Set<DayOfWeek>,
    val workoutTypes: Set<WorkoutType>
)

sealed class UserProfileEditorEvent {
    data class NameChanged(val newName: String) : UserProfileEditorEvent()
    data class SurnameChanged(val newSurname: String) : UserProfileEditorEvent()
    data class ExerciseDaysChanged(val newExerciseDays: Set<DayOfWeek>) : UserProfileEditorEvent()
    data class WorkoutTypesChanged(val newWorkoutTypes: Set<WorkoutType>) : UserProfileEditorEvent()
}

class UserProfileEditorViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val route: Routes.UserProfileEditor = savedStateHandle.toRoute()

    private val _userProfile = MutableStateFlow(
        UserProfileUiState(
            name = route.name,
            surname = route.surname,
            exerciseDays = route.exerciseDaysString.toDayOfWeekSet(),
            workoutTypes = route.workoutTypesString.toWorkoutTypeSet()
        )
    )

    val userProfile = _userProfile.asStateFlow()

    fun onEvent(event: UserProfileEditorEvent) {
        when (event) {
            is UserProfileEditorEvent.NameChanged -> {
                _userProfile.value = _userProfile.value.copy(
                    name = event.newName
                )
            }
            is UserProfileEditorEvent.SurnameChanged -> {
                _userProfile.value = _userProfile.value.copy(
                    surname = event.newSurname
                )
            }
            is UserProfileEditorEvent.ExerciseDaysChanged -> {
                _userProfile.value = _userProfile.value.copy(
                    exerciseDays = event.newExerciseDays
                )
            }
            is UserProfileEditorEvent.WorkoutTypesChanged -> {
                _userProfile.value = _userProfile.value.copy(
                    workoutTypes = event.newWorkoutTypes
                )
            }
        }
    }
}
