package com.example.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.DeckEntity
import com.example.data.model.DeckWithCards
import com.example.data.model.FlashcardEntity
import com.example.data.model.UserStatsEntity
import com.example.data.repository.FlashcardRepository
import com.example.util.MinecraftAudioSynth
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

sealed class Screen {
    data object Dashboard : Screen()
    data class Study(val deckId: String) : Screen()
    data class Exam(val deckId: String) : Screen()
    data class CreeperBattle(val deckId: String) : Screen()
    data class CreeperMultipleChoice(val deckId: String) : Screen()
}

class FlashcardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FlashcardRepository
    val audioSynth = MinecraftAudioSynth()

    var currentScreen by mutableStateOf<Screen>(Screen.Dashboard)
        private set

    var isMuted by mutableStateOf(false)
        private set

    // Dialog state holders
    var showCreateDeckDialog by mutableStateOf(false)
    var showEditDeckDialog by mutableStateOf<DeckEntity?>(null)
    var showManageCardsDialog by mutableStateOf<DeckWithCards?>(null)
    var showExportDialog by mutableStateOf(false)
    var showImportDialog by mutableStateOf(false)
    var showInfoDialog by mutableStateOf(false)

    var exportJsonText by mutableStateOf("")
    var importJsonText by mutableStateOf("")
    var importStatusMessage by mutableStateOf<String?>(null)

    val decksWithCards: StateFlow<List<DeckWithCards>>
    val userStats: StateFlow<UserStatsEntity>

    init {
        val db = AppDatabase.getDatabase(application)
        repository = FlashcardRepository(db.flashcardDao())

        decksWithCards = repository.allDecksWithCards.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        userStats = repository.userStats.map { stats ->
            stats ?: UserStatsEntity(id = 1, xp = 0, level = 1, hearts = 3, diamonds = 0, creepersDefeated = 0)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserStatsEntity(id = 1, xp = 0, level = 1, hearts = 3, diamonds = 0, creepersDefeated = 0)
        )

        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    fun navigateTo(screen: Screen) {
        audioSynth.playClick()
        currentScreen = screen
    }

    fun toggleMute() {
        isMuted = !isMuted
        audioSynth.isMuted = isMuted
        if (!isMuted) audioSynth.playClick()
    }

    fun createDeck(title: String, description: String, emoji: String) {
        viewModelScope.launch {
            audioSynth.playClick()
            repository.createDeck(title, description, emoji)
            showCreateDeckDialog = false
        }
    }

    fun updateDeck(deck: DeckEntity) {
        viewModelScope.launch {
            audioSynth.playClick()
            repository.updateDeck(deck)
            showEditDeckDialog = null
        }
    }

    fun deleteDeck(deckId: String) {
        viewModelScope.launch {
            audioSynth.playWrong()
            repository.deleteDeck(deckId)
            showEditDeckDialog = null
        }
    }

    fun addCardToDeck(deckId: String, question: String, answer: String) {
        viewModelScope.launch {
            audioSynth.playClick()
            repository.addCardToDeck(deckId, question, answer)
        }
    }

    fun deleteCard(cardId: String) {
        viewModelScope.launch {
            audioSynth.playClick()
            repository.deleteCard(cardId)
        }
    }

    fun awardXp(amount: Int) {
        viewModelScope.launch {
            val current = userStats.value
            val newXp = current.xp + amount
            val newLevel = (newXp / 100) + 1
            val newDiamonds = newXp / 50

            if (newLevel > current.level) {
                audioSynth.playLevelUp()
            }

            repository.saveUserStats(
                current.copy(
                    xp = newXp,
                    level = newLevel,
                    diamonds = newDiamonds
                )
            )
        }
    }

    fun recordCreeperDefeated() {
        viewModelScope.launch {
            audioSynth.playVictory()
            val current = userStats.value
            val newXp = current.xp + 50
            val newDefeated = current.creepersDefeated + 1
            val newLevel = (newXp / 100) + 1
            val newDiamonds = newXp / 50

            repository.saveUserStats(
                current.copy(
                    xp = newXp,
                    level = newLevel,
                    diamonds = newDiamonds,
                    creepersDefeated = newDefeated
                )
            )
        }
    }

    fun prepareExport() {
        val currentDecks = decksWithCards.value
        val rootArray = JSONArray()

        for (item in currentDecks) {
            val deckObj = JSONObject()
            deckObj.put("id", item.deck.id)
            deckObj.put("title", item.deck.title)
            deckObj.put("description", item.deck.description)
            deckObj.put("emoji", item.deck.emoji)

            val cardsArray = JSONArray()
            for (card in item.cards) {
                val cardObj = JSONObject()
                cardObj.put("id", card.id)
                cardObj.put("question", card.question)
                cardObj.put("answer", card.answer)
                cardsArray.put(cardObj)
            }
            deckObj.put("cards", cardsArray)
            rootArray.put(deckObj)
        }

        exportJsonText = rootArray.toString(2)
        showExportDialog = true
    }

    fun importJson() {
        try {
            val trimmed = importJsonText.trim()
            val jsonArray = JSONArray(trimmed)
            val newDecks = mutableListOf<DeckEntity>()
            val newCards = mutableListOf<FlashcardEntity>()

            for (i in 0 until jsonArray.length()) {
                val deckObj = jsonArray.getJSONObject(i)
                val deckId = if (deckObj.has("id")) deckObj.getString("id") else UUID.randomUUID().toString()
                val title = deckObj.optString("title", "Mazo Importado")
                val description = deckObj.optString("description", "")
                val emoji = deckObj.optString("emoji", "⛏️")

                newDecks.add(DeckEntity(id = deckId, title = title, description = description, emoji = emoji))

                if (deckObj.has("cards")) {
                    val cardsArray = deckObj.getJSONArray("cards")
                    for (j in 0 until cardsArray.length()) {
                        val cardObj = cardsArray.getJSONObject(j)
                        val cardId = if (cardObj.has("id")) cardObj.getString("id") else UUID.randomUUID().toString()
                        val q = cardObj.optString("question", "")
                        val a = cardObj.optString("answer", "")
                        if (q.isNotBlank() && a.isNotBlank()) {
                            newCards.add(FlashcardEntity(id = cardId, deckId = deckId, question = q, answer = a))
                        }
                    }
                }
            }

            viewModelScope.launch {
                repository.insertDecksAndCards(newDecks, newCards)
                importStatusMessage = "¡Éxito! Se importaron ${newDecks.size} mazos y ${newCards.size} tarjetas."
                audioSynth.playLevelUp()
            }
        } catch (e: Exception) {
            importStatusMessage = "Error al parsear JSON: ${e.localizedMessage}"
            audioSynth.playWrong()
        }
    }
}
