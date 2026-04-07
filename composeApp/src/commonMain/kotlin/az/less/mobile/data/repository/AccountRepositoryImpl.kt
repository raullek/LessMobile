package az.less.mobile.data.repository

import az.less.mobile.data.datasource.AccountDataSource
import az.less.mobile.data.remote.model.UserProfileData
import az.less.mobile.data.remote.model.UserProfileUser
import az.less.mobile.data.remote.model.account.UpdateUserRequest
import az.less.mobile.domain.repository.AccountRepository
import az.less.mobile.network.NetworkResult

class AccountRepositoryImpl(
    private val accountDataSource: AccountDataSource
) : AccountRepository {
    override suspend fun getProfile(): NetworkResult<UserProfileData> {
        return accountDataSource.getProfile()
    }

    override suspend fun updateUser(request: UpdateUserRequest): NetworkResult<UserProfileUser> {
        return accountDataSource.updateUser(request)
    }

    override suspend fun deleteProfile(): NetworkResult<Unit> {
        return accountDataSource.deleteProfile()
    }
}
