package com.example.data.repository

import com.example.data.model.DeckEntity
import com.example.data.model.FlashcardEntity
import java.util.UUID

object DefaultData {

    fun getDefaultDecksWithCards(): List<Pair<DeckEntity, List<FlashcardEntity>>> {
        val deck1Id = UUID.randomUUID().toString()
        val deck1 = DeckEntity(
            id = deck1Id,
            title = "Geografía y Crafteo Minecraft",
            description = "Preguntas esenciales sobre biomas, minerales y mecánicas de crafteo.",
            emoji = "⛏️"
        )
        val cards1 = listOf(
            FlashcardEntity(deckId = deck1Id, question = "¿En qué capa Y se encuentra principalmente el Diamante en la versión 1.18+?", answer = "En la capa Y = -58 a Y = -64"),
            FlashcardEntity(deckId = deck1Id, question = "¿Qué objeto necesitas para domesticar a un Lobo?", answer = "Huesos"),
            FlashcardEntity(deckId = deck1Id, question = "¿Cómo se forma el bloque de Obsidiana?", answer = "Al juntar agua corriente con lava estática"),
            FlashcardEntity(deckId = deck1Id, question = "¿Qué objeto bloquea el 100% del daño de un impacto de Creeper?", answer = "El Escudo"),
            FlashcardEntity(deckId = deck1Id, question = "¿Qué flor se utiliza para craftear tinte azul en Minecraft?", answer = "El Aciano (Cornflower)"),
            FlashcardEntity(deckId = deck1Id, question = "¿Qué alimento se necesita para criar Pandas?", answer = "Bambú")
        )

        val deck2Id = UUID.randomUUID().toString()
        val deck2 = DeckEntity(
            id = deck2Id,
            title = "Programación Kotlin & Android",
            description = "Conceptos fundamentales de desarrollo de aplicaciones con Jetpack Compose.",
            emoji = "💻"
        )
        val cards2 = listOf(
            FlashcardEntity(deckId = deck2Id, question = "¿Qué palabra clave declara una variable inmutable en Kotlin?", answer = "val"),
            FlashcardEntity(deckId = deck2Id, question = "¿Qué composable se usa para mostrar listas verticales desplazables?", answer = "LazyColumn"),
            FlashcardEntity(deckId = deck2Id, question = "¿Qué biblioteca oficial reemplaza SQLite directo en Android?", answer = "Room Database"),
            FlashcardEntity(deckId = deck2Id, question = "¿Qué contenedor organiza elementos horizontalmente en Jetpack Compose?", answer = "Row"),
            FlashcardEntity(deckId = deck2Id, question = "¿Qué anotación indica un componente de interfaz visual en Compose?", answer = "@Composable")
        )

        val deck3Id = UUID.randomUUID().toString()
        val deck3 = DeckEntity(
            id = deck3Id,
            title = "Inglés Gamer & Vocabulario",
            description = "Términos y palabras habituales en videojuegos en inglés.",
            emoji = "🇬🇧"
        )
        val cards3 = listOf(
            FlashcardEntity(deckId = deck3Id, question = "¿Qué significa el término 'Crafting'?", answer = "Elaboración / Fabricación de objetos"),
            FlashcardEntity(deckId = deck3Id, question = "¿Qué significa el verbo 'Enchant'?", answer = "Encantar o aplicar magia a un ítem"),
            FlashcardEntity(deckId = deck3Id, question = "¿Qué es un 'Brewing Stand'?", answer = "Soporte para elaboración de pociones"),
            FlashcardEntity(deckId = deck3Id, question = "¿Qué traducción tiene 'Smelt'?", answer = "Fundir o procesar minerales en el horno"),
            FlashcardEntity(deckId = deck3Id, question = "¿Qué significa 'Cooldown'?", answer = "Tiempo de recarga antes de volver a usar una habilidad")
        )

        val deck4Id = UUID.randomUUID().toString()
        val deck4 = DeckEntity(
            id = deck4Id,
            title = "Ciencia y Naturaleza",
            description = "Preguntas sobre cultura general, biología y física.",
            emoji = "🧪"
        )
        val cards4 = listOf(
            FlashcardEntity(deckId = deck4Id, question = "¿Cuál es el gas más abundante en la atmósfera terrestre?", answer = "Nitrógeno (78%)"),
            FlashcardEntity(deckId = deck4Id, question = "¿Cuál es el planeta más grande de nuestro sistema solar?", answer = "Júpiter"),
            FlashcardEntity(deckId = deck4Id, question = "¿Cuál es el elemento químico con el símbolo Au?", answer = "El Oro"),
            FlashcardEntity(deckId = deck4Id, question = "¿En qué órgano humano se produce la insulina?", answer = "En el Páncreas")
        )

        return listOf(
            deck1 to cards1,
            deck2 to cards2,
            deck3 to cards3,
            deck4 to cards4
        )
    }
}
