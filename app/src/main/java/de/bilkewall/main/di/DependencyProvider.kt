package de.bilkewall.main.di

import android.content.Context
import de.bilkewall.application.service.api.ApiService
import de.bilkewall.application.service.database.CreateProfileService
import de.bilkewall.application.service.database.DrinkService
import de.bilkewall.application.service.database.LandingPageService
import de.bilkewall.application.service.database.ProfileService
import de.bilkewall.plugins.api.APIManager
import de.bilkewall.plugins.api.APIWrapper
import de.bilkewall.plugins.database.CinderDatabase
import de.bilkewall.plugins.database.drink.DrinkRepository
import de.bilkewall.plugins.database.drinkIngredientCrossRef.DrinkIngredientCrossRefRepository
import de.bilkewall.plugins.database.filter.SharedFilterRepository
import de.bilkewall.plugins.database.match.MatchRepository
import de.bilkewall.plugins.database.profile.ProfileRepository

object DependencyProvider {
    private lateinit var database: CinderDatabase
    private val apiManager = APIManager()
    private val apiWrapper = APIWrapper(apiManager)

    fun initialize(context: Context) {
        database = CinderDatabase.getInstance(context)
    }

    val drinkRepository: DrinkRepository by lazy {
        DrinkRepository(database.drinkDao)
    }

    val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefRepository by lazy {
        DrinkIngredientCrossRefRepository(database.drinkIngredientDao)
    }

    val matchRepository: MatchRepository by lazy {
        MatchRepository(database.matchDao)
    }

    val profileRepository: ProfileRepository by lazy {
        ProfileRepository(database.profileDao)
    }

    val sharedFilterRepository: SharedFilterRepository by lazy {
        SharedFilterRepository(database.drinkTypeFilterDao, database.ingredientValueFilterDao)
    }

    val apiService: ApiService by lazy {
        ApiService(apiWrapper)
    }

    val drinkService: DrinkService by lazy {
        DrinkService(drinkRepository, drinkIngredientCrossRefRepository)
    }

    val landingPageService: LandingPageService by lazy {
        LandingPageService(drinkRepository, drinkIngredientCrossRefRepository)
    }

    val createProfileService: CreateProfileService by lazy {
        CreateProfileService(
            profileRepository,
            sharedFilterRepository,
            drinkIngredientCrossRefRepository
        )
    }

    val profileService: ProfileService by lazy {
        ProfileService(profileRepository)
    }
}