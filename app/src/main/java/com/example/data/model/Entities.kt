package com.example.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "decks")
data class DeckEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val description: String = "",
    val emoji: String = "⛏️",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "flashcards",
    foreignKeys = [
        ForeignKey(
            entity = DeckEntity::class,
            parentColumns = ["id"],
            childColumns = ["deckId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("deckId")]
)
data class FlashcardEntity(
    @PrimaryKey
    val id: String = java.util.UUID.randomUUID().toString(),
    val deckId: String,
    val question: String,
    val answer: String,
    val reviewCount: Int = 0,
    val correctCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

data class DeckWithCards(
    @Embedded
    val deck: DeckEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "deckId"
    )
    val cards: List<FlashcardEntity>
)

@Entity(tableName = "user_stats")
data class UserStatsEntity(
    @PrimaryKey
    val id: Int = 1,
    val xp: Int = 0,
    val level: Int = 1,
    val hearts: Int = 3,
    val diamonds: Int = 0,
    val creepersDefeated: Int = 0
)
