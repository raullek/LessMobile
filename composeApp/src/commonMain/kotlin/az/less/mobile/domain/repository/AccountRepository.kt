package az.less.mobile.domain.repository

import az.less.mobile.data.remote.model.account.UpdateUserData
import az.less.mobile.data.remote.model.account.UpdateUserRequest
import az.less.mobile.network.NetworkResult

interface AccountRepository {
    suspend fun updateUser(userId: String, request: UpdateUserRequest): NetworkResult<UpdateUserData>
    suspend fun deleteProfile(): NetworkResult<Unit>
}
