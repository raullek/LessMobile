package az.less.mobile.di

import az.less.mobile.presentation.main.more.MoreViewModel
import az.less.mobile.presentation.main.offers.OffersViewModel
import az.less.mobile.presentation.main.orders.OrdersViewModel
import az.less.mobile.presentation.main.saved.SavedViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MoreViewModel)
    viewModelOf(::OffersViewModel)
    viewModelOf(::SavedViewModel)
    viewModelOf(::OrdersViewModel)
}


