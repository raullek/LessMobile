package az.less.mobile.di

import az.less.mobile.data.datasource.AuthDataSource
import az.less.mobile.data.datasource.FavoritesDataSource
import az.less.mobile.data.datasource.OffersDataSource
import az.less.mobile.data.datasource.OrdersDataSource
import az.less.mobile.data.repository.AuthorizationRepositoryImpl
import az.less.mobile.data.repository.FavoritesRepositoryImpl
import az.less.mobile.data.repository.OffersRepositoryImpl
import az.less.mobile.data.repository.OrdersRepositoryImpl
import az.less.mobile.domain.repository.AuthorizationRepository
import az.less.mobile.domain.repository.FavoritesRepository
import az.less.mobile.domain.repository.OffersRepository
import az.less.mobile.domain.repository.OrdersRepository
import org.koin.dsl.module

val dataModule = module {
    // DataSources
    single { AuthDataSource(get()) }
    single { OffersDataSource(get()) }
    single { FavoritesDataSource(get()) }
    single { OrdersDataSource(get()) }

    // Repositories
    single<AuthorizationRepository> { AuthorizationRepositoryImpl(get()) }
    single<OffersRepository> { OffersRepositoryImpl(get()) }
    single<FavoritesRepository> { FavoritesRepositoryImpl(get()) }
    single<OrdersRepository> { OrdersRepositoryImpl(get()) }
}
