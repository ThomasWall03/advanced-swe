package de.bilkewall.cinder.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.bilkewall.cinder.database.drink.Drink
import de.bilkewall.cinder.database.drink.DrinkDao
import de.bilkewall.cinder.database.drinkIngredientCrossRef.DrinkIngredientCrossRef
import de.bilkewall.cinder.database.drinkIngredientCrossRef.DrinkIngredientDao
import de.bilkewall.cinder.database.filter.DrinkTypeFilter
import de.bilkewall.cinder.database.filter.DrinkTypeFilterDao
import de.bilkewall.cinder.database.filter.IngredientValueFilter
import de.bilkewall.cinder.database.filter.IngredientValueFilterDao
import de.bilkewall.cinder.database.match.Match
import de.bilkewall.cinder.database.match.MatchDao
import de.bilkewall.cinder.database.profile.Profile
import de.bilkewall.cinder.database.profile.ProfileDao

@Database(entities = [Drink::class, DrinkIngredientCrossRef::class, Match::class, Profile::class, DrinkTypeFilter::class, IngredientValueFilter::class], version = 1, exportSchema = false)
abstract class CinderDatabase: RoomDatabase() {
    abstract val drinkDao: DrinkDao
    abstract val drinkIngredientDao: DrinkIngredientDao
    abstract val matchDao: MatchDao
    abstract val profileDao: ProfileDao
    abstract val ingredientValueFilterDao: IngredientValueFilterDao
    abstract val drinkTypeFilterDao: DrinkTypeFilterDao

    companion object {
        @Volatile
        private var INSTANCE: CinderDatabase? = null

        fun getInstance(context: Context):CinderDatabase {
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CinderDatabase::class.java,
                        "cinder_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                }
                return instance
            }
        }
    }
}