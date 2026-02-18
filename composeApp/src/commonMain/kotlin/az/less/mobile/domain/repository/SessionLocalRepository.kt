package az.less.mobile.domain.repository

import az.less.mobile.domain.model.auth.User
import kotlinx.coroutines.flow.Flow

interface SessionLocalRepository {
    val currentUser: Flow<User?>
    val isLoggedIn: Flow<Boolean>
    val accessToken: Flow<String?>
    val refreshToken: Flow<String?>
    suspend fun saveSession(user: User, accessToken: String, refreshToken: String)
    suspend fun clearSession()
}
