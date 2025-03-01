package de.bilkewall.main

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
import de.bilkewall.adapters.viewmodel.CreateProfileViewModel
import de.bilkewall.adapters.viewmodel.DrinkDetailViewModel
import de.bilkewall.adapters.viewmodel.DrinkListViewModel
import de.bilkewall.adapters.viewmodel.LandingPageViewModel
import de.bilkewall.adapters.viewmodel.MainViewModel
import de.bilkewall.adapters.viewmodel.MatchesViewModel
import de.bilkewall.application.service.CategoryService
import de.bilkewall.application.service.DrinkFetchingService
import de.bilkewall.application.service.DrinkFilterService
import de.bilkewall.application.service.IngredientService
import de.bilkewall.application.service.MatchService
import de.bilkewall.application.service.ProfileManagementService
import de.bilkewall.application.service.SharedFilterService
import de.bilkewall.cinder.R
import de.bilkewall.main.di.CreateProfileViewModelFactory
import de.bilkewall.main.di.DependencyProvider
import de.bilkewall.main.di.DrinkDetailViewModelFactory
import de.bilkewall.main.di.DrinkListViewModelFactory
import de.bilkewall.main.di.LandingPageViewModelFactory
import de.bilkewall.main.di.MainViewModelFactory
import de.bilkewall.main.di.MatchesViewModelFactory
import de.bilkewall.plugins.theme.CinderTheme
import de.bilkewall.plugins.view.bottomBar.CinderBar
import de.bilkewall.plugins.view.bottomBar.TabBarItem
import de.bilkewall.plugins.view.createProfile.CreateProfileView
import de.bilkewall.plugins.view.drinkDetail.DrinkDetailView
import de.bilkewall.plugins.view.drinkList.DrinkListView
import de.bilkewall.plugins.view.landingPage.StartUpView
import de.bilkewall.plugins.view.main.MainView
import de.bilkewall.plugins.view.matches.MatchesView

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
    val mainViewModel: MainViewModel =
        viewModel(
            factory =
                MainViewModelFactory(
                    DrinkFilterService.getInstance(
                        DrinkFetchingService.getInstance(
                            DependencyProvider.drinkRepository,
                            DependencyProvider.drinkIngredientCrossRefRepository,
                        ),
                    ),
                    ProfileManagementService.getInstance(
                        DependencyProvider.profileRepository,
                    ),
                    MatchService.getInstance(DependencyProvider.matchRepository),
                    SharedFilterService.getInstance(DependencyProvider.sharedFilterRepository),
                    DrinkFetchingService.getInstance(
                        DependencyProvider.drinkRepository,
                        DependencyProvider.drinkIngredientCrossRefRepository,
                    ),
                ),
        )
    val drinkListViewModel: DrinkListViewModel =
        viewModel(
            factory =
                DrinkListViewModelFactory(
                    DrinkFetchingService.getInstance(
                        DependencyProvider.drinkRepository,
                        DependencyProvider.drinkIngredientCrossRefRepository,
                    ),
                ),
        )
    val drinkViewModel: DrinkDetailViewModel =
        viewModel(
            factory =
                DrinkDetailViewModelFactory(
                    DrinkFetchingService.getInstance(
                        DependencyProvider.drinkRepository,
                        DependencyProvider.drinkIngredientCrossRefRepository,
                    ),
                ),
        )
    val matchesViewModel: MatchesViewModel =
        viewModel(
            factory =
                MatchesViewModelFactory(
                    ProfileManagementService.getInstance(
                        DependencyProvider.profileRepository,
                    ),
                    DrinkFetchingService.getInstance(
                        DependencyProvider.drinkRepository,
                        DependencyProvider.drinkIngredientCrossRefRepository,
                    ),
                ),
        )
    val createProfileViewModel: CreateProfileViewModel =
        viewModel(
            factory =
                CreateProfileViewModelFactory(
                    ProfileManagementService.getInstance(
                        DependencyProvider.profileRepository,
                    ),
                    SharedFilterService.getInstance(DependencyProvider.sharedFilterRepository),
                    IngredientService.getInstance(DependencyProvider.drinkIngredientCrossRefRepository),
                    CategoryService.getInstance(DependencyProvider.categoryRepository),
                ),
        )
    val landingPageViewModel: LandingPageViewModel =
        viewModel(
            factory =
                LandingPageViewModelFactory(
                    DrinkFetchingService.getInstance(
                        DependencyProvider.drinkRepository,
                        DependencyProvider.drinkIngredientCrossRefRepository,
                    ),
                    ProfileManagementService.getInstance(
                        DependencyProvider.profileRepository,
                    ),
                    DependencyProvider.databasePopulator,
                ),
        )

    val matchesTab =
        TabBarItem(
            title = "matchesView",
            selectedIcon = painterResource(id = R.drawable.cherry),
            unselectedIcon = painterResource(id = R.drawable.cherry),
        )
    val mainTab =
        TabBarItem(
            title = "mainView",
            selectedIcon = painterResource(id = R.drawable.flame),
            unselectedIcon = painterResource(id = R.drawable.flame),
        )
    val cocktailListTab =
        TabBarItem(
            title = "cocktailListView",
            selectedIcon = painterResource(id = R.drawable.glass),
            unselectedIcon = painterResource(id = R.drawable.glass),
        )

    val tabBarItems = listOf(cocktailListTab, mainTab, matchesTab)
    val bottomBar = @Composable { CinderBar(tabBarItems, navController) }

    NavHost(navController = navController, startDestination = "startUpView") {
        composable(mainTab.title) {
            MainView(navController, mainViewModel, bottomBar)
        }

        composable(
            route = "startUpView",
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
