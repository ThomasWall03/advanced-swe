package de.bilkewall.cinder.di

import android.content.Context
import de.bilkewall.cinder.database.CinderDatabase
import de.bilkewall.cinder.database.drink.DrinkRepository
import de.bilkewall.cinder.database.drinkIngredientCrossRef.DrinkIngredientCrossRefRepository
import de.bilkewall.cinder.database.filter.SharedFilterRepository
import de.bilkewall.cinder.database.match.MatchRepository
import de.bilkewall.cinder.database.profile.ProfileRepository
import de.bilkewall.cinder.service.DrinkService

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