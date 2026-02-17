package az.less.mobile.di

import az.less.mobile.data.datasource.AuthDataSource
import az.less.mobile.data.repository.AuthorizationRepositoryImpl
import az.less.mobile.domain.repository.AuthorizationRepository
import org.koin.dsl.module

val dataModule = module {
    // DataSources
    single { AuthDataSource(get()) }

    // Repositories
    single<AuthorizationRepository> { AuthorizationRepositoryImpl(get()) }
}
