package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DeckWithCards
import com.example.ui.components.MinecraftButton
import com.example.ui.components.MinecraftButtonStyle
import com.example.ui.components.MinecraftCard
import com.example.ui.theme.McEmerald
import com.example.ui.theme.McGold
import com.example.ui.theme.McRedTnt
import com.example.ui.theme.McStoneShadow
import com.example.ui.theme.McTextPrimary
import com.example.ui.theme.McTextSecondary
import com.example.ui.viewmodel.FlashcardViewModel

@Composable
fun ExamScreen(
    viewModel: FlashcardViewModel,
    deckWithCards: DeckWithCards,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cards = remember(deckWithCards) { deckWithCards.cards }
    val total = cards.size

    var currentIndex by remember { mutableIntStateOf(0) }
    var correctCount by remember { mutableIntStateOf(0) }
    var wrongCount by remember { mutableIntStateOf(0) }
    var isAnswerRevealed by remember { mutableStateOf(false) }
    var isFinished by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
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
                text = "📝 MODO EXAMEN",
                color = McGold,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isFinished) {
            // Exam Finished Summary Screen
            val percentage = if (total > 0) (correctCount.toFloat() / total * 100).toInt() else 0
            val bonusXp = percentage / 2 + 10

            MinecraftCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFF1E293B),
                borderColorHighlight = McGold,
                contentPadding = 20.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (percentage >= 70) "🎉 ¡EXAMEN APROBADO!" else "🗡️ ¡Sigue Practicando!",
                        color = if (percentage >= 70) McEmerald else McGold,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Puntuación Final: $percentage%",
                        color = McTextPrimary,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Aciertos: $correctCount / $total",
                        color = McEmerald,
                        fontSize = 14.sp
                    )

                    Text(
                        text = "Errores: $wrongCount / $total",
                        color = McRedTnt,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        MinecraftCard(
                            modifier = Modifier.fillMaxWidth(),
                            backgroundColor = Color(0xFF0F172A),
                            borderColorHighlight = McEmerald
                        ) {
                            Text(
                                text = "🎁 Recompensa: +$bonusXp XP",
                                color = McGold,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    MinecraftButton(
                        text = "REINTENTAR EXAMEN",
                        onClick = {
                            currentIndex = 0
                            correctCount = 0
                            wrongCount = 0
                            isAnswerRevealed = false
                            isFinished = false
                        },
                        style = MinecraftButtonStyle.GOLD,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    MinecraftButton(
                        text = "VOLVER AL MENÚ",
                        onClick = onBack,
                        style = MinecraftButtonStyle.STONE,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        } else if (total > 0 && currentIndex < total) {
            val currentCard = cards[currentIndex]
            val progress = (currentIndex.toFloat() / total.toFloat()).coerceIn(0f, 1f)

            // Progress Bar
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = McGold,
                trackColor = McStoneShadow
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Pregunta ${currentIndex + 1} de $total",
                color = McTextSecondary,
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Card
            MinecraftCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                backgroundColor = Color(0xFF1E293B),
                borderColorHighlight = McGold,
                contentPadding = 20.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "PREGUNTA ${currentIndex + 1}",
                        color = McGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentCard.question,
                        color = McTextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    if (isAnswerRevealed) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "RESPUESTA:",
                            color = McEmerald,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = currentCard.answer,
                            color = McEmerald,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (!isAnswerRevealed) {
                MinecraftButton(
                    text = "👁️ MOSTRAR RESPUESTA",
                    onClick = {
                        viewModel.audioSynth.playClick()
                        isAnswerRevealed = true
                    },
                    style = MinecraftButtonStyle.GOLD,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MinecraftButton(
                        text = "❌ INCORRECTO",
                        onClick = {
                            viewModel.audioSynth.playWrong()
                            wrongCount++
                            if (currentIndex < total - 1) {
                                currentIndex++
                                isAnswerRevealed = false
                            } else {
                                isFinished = true
                                val finalBonus = ((correctCount.toFloat() / total) * 50).toInt() + 10
                                viewModel.awardXp(finalBonus)
                                viewModel.audioSynth.playVictory()
                            }
                        },
                        style = MinecraftButtonStyle.RED_TNT,
                        modifier = Modifier.weight(1f)
                    )

                    MinecraftButton(
                        text = "✅ CORRECTO",
                        onClick = {
                            viewModel.audioSynth.playCorrect()
                            correctCount++
                            if (currentIndex < total - 1) {
                                currentIndex++
                                isAnswerRevealed = false
                            } else {
                                isFinished = true
                                val finalBonus = (((correctCount + 1).toFloat() / total) * 50).toInt() + 10
                                viewModel.awardXp(finalBonus)
                                viewModel.audioSynth.playVictory()
                            }
                        },
                        style = MinecraftButtonStyle.EMERALD,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
