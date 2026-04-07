package az.less.mobile.domain.repository

import az.less.mobile.data.remote.model.UserProfileData
import az.less.mobile.data.remote.model.UserProfileUser
import az.less.mobile.data.remote.model.account.UpdateUserRequest
import az.less.mobile.network.NetworkResult

interface AccountRepository {
    suspend fun getProfile(): NetworkResult<UserProfileData>
    suspend fun updateUser(request: UpdateUserRequest): NetworkResult<UserProfileUser>
    suspend fun deleteProfile(): NetworkResult<Unit>
}
