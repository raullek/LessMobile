package az.less.mobile.domain.repository

import az.less.mobile.domain.model.auth.AppMode
import az.less.mobile.domain.model.auth.User
import kotlinx.coroutines.flow.Flow

interface SessionLocalRepository {
    val currentUser: Flow<User?>
    val isLoggedIn: Flow<Boolean>
    val accessToken: Flow<String?>
    val refreshToken: Flow<String?>
    val lastUsedMode: Flow<AppMode>
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun saveSession(user: User, accessToken: String, refreshToken: String)
    suspend fun updateUser(user: User)
    suspend fun updateTokens(accessToken: String, refreshToken: String)
    suspend fun saveLastUsedMode(mode: AppMode)
    suspend fun clearSession()
}
