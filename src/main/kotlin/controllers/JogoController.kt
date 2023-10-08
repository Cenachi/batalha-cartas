package controllers

import models.*
import tools.LeitorCartas
import view.ConsoleView

class JogoController(
    private val jogo: Jogo,
    private val view: ConsoleView
) {

    fun iniciarJogo() {
        view.exibirMensagem("Bem-vindo ao Jogo de Cartas Colecionáveis!")

        while (!jogo.oJogoTerminou()) {
            // Chama o método para jogar uma rodada do jogo
            jogo.jogarTurno()

            // Exibe uma mensagem indicando o início de uma nova rodada
            view.exibirMensagem("\nRodada atual:")

            // Exibir a pontuação final dos jogadores
            view.exibirPontuacao(jogo.getJogadores()[0])
            view.exibirPontuacao(jogo.getJogadores()[1])
        }

        view.exibirMensagem("\nFim do jogo!")

        view.exibirPontuacao(jogo.getJogadores()[0])
        view.exibirPontuacao(jogo.getJogadores()[1])

        // Obtém e exibe o vencedor do jogo, se houver um
        val vencedor = jogo.getVencedor()
        if (vencedor != null) {
            view.exibirMensagem("$vencedor venceu!")
        }
    }
}
