package ru.mikov.test.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.mikov.test.App
import ru.mikov.test.BuildConfig
import ru.mikov.test.data.local.dao.CardsDao
import ru.mikov.test.data.local.dao.CategoriesDao
import ru.mikov.test.data.local.entities.*

object DbManager {
    val db = Room.databaseBuilder(
        App.applicationContext(),
        AppDb::class.java,
        AppDb.DATABASE_NAME
    ).build()
}

@Database(
    entities = [Card::class,
        Category::class,
        CardCategoryXRef::class],
    version = AppDb.DATABASE_VERSION,
    exportSchema = false,
    views = []
)

@TypeConverters(DateConverter::class)
abstract class AppDb : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = BuildConfig.APPLICATION_ID + ".db"
        const val DATABASE_VERSION = 1
    }

    abstract fun cardsDao(): CardsDao
    abstract fun categoriesDao(): CategoriesDao
}

