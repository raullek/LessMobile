package az.less.mobile.di

import az.less.mobile.presentation.main.explore.ExploreViewModel
import az.less.mobile.presentation.main.more.account.AccountViewModel
import az.less.mobile.presentation.main.more.paymentmethods.PaymentMethodsViewModel
import az.less.mobile.presentation.main.more.paymentmethods.addnewcard.AddNewCardViewModel
import az.less.mobile.presentation.main.more.root.MoreViewModel
import az.less.mobile.presentation.main.offers.OffersViewModel
import az.less.mobile.presentation.main.orders.OrdersViewModel
import az.less.mobile.presentation.main.saved.SavedViewModel
import az.less.mobile.presentation.main.search.SearchViewModel
import az.less.mobile.presentation.reserve.ReserveViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MoreViewModel)
    viewModelOf(::AccountViewModel)
    viewModelOf(::PaymentMethodsViewModel)
    viewModelOf(::AddNewCardViewModel)
    viewModelOf(::OffersViewModel)
    viewModelOf(::ExploreViewModel)
    viewModelOf(::SavedViewModel)
    viewModelOf(::OrdersViewModel)
    viewModelOf(::ReserveViewModel)
    viewModelOf(::SearchViewModel)
}


