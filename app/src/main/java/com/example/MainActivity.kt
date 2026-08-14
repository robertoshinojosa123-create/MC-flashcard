package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.HeaderBar
import com.example.ui.components.MinecraftButton
import com.example.ui.components.MinecraftButtonStyle
import com.example.ui.components.MinecraftCard
import com.example.ui.screens.CreeperBattleScreen
import com.example.ui.screens.CreeperMultipleChoiceScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.ExamScreen
import com.example.ui.screens.StudyScreen
import com.example.ui.theme.McDarkBg
import com.example.ui.theme.McTextPrimary
import com.example.ui.theme.MinecraftFlashcardsTheme
import com.example.ui.viewmodel.FlashcardViewModel
import com.example.ui.viewmodel.Screen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MinecraftFlashcardsTheme {
                MinecraftFlashcardsApp()
            }
        }
    }
}

@Composable
fun MinecraftFlashcardsApp(
    viewModel: FlashcardViewModel = viewModel()
) {
    val decks by viewModel.decksWithCards.collectAsStateWithLifecycle()
    val stats by viewModel.userStats.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(McDarkBg)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(McDarkBg)
                .padding(innerPadding)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            // Persistent Minecraft Header
            HeaderBar(
                userStats = stats,
                isMuted = viewModel.isMuted,
                onToggleMute = { viewModel.toggleMute() },
                onOpenInfoModal = { viewModel.showInfoDialog = true }
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Main Content Area based on active screen
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                when (val screen = viewModel.currentScreen) {
                    is Screen.Dashboard -> {
                        DashboardScreen(
                            viewModel = viewModel,
                            decks = decks,
                            onSelectStudy = { deckId -> viewModel.navigateTo(Screen.Study(deckId)) },
                            onSelectExam = { deckId -> viewModel.navigateTo(Screen.Exam(deckId)) },
                            onSelectCreeperBattle = { deckId -> viewModel.navigateTo(Screen.CreeperBattle(deckId)) },
                            onSelectCreeperMultipleChoice = { deckId -> viewModel.navigateTo(Screen.CreeperMultipleChoice(deckId)) }
                        )
                    }
                    is Screen.Study -> {
                        val deckWithCards = decks.find { it.deck.id == screen.deckId }
                        if (deckWithCards != null) {
                            StudyScreen(
                                viewModel = viewModel,
                                deckWithCards = deckWithCards,
                                onBack = { viewModel.navigateTo(Screen.Dashboard) }
                            )
                        } else {
                            DeckNotFoundState { viewModel.navigateTo(Screen.Dashboard) }
                        }
                    }
                    is Screen.Exam -> {
                        val deckWithCards = decks.find { it.deck.id == screen.deckId }
                        if (deckWithCards != null) {
                            ExamScreen(
                                viewModel = viewModel,
                                deckWithCards = deckWithCards,
                                onBack = { viewModel.navigateTo(Screen.Dashboard) }
                            )
                        } else {
                            DeckNotFoundState { viewModel.navigateTo(Screen.Dashboard) }
                        }
                    }
                    is Screen.CreeperBattle -> {
                        val deckWithCards = decks.find { it.deck.id == screen.deckId }
                        if (deckWithCards != null) {
                            CreeperBattleScreen(
                                viewModel = viewModel,
                                deckWithCards = deckWithCards,
                                onBack = { viewModel.navigateTo(Screen.Dashboard) }
                            )
                        } else {
                            DeckNotFoundState { viewModel.navigateTo(Screen.Dashboard) }
                        }
                    }
                    is Screen.CreeperMultipleChoice -> {
                        val deckWithCards = decks.find { it.deck.id == screen.deckId }
                        if (deckWithCards != null) {
                            CreeperMultipleChoiceScreen(
                                viewModel = viewModel,
                                deckWithCards = deckWithCards,
                                allDecks = decks,
                                onBack = { viewModel.navigateTo(Screen.Dashboard) }
                            )
                        } else {
                            DeckNotFoundState { viewModel.navigateTo(Screen.Dashboard) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeckNotFoundState(onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        MinecraftCard(modifier = Modifier.fillMaxWidth(), contentPadding = 20.dp) {
            Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                Text("Mazo no encontrado o eliminado.", color = McTextPrimary)
                Spacer(modifier = Modifier.height(12.dp))
                MinecraftButton(text = "Volver al Menú", onClick = onBack, style = MinecraftButtonStyle.STONE)
            }
        }
    }
}
