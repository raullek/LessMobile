package az.less.mobile.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import az.less.mobile.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repository for managing user data with DataStore
 */
class UserRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val USER_ID = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_AVATAR_URL = stringPreferencesKey("user_avatar_url")
        val USER_CO2_SAVED = stringPreferencesKey("user_co2_saved")
        val USER_MONEY_SAVED = stringPreferencesKey("user_money_saved")
    }

    /**
     * Get current user as Flow
     * Returns null if user is not logged in
     */
    val currentUser: Flow<User?> = dataStore.data.map { preferences ->
        val id = preferences[USER_ID]
        val name = preferences[USER_NAME]
        val email = preferences[USER_EMAIL]

        if (id != null && name != null && email != null) {
            User(
                id = id,
                name = name,
                email = email,
                avatarUrl = preferences[USER_AVATAR_URL],
                co2Saved = preferences[USER_CO2_SAVED] ?: "0 kg",
                moneySaved = preferences[USER_MONEY_SAVED] ?: "$0"
            )
        } else {
            null
        }
    }

    /**
     * Check if user is logged in
     */
    val isLoggedIn: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[USER_ID] != null
    }

    /**
     * Save user to DataStore
     */
    suspend fun saveUser(user: User) {
        dataStore.edit { preferences ->
            preferences[USER_ID] = user.id
            preferences[USER_NAME] = user.name
            preferences[USER_EMAIL] = user.email
            user.avatarUrl?.let { preferences[USER_AVATAR_URL] = it }
            preferences[USER_CO2_SAVED] = user.co2Saved
            preferences[USER_MONEY_SAVED] = user.moneySaved
        }
    }

    /**
     * Clear user data (logout)
     */
    suspend fun clearUser() {
        dataStore.edit { preferences ->
            preferences.remove(USER_ID)
            preferences.remove(USER_NAME)
            preferences.remove(USER_EMAIL)
            preferences.remove(USER_AVATAR_URL)
            preferences.remove(USER_CO2_SAVED)
            preferences.remove(USER_MONEY_SAVED)
        }
    }
}
