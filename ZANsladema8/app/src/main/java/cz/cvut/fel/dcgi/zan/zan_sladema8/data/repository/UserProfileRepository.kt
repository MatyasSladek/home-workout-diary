package cz.cvut.fel.dcgi.zan.zan_sladema8.data.repository

import cz.cvut.fel.dcgi.zan.zan_sladema8.data.local.UserProfile

interface UserProfileRepository {
    suspend fun saveUserProfile(profile: UserProfile)
    suspend fun getUserProfile(): UserProfile?
}