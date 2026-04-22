package az.less.mobile.di

import az.less.mobile.presentation.client.account.account.AccountViewModel
import az.less.mobile.presentation.client.account.paymentmethods.PaymentMethodsViewModel
import az.less.mobile.presentation.client.account.paymentmethods.addcard.AddCardWebViewViewModel
import az.less.mobile.presentation.client.main.categoryoffers.CategoryOffersViewModel
import az.less.mobile.presentation.client.main.explore.ExploreViewModel
import az.less.mobile.presentation.client.main.merchant.MerchantProfileViewModel
import az.less.mobile.presentation.client.main.more.root.MoreViewModel
import az.less.mobile.presentation.client.main.offers.OffersViewModel
import az.less.mobile.presentation.client.main.orders.OrdersViewModel
import az.less.mobile.presentation.client.main.favorites.FavoritesViewModel
import az.less.mobile.presentation.client.main.search.SearchViewModel
import az.less.mobile.presentation.client.main.voucher.VoucherViewModel
import az.less.mobile.presentation.client.onboarding.loginemail.LoginEmailViewModel
import az.less.mobile.presentation.client.onboarding.loginpassword.LoginPasswordViewModel
import az.less.mobile.presentation.client.onboarding.otp.LoginCodeViewModel
import az.less.mobile.presentation.client.reserve.ReserveViewModel
import az.less.mobile.presentation.main.more.paymentmethods.addnewcard.AddNewCardViewModel
import az.less.mobile.presentation.merchant.add.addlot.AddLotViewModel
import az.less.mobile.presentation.merchant.history.IncomeHistoryViewModel
import az.less.mobile.presentation.merchant.more.MerchMoreViewModel
import az.less.mobile.presentation.merchant.orders.MerchOrdersViewModel
import az.less.mobile.presentation.merchant.places.MerchPlacesViewModel
import az.less.mobile.presentation.merchant.places.edit.EditMerchantProfileViewModel
import az.less.mobile.presentation.merchant.places.edit.branchusers.BranchUsersViewModel
import az.less.mobile.presentation.merchant.places.edit.branchusers.addbranchuser.AddBranchUserViewModel
import az.less.mobile.presentation.merchant.places.edit.selectlocation.InputAddressViewModel
import az.less.mobile.presentation.merchant.places.edit.selectlocation.SelectBranchLocationOnMapViewModel
import az.less.mobile.presentation.partner.more.PartnerMoreViewModel
import az.less.mobile.presentation.partner.places.PartnerPlacesViewModel
import az.less.mobile.presentation.partner.history.IncomeHistoryViewModel as PartnerIncomeHistoryViewModel
import az.less.mobile.presentation.partner.places.edit.EditMerchantProfileViewModel as PartnerEditMerchantProfileViewModel
import az.less.mobile.presentation.partner.places.edit.branchusers.BranchUsersViewModel as PartnerBranchUsersViewModel
import az.less.mobile.presentation.partner.places.edit.branchusers.addbranchuser.AddBranchUserViewModel as PartnerAddBranchUserViewModel
import az.less.mobile.presentation.partner.places.edit.selectlocation.InputAddressViewModel as PartnerInputAddressViewModel
import az.less.mobile.presentation.partner.places.edit.selectlocation.SelectBranchLocationOnMapViewModel as PartnerSelectBranchLocationOnMapViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::MoreViewModel)
    viewModelOf(::MerchMoreViewModel)
    viewModelOf(::IncomeHistoryViewModel)
    viewModelOf(::MerchOrdersViewModel)
    viewModelOf(::MerchPlacesViewModel)
    viewModelOf(::AddLotViewModel)
    viewModelOf(::EditMerchantProfileViewModel)
    viewModelOf(::BranchUsersViewModel)
    viewModelOf(::AddBranchUserViewModel)
    viewModelOf(::SelectBranchLocationOnMapViewModel)
    viewModelOf(::InputAddressViewModel)
    viewModelOf(::AccountViewModel)
    viewModelOf(::PaymentMethodsViewModel)
    viewModelOf(::AddCardWebViewViewModel)
    viewModelOf(::AddNewCardViewModel)
    viewModelOf(::OffersViewModel)
    viewModelOf(::ExploreViewModel)
    viewModelOf(::FavoritesViewModel)
    viewModelOf(::OrdersViewModel)
    viewModelOf(::ReserveViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::LoginEmailViewModel)
    viewModelOf(::LoginPasswordViewModel)
    viewModelOf(::LoginCodeViewModel)
    viewModelOf(::CategoryOffersViewModel)
    viewModelOf(::MerchantProfileViewModel)
    viewModelOf(::VoucherViewModel)

    // Partner flow (scaffold — copies of merchant screens, will be adapted)
    viewModelOf(::PartnerMoreViewModel)
    viewModelOf(::PartnerPlacesViewModel)
    viewModelOf(::PartnerIncomeHistoryViewModel)
    viewModelOf(::PartnerEditMerchantProfileViewModel)
    viewModelOf(::PartnerBranchUsersViewModel)
    viewModelOf(::PartnerAddBranchUserViewModel)
    viewModelOf(::PartnerInputAddressViewModel)
    viewModelOf(::PartnerSelectBranchLocationOnMapViewModel)
}


