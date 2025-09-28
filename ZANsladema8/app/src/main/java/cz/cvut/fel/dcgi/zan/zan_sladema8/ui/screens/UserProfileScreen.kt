package cz.cvut.fel.dcgi.zan.zan_sladema8.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.cvut.fel.dcgi.zan.zan_sladema8.R
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.UserProfile
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.components.DaySelector
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.components.MainBottomNavigation
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.components.ProfileImage
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.components.WorkoutTypeDisplay
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.navigation.BottomNavigationItem
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.theme.DiaryAppTheme

@Composable
fun UserProfileScreen(
    mainBottomNavigationItems: List<BottomNavigationItem>,
    currentRoute: String?,
    userProfile: UserProfile,
    startEditing: () -> Unit,
) {
    Scaffold(
        topBar = { UserProfileAppBar(startEditing) },
        bottomBar = { MainBottomNavigation(mainBottomNavigationItems, currentRoute) },
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        UserProfileContent(
            userProfile,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileAppBar(
    startEditing: () -> Unit,
    logout: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(text = "User Profile")
        },
        actions = {
            IconButton(
                onClick = startEditing
            ) {
                Icon(
                    painter = painterResource(R.drawable.edit_24px),
                    contentDescription = "Edit user profile button",
                )
            }
            IconButton(
                onClick = logout
            ) {
                Icon(
                    painter = painterResource(R.drawable.logout_24px),
                    contentDescription = "Edit user profile button",
                )
            }
        },
    )
}

@Composable
fun UserProfileContent(
    userProfile: UserProfile,
    modifier: Modifier = Modifier
) {
    val scrollableColumnState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(2.dp)
            .verticalScroll(scrollableColumnState),
    ) {
        ProfileImage()

        Text(
            text = "${userProfile.name} ${userProfile.surname}",
            style = MaterialTheme.typography.displayMedium,
            modifier = Modifier.padding(top = 32.dp),
        )

        Text(
            text = "Exercise Days",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )

        DaySelector(selectedDays = userProfile.exerciseDays)

        Text(
            text = "Workout Types",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )

        WorkoutTypeDisplay(workoutTypes = userProfile.workoutTypes)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun UserProfileScreenPreview() {
    DiaryAppTheme {
        UserProfileScreen(
            mainBottomNavigationItems = emptyList(),
            currentRoute = "",
            UserProfile(),
            startEditing = {},
        )
    }
}