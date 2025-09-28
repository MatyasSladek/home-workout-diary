package cz.cvut.fel.dcgi.zan.zan_sladema8.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cz.cvut.fel.dcgi.zan.zan_sladema8.AppContainer
import cz.cvut.fel.dcgi.zan.zan_sladema8.R
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.UserProfile
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.WorkoutType
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.screens.CalendarScreen
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.screens.PermissionScreen
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.screens.Splashscreen
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.screens.UserProfileEditorScreen
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.screens.UserProfileScreen
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.screens.WeatherDashboardScreen
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.viewmodels.CalendarViewModel
import kotlinx.coroutines.launch

@Composable
fun AppRouter() {
    val navController = rememberNavController()

    MainAppRouter(
        navController = navController,
    )
}

@Composable
fun MainAppRouter(navController: NavHostController) {
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val coroutineScope = rememberCoroutineScope()

    val mainBottomNavigationItems = remember {
        listOf(
            BottomNavigationItem(
                route = Routes.Dashboard::class.qualifiedName.toString(),
                label = Routes.Dashboard.route,
                iconId = R.drawable.dashboard_24,
                contentDescription = "Diary nav bar item",
                onClick = {
                    navigateToBottomNavItem(navController, Routes.Dashboard)
                }
            ),
            BottomNavigationItem(
                route = Routes.Calendar::class.qualifiedName.toString(),
                label = Routes.Calendar.route,
                iconId = R.drawable.calendar_month_24px,
                contentDescription = "Calendar nav bar item",
                onClick = {
                    navigateToBottomNavItem(navController, Routes.Calendar)
                }
            ),
            BottomNavigationItem(
                route = Routes.UserProfile::class.qualifiedName.toString(),
                label = Routes.UserProfile.route,
                iconId = R.drawable.person_24px,
                contentDescription = "User profile nav bar item",
                onClick = {
                    navigateToBottomNavItem(navController, Routes.UserProfile)
                }
            ),
            BottomNavigationItem(
                route = Routes.Permissions::class.qualifiedName.toString(),
                label = Routes.Permissions.route,
                iconId = R.drawable.outline_build_24,
                contentDescription = "Permissions nav bar item",
                onClick = {
                    navigateToBottomNavItem(navController, Routes.Permissions)
                }
            ),
        )
    }

    var userProfile by remember {
        mutableStateOf(
            UserProfile(
                name = "",
                surname = "",
                exerciseDays = emptySet(),
                workoutTypes = setOf(WorkoutType.LEGS, WorkoutType.ARMS)
            )
        )
    }

    LaunchedEffect(Unit) {
        val savedProfile = AppContainer.userProfileRepository.getUserProfile()
        if (savedProfile != null) {
            userProfile = savedProfile
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.Splashscreen
    ) {
        composable<Routes.Splashscreen> {
            Splashscreen(
                onNavigate = {
                    navController.navigate(Routes.Dashboard) {
                        popUpTo(Routes.Splashscreen) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<Routes.Calendar> {
            val calendarViewModel: CalendarViewModel = viewModel()
            CalendarScreen(
                viewModel = calendarViewModel,
                mainBottomNavigationItems = mainBottomNavigationItems,
                currentRoute = currentBackStackEntry.value?.destination?.route
            )
        }

        composable<Routes.Permissions> {
            PermissionScreen(
                mainBottomNavigationItems = mainBottomNavigationItems,
                currentDestination = currentBackStackEntry.value?.destination?.route,
                onPermissionsGranted = {
                    navController.navigate(Routes.Dashboard) {
                        popUpTo(Routes.Permissions) {
                            inclusive = true
                        }
                    }
                },
                onRequestNotificationPermission = { /* TODO: Implement this */ },
                onRequestAlarmPermission = { /* TODO: Implement this */ }
            )
        }

        composable<Routes.Dashboard> {
            WeatherDashboardScreen(
                mainBottomNavigationItems,
                currentBackStackEntry.value?.destination?.route,
            )
        }

        composable<Routes.UserProfile> {
            UserProfileScreen(
                mainBottomNavigationItems,
                currentBackStackEntry.value?.destination?.route,
                userProfile
            ) {
                navController.navigate(Routes.UserProfileEditor(
                    userProfile.name,
                    userProfile.surname,
                    userProfile.exerciseDays.toSerializableString(),
                    userProfile.workoutTypes.toSerializableString()
                ))
            }
        }

        composable<Routes.UserProfileEditor> {
            UserProfileEditorScreen(
                saveUserProfile = { newProfile ->
                    coroutineScope.launch {
                        AppContainer.userProfileRepository.saveUserProfile(newProfile)
                        userProfile = newProfile
                    }
                    navController.popBackStack()
                },
                cancelEditing = {
                    navController.popBackStack()
                }
            )
        }
    }
}

fun navigateToBottomNavItem(navController: NavHostController, route: Routes) {
    navController.navigate(route) {
        popUpTo(navController.graph.startDestinationId) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}