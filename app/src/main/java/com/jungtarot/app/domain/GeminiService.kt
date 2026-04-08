package com.jungtarot.app.domain

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.jungtarot.app.BuildConfig
import com.jungtarot.app.data.CardMetadata
import com.jungtarot.app.data.SpreadType
import com.jungtarot.app.data.TarotRepository

class GeminiService(private val repository: TarotRepository = TarotRepository()) {

    private val model = GenerativeModel(
        modelName = "gemini-2.0-flash-001",
        apiKey = BuildConfig.GEMINI_API_KEY,
        generationConfig = generationConfig {
            temperature = 0.9f
            maxOutputTokens = 800
        },
        systemInstruction = content {
            text(SYSTEM_PROMPT)
        }
    )

    suspend fun generateReading(spreadType: SpreadType, cards: List<CardMetadata>): String {
        val spanishNames = repository.spanishNames
        val prompt = buildPrompt(spreadType, cards, spanishNames)
        return try {
            model.generateContent(prompt).text
                ?: "El oráculo guarda silencio. Intenta de nuevo."
        } catch (e: Exception) {
            android.util.Log.e("GeminiService", "Error completo", e)
            "${e::class.java.name}: ${e.message}\n\nCausa: ${e.cause?.message}"
        }
    }

    private fun buildPrompt(
        spreadType: SpreadType,
        cards: List<CardMetadata>,
        spanishNames: Map<String, String>
    ): String {
        val tirada = when (spreadType) {
            SpreadType.LA_CRUZ -> {
                val posiciones = listOf("Pasado", "Presente", "Futuro")
                cards.mapIndexed { i, c ->
                    "- ${posiciones[i]}: ${spanishNames[c.card.id] ?: c.card.name}"
                }.joinToString("\n")
            }
            SpreadType.LA_CRUZ_CELTA -> {
                val posiciones = listOf("Situación central", "Lo que cruza (obstáculo)", "Pasado", "Futuro inmediato", "Resultado probable")
                cards.mapIndexed { i, c ->
                    "- ${posiciones[i]}: ${spanishNames[c.card.id] ?: c.card.name}"
                }.joinToString("\n")
            }
            SpreadType.LA_HERRADURA -> {
                val posiciones = listOf("Pasado lejano", "Pasado reciente", "Presente", "Obstáculo", "Influencias externas", "Consejo", "Resultado probable")
                cards.mapIndexed { i, c ->
                    "- ${posiciones[i]}: ${spanishNames[c.card.id] ?: c.card.name}"
                }.joinToString("\n")
            }
        }

        return """
Tirada: ${spreadType.displayName()}

Cartas:
$tirada

Realiza una lectura de tarot real, narrativa y profunda para esta tirada. 
Habla directamente al consultante en segunda persona. 
No listes las cartas una por una. Narra la lectura como un todo coherente donde las cartas se relacionan entre sí.
Termina con una pregunta de introspección.
        """.trimIndent()
    }

    companion object {
        private val SYSTEM_PROMPT = """
Eres un maestro de Tarot Evolutivo que fusiona la sabiduría de Alejandro Jodorowsky (La Vía del Tarot) con la psicología analítica de Carl Jung. Tu objetivo no es predecir el futuro, sino ayudar al consultante a comprender su presente como un espejo de su psique.

REGLAS:
- Sin juicio moral. No hay cartas buenas ni malas. El Arcano Sin Nombre es transformación; El Diablo es creatividad e instinto amordazado.
- Habla siempre en español, en tono místico, psicológico y poético.
- Las cartas se hablan entre sí. Nunca las interpretes de forma aislada.
- Narra una historia coherente con la dinámica entre posiciones.
- Usa los arquetipos junguianos: Sombra, Ánima, Self, Individuación, Sincronicidad.
- El pasado está a la izquierda, el futuro a la derecha. Los personajes que se miran crean diálogo; los que se dan la espalda, tensión.
- Responde siempre en segunda persona, directamente al consultante.
- Máximo 4 párrafos. Sin listas. Sin títulos. Solo narrativa fluida.
- Termina siempre con una pregunta de introspección al consultante.

ARQUETIPOS POR ARCANO:
0 El Loco: libertad, impulso vital, el Self que busca encarnar.
I El Mago: ego joven, voluntad, inicio consciente.
II La Papisa: Ánima sabia, gestación, conocimiento oculto.
III La Emperatriz: creatividad explosiva, Madre Naturaleza.
IIII El Emperador: orden, Arquetipo del Padre, estructura.
V El Papa: mediación, puente entre humano y divino.
VI El Enamorado: elección consciente, salida de la infancia.
VII El Carro: triunfo del ego consciente, equilibrio de opuestos.
VIII La Justicia: objetividad, equilibrio interno, verdad sin autoengaño.
VIIII El Ermitaño: sabiduría del desapego, luz interior.
X La Rueda de la Fortuna: fin de karma, cambio de ciclo.
XI La Fuerza: domesticar el instinto con amor, no represión.
XII El Colgado: inversión de valores, ver desde el espíritu.
XIII Arcano Sin Nombre: muerte como renovación radical.
XIIII La Templanza: alquimia de opuestos, sanación.
XV El Diablo: Sombra junguiana, fuerza creativa reprimida.
XVI La Torre: ruptura de estructuras rígidas, despertar.
XVII La Estrella: esperanza, entrega al propio destino.
XVIII La Luna: noche oscura del alma, lo femenino profundo.
XVIIII El Sol: claridad, éxito, nueva vida bajo la verdad.
XX El Juicio: llamada de la vocación, nacimiento del Hombre Nuevo.
XXI El Mundo: individuación completa, plenitud del Self.
        """.trimIndent()
    }
}
