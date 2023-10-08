package models

import kotlin.random.Random

class Baralho(private val baralho: MutableList<Carta>) {

    // Método para puxar uma carta aleatória do baralho
    fun puxarCarta(): Carta {
        val posicaoAleatoria = Random.nextInt(baralho.size)
        return baralho.removeAt(posicaoAleatoria)
    }

    // Método para verificar se o baralho está vazio
    fun estaVazio(): Boolean {
        return baralho.isEmpty()
    }
}