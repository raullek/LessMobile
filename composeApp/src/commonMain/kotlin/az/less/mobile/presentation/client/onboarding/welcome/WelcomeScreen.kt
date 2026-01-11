package az.less.mobile.presentation.client.onboarding.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import az.less.designsystem.base.LessTheme
import az.less.designsystem.components.DsButton
import az.less.mobile.navigation.ClientRoute
import lessmobile.composeapp.generated.resources.Res
import lessmobile.composeapp.generated.resources.ic_mail_24dp
import lessmobile.composeapp.generated.resources.ill_welcome_screen_320dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource


@Composable
fun WelcomeScreen(
    navController: NavController,
) {

    WelcomeScreenContent(
        onLoginClick = {
            navController.navigate(ClientRoute.LoginEmail)
        }
    )
}

@Composable
fun WelcomeScreenContent(
    onLoginClick: () -> Unit,
) {
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = LessTheme.spacing.medium)
            .background(LessTheme.colors.backgroundSecond)
            .systemBarsPadding()
            .navigationBarsPadding()
    ) {

        Spacer(modifier = Modifier.height(LessTheme.spacing.xxxLarge))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(modifier = Modifier
                .fillMaxWidth()
                .height(320.dp),
               painter =  painterResource(Res.drawable.ill_welcome_screen_320dp),
                contentDescription = "")

            // Spacer to push content to center

            Spacer(modifier = Modifier.height(LessTheme.spacing.xLarge))

            // Welcome message
            Text(
                text = "Welcome to Less",
                style = LessTheme.typography.title24Bold,
                color = LessTheme.colors.textIconsBrand,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(LessTheme.spacing.medium))

            Text(
                text = "Select your preferred method to log in",
                style = LessTheme.typography.body16Bold,
                color = LessTheme.colors.textIconsBlack,
                textAlign = TextAlign.Center
            )
        }

        // Button at the bottom
        DsButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Log in with email",
            onClick = {
                onLoginClick()
            },
            leadingIcon = vectorResource(Res.drawable.ic_mail_24dp)
        )

        Spacer(modifier = Modifier.height(LessTheme.spacing.medium))
    }
}

