package az.less.mobile.di

import az.less.mobile.data.datasource.AuthDataSource
import az.less.mobile.data.datasource.OffersDataSource
import az.less.mobile.data.repository.AuthorizationRepositoryImpl
import az.less.mobile.data.repository.OffersRepositoryImpl
import az.less.mobile.domain.repository.AuthorizationRepository
import az.less.mobile.domain.repository.OffersRepository
import org.koin.dsl.module

val dataModule = module {
    // DataSources
    single { AuthDataSource(get()) }
    single { OffersDataSource(get()) }

    // Repositories
    single<AuthorizationRepository> { AuthorizationRepositoryImpl(get()) }
    single<OffersRepository> { OffersRepositoryImpl(get()) }
}
