package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.graphicsLayer
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
import com.example.ui.theme.McStoneShadow
import com.example.ui.theme.McTextPrimary
import com.example.ui.theme.McTextSecondary
import com.example.ui.viewmodel.FlashcardViewModel

@Composable
fun StudyScreen(
    viewModel: FlashcardViewModel,
    deckWithCards: DeckWithCards,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isShuffleMode by remember { mutableStateOf(false) }
    var currentCards by remember(deckWithCards, isShuffleMode) {
        mutableStateOf(if (isShuffleMode) deckWithCards.cards.shuffled() else deckWithCards.cards)
    }

    var currentIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }

    val totalCards = currentCards.size
    val currentCard = if (totalCards > 0 && currentIndex < totalCards) currentCards[currentIndex] else null

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
                text = "⬅️ VOLVER",
                onClick = onBack,
                style = MinecraftButtonStyle.STONE
            )

            // Mode Selector
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                MinecraftButton(
                    text = "📋 CLÁSICO",
                    onClick = {
                        isShuffleMode = false
                        currentIndex = 0
                        isFlipped = false
                    },
                    style = if (!isShuffleMode) MinecraftButtonStyle.GOLD else MinecraftButtonStyle.STONE
                )
                MinecraftButton(
                    text = "🔀 ALEATORIO",
                    onClick = {
                        isShuffleMode = true
                        currentIndex = 0
                        isFlipped = false
                    },
                    style = if (isShuffleMode) MinecraftButtonStyle.GOLD else MinecraftButtonStyle.STONE
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${deckWithCards.deck.emoji} ${deckWithCards.deck.title}",
            color = McGold,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Tarjeta ${if (totalCards > 0) currentIndex + 1 else 0} de $totalCards",
            color = McTextSecondary,
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (currentCard != null) {
            // 3D Flip Card Animation
            val rotation by animateFloatAsState(
                targetValue = if (isFlipped) 180f else 0f,
                animationSpec = tween(durationMillis = 350, easing = FastOutSlowInEasing),
                label = "CardFlipAnimation"
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .graphicsLayer {
                        rotationY = rotation
                        cameraDistance = 12 * density
                    }
                    .clickable {
                        viewModel.audioSynth.playClick()
                        isFlipped = !isFlipped
                    },
                contentAlignment = Alignment.Center
            ) {
                if (rotation <= 90f) {
                    // Front Face: Question
                    MinecraftCard(
                        modifier = Modifier.fillMaxSize(),
                        backgroundColor = Color(0xFF1E293B),
                        borderColorHighlight = McGold,
                        borderColorShadow = McStoneShadow,
                        contentPadding = 20.dp
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "PREGUNTA",
                                color = McGold,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = currentCard.question,
                                color = McTextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "👇 Toca para voltear tarjeta",
                                color = McTextSecondary,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                } else {
                    // Back Face: Answer
                    MinecraftCard(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer { rotationY = 180f },
                        backgroundColor = Color(0xFF064E3B),
                        borderColorHighlight = McEmerald,
                        borderColorShadow = Color(0xFF022C22),
                        contentPadding = 20.dp
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "RESPUESTA",
                                color = McEmerald,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = currentCard.answer,
                                color = McTextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Rating / Next Buttons
            if (isFlipped) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MinecraftButton(
                        text = "❌ INCORRECTO",
                        onClick = {
                            viewModel.audioSynth.playWrong()
                            isFlipped = false
                            if (currentIndex < totalCards - 1) {
                                currentIndex += 1
                            } else {
                                currentIndex = 0
                            }
                        },
                        style = MinecraftButtonStyle.RED_TNT,
                        modifier = Modifier.weight(1f)
                    )

                    MinecraftButton(
                        text = "✅ CORRECTO (+10 XP)",
                        onClick = {
                            viewModel.audioSynth.playCorrect()
                            viewModel.awardXp(10)
                            isFlipped = false
                            if (currentIndex < totalCards - 1) {
                                currentIndex += 1
                            } else {
                                currentIndex = 0
                            }
                        },
                        style = MinecraftButtonStyle.EMERALD,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                Text(
                    text = "Gira la tarjeta para evaluar tu respuesta",
                    color = McTextSecondary,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        } else {
            MinecraftCard(modifier = Modifier.fillMaxWidth(), contentPadding = 20.dp) {
                Text("No hay tarjetas disponibles en este mazo.", color = McTextPrimary)
            }
        }
    }
}
