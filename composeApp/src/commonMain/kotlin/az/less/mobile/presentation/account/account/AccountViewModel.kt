package az.less.mobile.presentation.account.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container

class AccountViewModel : ViewModel(), ContainerHost<AccountState, AccountSideEffect> {
    
    override val container: Container<AccountState, AccountSideEffect> = 
        viewModelScope.container(AccountState())

    fun onIntent(intent: AccountIntent) {
        when (intent) {
            is AccountIntent.OnBackClicked -> handleBackClicked()
            is AccountIntent.OnEditPhotoClicked -> handleEditPhotoClicked()
            is AccountIntent.OnFullNameChanged -> handleFullNameChanged(intent.fullName)
            is AccountIntent.OnPhoneChanged -> handlePhoneChanged(intent.phone)
            is AccountIntent.OnEmailChanged -> handleEmailChanged(intent.email)
            is AccountIntent.OnGenderChanged -> handleGenderChanged(intent.gender)
            is AccountIntent.OnBirthDateChanged -> handleBirthDateChanged(intent.birthDate)
            is AccountIntent.OnGenderClick -> handleGenderClick()
            is AccountIntent.OnGenderBottomSheetDismiss -> handleGenderBottomSheetDismiss()
            is AccountIntent.OnGenderClear -> handleGenderClear()
            is AccountIntent.OnBirthDateClick -> handleBirthDateClick()
            is AccountIntent.OnBirthDateClear -> handleBirthDateClear()
            is AccountIntent.OnDeleteAccountClicked -> handleDeleteAccountClicked()
            is AccountIntent.OnSaveClicked -> handleSaveClicked()
        }
    }
    
    private fun handleBackClicked() = intent {
        postSideEffect(AccountSideEffect.NavigateBack)
    }
    
    private fun handleEditPhotoClicked() = intent {
    }
    
    private fun handleFullNameChanged(fullName: String) = intent {
        reduce {
            state.copy(fullName = fullName)
        }
    }
    
    private fun handlePhoneChanged(phone: String) = intent {
        // Remove +994 prefix if user is typing (it will be added automatically on focus)
        val cleanedPhone = if (phone.startsWith("+994")) {
            phone.substring(4)
        } else {
            phone
        }
        reduce {
            state.copy(
                phoneNumber = cleanedPhone,
                phoneNumberError = null // Clear error when user makes changes
            )
        }
    }
    
    private fun handleEmailChanged(email: String) = intent {
        reduce {
            state.copy(
                email = email,
                emailError = null // Clear error when user makes changes
            )
        }
    }
    
    private fun validatePhoneNumber(phone: String): String? {
        if (phone.isEmpty()) return null
        
        // Remove spaces and other formatting characters for validation
        val cleanedPhone = phone.replace(" ", "").replace("-", "").replace("(", "").replace(")", "")
        
        // Check if phone starts with +994 and has correct length
        // Format: +994XXXXXXXXX (12 digits after +994)
        return when {
            !cleanedPhone.startsWith("+994") -> "Phone number must start with +994"
            cleanedPhone.length < 13 -> "Phone number is too short"
            cleanedPhone.length > 13 -> "Phone number is too long"
            !cleanedPhone.substring(4).all { it.isDigit() } -> "Phone number contains invalid characters"
            else -> null
        }
    }
    
    private fun validateEmail(email: String): String? {
        if (email.isEmpty()) return null
        
        return when {
            !email.contains("@") -> "Email must contain @"
            else -> null
        }
    }
    
    private fun handleGenderChanged(gender: String) = intent {
        reduce {
            state.copy(
                gender = gender,
                showGenderBottomSheet = false
            )
        }
    }
    
    private fun handleBirthDateChanged(birthDate: String) = intent {
        reduce {
            state.copy(birthDate = birthDate)
        }
    }
    
    private fun handleGenderClick() = intent {
        reduce {
            state.copy(showGenderBottomSheet = true)
        }
    }
    
    private fun handleGenderBottomSheetDismiss() = intent {
        reduce {
            state.copy(showGenderBottomSheet = false)
        }
    }
    
    private fun handleGenderClear() = intent {
        reduce {
            state.copy(gender = "")
        }
    }
    
    private fun handleBirthDateClick() = intent {
    }
    
    private fun handleBirthDateClear() = intent {
        reduce {
            state.copy(birthDate = "")
        }
    }
    
    private fun handleDeleteAccountClicked() = intent {
    }
    
    private fun handleSaveClicked() = intent {
        // Add +994 prefix for validation if phone number is not empty
        val phoneForValidation = if (state.phoneNumber.isNotEmpty() && !state.phoneNumber.startsWith("+994")) {
            "+994${state.phoneNumber}"
        } else {
            state.phoneNumber
        }
        val phoneError = validatePhoneNumber(phoneForValidation)
        val emailError = validateEmail(state.email)
        
        reduce {
            state.copy(
                phoneNumberError = phoneError,
                emailError = emailError
            )
        }
        
        // If there are no errors, proceed with save
        if (phoneError == null && emailError == null) {
            // TODO: Implement actual save logic
        }
    }
}
