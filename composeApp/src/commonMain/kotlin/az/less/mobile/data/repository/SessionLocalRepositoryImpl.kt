package az.less.mobile.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import az.less.mobile.domain.model.auth.User
import az.less.mobile.data.model.UserEntity
import az.less.mobile.domain.repository.SessionLocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class SessionLocalRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val json: Json
) : SessionLocalRepository {

    private companion object {
        val USER_JSON = stringPreferencesKey("user_json")
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    override val currentUser: Flow<User?> = dataStore.data.map { preferences ->
        preferences[USER_JSON]?.let { jsonString ->
            try {
                json.decodeFromString<UserEntity>(jsonString).toUser()
            } catch (_: Exception) {
                null
            }
        }
    }

    override val isLoggedIn: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN] != null
    }

    override val accessToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[ACCESS_TOKEN]
    }

    override val refreshToken: Flow<String?> = dataStore.data.map { preferences ->
        preferences[REFRESH_TOKEN]
    }

    override suspend fun getAccessToken(): String? {
        return dataStore.data.first()[ACCESS_TOKEN]
    }

    override suspend fun getRefreshToken(): String? {
        return dataStore.data.first()[REFRESH_TOKEN]
    }

    override suspend fun saveSession(user: User, accessToken: String, refreshToken: String) {
        val entity = UserEntity(
            id = user.id,
            name = user.name,
            email = user.email,
            roles = user.roles,
            status = user.status,
            avatarUrl = user.avatarUrl,
            phone = user.phone,
            gender = user.gender,
            birthDay = user.birthDay
        )
        dataStore.edit { preferences ->
            preferences[USER_JSON] = json.encodeToString(UserEntity.serializer(), entity)
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

    override suspend fun updateUser(user: User) {
        val entity = UserEntity(
            id = user.id,
            name = user.name,
            email = user.email,
            roles = user.roles,
            status = user.status,
            avatarUrl = user.avatarUrl,
            phone = user.phone,
            gender = user.gender,
            birthDay = user.birthDay
        )
        dataStore.edit { preferences ->
            preferences[USER_JSON] = json.encodeToString(UserEntity.serializer(), entity)
        }
    }

    override suspend fun updateTokens(accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

    override suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.remove(USER_JSON)
            preferences.remove(ACCESS_TOKEN)
            preferences.remove(REFRESH_TOKEN)
        }
    }

}
