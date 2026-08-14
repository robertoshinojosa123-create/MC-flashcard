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
import com.example.ui.theme.McRedDark
import com.example.ui.theme.McRedTnt
import com.example.ui.theme.McTextPrimary
import com.example.ui.theme.McTextSecondary
import com.example.ui.viewmodel.FlashcardViewModel

@Composable
fun CreeperBattleScreen(
    viewModel: FlashcardViewModel,
    deckWithCards: DeckWithCards,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cards = remember(deckWithCards) { deckWithCards.cards.shuffled() }
    val totalCards = cards.size

    var creeperHp by remember { mutableIntStateOf(100) }
    var playerHearts by remember { mutableIntStateOf(3) }
    var currentCardIndex by remember { mutableIntStateOf(0) }
    var isAnswerRevealed by remember { mutableStateOf(false) }

    var isVictory by remember { mutableStateOf(false) }
    var isGameOver by remember { mutableStateOf(false) }
    var attackMessage by remember { mutableStateOf<String?>(null) }

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
                text = "⬅️ HUIR",
                onClick = onBack,
                style = MinecraftButtonStyle.STONE
            )

            Text(
                text = "⚔️ BATALLA CREEPER BOSS",
                color = McRedTnt,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (isVictory) {
            // Victory Screen
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
                        text = "🏆 ¡CREEPER DERROTADO!",
                        color = McEmerald,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "¡Has defendido tu aldea con éxito!",
                        color = McTextPrimary,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "🎁 Recompensa Boss: +50 XP",
                        color = McGold,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    MinecraftButton(
                        text = "OTRA BATALLA",
                        onClick = {
                            creeperHp = 100
                            playerHearts = 3
                            currentCardIndex = 0
                            isAnswerRevealed = false
                            isVictory = false
                            isGameOver = false
                        },
                        style = MinecraftButtonStyle.EMERALD,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        } else if (isGameOver) {
            // Game Over Screen
            MinecraftCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFF450A0A),
                borderColorHighlight = McRedTnt,
                contentPadding = 20.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "💥 ¡SSTTT... BOOM!",
                        color = McRedTnt,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "El Creeper ha explotado. Te has quedado sin vidas.",
                        color = McTextPrimary,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    MinecraftButton(
                        text = "REINTENTAR BATALLA",
                        onClick = {
                            creeperHp = 100
                            playerHearts = 3
                            currentCardIndex = 0
                            isAnswerRevealed = false
                            isVictory = false
                            isGameOver = false
                        },
                        style = MinecraftButtonStyle.RED_TNT,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        } else if (totalCards > 0) {
            val currentCard = cards[currentCardIndex % totalCards]

            // Creeper Status Panel
            MinecraftCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFF181825),
                borderColorHighlight = McRedTnt,
                contentPadding = 12.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_creeper_boss),
                            contentDescription = "Creeper Boss",
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "👾 CREEPER BOSS",
                                    color = McRedTnt,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = "$creeperHp/100 HP",
                                    color = McRedTnt,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Creeper Health Bar
                            LinearProgressIndicator(
                                progress = { (creeperHp.toFloat() / 100f).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp),
                                color = McRedTnt,
                                trackColor = McRedDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Player Hearts Display
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tus Vidas:",
                            color = McTextSecondary,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Row {
                            repeat(3) { i ->
                                Text(
                                    text = if (i < playerHearts) "❤️ " else "🖤 ",
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Attack feedback message
            attackMessage?.let { msg ->
                Text(
                    text = msg,
                    color = if (msg.contains("daño")) McEmerald else McRedTnt,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            // Question Card
            MinecraftCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
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
                        text = "PREGUNTA DE ATAQUE",
                        color = McGold,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentCard.question,
                        color = McTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    if (isAnswerRevealed) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "RESPUESTA: ${currentCard.answer}",
                            color = McEmerald,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!isAnswerRevealed) {
                MinecraftButton(
                    text = "🗡️ ATACAR (REVELAR RESPUESTA)",
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
                        text = "❌ FALLO (RECIBIR DAÑO)",
                        onClick = {
                            viewModel.audioSynth.playCreeperHiss()
                            playerHearts--
                            attackMessage = "⚡ ¡El Creeper te atacó! Perdiste 1 vida."
                            isAnswerRevealed = false

                            if (playerHearts <= 0) {
                                isGameOver = true
                            } else {
                                currentCardIndex++
                            }
                        },
                        style = MinecraftButtonStyle.RED_TNT,
                        modifier = Modifier.weight(1f)
                    )

                    MinecraftButton(
                        text = "⚔️ ACIERTO (GOLPEAR BOSS)",
                        onClick = {
                            viewModel.audioSynth.playCorrect()
                            val damagePerHit = 25
                            val newHp = creeperHp - damagePerHit
                            attackMessage = "💥 ¡Golpe Crítico! Infligiste $damagePerHit de daño."
                            isAnswerRevealed = false

                            if (newHp <= 0) {
                                creeperHp = 0
                                isVictory = true
                                viewModel.recordCreeperDefeated()
                            } else {
                                creeperHp = newHp
                                currentCardIndex++
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
