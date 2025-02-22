package de.bilkewall.plugins.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.bilkewall.plugins.database.category.CategoryEntity
import de.bilkewall.plugins.database.category.CategoryDao
import de.bilkewall.plugins.database.drink.DrinkEntity
import de.bilkewall.plugins.database.drink.DrinkDao
import de.bilkewall.plugins.database.drinkIngredientCrossRef.DrinkIngredientCrossRefEntity
import de.bilkewall.plugins.database.drinkIngredientCrossRef.DrinkIngredientDao
import de.bilkewall.plugins.database.filter.DrinkTypeFilterEntity
import de.bilkewall.plugins.database.filter.DrinkTypeFilterDao
import de.bilkewall.plugins.database.filter.IngredientValueFilterEntity
import de.bilkewall.plugins.database.filter.IngredientValueFilterDao
import de.bilkewall.plugins.database.match.MatchEntity
import de.bilkewall.plugins.database.match.MatchDao
import de.bilkewall.plugins.database.profile.ProfileEntity
import de.bilkewall.plugins.database.profile.ProfileDao

@Database(
    entities = [DrinkEntity::class,
        DrinkIngredientCrossRefEntity::class,
        MatchEntity::class,
        ProfileEntity::class,
        DrinkTypeFilterEntity::class,
        IngredientValueFilterEntity::class,
        CategoryEntity::class],
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
    abstract val categoryDao: CategoryDao

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