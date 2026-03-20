package az.less.mobile.data.repository

import az.less.mobile.data.datasource.AccountDataSource
import az.less.mobile.data.remote.model.UserProfileData
import az.less.mobile.data.remote.model.account.UpdateUserData
import az.less.mobile.domain.repository.AccountRepository
import az.less.mobile.network.NetworkResult

class AccountRepositoryImpl(
    private val accountDataSource: AccountDataSource
) : AccountRepository {
    override suspend fun getProfile(): NetworkResult<UserProfileData> {
        return accountDataSource.getProfile()
    }

    override suspend fun updateUser(
        userId: String,
        name: String?,
        phone: String?,
        gender: String?,
        birthDay: String?,
        avatar: ByteArray?
    ): NetworkResult<UpdateUserData> {
        return accountDataSource.updateUser(userId, name, phone, gender, birthDay, avatar)
    }

    override suspend fun deleteProfile(): NetworkResult<Unit> {
        return accountDataSource.deleteProfile()
    }
}
