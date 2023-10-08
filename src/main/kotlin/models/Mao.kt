package models

class Mao(private val cartas: MutableList<Carta>) {

    // Método para adicionar uma carta à mão do jogador.
    fun adicionarCarta(carta: Carta) {
        cartas.add(carta)
    }

    // Método para remover uma carta da mão do jogador.
    fun removerCarta(carta: Carta) {
        cartas.remove(carta)
    }

    // Método para obter uma carta específica da mão do jogador com base em sua posição na mão.
    fun getCarta(posicao: Int): Carta {
        return cartas[posicao]
    }

    // Método para obter todas as cartas atualmente na mão do jogador.
    fun getCartas(): MutableList<Carta> {
        return cartas
    }
}
