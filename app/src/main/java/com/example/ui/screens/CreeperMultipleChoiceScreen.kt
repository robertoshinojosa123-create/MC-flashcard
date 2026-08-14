package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.DeckWithCards
import com.example.ui.components.MinecraftButton
import com.example.ui.components.MinecraftButtonStyle
import com.example.ui.components.MinecraftCard
import com.example.ui.theme.McEmerald
import com.example.ui.theme.McGold
import com.example.ui.theme.McRedTnt
import com.example.ui.theme.McStoneDark
import com.example.ui.theme.McTextPrimary
import com.example.ui.theme.McTextSecondary
import com.example.ui.viewmodel.FlashcardViewModel

@Composable
fun CreeperMultipleChoiceScreen(
    viewModel: FlashcardViewModel,
    deckWithCards: DeckWithCards,
    allDecks: List<DeckWithCards>,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cards = remember(deckWithCards) { deckWithCards.cards.shuffled() }
    val totalCards = cards.size

    var currentIndex by remember { mutableIntStateOf(0) }
    var streak by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var isCorrectOption by remember { mutableStateOf<Boolean?>(null) }
    var isQuizCompleted by remember { mutableStateOf(false) }

    // Collect distractors from all decks
    val allAnswers = remember(allDecks) {
        allDecks.flatMap { it.cards.map { c -> c.answer } }.distinct()
    }

    val currentCard = if (totalCards > 0 && currentIndex < totalCards) cards[currentIndex] else null

    // Prepare 4 options for current card
    val options = remember(currentCard) {
        if (currentCard != null) {
            val wrongOptions = allAnswers.filter { it != currentCard.answer }.shuffled().take(3)
            val fallbackDefaults = listOf(
                "Pico de Hierro",
                "Mesa de Trabajo",
                "Bloque de Redstone",
                "Manzana Dorada"
            ).filter { it != currentCard.answer }

            val combinedWrong = (wrongOptions + fallbackDefaults).distinct().take(3)
            (combinedWrong + currentCard.answer).shuffled()
        } else {
            emptyList()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MinecraftButton(
                text = "⬅️ SALIR",
                onClick = onBack,
                style = MinecraftButtonStyle.STONE
            )

            Text(
                text = "⚡ OPCIONES MÚLTIPLES",
                color = McGold,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (isQuizCompleted) {
            // Summary
            MinecraftCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFF064E3B),
                borderColorHighlight = McEmerald,
                contentPadding = 20.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🎉 ¡DESAFÍO COMPLETADO!",
                        color = McEmerald,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Racha máxima alcanzada: $streak aciertos seguidos",
                        color = McTextPrimary,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Recompensa: +${streak * 5 + 20} XP",
                        color = McGold,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    MinecraftButton(
                        text = "VOLVER AL MENÚ",
                        onClick = onBack,
                        style = MinecraftButtonStyle.GOLD,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        } else if (currentCard != null) {
            // Status Bar
            MinecraftCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = McStoneDark,
                borderColorHighlight = McGold,
                contentPadding = 8.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.img_creeper_boss),
                            contentDescription = "Creeper",
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Pregunta ${currentIndex + 1} de $totalCards",
                            color = McTextSecondary,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Text(
                        text = "🔥 Racha: $streak",
                        color = McGold,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Question Card
            MinecraftCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                backgroundColor = Color(0xFF1E293B),
                borderColorHighlight = McGold,
                contentPadding = 16.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = currentCard.question,
                        color = McTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Options List
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                options.forEach { optionText ->
                    val isThisSelected = selectedOption == optionText
                    val isThisCorrect = optionText == currentCard.answer

                    val btnStyle = when {
                        selectedOption == null -> MinecraftButtonStyle.STONE
                        isThisSelected && isThisCorrect -> MinecraftButtonStyle.EMERALD
                        isThisSelected && !isThisCorrect -> MinecraftButtonStyle.RED_TNT
                        !isThisSelected && isThisCorrect && selectedOption != null -> MinecraftButtonStyle.EMERALD
                        else -> MinecraftButtonStyle.STONE
                    }

                    MinecraftButton(
                        text = optionText,
                        onClick = {
                            if (selectedOption == null) {
                                selectedOption = optionText
                                if (optionText == currentCard.answer) {
                                    isCorrectOption = true
                                    streak++
                                    viewModel.audioSynth.playCorrect()
                                    viewModel.awardXp(10)
                                } else {
                                    isCorrectOption = false
                                    streak = 0
                                    viewModel.audioSynth.playCreeperHiss()
                                }
                            }
                        },
                        style = btnStyle,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Next button if selected
            if (selectedOption != null) {
                MinecraftButton(
                    text = if (currentIndex < totalCards - 1) "SIGUIENTE PREGUNTA ➡️" else "VER RESULTADOS 🏁",
                    onClick = {
                        selectedOption = null
                        isCorrectOption = null
                        if (currentIndex < totalCards - 1) {
                            currentIndex++
                        } else {
                            isQuizCompleted = true
                            viewModel.audioSynth.playVictory()
                        }
                    },
                    style = MinecraftButtonStyle.GOLD,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
