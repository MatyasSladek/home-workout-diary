package cz.cvut.fel.dcgi.zan.zan_sladema8

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.repository.UserProfileRepository
import cz.cvut.fel.dcgi.zan.zan_sladema8.data.repository.UserProfileRepositoryImpl

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

object AppContainer {

    lateinit var userProfileRepository: UserProfileRepository
        private set

    fun init(context: Context) {
        userProfileRepository = UserProfileRepositoryImpl(context.dataStore)
    }
}