package com.jungtarot.app.domain

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import com.jungtarot.app.BuildConfig
import com.jungtarot.app.data.CardMetadata
import com.jungtarot.app.data.SpreadType
import com.jungtarot.app.data.TarotRepository
import kotlinx.coroutines.delay
import java.io.IOException
import java.net.SocketTimeoutException

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
            retryWithExponentialBackoff(maxRetries = 3) {
                model.generateContent(prompt).text
                    ?: "El oráculo guarda silencio. Intenta de nuevo."
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error después de todos los reintentos", e)
            // All error messages remain in Spanish for user-facing experience
            when (e) {
                is IOException -> "Conexión perdida con el oráculo. Por favor, verifica tu conexión de internet."
                is SocketTimeoutException -> "El oráculo tardó demasiado en responder. Por favor, intenta de nuevo."
                is java.util.concurrent.TimeoutException -> "Se agotó el tiempo esperando al oráculo. Por favor, intenta de nuevo."
                is IllegalArgumentException -> "Solicitud inválida al oráculo. Por favor, intenta de nuevo."
                else -> "${e::class.java.simpleName}: ${e.message ?: "Error desconocido del oráculo"}"
            }
        }
    }

    /**
     * Retry mechanism with exponential backoff for API calls.
     * Retries up to 3 times with increasing delays: 2s, 4s, 8s
     */
    private suspend inline fun <T> retryWithExponentialBackoff(
        maxRetries: Int = 3,
        initialDelayMs: Long = 2000,
        crossinline block: suspend () -> T
    ): T {
        var lastException: Exception? = null
        
        repeat(maxRetries) { attemptNumber ->
            try {
                Log.d(TAG, "Intento ${attemptNumber + 1}/$maxRetries de conexión con Gemini API")
                val result = block()
                Log.d(TAG, "Solicitud a Gemini API exitosa en intento ${attemptNumber + 1}")
                return result
            } catch (e: Exception) {
                lastException = e
                val isRetryable = e.isRetryable()
                
                Log.w(
                    TAG,
                    "Intento ${attemptNumber + 1}/$maxRetries falló" +
                            if (isRetryable) " (reintentable): ${e.message}" else " (no reintentable): ${e.message}",
                    e
                )
                
                // Only retry on specific retryable errors
                if (!isRetryable || attemptNumber == maxRetries - 1) {
                    throw e
                }
                
                // Calculate exponential backoff delay: 2s, 4s, 8s (2^attemptNumber)
                val delayMs = initialDelayMs * (1L shl attemptNumber)
                Log.d(TAG, "Esperando ${delayMs}ms antes del siguiente reintento...")
                delay(delayMs)
            }
        }
        
        // Should never reach here due to throw in catch, but for safety
        throw lastException ?: Exception("Error desconocido después de $maxRetries reintentos")
    }

    /**
     * Determines if an exception is retryable.
     * Network and timeout errors are retryable; invalid arguments and other client errors are not.
     */
    private fun Exception.isRetryable(): Boolean = when (this) {
        is IOException -> true                           // Network errors
        is SocketTimeoutException -> true                // Socket timeout
        is java.util.concurrent.TimeoutException -> true  // General timeout
        is IllegalArgumentException -> false              // Invalid request - don't retry
        else -> false                                     // Unknown errors - don't retry
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
        private const val TAG = "GeminiService"
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
