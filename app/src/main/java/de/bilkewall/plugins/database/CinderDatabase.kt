package de.bilkewall.plugins.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.bilkewall.plugins.database.drink.Drink
import de.bilkewall.plugins.database.drink.DrinkDao
import de.bilkewall.plugins.database.drinkIngredientCrossRef.DrinkIngredientCrossRef
import de.bilkewall.plugins.database.drinkIngredientCrossRef.DrinkIngredientDao
import de.bilkewall.plugins.database.filter.DrinkTypeFilter
import de.bilkewall.plugins.database.filter.DrinkTypeFilterDao
import de.bilkewall.plugins.database.filter.IngredientValueFilter
import de.bilkewall.plugins.database.filter.IngredientValueFilterDao
import de.bilkewall.plugins.database.match.Match
import de.bilkewall.plugins.database.match.MatchDao
import de.bilkewall.plugins.database.profile.Profile
import de.bilkewall.plugins.database.profile.ProfileDao

@Database(
    entities = [Drink::class, DrinkIngredientCrossRef::class, Match::class, Profile::class, DrinkTypeFilter::class, IngredientValueFilter::class],
    version = 1,
    exportSchema = false
)
abstract class CinderDatabase : RoomDatabase() {
    abstract val drinkDao: DrinkDao
    abstract val drinkIngredientDao: DrinkIngredientDao
    abstract val matchDao: MatchDao
    abstract val profileDao: ProfileDao
    abstract val ingredientValueFilterDao: IngredientValueFilterDao
    abstract val drinkTypeFilterDao: DrinkTypeFilterDao

    companion object {
        @Volatile
        private var INSTANCE: CinderDatabase? = null

        fun getInstance(context: Context): CinderDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
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