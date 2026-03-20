package az.less.mobile.domain.repository

import az.less.mobile.data.remote.model.UserProfileData
import az.less.mobile.data.remote.model.account.UpdateUserData
import az.less.mobile.network.NetworkResult

interface AccountRepository {
    suspend fun getProfile(): NetworkResult<UserProfileData>
    suspend fun updateUser(
        userId: String,
        name: String?,
        phone: String?,
        gender: String?,
        birthDay: String?,
        avatar: ByteArray?
    ): NetworkResult<UpdateUserData>
    suspend fun deleteProfile(): NetworkResult<Unit>
}
