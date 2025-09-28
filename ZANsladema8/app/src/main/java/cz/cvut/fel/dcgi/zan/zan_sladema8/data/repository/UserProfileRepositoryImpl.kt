package cz.cvut.fel.dcgi.zan.zan_sladema8.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.UserProfile
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.WorkoutType
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.navigation.toDayOfWeekSet
import cz.cvut.fel.dcgi.zan.zan_sladema8.ui.navigation.toWorkoutTypeSet
import kotlinx.coroutines.flow.firstOrNull

class UserProfileRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : UserProfileRepository {
    override suspend fun saveUserProfile(profile: UserProfile) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME] = profile.name
            preferences[PreferencesKeys.USER_SURNAME] = profile.surname
            preferences[PreferencesKeys.EXERCISE_DAYS] = profile.exerciseDays.joinToString(",") { it.name }
            preferences[PreferencesKeys.WORKOUT_TYPES] = profile.workoutTypes.joinToString(",") { it.name }
        }
    }

    override suspend fun getUserProfile(): UserProfile? {
        val preferences = dataStore.data.firstOrNull() ?: return null

        return UserProfile(
            name = preferences[PreferencesKeys.USER_NAME] ?: "",
            surname = preferences[PreferencesKeys.USER_SURNAME] ?: "",
            exerciseDays = preferences[PreferencesKeys.EXERCISE_DAYS]?.toDayOfWeekSet() ?: emptySet(),
            workoutTypes = preferences[PreferencesKeys.WORKOUT_TYPES]?.toWorkoutTypeSet()
                ?: setOf(WorkoutType.LEGS, WorkoutType.ARMS)
        )
    }
}

private object PreferencesKeys {
    val USER_NAME = stringPreferencesKey("user_name")
    val USER_SURNAME = stringPreferencesKey("user_surname")
    val EXERCISE_DAYS = stringPreferencesKey("exercise_days")
    val WORKOUT_TYPES = stringPreferencesKey("workout_types")
}