package az.less.mobile.domain.usecase

import az.less.mobile.data.remote.model.mapper.toUser
import az.less.mobile.domain.model.auth.User
import az.less.mobile.domain.repository.AccountRepository
import az.less.mobile.domain.repository.SessionLocalRepository
import az.less.mobile.network.NetworkResult

/**
 * Fetches fresh profile from the server and updates the cached user.
 *
 * Returns null when there is no session (no access token) — caller must treat
 * null as "skipped, not logged in" and avoid showing errors. Otherwise returns
 * the NetworkResult from the API call.
 */
class RefreshUserProfileUseCase(
    private val accountRepository: AccountRepository,
    private val sessionLocalRepository: SessionLocalRepository
) {
    suspend operator fun invoke(): NetworkResult<User>? {
        if (sessionLocalRepository.getAccessToken() == null) return null

        return accountRepository.getProfile()
            .onSuccess { profileData ->
                sessionLocalRepository.updateUser(profileData.toUser())
            }
            .map { it.toUser() }
    }
}
