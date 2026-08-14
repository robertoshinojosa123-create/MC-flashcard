package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.McDiamond
import com.example.ui.theme.McDiamondDark
import com.example.ui.theme.McEmerald
import com.example.ui.theme.McEmeraldDark
import com.example.ui.theme.McGold
import com.example.ui.theme.McGoldDark
import com.example.ui.theme.McRedDark
import com.example.ui.theme.McRedTnt
import com.example.ui.theme.McStoneDark
import com.example.ui.theme.McStoneLightBorder
import com.example.ui.theme.McStoneMedium
import com.example.ui.theme.McStoneShadow
import com.example.ui.theme.McTextPrimary
import com.example.ui.theme.McTextSecondary

enum class MinecraftButtonStyle {
    STONE,
    EMERALD,
    GOLD,
    RED_TNT,
    DIAMOND
}

@Composable
fun MinecraftButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: MinecraftButtonStyle = MinecraftButtonStyle.STONE,
    enabled: Boolean = true,
    paddingValues: PaddingValues = PaddingValues(horizontal = 12.dp, vertical = 10.dp)
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val (bgColor, borderLight, borderDark, textColor) = when (style) {
        MinecraftButtonStyle.STONE -> {
            if (!enabled) {
                Quad(McStoneShadow, McStoneDark, Color(0xFF101014), McTextSecondary.copy(alpha = 0.5f))
            } else if (isPressed) {
                Quad(McStoneDark, McStoneShadow, McStoneLightBorder, McGold)
            } else {
                Quad(McStoneMedium, McStoneLightBorder, McStoneShadow, McTextPrimary)
            }
        }
        MinecraftButtonStyle.EMERALD -> {
            if (!enabled) {
                Quad(McStoneShadow, McStoneDark, Color(0xFF101014), McTextSecondary.copy(alpha = 0.5f))
            } else if (isPressed) {
                Quad(McEmeraldDark, Color(0xFF134E13), McEmerald, Color.White)
            } else {
                Quad(Color(0xFF2E7D32), McEmerald, McEmeraldDark, Color.White)
            }
        }
        MinecraftButtonStyle.GOLD -> {
            if (!enabled) {
                Quad(McStoneShadow, McStoneDark, Color(0xFF101014), McTextSecondary.copy(alpha = 0.5f))
            } else if (isPressed) {
                Quad(McGoldDark, Color(0xFF5C3D00), McGold, Color.White)
            } else {
                Quad(Color(0xFFB45309), McGold, McGoldDark, Color.White)
            }
        }
        MinecraftButtonStyle.RED_TNT -> {
            if (!enabled) {
                Quad(McStoneShadow, McStoneDark, Color(0xFF101014), McTextSecondary.copy(alpha = 0.5f))
            } else if (isPressed) {
                Quad(McRedDark, Color(0xFF450A0A), McRedTnt, Color.White)
            } else {
                Quad(Color(0xFFB91C1C), McRedTnt, McRedDark, Color.White)
            }
        }
        MinecraftButtonStyle.DIAMOND -> {
            if (!enabled) {
                Quad(McStoneShadow, McStoneDark, Color(0xFF101014), McTextSecondary.copy(alpha = 0.5f))
            } else if (isPressed) {
                Quad(McDiamondDark, Color(0xFF004D4D), McDiamond, Color.White)
            } else {
                Quad(Color(0xFF0891B2), McDiamond, McDiamondDark, Color.White)
            }
        }
    }

    val borderWidth = 2.dp

    Box(
        modifier = modifier
            .testTag("mc_button_${text.take(10).replace(" ", "_")}")
            .background(bgColor, RoundedCornerShape(2.dp))
            .drawBehind {
                val strokePx = borderWidth.toPx()
                // Top & Left highlight
                drawLine(
                    color = if (isPressed) borderDark else borderLight,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokePx
                )
                drawLine(
                    color = if (isPressed) borderDark else borderLight,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = strokePx
                )
                // Bottom & Right shadow
                drawLine(
                    color = if (isPressed) borderLight else borderDark,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokePx
                )
                drawLine(
                    color = if (isPressed) borderLight else borderDark,
                    start = Offset(size.width, 0f),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokePx
                )
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = enabled,
                onClick = onClick
            )
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            textAlign = TextAlign.Center
        )
    }
}

private data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
