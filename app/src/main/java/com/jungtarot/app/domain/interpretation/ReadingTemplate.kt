package com.jungtarot.app.domain.interpretation

import com.jungtarot.app.data.CardMetadata
import com.jungtarot.app.data.SpreadType
import com.jungtarot.app.data.TarotRepository
import com.jungtarot.app.domain.TarotReading

private data class Arcano(
    val palabrasClave: String,
    val conceptoCentral: String
)

private val diccionario: Map<String, Arcano> = mapOf(
    "major_0_fool"        to Arcano("libertad, impulso, caos vital", "La energía del Self que busca encarnar. El vagabundo espiritual que no teme al vacío."),
    "major_1_magician"    to Arcano("inicio, destreza, potencial", "El ego joven que comienza a manejar las herramientas del mundo con asombro y voluntad."),
    "major_2_papess"      to Arcano("gestación, intuición, inconsciente", "El Ánima sabia. El conocimiento acumulado que espera en silencio ser revelado."),
    "major_3_empress"     to Arcano("creatividad, acción, estallido", "La Madre Naturaleza. La explosión creativa sin miedo al error ni a la forma."),
    "major_4_emperor"     to Arcano("estabilidad, padre, cimiento", "El Arquetipo del Padre. La autoridad que da orden y estructura a la materia."),
    "major_5_pope"        to Arcano("guía, mediador, puente", "El Hierofante. La comunicación sagrada entre lo humano y lo divino."),
    "major_6_lovers"      to Arcano("elección, unión, placer", "La salida de la infancia hacia la responsabilidad de amar y elegir con conciencia."),
    "major_7_chariot"     to Arcano("triunfo, conciencia, viaje", "El guerrero consciente que dirige su destino con equilibrio entre opuestos."),
    "major_8_justice"     to Arcano("rigor, equilibrio, verdad", "La capacidad de juzgarse a uno mismo con objetividad y sin autoengaño."),
    "major_9_hermit"      to Arcano("crisis, sabiduría, desapego", "El viejo sabio que ilumina el pasado para avanzar hacia el futuro sin aferrarse."),
    "major_10_wheel"      to Arcano("ciclo, destino, oportunidad", "El fin de un karma. El momento justo en que el universo invita a cambiar de estado."),
    "major_11_strength"   to Arcano("dominio, instinto, valor", "La conciencia que domestica la fuerza animal con amor, no con represión."),
    "major_12_hanged_man" to Arcano("pausa, entrega, meditación", "La inversión de valores. Ver el mundo desde el espíritu y no desde el ego."),
    "major_13_death"      to Arcano("mutación, limpieza, corte", "La muerte como renovación radical. Lo que termina hace espacio a lo que debe nacer."),
    "major_14_temperance" to Arcano("armonía, sanación, alquimia", "La mezcla de opuestos. El ángel que equilibra lo consciente e inconsciente."),
    "major_15_devil"      to Arcano("sombra, pasión, creatividad", "El encuentro con la Sombra junguiana. La fuerza creativa que pide ser integrada."),
    "major_16_tower"      to Arcano("liberación, apertura, gozo", "La ruptura de estructuras mentales rígidas. El despertar que llega como relámpago."),
    "major_17_star"       to Arcano("destino, esperanza, entrega", "El hallazgo del propio lugar en el cosmos. La pureza de dar sin esperar retorno."),
    "major_18_moon"       to Arcano("madre, misterio, imaginación", "La noche oscura del alma. El reino de los sueños y lo femenino más profundo."),
    "major_19_sun"        to Arcano("claridad, fraternidad, éxito", "El Sol central. La construcción de una nueva vida bajo la luz de la verdad."),
    "major_20_judgement"  to Arcano("despertar, llamado, conciencia", "El nacimiento del Hombre Nuevo. La llamada irrenunciable de la vocación."),
    "major_21_world"      to Arcano("plenitud, totalidad, realización", "La individuación completa. El éxtasis de ser uno con el Todo.")
)

class ReadingTemplate(private val repository: TarotRepository = TarotRepository()) {

    private fun nombre(card: CardMetadata): String =
        repository.spanishNames[card.card.id] ?: card.card.name

    fun buildReading(spreadType: SpreadType, cards: List<CardMetadata>): TarotReading {
        val response = when (spreadType) {
            SpreadType.LA_CRUZ         -> buildLaCruz(cards)
            SpreadType.LA_CRUZ_CELTA   -> buildCruzCelta(cards)
            SpreadType.LA_HERRADURA    -> buildHerradura(cards)
        }
        return TarotReading(response = response, cardDetails = cards)
    }

    private fun buildLaCruz(cards: List<CardMetadata>): String {
        val pasado   = cards[0]
        val presente = cards[1]
        val futuro   = cards[2]
        return buildString {
            append("El oráculo despliega el mapa de tu proceso.\n\n")
            append("Lo que viene del pasado y aún resuena en ti: ${nombre(pasado)}. ")
            diccionario[pasado.card.id]?.let { append("${it.conceptoCentral} Esta energía de ${it.palabrasClave} ha sido el suelo desde el que partes. ") }
            append("\n\nEl centro, lo que vives ahora: ${nombre(presente)}. ")
            diccionario[presente.card.id]?.let { append("${it.conceptoCentral} Aquí está el nudo: ${it.palabrasClave}. ") }
            append("\n\nLo que se abre hacia adelante si integras esta lección: ${nombre(futuro)}. ")
            diccionario[futuro.card.id]?.let { append("${it.conceptoCentral} El camino apunta hacia ${it.palabrasClave}. ") }
            append("\n\nLos tres Arcanos forman una sola frase que tu psique pronuncia. No hay azar aquí, solo sincronicidad.")
            append("\n\n¿Qué transformación estás dispuesto a abrazar?")
        }
    }

    private fun buildCruzCelta(cards: List<CardMetadata>): String {
        val situacion  = cards[0]
        val obstaculo  = cards[1]
        val pasado     = cards[2]
        val futuro     = cards[3]
        val resultado  = cards[4]
        return buildString {
            append("La Cruz Celta revela las capas de tu situación.\n\n")
            append("En el centro, lo que te define ahora: ${nombre(situacion)}. ")
            diccionario[situacion.card.id]?.let { append("${it.conceptoCentral} ") }
            append("\n\nLo que te cruza, el obstáculo o la energía que tensiona: ${nombre(obstaculo)}. ")
            diccionario[obstaculo.card.id]?.let { append("Aquí aparece ${it.palabrasClave}. No es tu enemigo, es tu maestro. ") }
            append("\n\nLo que el pasado dejó como herencia: ${nombre(pasado)}. ")
            diccionario[pasado.card.id]?.let { append("${it.conceptoCentral} ") }
            append("\n\nLo que se aproxima si continúas este camino: ${nombre(futuro)}. ")
            diccionario[futuro.card.id]?.let { append("La energía de ${it.palabrasClave} se acerca. ") }
            append("\n\nEl resultado probable, la síntesis de todo: ${nombre(resultado)}. ")
            diccionario[resultado.card.id]?.let { append("${it.conceptoCentral} ") }
            append("\n\nCinco espejos, una sola psique. La Cruz Celta no predice, revela lo que ya sabes en lo profundo.")
            append("\n\n¿Cuál de estas cinco energías sientes más urgente integrar?")
        }
    }

    private fun buildHerradura(cards: List<CardMetadata>): String {
        val pasadoLejano   = cards[0]
        val pasadoReciente = cards[1]
        val presente       = cards[2]
        val obstaculo      = cards[3]
        val externo        = cards[4]
        val consejo        = cards[5]
        val resultado      = cards[6]
        return buildString {
            append("La Herradura abre el arco completo de tu proceso.\n\n")
            append("El pasado lejano, la raíz de todo: ${nombre(pasadoLejano)}. ")
            diccionario[pasadoLejano.card.id]?.let { append("${it.conceptoCentral} ") }
            append("\n\nEl pasado reciente, lo que aún no termina de irse: ${nombre(pasadoReciente)}. ")
            diccionario[pasadoReciente.card.id]?.let { append("La energía de ${it.palabrasClave} todavía resuena. ") }
            append("\n\nEl presente, donde estás parado hoy: ${nombre(presente)}. ")
            diccionario[presente.card.id]?.let { append("${it.conceptoCentral} ") }
            append("\n\nEl obstáculo, lo que pide ser visto: ${nombre(obstaculo)}. ")
            diccionario[obstaculo.card.id]?.let { append("Aquí la psique señala ${it.palabrasClave}. ") }
            append("\n\nLas influencias externas, lo que viene del entorno: ${nombre(externo)}. ")
            diccionario[externo.card.id]?.let { append("${it.conceptoCentral} ") }
            append("\n\nEl consejo del oráculo: ${nombre(consejo)}. ")
            diccionario[consejo.card.id]?.let { append("La vía que se sugiere es ${it.palabrasClave}. ${it.conceptoCentral} ") }
            append("\n\nEl resultado probable si integras esta lectura: ${nombre(resultado)}. ")
            diccionario[resultado.card.id]?.let { append("${it.conceptoCentral} ") }
            append("\n\nSiete Arcanos, siete espejos de una misma alma en movimiento.")
            append("\n\n¿Qué parte de este mapa reconoces como verdadera?")
        }
    }
}
