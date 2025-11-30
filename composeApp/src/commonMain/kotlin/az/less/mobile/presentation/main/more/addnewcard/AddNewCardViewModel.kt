package az.less.mobile.presentation.main.more.paymentmethods.addnewcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class AddNewCardViewModel : ViewModel(), ContainerHost<AddNewCardState, AddNewCardSideEffect> {
    
    override val container: Container<AddNewCardState, AddNewCardSideEffect> = 
        viewModelScope.container(AddNewCardState())

    fun onIntent(intent: AddNewCardIntent) {
        when (intent) {
            is AddNewCardIntent.OnBackClicked -> handleBackClicked()
            is AddNewCardIntent.OnCardNumberChanged -> handleCardNumberChanged(intent.value)
            is AddNewCardIntent.OnExpirationDateChanged -> handleExpirationDateChanged(intent.value)
            is AddNewCardIntent.OnCvvChanged -> handleCvvChanged(intent.value)
            is AddNewCardIntent.OnAddCardClicked -> handleAddCardClicked()
        }
    }
    
    private fun handleBackClicked() = intent {
        postSideEffect(AddNewCardSideEffect.NavigateBack)
    }
    
    private fun handleCardNumberChanged(value: String) = intent {
        // Remove all non-digit characters
        val digitsOnly = value.filter { it.isDigit() }
        // Limit to 16 digits
        val limited = digitsOnly.take(16)
        
        reduce {
            state.copy(
                cardNumber = limited,
                cardNumberError = if (limited.length < 16 && limited.isNotEmpty()) "Card number must be 16 digits" else null
            )
        }
    }
    
    private fun handleExpirationDateChanged(value: String) = intent {
        // Remove all non-digit characters
        val digitsOnly = value.filter { it.isDigit() }
        // Format as MM/YY (limit to 4 digits)
        val limited = digitsOnly.take(4)
        val formatted = when {
            limited.length <= 2 -> limited
            else -> "${limited.take(2)}/${limited.drop(2)}"
        }
        
        reduce {
            state.copy(
                expirationDate = formatted,
                expirationDateError = if (limited.length < 4 && limited.isNotEmpty()) "Invalid date format" else null
            )
        }
    }
    
    private fun handleCvvChanged(value: String) = intent {
        // Remove all non-digit characters
        val digitsOnly = value.filter { it.isDigit() }
        // Limit to 4 digits (some cards have 4-digit CVV)
        val limited = digitsOnly.take(4)
        
        reduce {
            state.copy(
                cvv = limited,
                cvvError = if (limited.length < 3 && limited.isNotEmpty()) "CVV must be 3-4 digits" else null
            )
        }
    }
    
    private fun handleAddCardClicked() = intent {
        // Validate all fields
        val cardNumberError = if (state.cardNumber.length != 16) "Card number must be 16 digits" else null
        val expirationDateError = if (state.expirationDate.length != 5) "Invalid expiration date" else null
        val cvvError = if (state.cvv.length < 3 || state.cvv.length > 4) "CVV must be 3-4 digits" else null
        
        if (cardNumberError != null || expirationDateError != null || cvvError != null) {
            reduce {
                state.copy(
                    cardNumberError = cardNumberError,
                    expirationDateError = expirationDateError,
                    cvvError = cvvError
                )
            }
        } else {
            // TODO: Call API to add card
            postSideEffect(AddNewCardSideEffect.NavigateToSuccess)
        }
    }
}

