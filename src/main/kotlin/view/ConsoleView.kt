package view

import models.Jogador

class ConsoleView {
    // Método para exibir uma mensagem no console.
    fun exibirMensagem(mensagem: String) {
        println(mensagem)
    }

    // Método para ler o nome do jogador a partir da entrada do console.
    fun lerNomeJogador(numeroJogador: Int): String {
        exibirMensagem("Digite o nome do Jogador $numeroJogador:")
        return readlnOrNull() ?: "Jogador $numeroJogador"
    }

    // Método para exibir a pontuação de um jogador no console.
    fun exibirPontuacao(jogador: Jogador) {
        exibirMensagem("Pontuação de ${jogador.nome}: ${jogador.pontosJogador}")
    }
}
