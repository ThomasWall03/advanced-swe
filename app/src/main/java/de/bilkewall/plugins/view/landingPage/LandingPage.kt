package de.bilkewall.plugins.view.landingPage

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import de.bilkewall.plugins.view.utils.CustomLoadingIndicator

@Composable
fun StartUpView(
    navController: NavController,
    viewModel: LandingPageAndroidViewModel,
) {
    Scaffold { innerPadding ->
        Column(
            modifier =
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            val adapter = viewModel.viewModel
            val isLoading by adapter.isLoading.collectAsState()
            val profilesExist by adapter.profilesExist.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.checkIfProfilesExist()
            }

            LaunchedEffect(isLoading) {
                if (!isLoading) {
                    // TODO ins viewmodel hier, nur noch auslesen
                    var navigationRoute = "mainView"

                    if (!profilesExist) {
                        navigationRoute = "createProfileView"
                    }

                    Log.d("LandingPage", "Navigate to $navigationRoute")

                    navController.navigate(navigationRoute) {
                        popUpTo("startUpScreen") {
                            inclusive = true
                        } // Remove start screen from back stack
                    }
                }
            }

            AnimatedVisibility(isLoading) {
                CustomLoadingIndicator()
            }
        }
    }
}
