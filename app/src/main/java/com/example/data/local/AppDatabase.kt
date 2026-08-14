package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.DeckEntity
import com.example.data.model.FlashcardEntity
import com.example.data.model.UserStatsEntity

@Database(
    entities = [
        DeckEntity::class,
        FlashcardEntity::class,
        UserStatsEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun flashcardDao(): FlashcardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "minecraft_flashcards.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
