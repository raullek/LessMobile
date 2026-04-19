package az.less.mobile.presentation.client.main.voucher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import az.less.mobile.domain.repository.VouchersRepository
import az.less.mobile.presentation.client.main.voucher.models.toDomain
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class VoucherViewModel(
    private val vouchersRepository: VouchersRepository
) : ViewModel(), ContainerHost<VoucherState, VoucherSideEffect> {

    override val container: Container<VoucherState, VoucherSideEffect> =
        viewModelScope.container(VoucherState())

    init {
        loadVouchers()
    }

    fun onIntent(intent: VoucherIntent) {
        when (intent) {
            is VoucherIntent.OnBackClicked -> handleBackClicked()
            is VoucherIntent.OnCopyCodeClicked -> handleCopyCodeClicked(intent.code)
            is VoucherIntent.Retry -> loadVouchers()
        }
    }

    private fun loadVouchers() = intent {
        reduce { state.copy(isLoading = true, error = null) }

        vouchersRepository.getVouchers()
            .onSuccess { data ->
                reduce {
                    state.copy(
                        isLoading = false,
                        vouchers = data.data.map { it.toDomain() }
                    )
                }
            }
            .onError { error ->
                reduce { state.copy(isLoading = false, error = error.message) }
            }
    }

    private fun handleBackClicked() = intent {
        postSideEffect(VoucherSideEffect.NavigateBack)
    }

    private fun handleCopyCodeClicked(code: String) = intent {
        postSideEffect(VoucherSideEffect.ShowCopiedToast(code))
    }
}
