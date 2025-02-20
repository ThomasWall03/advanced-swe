package de.bilkewall.main.di

import android.content.Context
import de.bilkewall.application.service.CategoryService
import de.bilkewall.application.service.DrinkService
import de.bilkewall.application.service.IngredientService
import de.bilkewall.application.service.MatchService
import de.bilkewall.application.service.ProfileService
import de.bilkewall.application.service.SharedFilterService
import de.bilkewall.plugins.api.APIManager
import de.bilkewall.plugins.api.APIWrapper
import de.bilkewall.plugins.api.DatabasePopulator
import de.bilkewall.plugins.database.CinderDatabase
import de.bilkewall.plugins.database.category.CategoryRepository
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

    private val drinkRepository: DrinkRepository by lazy {
        DrinkRepository(database.drinkDao)
    }

    private val drinkIngredientCrossRefRepository: DrinkIngredientCrossRefRepository by lazy {
        DrinkIngredientCrossRefRepository(database.drinkIngredientDao)
    }

    private val matchRepository: MatchRepository by lazy {
        MatchRepository(database.matchDao)
    }

    private val profileRepository: ProfileRepository by lazy {
        ProfileRepository(database.profileDao)
    }

    private val sharedFilterRepository: SharedFilterRepository by lazy {
        SharedFilterRepository(database.drinkTypeFilterDao, database.ingredientValueFilterDao)
    }

    private val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(database.categoryDao)
    }

    val drinkService: DrinkService by lazy {
        DrinkService(drinkRepository, drinkIngredientCrossRefRepository)
    }

    val ingredientService: IngredientService by lazy {
        IngredientService(drinkIngredientCrossRefRepository)
    }

    val profileService: ProfileService by lazy {
        ProfileService(profileRepository, sharedFilterRepository, matchRepository)
    }

    val matchService: MatchService by lazy {
        MatchService(matchRepository)
    }

    val categoryService: CategoryService by lazy {
        CategoryService(categoryRepository)
    }

    val sharedFilterService: SharedFilterService by lazy {
        SharedFilterService(sharedFilterRepository)
    }

    val databasePopulator: DatabasePopulator by lazy {
        DatabasePopulator(drinkRepository, drinkIngredientCrossRefRepository, categoryRepository, apiWrapper)
    }
}