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

    val categoryRepository: CategoryRepository by lazy {
        CategoryRepository(database.categoryDao)
    }

    val databasePopulator: DatabasePopulator by lazy {
        DatabasePopulator(drinkRepository, drinkIngredientCrossRefRepository, categoryRepository, apiWrapper)
    }
}