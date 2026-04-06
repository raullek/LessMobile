package az.less.mobile.presentation.theme

import az.less.mobile.domain.repository.SessionLocalRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeManager(
    private val sessionLocalRepository: SessionLocalRepository,
    scope: CoroutineScope
) {
    val isDarkMode: StateFlow<Boolean> = sessionLocalRepository.isDarkMode
        .stateIn(scope, SharingStarted.Eagerly, false)

    fun toggleDarkMode(scope: CoroutineScope) {
        scope.launch {
            sessionLocalRepository.saveDarkMode(!isDarkMode.value)
        }
    }

    fun setDarkMode(enabled: Boolean, scope: CoroutineScope) {
        scope.launch {
            sessionLocalRepository.saveDarkMode(enabled)
        }
    }
}
