package de.bilkewall.cinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import de.bilkewall.cinder.di.DependencyProvider
import de.bilkewall.cinder.ui.theme.CinderTheme
import de.bilkewall.cinder.view.bottomBar.CinderBar
import de.bilkewall.cinder.view.bottomBar.TabBarItem
import de.bilkewall.cinder.view.createProfile.CreateProfileView
import de.bilkewall.cinder.view.createProfile.CreateProfileViewModel
import de.bilkewall.cinder.view.drinkDetail.DrinkDetailView
import de.bilkewall.cinder.view.drinkDetail.DrinkDetailViewModel
import de.bilkewall.cinder.view.drinkList.DrinkListView
import de.bilkewall.cinder.view.drinkList.DrinkListViewModel
import de.bilkewall.cinder.view.landingPage.LandingPageViewModel
import de.bilkewall.cinder.view.landingPage.StartUpView
import de.bilkewall.cinder.view.main.MainView
import de.bilkewall.cinder.view.main.MainViewModel
import de.bilkewall.cinder.view.matches.MatchesView
import de.bilkewall.cinder.view.matches.MatchesViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CinderTheme {
                CinderApp()
            }
        }
    }
}

@Composable
fun CinderApp() {
    val context = LocalContext.current
    DependencyProvider.initialize(context)

    val navController = rememberNavController()
    val mainViewModel = viewModel<MainViewModel>()
    val drinkListViewModel = viewModel<DrinkListViewModel>()
    val drinkViewModel = viewModel<DrinkDetailViewModel>()
    val matchesViewModel = viewModel<MatchesViewModel>()
    val createProfileViewModel = viewModel<CreateProfileViewModel>()
    val landingPageViewModel = viewModel<LandingPageViewModel>()

    val matchesTab = TabBarItem(
        title = "matchesView",
        selectedIcon = painterResource(id = R.drawable.cherry),
        unselectedIcon = painterResource(id = R.drawable.cherry)
    )
    val mainTab = TabBarItem(
        title = "mainView",
        selectedIcon = painterResource(id = R.drawable.flame),
        unselectedIcon = painterResource(id = R.drawable.flame)
    )
    val cocktailListTab = TabBarItem(
        title = "cocktailListView",
        selectedIcon = painterResource(id = R.drawable.glass),
        unselectedIcon = painterResource(id = R.drawable.glass)
    )

    val tabBarItems = listOf(cocktailListTab, mainTab, matchesTab)
    val bottomBar = @Composable { CinderBar(tabBarItems, navController) }

    NavHost(navController = navController, startDestination = "startUpView") {
        composable(mainTab.title) {
            MainView(navController, mainViewModel, bottomBar)
        }

        composable(
            route = "startUpView"
        ) {
            StartUpView(navController, landingPageViewModel)
        }

        composable(cocktailListTab.title) {
            DrinkListView(navController, drinkListViewModel, bottomBar)
        }

        composable(matchesTab.title) {
            MatchesView(navController, matchesViewModel, bottomBar)
        }

        composable(
            route = "cocktailDetailView/{drinkId}",
            arguments = listOf(navArgument("drinkId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val drinkId = backStackEntry.arguments?.getString("drinkId") ?: ""
            DrinkDetailView(navController, drinkViewModel, drinkId)
        }

        composable("createProfileView") {
            CreateProfileView(navController, createProfileViewModel)
        }
    }

    BackHandler {
        val currentRoute = navController.currentBackStackEntry?.destination?.route
        if (currentRoute == cocktailListTab.title || currentRoute == matchesTab.title) {
            navController.navigate(mainTab.title) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        } else {
            navController.popBackStack()
        }
    }


}