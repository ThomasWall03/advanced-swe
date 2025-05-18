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
import de.bilkewall.main.di.CreateProfileAndroidViewModelFactory
import de.bilkewall.main.di.DependencyProvider
import de.bilkewall.main.di.DrinkDetailAndroidViewModelFactory
import de.bilkewall.main.di.DrinkListAndroidViewModelFactory
import de.bilkewall.main.di.LandingPageAndroidViewModelFactory
import de.bilkewall.main.di.MainAndroidViewModelFactory
import de.bilkewall.main.di.MatchesAndroidViewModelFactory
import de.bilkewall.plugins.theme.CinderTheme
import de.bilkewall.plugins.view.bottomBar.CinderBar
import de.bilkewall.plugins.view.bottomBar.TabBarItem
import de.bilkewall.plugins.view.createProfile.CreateProfileAndroidViewModel
import de.bilkewall.plugins.view.createProfile.CreateProfileView
import de.bilkewall.plugins.view.drinkDetail.DrinkDetailAndroidViewModel
import de.bilkewall.plugins.view.drinkDetail.DrinkDetailView
import de.bilkewall.plugins.view.drinkList.DrinkListAndroidViewModel
import de.bilkewall.plugins.view.drinkList.DrinkListView
import de.bilkewall.plugins.view.landingPage.LandingPageAndroidViewModel
import de.bilkewall.plugins.view.landingPage.StartUpView
import de.bilkewall.plugins.view.main.MainAndroidViewModel
import de.bilkewall.plugins.view.main.MainView
import de.bilkewall.plugins.view.matches.MatchesAndroidViewModel
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
    val mainAndroidViewModel: MainAndroidViewModel =
        viewModel(
            factory =
            MainAndroidViewModelFactory(
                MainViewModel(
                    ProfileManagementService.getInstance(
                        DependencyProvider.profileRepository,
                    ),
                    MatchService.getInstance(DependencyProvider.matchRepository),
                    DrinkFilterService.getInstance(
                        DrinkFetchingService.getInstance(
                            DependencyProvider.drinkRepository,
                            DependencyProvider.drinkIngredientCrossRefRepository,
                        ),
                    ),
                    DrinkFetchingService.getInstance(
                        DependencyProvider.drinkRepository,
                        DependencyProvider.drinkIngredientCrossRefRepository,
                    ),
                    SharedFilterService.getInstance(DependencyProvider.sharedFilterRepository),
                )
            ),
        )
    val drinkListAndroidViewModel: DrinkListAndroidViewModel =
        viewModel(
            factory =
            DrinkListAndroidViewModelFactory(
                DrinkListViewModel(
                    DrinkFetchingService.getInstance(
                        DependencyProvider.drinkRepository,
                        DependencyProvider.drinkIngredientCrossRefRepository,
                    ),
                ),
            ),
        )
    val drinkAndroidViewModel: DrinkDetailAndroidViewModel =
        viewModel(
            factory =
            DrinkDetailAndroidViewModelFactory(
                DrinkDetailViewModel(
                    DrinkFetchingService.getInstance(
                        DependencyProvider.drinkRepository,
                        DependencyProvider.drinkIngredientCrossRefRepository,
                    ),
                ),
            ),
        )
    val matchesAndroidViewModel: MatchesAndroidViewModel =
        viewModel(
            factory =
            MatchesAndroidViewModelFactory(
                MatchesViewModel(
                    ProfileManagementService.getInstance(
                        DependencyProvider.profileRepository,
                    ),
                    DrinkFetchingService.getInstance(
                        DependencyProvider.drinkRepository,
                        DependencyProvider.drinkIngredientCrossRefRepository,
                    ),
                )
            ),
        )
    val createProfileAndroidViewModel: CreateProfileAndroidViewModel =
        viewModel(
            factory =
            CreateProfileAndroidViewModelFactory(
                CreateProfileViewModel(
                    ProfileManagementService.getInstance(
                        DependencyProvider.profileRepository,
                    ),
                    SharedFilterService.getInstance(DependencyProvider.sharedFilterRepository),
                    IngredientService.getInstance(DependencyProvider.drinkIngredientCrossRefRepository),
                    CategoryService.getInstance(DependencyProvider.categoryRepository)
                )
            ),
        )
    val landingPageAndroidViewModel: LandingPageAndroidViewModel =
        viewModel(
            factory =
            LandingPageAndroidViewModelFactory(
                LandingPageViewModel(
                    DrinkFetchingService.getInstance(
                        DependencyProvider.drinkRepository,
                        DependencyProvider.drinkIngredientCrossRefRepository,
                    ),
                    ProfileManagementService.getInstance(
                        DependencyProvider.profileRepository,
                    ),
                    DependencyProvider.databasePopulator,
                ),
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
            MainView(navController, mainAndroidViewModel, bottomBar)
        }

        composable(
            route = "startUpView",
        ) {
            StartUpView(navController, landingPageAndroidViewModel)
        }

        composable(cocktailListTab.title) {
            DrinkListView(navController, drinkListAndroidViewModel, bottomBar)
        }

        composable(matchesTab.title) {
            MatchesView(navController, matchesAndroidViewModel, bottomBar)
        }

        composable(
            route = "cocktailDetailView/{drinkId}",
            arguments = listOf(navArgument("drinkId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val drinkId = backStackEntry.arguments?.getString("drinkId") ?: ""
            DrinkDetailView(navController, drinkAndroidViewModel, drinkId)
        }

        composable("createProfileView") {
            CreateProfileView(navController, createProfileAndroidViewModel)
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
