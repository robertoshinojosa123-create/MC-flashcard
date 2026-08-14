package com.example.data.repository

import com.example.data.local.FlashcardDao
import com.example.data.model.DeckEntity
import com.example.data.model.DeckWithCards
import com.example.data.model.FlashcardEntity
import com.example.data.model.UserStatsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID

class FlashcardRepository(private val dao: FlashcardDao) {

    val allDecksWithCards: Flow<List<DeckWithCards>> = dao.getAllDecksWithCards()
    val userStats: Flow<UserStatsEntity?> = dao.getUserStats()

    fun getDeckWithCards(deckId: String): Flow<DeckWithCards?> {
        return dao.getDeckWithCards(deckId)
    }

    suspend fun createDeck(title: String, description: String, emoji: String): String {
        val deckId = UUID.randomUUID().toString()
        val newDeck = DeckEntity(
            id = deckId,
            title = title,
            description = description,
            emoji = emoji.ifBlank { "⛏️" }
        )
        dao.insertDeck(newDeck)
        return deckId
    }

    suspend fun updateDeck(deck: DeckEntity) {
        dao.updateDeck(deck)
    }

    suspend fun deleteDeck(deckId: String) {
        dao.deleteDeckById(deckId)
    }

    suspend fun addCardToDeck(deckId: String, question: String, answer: String) {
        val card = FlashcardEntity(
            id = UUID.randomUUID().toString(),
            deckId = deckId,
            question = question,
            answer = answer
        )
        dao.insertCard(card)
    }

    suspend fun deleteCard(cardId: String) {
        dao.deleteCardById(cardId)
    }

    suspend fun updateCard(card: FlashcardEntity) {
        dao.updateCard(card)
    }

    suspend fun saveUserStats(stats: UserStatsEntity) {
        dao.insertOrUpdateUserStats(stats)
    }

    suspend fun insertDecksAndCards(decks: List<DeckEntity>, cards: List<FlashcardEntity>) {
        dao.insertDecks(decks)
        dao.insertCards(cards)
    }

    suspend fun seedInitialDataIfEmpty() {
        val existing = dao.getAllDecks().firstOrNull()
        if (existing.isNullOrEmpty()) {
            val deck1Id = "deck_crafting"
            val deck2Id = "deck_mobs"
            val deck3Id = "deck_redstone"
            val deck4Id = "deck_kotlin"

            val decks = listOf(
                DeckEntity(
                    id = deck1Id,
                    title = "Crafting y Herramientas",
                    description = "Recetas de crafteo básicas y avanzadas",
                    emoji = "⛏️"
                ),
                DeckEntity(
                    id = deck2Id,
                    title = "Mobs y Criaturas",
                    description = "Comportamiento y debilidades de mobs",
                    emoji = "👾"
                ),
                DeckEntity(
                    id = deck3Id,
                    title = "Circuitos de Redstone",
                    description = "Mecanismos, repetidores y comparadores",
                    emoji = "🔴"
                ),
                DeckEntity(
                    id = deck4Id,
                    title = "Programación Kotlin",
                    description = "Conceptos esenciales de Android y Kotlin",
                    emoji = "💻"
                )
            )

            val cards = listOf(
                // Crafting
                FlashcardEntity(
                    id = UUID.randomUUID().toString(),
                    deckId = deck1Id,
                    question = "¿Cuántos lingotes de hierro se necesitan para un Yunque?",
                    answer = "31 lingotes (3 bloques de hierro + 4 lingotes)"
                ),
                FlashcardEntity(
                    id = UUID.randomUUID().toString(),
                    deckId = deck1Id,
                    question = "¿Qué se necesita para fabricar una Mesa de Encantamientos?",
                    answer = "1 Libro, 2 Diamantes y 4 de Obsidiana"
                ),
                FlashcardEntity(
                    id = UUID.randomUUID().toString(),
                    deckId = deck1Id,
                    question = "¿Con qué herramienta se pica la Obsidiana?",
                    answer = "Pico de Diamante o Netherite"
                ),
                FlashcardEntity(
                    id = UUID.randomUUID().toString(),
                    deckId = deck1Id,
                    question = "¿Cómo se elabora una Poción de Curación?",
                    answer = "Poción Rara + Rodaja de Sandía Reluciente"
                ),

                // Mobs
                FlashcardEntity(
                    id = UUID.randomUUID().toString(),
                    deckId = deck2Id,
                    question = "¿A qué criatura le tienen miedo los Creepers?",
                    answer = "A los Gatos y Ocelotes"
                ),
                FlashcardEntity(
                    id = UUID.randomUUID().toString(),
                    deckId = deck2Id,
                    question = "¿Qué pasa cuando un Creeper es alcanzado por un rayo?",
                    answer = "Se convierte en un Creeper Cargado (Charged Creeper)"
                ),
                FlashcardEntity(
                    id = UUID.randomUUID().toString(),
                    deckId = deck2Id,
                    question = "¿Cómo evitar que un Enderman te ataque al mirarlo?",
                    answer = "Usando una Calabaza tallada en la cabeza"
                ),
                FlashcardEntity(
                    id = UUID.randomUUID().toString(),
                    deckId = deck2Id,
                    question = "¿Qué drop sueltan los Blazes?",
                    answer = "Varas de Blaze (Blaze Rods)"
                ),

                // Redstone
                FlashcardEntity(
                    id = UUID.randomUUID().toString(),
                    deckId = deck3Id,
                    question = "¿Cuál es el alcance máximo de una señal de Redstone antes de necesitar repetidor?",
                    answer = "15 bloques"
                ),
                FlashcardEntity(
                    id = UUID.randomUUID().toString(),
                    deckId = deck3Id,
                    question = "¿Qué función tiene el Comparador de Redstone?",
                    answer = "Compara la fuerza de dos señales o detecta la capacidad de un contenedor"
                ),
                FlashcardEntity(
                    id = UUID.randomUUID().toString(),
                    deckId = deck3Id,
                    question = "¿Cuánto retraso añade un Repetidor en su nivel máximo (4 ticks)?",
                    answer = "0.4 segundos (4 redstone ticks)"
                ),

                // Kotlin
                FlashcardEntity(
                    id = UUID.randomUUID().toString(),
                    deckId = deck4Id,
                    question = "¿Qué palabra clave define una variable inmutable en Kotlin?",
                    answer = "val"
                ),
                FlashcardEntity(
                    id = UUID.randomUUID().toString(),
                    deckId = deck4Id,
                    question = "¿Qué función de Compose se usa para recordar un estado recomponible?",
                    answer = "remember { mutableStateOf(...) }"
                ),
                FlashcardEntity(
                    id = UUID.randomUUID().toString(),
                    deckId = deck4Id,
                    question = "¿Cuál es la diferencia entre 'data class' y una clase regular?",
                    answer = "data class genera automáticamente equals(), hashCode(), toString(), y copy()"
                )
            )

            dao.insertDecks(decks)
            dao.insertCards(cards)
            dao.insertOrUpdateUserStats(UserStatsEntity(id = 1, xp = 20, level = 1, hearts = 3, diamonds = 5, creepersDefeated = 0))
        }
    }
}
