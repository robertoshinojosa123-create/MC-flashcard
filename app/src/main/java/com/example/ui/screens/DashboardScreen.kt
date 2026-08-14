package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DeckEntity
import com.example.data.model.DeckWithCards
import com.example.ui.components.MinecraftButton
import com.example.ui.components.MinecraftButtonStyle
import com.example.ui.components.MinecraftCard
import com.example.ui.theme.McEmerald
import com.example.ui.theme.McGold
import com.example.ui.theme.McRedTnt
import com.example.ui.theme.McStoneDark
import com.example.ui.theme.McStoneLightBorder
import com.example.ui.theme.McStoneShadow
import com.example.ui.theme.McTextPrimary
import com.example.ui.theme.McTextSecondary
import com.example.ui.viewmodel.FlashcardViewModel

@Composable
fun DashboardScreen(
    viewModel: FlashcardViewModel,
    decks: List<DeckWithCards>,
    onSelectStudy: (String) -> Unit,
    onSelectExam: (String) -> Unit,
    onSelectCreeperBattle: (String) -> Unit,
    onSelectCreeperMultipleChoice: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Hero Banner
        item {
            MinecraftCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFF1E293B),
                borderColorHighlight = McEmerald,
                borderColorShadow = Color(0xFF0F172A),
                contentPadding = 14.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Aprende Jugando en Estilo Minecraft",
                        color = McGold,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Estudia con tarjetas interactivas, haz exámenes y vence al Creeper Boss.",
                        color = McTextSecondary,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    MinecraftButton(
                        text = "+ CREAR NUEVO MAZO",
                        onClick = { viewModel.showCreateDeckDialog = true },
                        style = MinecraftButtonStyle.GOLD,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Action Row: Export / Import JSON
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MinecraftButton(
                    text = "📤 Exportar JSON",
                    onClick = { viewModel.prepareExport() },
                    style = MinecraftButtonStyle.STONE,
                    modifier = Modifier.weight(1f)
                )

                MinecraftButton(
                    text = "📥 Importar JSON",
                    onClick = {
                        viewModel.importStatusMessage = null
                        viewModel.showImportDialog = true
                    },
                    style = MinecraftButtonStyle.STONE,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Section Title
        item {
            Text(
                text = "MIS MAZOS DE TARJETAS (${decks.size})",
                color = McGold,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
            )
        }

        // Decks List
        if (decks.isEmpty()) {
            item {
                MinecraftCard(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = 20.dp
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📦 No hay mazos aún",
                            color = McTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Pulsa '+ CREAR NUEVO MAZO' para empezar a estudiar.",
                            color = McTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        } else {
            items(decks, key = { it.deck.id }) { item ->
                DeckItemCard(
                    deckWithCards = item,
                    onEdit = { viewModel.showEditDeckDialog = item.deck },
                    onManageCards = { viewModel.showManageCardsDialog = item },
                    onStudy = { onSelectStudy(item.deck.id) },
                    onExam = { onSelectExam(item.deck.id) },
                    onCreeperBattle = { onSelectCreeperBattle(item.deck.id) },
                    onCreeperMultipleChoice = { onSelectCreeperMultipleChoice(item.deck.id) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Modal: Create Deck
    if (viewModel.showCreateDeckDialog) {
        CreateDeckModal(
            onDismiss = { viewModel.showCreateDeckDialog = false },
            onCreate = { title, desc, emoji ->
                viewModel.createDeck(title, desc, emoji)
            }
        )
    }

    // Modal: Edit Deck
    viewModel.showEditDeckDialog?.let { deckToEdit ->
        EditDeckModal(
            deck = deckToEdit,
            onDismiss = { viewModel.showEditDeckDialog = null },
            onUpdate = { updated -> viewModel.updateDeck(updated) },
            onDelete = { viewModel.deleteDeck(deckToEdit.id) }
        )
    }

    // Modal: Manage Deck Cards
    viewModel.showManageCardsDialog?.let { deckWithCards ->
        ManageCardsModal(
            deckWithCards = deckWithCards,
            onDismiss = { viewModel.showManageCardsDialog = null },
            onAddCard = { q, a -> viewModel.addCardToDeck(deckWithCards.deck.id, q, a) },
            onDeleteCard = { cardId -> viewModel.deleteCard(cardId) }
        )
    }

    // Modal: Export JSON
    if (viewModel.showExportDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.showExportDialog = false },
            containerColor = McStoneDark,
            title = {
                Text("Exportar Mazos a JSON", color = McGold, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            },
            text = {
                Column {
                    Text("Copia este contenido JSON para guardar tu copia de seguridad:", color = McTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    SelectionContainer {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .background(McStoneShadow)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = viewModel.exportJsonText,
                                color = McTextPrimary,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            },
            confirmButton = {
                MinecraftButton(
                    text = "Copiar al Portapapeles",
                    onClick = {
                        clipboardManager.setText(AnnotatedString(viewModel.exportJsonText))
                        Toast.makeText(context, "JSON copiado al portapapeles", Toast.LENGTH_SHORT).show()
                        viewModel.showExportDialog = false
                    },
                    style = MinecraftButtonStyle.EMERALD
                )
            },
            dismissButton = {
                TextButton(onClick = { viewModel.showExportDialog = false }) {
                    Text("Cerrar", color = McTextSecondary)
                }
            }
        )
    }

    // Modal: Import JSON
    if (viewModel.showImportDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.showImportDialog = false },
            containerColor = McStoneDark,
            title = {
                Text("Importar Mazos JSON", color = McGold, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            },
            text = {
                Column {
                    Text("Pega el texto JSON de los mazos que deseas importar:", color = McTextSecondary, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = viewModel.importJsonText,
                        onValueChange = { viewModel.importJsonText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = McGold,
                            unfocusedBorderColor = McStoneLightBorder,
                            focusedTextColor = McTextPrimary,
                            unfocusedTextColor = McTextPrimary,
                            focusedContainerColor = McStoneShadow,
                            unfocusedContainerColor = McStoneShadow
                        )
                    )
                    viewModel.importStatusMessage?.let { msg ->
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = msg, color = if (msg.startsWith("¡Éxito")) McEmerald else McRedTnt, fontSize = 12.sp)
                    }
                }
            },
            confirmButton = {
                MinecraftButton(
                    text = "Procesar Importación",
                    onClick = { viewModel.importJson() },
                    style = MinecraftButtonStyle.EMERALD
                )
            },
            dismissButton = {
                TextButton(onClick = { viewModel.showImportDialog = false }) {
                    Text("Cerrar", color = McTextSecondary)
                }
            }
        )
    }

    // Modal: App Info / Install Modal
    if (viewModel.showInfoDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.showInfoDialog = false },
            containerColor = McStoneDark,
            title = {
                Text("⛏️ Acerca de MC Flashcards", color = McGold, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "MC Flashcards es una aplicación de tarjetas didácticas retro en estilo Minecraft con modos de estudio, examen y batallas contra el Creeper Boss.",
                        color = McTextPrimary,
                        fontSize = 13.sp
                    )
                    HorizontalDivider(color = McStoneLightBorder)
                    Text(
                        text = "📲 Características:",
                        color = McEmerald,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "• Tarjetas volteables con modo clásico y aleatorio\n• Modo Examen con puntuación y premios de XP\n• Batalla Creeper Boss y Desafío de Opciones Múltiples\n• Copias de seguridad en formato JSON offline",
                        color = McTextSecondary,
                        fontSize = 12.sp
                    )
                }
            },
            confirmButton = {
                MinecraftButton(
                    text = "¡Entendido!",
                    onClick = { viewModel.showInfoDialog = false },
                    style = MinecraftButtonStyle.GOLD
                )
            }
        )
    }
}

@Composable
fun DeckItemCard(
    deckWithCards: DeckWithCards,
    onEdit: () -> Unit,
    onManageCards: () -> Unit,
    onStudy: () -> Unit,
    onExam: () -> Unit,
    onCreeperBattle: () -> Unit,
    onCreeperMultipleChoice: () -> Unit
) {
    val cardCount = deckWithCards.cards.size
    val hasCards = cardCount > 0

    MinecraftCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = McStoneDark,
        contentPadding = 12.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Title & Actions Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = deckWithCards.deck.emoji,
                        fontSize = 22.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Column {
                        Text(
                            text = deckWithCards.deck.title,
                            color = McTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "$cardCount Tarjeta(s)",
                            color = McGold,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Row {
                    IconButton(onClick = onManageCards, modifier = Modifier.size(36.dp)) {
                        Icon(imageVector = Icons.Default.LibraryAdd, contentDescription = "Gestionar Tarjetas", tint = McEmerald)
                    }
                    IconButton(onClick = onEdit, modifier = Modifier.size(36.dp)) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar Mazo", tint = McGold)
                    }
                }
            }

            if (deckWithCards.deck.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = deckWithCards.deck.description,
                    color = McTextSecondary,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Symmetric 2x2 Grid of Action Mode Buttons
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    MinecraftButton(
                        text = "📖 ESTUDIAR",
                        onClick = onStudy,
                        enabled = hasCards,
                        style = MinecraftButtonStyle.STONE,
                        modifier = Modifier.weight(1f)
                    )
                    MinecraftButton(
                        text = "📝 EXAMEN",
                        onClick = onExam,
                        enabled = hasCards,
                        style = MinecraftButtonStyle.GOLD,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    MinecraftButton(
                        text = "⚔️ BATALLA BOSS",
                        onClick = onCreeperBattle,
                        enabled = hasCards,
                        style = MinecraftButtonStyle.RED_TNT,
                        modifier = Modifier.weight(1f)
                    )
                    MinecraftButton(
                        text = "⚡ OPC. MÚLTIPLES",
                        onClick = onCreeperMultipleChoice,
                        enabled = hasCards,
                        style = MinecraftButtonStyle.EMERALD,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun CreateDeckModal(
    onDismiss: () -> Unit,
    onCreate: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var emoji by remember { mutableStateOf("⛏️") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = McStoneDark,
        title = {
            Text("⛏️ Crear Nuevo Mazo", color = McGold, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título del Mazo") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = McGold,
                        focusedLabelColor = McGold,
                        focusedTextColor = McTextPrimary,
                        unfocusedTextColor = McTextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Descripción") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = McGold,
                        focusedLabelColor = McGold,
                        focusedTextColor = McTextPrimary,
                        unfocusedTextColor = McTextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = emoji,
                    onValueChange = { emoji = it },
                    label = { Text("Ícono Emoji (ej. ⛏️, 💻, 🧠)") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = McGold,
                        focusedLabelColor = McGold,
                        focusedTextColor = McTextPrimary,
                        unfocusedTextColor = McTextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            MinecraftButton(
                text = "Crear Mazo",
                onClick = { onCreate(title, desc, emoji) },
                enabled = title.isNotBlank(),
                style = MinecraftButtonStyle.EMERALD
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = McTextSecondary)
            }
        }
    )
}

@Composable
fun EditDeckModal(
    deck: DeckEntity,
    onDismiss: () -> Unit,
    onUpdate: (DeckEntity) -> Unit,
    onDelete: () -> Unit
) {
    var title by remember { mutableStateOf(deck.title) }
    var desc by remember { mutableStateOf(deck.description) }
    var emoji by remember { mutableStateOf(deck.emoji) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = McStoneDark,
        title = {
            Text("✏️ Editar Mazo", color = McGold, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = McGold, focusedTextColor = McTextPrimary, unfocusedTextColor = McTextPrimary),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Descripción") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = McGold, focusedTextColor = McTextPrimary, unfocusedTextColor = McTextPrimary),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = emoji,
                    onValueChange = { emoji = it },
                    label = { Text("Emoji") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = McGold, focusedTextColor = McTextPrimary, unfocusedTextColor = McTextPrimary),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                MinecraftButton(
                    text = "Eliminar",
                    onClick = onDelete,
                    style = MinecraftButtonStyle.RED_TNT
                )
                MinecraftButton(
                    text = "Guardar",
                    onClick = { onUpdate(deck.copy(title = title, description = desc, emoji = emoji)) },
                    style = MinecraftButtonStyle.EMERALD
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = McTextSecondary)
            }
        }
    )
}

@Composable
fun ManageCardsModal(
    deckWithCards: DeckWithCards,
    onDismiss: () -> Unit,
    onAddCard: (String, String) -> Unit,
    onDeleteCard: (String) -> Unit
) {
    var newQuestion by remember { mutableStateOf("") }
    var newAnswer by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = McStoneDark,
        title = {
            Text("🎴 Tarjetas de ${deckWithCards.deck.title}", color = McGold, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        },
        text = {
            Column(modifier = Modifier.height(340.dp)) {
                // Add Card Form
                Text("Añadir Nueva Tarjeta:", color = McEmerald, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = newQuestion,
                    onValueChange = { newQuestion = it },
                    label = { Text("Pregunta") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = McEmerald, focusedTextColor = McTextPrimary, unfocusedTextColor = McTextPrimary),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = newAnswer,
                    onValueChange = { newAnswer = it },
                    label = { Text("Respuesta") },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = McEmerald, focusedTextColor = McTextPrimary, unfocusedTextColor = McTextPrimary),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                MinecraftButton(
                    text = "+ AÑADIR TARJETA",
                    onClick = {
                        onAddCard(newQuestion, newAnswer)
                        newQuestion = ""
                        newAnswer = ""
                    },
                    enabled = newQuestion.isNotBlank() && newAnswer.isNotBlank(),
                    style = MinecraftButtonStyle.EMERALD,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = McStoneLightBorder)
                Spacer(modifier = Modifier.height(6.dp))

                // Cards List
                Text("Tarjetas Existentes (${deckWithCards.cards.size}):", color = McTextSecondary, fontSize = 12.sp)
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(deckWithCards.cards, key = { it.id }) { card ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(McStoneShadow)
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = "P: ${card.question}", color = McTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text(text = "R: ${card.answer}", color = McEmerald, fontSize = 11.sp)
                            }
                            IconButton(onClick = { onDeleteCard(card.id) }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", tint = McRedTnt)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            MinecraftButton(text = "Listo", onClick = onDismiss, style = MinecraftButtonStyle.STONE)
        }
    )
}
