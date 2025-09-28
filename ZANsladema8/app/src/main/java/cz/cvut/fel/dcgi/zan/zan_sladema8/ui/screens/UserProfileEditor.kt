package cz.cvut.fel.dcgi.zan.zan_sladema8.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.cvut.fel.dcgi.zan.zan_sladema8.R
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.UserProfile
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.WorkoutPreferences
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.components.DaySelector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.components.ProfileImage
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.components.UserProfileTextField
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.components.WorkoutTypeSelector
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.theme.DiaryAppTheme
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.viewmodels.CalendarViewModel
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.viewmodels.UserProfileEditorEvent
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.viewmodels.UserProfileEditorViewModel
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.viewmodels.UserProfileUiState

@Composable
fun UserProfileEditorScreen(
    saveUserProfile: (UserProfile) -> Unit,
    cancelEditing: () -> Unit,
    viewModel: UserProfileEditorViewModel = viewModel(),
    calendarViewModel: CalendarViewModel = viewModel(),
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        calendarViewModel.loadWorkouts()
    }

    Scaffold(
        topBar = {
            UserProfileEditorAppBar(
                cancelEditing = cancelEditing,
                saveUserProfile = {
                    val profile = UserProfile(
                        name = userProfile.name,
                        surname = userProfile.surname,
                        exerciseDays = userProfile.exerciseDays,
                        workoutTypes = userProfile.workoutTypes
                    )

                    val daysString = userProfile.exerciseDays.joinToString(",") { it.name }
                    val typesString = userProfile.workoutTypes.joinToString(",") { it.name }

                    calendarViewModel.updateWorkoutPreferences(
                        WorkoutPreferences(
                            selectedDays = daysString,
                            workoutTypes = typesString,
                            lastWorkoutRotation = 0
                        )
                    )

                    saveUserProfile(profile)
                }
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        UserProfileEditorContent(
            userProfile,
            { event -> viewModel.onEvent(event) },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileEditorAppBar(
    cancelEditing: () -> Unit,
    saveUserProfile: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(text = "Edit Profile")
        },
        navigationIcon = {
            IconButton(
                onClick = cancelEditing
            ) {
                Icon(
                    Icons.Filled.Close,
                    contentDescription = "Navigation back",
                )
            }
        },
        actions = {
            IconButton(
                onClick = saveUserProfile
            ) {
                Icon(
                    Icons.Filled.Done,
                    contentDescription = "Save user profile",
                )
            }
        },
    )
}

@Composable
fun UserProfileEditorContent(
    userProfile: UserProfileUiState,
    onScreenEvent: (UserProfileEditorEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val textFieldModifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth()
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
    ) {
        ProfileImage()

        UserProfileTextField(
            value = userProfile.name,
            onValueChange = { onScreenEvent(UserProfileEditorEvent.NameChanged(it)) },
            label = "Name",
            icon = ImageVector.vectorResource(R.drawable.cancel_24px),
            modifier = textFieldModifier,
        )

        UserProfileTextField(
            value = userProfile.surname,
            onValueChange = { onScreenEvent(UserProfileEditorEvent.SurnameChanged(it)) },
            label = "Surname",
            icon = ImageVector.vectorResource(R.drawable.cancel_24px),
            modifier = textFieldModifier,
        )

        Text(
            "Select Exercise Days",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )

        DaySelector(
            selectedDays = userProfile.exerciseDays,
            onDayToggle = { newDays ->
                onScreenEvent(UserProfileEditorEvent.ExerciseDaysChanged(newDays))
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Select Workout Types",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        WorkoutTypeSelector(
            selectedWorkoutTypes = userProfile.workoutTypes,
            onWorkoutTypesChanged = { newTypes ->
                onScreenEvent(UserProfileEditorEvent.WorkoutTypesChanged(newTypes))
            }
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun UserProfileEditorScreenPreview() {
    DiaryAppTheme {
        UserProfileEditorScreen(saveUserProfile = {}, cancelEditing = {})
    }
}