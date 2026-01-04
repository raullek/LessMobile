package az.less.mobile.presentation.client.main.voucher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.presentation.client.main.voucher.models.Voucher
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

/**
 * ViewModel for Voucher Screen using Orbit MVI
 */
class VoucherViewModel : ViewModel(), ContainerHost<VoucherState, VoucherSideEffect> {

    override val container: Container<VoucherState, VoucherSideEffect> =
        viewModelScope.container(VoucherState())

    init {
        loadInitialData()
    }

    /**
     * Handle user intents
     */
    fun onIntent(intent: VoucherIntent) {
        when (intent) {
            is VoucherIntent.OnBackClicked -> handleBackClicked()
            is VoucherIntent.OnCopyCodeClicked -> handleCopyCodeClicked(intent.code)
            is VoucherIntent.OnShareClicked -> handleShareClicked()
        }
    }

    private fun loadInitialData() = intent {
        reduce {
            state.copy(
                vouchers = getMockVouchers(),
                referralCode = "1KK3vds3"
            )
        }
    }

    private fun handleBackClicked() = intent {
        postSideEffect(VoucherSideEffect.NavigateBack)
    }

    private fun handleCopyCodeClicked(code: String) = intent {
        postSideEffect(VoucherSideEffect.ShowCopiedToast("Code copied to clipboard"))
    }

    private fun handleShareClicked() = intent {
        postSideEffect(VoucherSideEffect.ShareReferralCode)
    }

    // Mock data - replace with repository calls in real app
    private fun getMockVouchers(): List<Voucher> {
        return listOf(
            Voucher(
                id = "1",
                title = "From referal program",
                amount = "2",
                expiryDate = "18 May 2026",
                loyaltyCode = "GRRQHBGSAW163"
            ),
            Voucher(
                id = "2",
                title = "Black friday",
                amount = "3",
                expiryDate = "18 May 2026",
                loyaltyCode = "GRRQHBGSAW163"
            )
        )
    }
}
