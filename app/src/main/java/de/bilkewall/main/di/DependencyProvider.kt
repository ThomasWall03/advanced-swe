package de.bilkewall.main.di

import android.content.Context
import de.bilkewall.adapters.service.DrinkService
import de.bilkewall.plugins.database.CinderDatabase
import de.bilkewall.plugins.database.drink.DrinkRepository
import de.bilkewall.plugins.database.drinkIngredientCrossRef.DrinkIngredientCrossRefRepository
import de.bilkewall.plugins.database.filter.SharedFilterRepository
import de.bilkewall.plugins.database.match.MatchRepository
import de.bilkewall.plugins.database.profile.ProfileRepository

object DependencyProvider {
    private lateinit var database: CinderDatabase

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

    val drinkService: DrinkService by lazy {
        DrinkService()
    }
}