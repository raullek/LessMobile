package az.less.mobile.di

import az.less.mobile.presentation.client.main.categoryoffers.CategoryOffersViewModel
import az.less.mobile.presentation.client.main.explore.ExploreViewModel
import az.less.mobile.presentation.client.main.merchant.MerchantProfileViewModel
import az.less.mobile.presentation.client.account.account.AccountViewModel
import az.less.mobile.presentation.client.account.paymentmethods.PaymentMethodsViewModel
import az.less.mobile.presentation.main.more.paymentmethods.addnewcard.AddNewCardViewModel
import az.less.mobile.presentation.client.main.more.root.MoreViewModel
import az.less.mobile.presentation.merchant.history.IncomeHistoryViewModel
import az.less.mobile.presentation.merchant.more.MerchMoreViewModel
import az.less.mobile.presentation.merchant.orders.MerchOrdersViewModel
import az.less.mobile.presentation.merchant.places.MerchPlacesViewModel
import az.less.mobile.presentation.merchant.places.edit.EditMerchantProfileViewModel
import az.less.mobile.presentation.client.main.offers.OffersViewModel
import az.less.mobile.presentation.client.main.orders.OrdersViewModel
import az.less.mobile.presentation.client.main.saved.SavedViewModel
import az.less.mobile.presentation.client.main.search.SearchViewModel
import az.less.mobile.presentation.client.onboarding.loginemail.LoginEmailViewModel
import az.less.mobile.presentation.client.onboarding.otp.LoginCodeViewModel
import az.less.mobile.presentation.client.reserve.ReserveViewModel
import az.less.mobile.presentation.merchant.places.edit.branchusers.BranchUsersViewModel
import az.less.mobile.presentation.merchant.places.edit.branchusers.addbranchuser.AddBranchUserViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MoreViewModel)
    viewModelOf(::MerchMoreViewModel)
    viewModelOf(::IncomeHistoryViewModel)
    viewModelOf(::MerchOrdersViewModel)
    viewModelOf(::MerchPlacesViewModel)
    viewModelOf(::EditMerchantProfileViewModel)
    viewModelOf(::BranchUsersViewModel)
    viewModelOf(::AddBranchUserViewModel)
    viewModelOf(::AccountViewModel)
    viewModelOf(::PaymentMethodsViewModel)
    viewModelOf(::AddNewCardViewModel)
    viewModelOf(::OffersViewModel)
    viewModelOf(::ExploreViewModel)
    viewModelOf(::SavedViewModel)
    viewModelOf(::OrdersViewModel)
    viewModelOf(::ReserveViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::LoginEmailViewModel)
    viewModelOf(::LoginCodeViewModel)
    viewModelOf(::CategoryOffersViewModel)
    viewModelOf(::MerchantProfileViewModel)
}


