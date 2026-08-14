package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserStatsEntity
import com.example.ui.theme.McDiamond
import com.example.ui.theme.McEmerald
import com.example.ui.theme.McEmeraldDark
import com.example.ui.theme.McGold
import com.example.ui.theme.McRedTnt
import com.example.ui.theme.McStoneDark
import com.example.ui.theme.McStoneLightBorder
import com.example.ui.theme.McStoneShadow
import com.example.ui.theme.McTextPrimary
import com.example.ui.theme.McTextSecondary

@Composable
fun HeaderBar(
    userStats: UserStatsEntity,
    isMuted: Boolean,
    onToggleMute: () -> Unit,
    onOpenInfoModal: () -> Unit,
    modifier: Modifier = Modifier
) {
    MinecraftCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = McStoneDark,
        borderColorHighlight = McStoneLightBorder,
        borderColorShadow = McStoneShadow,
        contentPadding = 8.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Title & Logo
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⛏️ MC FLASHCARDS",
                        color = McGold,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Actions: Mute & Info
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleMute,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isMuted) Icons.Default.VolumeMute else Icons.Default.VolumeUp,
                            contentDescription = if (isMuted) "Activar Sonido" else "Silenciar",
                            tint = if (isMuted) McTextSecondary else McEmerald
                        )
                    }

                    IconButton(
                        onClick = onOpenInfoModal,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Información de la App",
                            tint = McDiamond
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Stats Bar (Level, Hearts, Diamonds, Creepers Defeated)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Level Badge
                Box(
                    modifier = Modifier
                        .background(McEmeraldDark, RoundedCornerShape(2.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "LVL ${userStats.level}",
                        color = McEmerald,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Hearts
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "❤️ 3/3",
                        color = McRedTnt,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Diamonds
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "💎 ${userStats.diamonds}",
                        color = McDiamond,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Creepers Defeated
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "👾 ${userStats.creepersDefeated}",
                        color = McGold,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // XP Progress Bar
            val xpInCurrentLevel = userStats.xp % 100
            val xpProgress = (xpInCurrentLevel / 100f).coerceIn(0f, 1f)

            Column(modifier = Modifier.fillMaxWidth()) {
                LinearProgressIndicator(
                    progress = { xpProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp),
                    color = McEmerald,
                    trackColor = Color(0xFF102810)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "XP: ${userStats.xp}",
                        color = McTextSecondary,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = "$xpInCurrentLevel / 100 XP para Nivel ${userStats.level + 1}",
                        color = McEmerald,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }
    }
}
