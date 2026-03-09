package az.less.mobile.presentation.client.account.paymentmethods.addcard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import az.less.designsystem.components.AnimatedToast
import az.less.designsystem.components.DsToolBar
import az.less.designsystem.components.ToastType
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

private const val CALLBACK_SUCCESS_PREFIX =
    "https://axshambazari.com/client/payment-methods/callback?status=success"
private const val CALLBACK_ERROR_PREFIX =
    "https://axshambazari.com/client/payment-methods/callback?status=error"

@Composable
fun AddCardWebViewScreen(
    navController: NavController,
    url: String,
    title: String,
    viewModel: AddCardWebViewViewModel = koinViewModel()
) {
    val vmState by viewModel.collectAsState()
    val webViewState = rememberWebViewState(url = url)
    val navigator = rememberWebViewNavigator()

    LaunchedEffect(webViewState) {
        snapshotFlow { webViewState.lastLoadedUrl }
            .collect { loadedUrl ->
                if (loadedUrl != null) {
                    when {
                        loadedUrl.startsWith(CALLBACK_SUCCESS_PREFIX) ->
                            viewModel.onIntent(AddCardWebViewIntent.OnCallbackUrlDetected(loadedUrl))
                        loadedUrl.startsWith(CALLBACK_ERROR_PREFIX) ->
                            viewModel.onIntent(AddCardWebViewIntent.OnErrorCallbackDetected(loadedUrl))
                    }
                }
            }
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is AddCardWebViewSideEffect.CardAddedSuccessfully -> {
                delay(2000)
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("card_added", true)
                navController.popBackStack()
            }
            is AddCardWebViewSideEffect.CardAdditionFailed -> {
                delay(2000)
                navController.popBackStack()
            }
            is AddCardWebViewSideEffect.ShowError -> { }
        }
    }

    Scaffold(
        topBar = {
            DsToolBar(
                title = title,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                val loadingState = webViewState.loadingState
                if (loadingState is LoadingState.Loading || vmState.isVerifying) {
                    LinearProgressIndicator(
                        progress = {
                            if (vmState.isVerifying) 1f
                            else (loadingState as? LoadingState.Loading)?.progress ?: 0f
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                WebView(
                    state = webViewState,
                    modifier = Modifier.fillMaxSize(),
                    navigator = navigator
                )
            }

            AnimatedToast(
                visible = vmState.showSuccessToast,
                title = "Card added successfully",
                type = ToastType.Success,
                modifier = Modifier.align(Alignment.TopCenter)
            )

            AnimatedToast(
                visible = vmState.showErrorToast,
                title = "Card addition failed",
                type = ToastType.Error,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}
