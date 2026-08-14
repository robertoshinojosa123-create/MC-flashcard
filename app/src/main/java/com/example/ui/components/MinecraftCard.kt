package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.McStoneDark
import com.example.ui.theme.McStoneLightBorder
import com.example.ui.theme.McStoneShadow

@Composable
fun MinecraftCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = McStoneDark,
    borderColorHighlight: Color = McStoneLightBorder,
    borderColorShadow: Color = McStoneShadow,
    borderWidth: Dp = 2.dp,
    contentPadding: Dp = 12.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .background(backgroundColor, RoundedCornerShape(2.dp))
            .drawBehind {
                val strokePx = borderWidth.toPx()
                // Top & Left highlight
                drawLine(
                    color = borderColorHighlight,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokePx
                )
                drawLine(
                    color = borderColorHighlight,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = strokePx
                )
                // Bottom & Right shadow
                drawLine(
                    color = borderColorShadow,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokePx
                )
                drawLine(
                    color = borderColorShadow,
                    start = Offset(size.width, 0f),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokePx
                )
            }
            .padding(contentPadding)
    ) {
        content()
    }
}
