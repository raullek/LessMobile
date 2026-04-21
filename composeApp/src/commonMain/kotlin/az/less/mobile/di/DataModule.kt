package az.less.mobile.di

import az.less.mobile.data.datasource.AccountDataSource
import az.less.mobile.data.datasource.AuthDataSource
import az.less.mobile.data.datasource.ContentDataSource
import az.less.mobile.data.datasource.ExploreDataSource
import az.less.mobile.data.datasource.FavoritesDataSource
import az.less.mobile.data.datasource.MerchantDataSource
import az.less.mobile.data.datasource.OffersDataSource
import az.less.mobile.data.datasource.OrdersDataSource
import az.less.mobile.data.datasource.VenuesDataSource
import az.less.mobile.data.datasource.VouchersDataSource
import az.less.mobile.data.repository.AccountRepositoryImpl
import az.less.mobile.data.repository.AuthorizationRepositoryImpl
import az.less.mobile.data.repository.ContentRepositoryImpl
import az.less.mobile.data.repository.ExploreRepositoryImpl
import az.less.mobile.data.repository.FavoritesRepositoryImpl
import az.less.mobile.data.repository.MerchantRepositoryImpl
import az.less.mobile.data.repository.OffersRepositoryImpl
import az.less.mobile.data.repository.OrdersRepositoryImpl
import az.less.mobile.data.repository.VenuesRepositoryImpl
import az.less.mobile.data.repository.VouchersRepositoryImpl
import az.less.mobile.domain.repository.AccountRepository
import az.less.mobile.domain.repository.AuthorizationRepository
import az.less.mobile.domain.repository.ContentRepository
import az.less.mobile.domain.repository.ExploreRepository
import az.less.mobile.domain.repository.FavoritesRepository
import az.less.mobile.domain.repository.MerchantRepository
import az.less.mobile.domain.repository.OffersRepository
import az.less.mobile.domain.repository.OrdersRepository
import az.less.mobile.domain.repository.VenuesRepository
import az.less.mobile.domain.repository.VouchersRepository
import az.less.mobile.domain.usecase.CalculateOrderPriceUseCase
import az.less.mobile.domain.usecase.ValidateAndBuildBoxRequestUseCase
import az.less.mobile.domain.usecase.ValidateVoucherUseCase
import org.koin.dsl.module

val dataModule = module {
    // DataSources
    single { AuthDataSource(get()) }
    single { AccountDataSource(get()) }
    single { OffersDataSource(get()) }
    single { FavoritesDataSource(get()) }
    single { OrdersDataSource(get()) }
    single { ExploreDataSource(get()) }
    single { MerchantDataSource(get()) }
    single { ContentDataSource(get()) }
    single { VenuesDataSource(get()) }
    single { VouchersDataSource(get()) }

    // Repositories
    single<AuthorizationRepository> { AuthorizationRepositoryImpl(get(), get(), get()) }
    single<AccountRepository> { AccountRepositoryImpl(get()) }
    single<OffersRepository> { OffersRepositoryImpl(get()) }
    single<FavoritesRepository> { FavoritesRepositoryImpl(get()) }
    single<OrdersRepository> { OrdersRepositoryImpl(get()) }
    single<ExploreRepository> { ExploreRepositoryImpl(get()) }
    single<MerchantRepository> { MerchantRepositoryImpl(get()) }
    single<ContentRepository> { ContentRepositoryImpl(get()) }
    single<VenuesRepository> { VenuesRepositoryImpl(get()) }
    single<VouchersRepository> { VouchersRepositoryImpl(get()) }

    // UseCases
    factory { ValidateAndBuildBoxRequestUseCase() }
    factory { CalculateOrderPriceUseCase() }
    factory { ValidateVoucherUseCase() }
}
