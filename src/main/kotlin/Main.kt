import controllers.JogoController
import models.*
import tools.LeitorCartas
import view.ConsoleView

fun main() {
    val view = ConsoleView() // Cria uma instância da ConsoleView para interagir com o usuário
    val baralho = Baralho(LeitorCartas.getCartas()) // Cria um baralho com as cartas obtidas do LeitorCartas
    val jogadores = mutableListOf<Jogador>()

    for (i in 1..2) {
        val nomeJogador = view.lerNomeJogador(i)
        val cartasDoJogador = mutableListOf<Carta>() // Cria uma lista de cartas vazia

        // Adiciona 5 cartas à lista do jogador
        for (j in 1..5) {
            val carta = baralho.puxarCarta()
            cartasDoJogador.add(carta)
        }

        // Cria uma instância de Mao para o jogador e passa a lista de cartas
        val jogador = Jogador(nomeJogador, Tabuleiro(), Mao(cartasDoJogador))
        jogadores.add(jogador)
    }

    // Cria uma instância do jogo com os jogadores e o baralho
    val jogo = Jogo(jogadores, baralho)

    // Cria uma instância do JogoController e inicia o jogo
    val jogoController = JogoController(jogo, view)
    jogoController.iniciarJogo()
}
