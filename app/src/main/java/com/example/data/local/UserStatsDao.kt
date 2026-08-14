package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.UserStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserStatsDao {

    @Query("SELECT * FROM user_stats WHERE id = 1")
    fun getUserStats(): Flow<UserStatsEntity?>

    @Query("SELECT * FROM user_stats WHERE id = 1")
    suspend fun getUserStatsSync(): UserStatsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStats(stats: UserStatsEntity)

    @Query("UPDATE user_stats SET xp = :newXp, level = :newLevel WHERE id = 1")
    suspend fun updateXpAndLevel(newXp: Int, newLevel: Int)

    @Query("UPDATE user_stats SET creepersDefeated = creepersDefeated + 1 WHERE id = 1")
    suspend fun incrementCreepersDefeated()
}
