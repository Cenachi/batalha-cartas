package tools

import models.Carta
import models.TipoCarta
import java.io.File
import java.io.InputStream
import java.util.*

class LeitorCartas {
    companion object {
        private lateinit var cartas: MutableList<Carta>

        // Método para obter a lista de cartas, lendo-as a partir do arquivo CSV se ainda não tiverem sido inicializadas.
        fun getCartas(): MutableList<Carta> {
            if (!::cartas.isInitialized) {
                cartas = lerCartasCSV().toMutableList()
            }
            return cartas
        }

        // Método privado para ler as cartas a partir de um arquivo CSV e convertê-las em objetos Carta.
        private fun lerCartasCSV(): List<Carta> {
            val streamDados: InputStream = File("cartas.csv").inputStream()
            val leitorStream = streamDados.bufferedReader()
            val cartas = mutableListOf<Carta>()

            // Lê as linhas do arquivo CSV, converte os dados em objetos Carta e os adiciona à lista de cartas.
            leitorStream.lineSequence()
                .filter { it.isNotBlank() } // Ignora linhas em branco
                .map { linha ->
                    val dados = linha.split(";")
                    if (dados.size == 5) {
                        val nome = dados[0]
                        val descricao = dados[1]
                        val tipo = when (dados[4].lowercase(Locale.getDefault())) {
                            "monstro" -> TipoCarta.MONSTRO
                            else -> TipoCarta.EQUIPAMENTO
                        }
                        val ataque = dados[2].toIntOrNull() ?: 0
                        val defesa = dados[3].toIntOrNull() ?: 0
                        Carta(nome, descricao, tipo, ataque, defesa)
                    } else {
                        null
                    }
                }
                .filterNotNull()
                .toList()
                .also { cartas.addAll(it) }

            return cartas
        }
    }
}
