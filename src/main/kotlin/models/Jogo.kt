package models

import java.util.*

class Jogo(private val jogadores: MutableList<Jogador>, private val baralho: Baralho) {
    private val scanner = Scanner(System.`in`)
    private val MAXIMO_CARTAS_NA_MAO = 10
    private var turno = 1

    // Verifica se o jogo terminou com base nas condições dadas.
    fun oJogoTerminou(): Boolean {
        return jogadores[0].pontosJogador <= 0 || jogadores[1].pontosJogador <= 0 || baralho.estaVazio()
    }

    // Inicia e gerência um turno de jogo.
    fun jogarTurno() {
        println("\nRodada $turno:")
        println("Pontuação de ${jogadores[0].nome}: ${jogadores[0].pontosJogador}")
        println("Pontuação de ${jogadores[1].nome}: ${jogadores[1].pontosJogador}")

        println("${jogadores[0].nome}, é a sua vez.")
        jogadores[0].reiniciarEstadoAtaqueMonstros() // Redefine o estado para o jogador 1
        executarTurnoDoJogador(jogadores[0], jogadores[1])

        if (oJogoTerminou()) {
            return
        }

        println("${jogadores[1].nome}, é a sua vez.")
        jogadores[1].reiniciarEstadoAtaqueMonstros() // Redefine o estado para o jogador 2
        executarTurnoDoJogador(jogadores[1], jogadores[0])

        if (oJogoTerminou()) {
            return
        }

        turno++
        receberCartaNaMao(jogadores[0])
        receberCartaNaMao(jogadores[1])
    }

    // Executa um turno do jogador atual.
    private fun executarTurnoDoJogador(jogadorAtual: Jogador, oponente: Jogador) {

        while (true) {
            println("Escolha uma ação:")
            println("1. Posicionar monstro no tabuleiro")
            println("2. Equipar monstro")
            println("3. Atacar")
            println("4. Alterar estado de monstro")
            println("5. Comprar uma carta")
            println("6. Descartar uma carta")
            println("7. Passar o turno")

            val jogadorDaVez = if (jogadorAtual == jogadores[0]) jogadores[1] else jogadores[0]
            val escolha = readInt()
            when (escolha) {
                1 -> {
                    if (jogadorAtual.getTabuleiro().tabuleiroEstaCheio()) {
                        println("Você não pode posicionar mais monstros no tabuleiro.")
                        jogadorAtual.getTabuleiro().mostrarTabuleiro()
                    } else if (!jogadorAtual.temCartaTipoMonstro()) {
                        println("${jogadorAtual.nome}, você não possui nenhuma carta de monstro na mão.")
                        println("Você pode comprar uma carta de monstro ou escolher outra ação.")
                    } else {
                        val cartaParaPosicionar =
                            selecionarCartaDaMão(jogadorAtual, "Selecione um monstro para posicionar:")
                        if (cartaParaPosicionar != null && cartaParaPosicionar.tipo == TipoCarta.MONSTRO) {
                            val estadoMonstro = selecionarEstadoDoMonstro()
                            jogadorAtual.colocarMonstroNoTabuleiro(cartaParaPosicionar, estadoMonstro)
                            jogadorAtual.getTabuleiro().mostrarTabuleiro()
                        } else {
                            println("Escolha inválida. Por favor, selecione um monstro para posicionar.")
                        }
                    }
                }

                2 -> {
                    val posicaoDoMonstroNoTabuleiro =
                        selecionarPosicaoDoMonstro(jogadorAtual, "Selecione o monstro para equipar:")
                    if (posicaoDoMonstroNoTabuleiro != -1) {
                        if (!jogadorAtual.temCartaTipoEquipamento()) {
                            println("${jogadorAtual.nome}, você não possui nenhuma carta de equipamento na mão.")
                            println("Você pode comprar uma carta de equipamento ou escolher outra ação.")
                        } else {
                            val cartaParaEquipar =
                                selecionarCartaDaMão(jogadorAtual, "Selecione um equipamento para o monstro:")
                            if (cartaParaEquipar != null && cartaParaEquipar.tipo == TipoCarta.EQUIPAMENTO) {
                                jogadorAtual.equiparMonstroComEquipamento(posicaoDoMonstroNoTabuleiro, cartaParaEquipar)
                                jogadorAtual.getTabuleiro().mostrarTabuleiro()
                            } else {
                                println("Escolha inválida. Por favor, selecione uma carta do tipo equipamento.")
                            }
                        }
                    }
                }

                3 -> {
                    if (turno == 1) {
                        println("Você não pode atacar no primeiro turno.")
                    } else if (jogadorAtual.podeAtacar()) {
                        val posicaoDoMonstroAtacanteNoTabuleiro =
                            selecionarPosicaoDoMonstro(jogadorAtual, "Selecione o monstro atacante:")
                        if (posicaoDoMonstroAtacanteNoTabuleiro != -1) {
                            val atacante =
                                jogadorAtual.getTabuleiro().getPosicoes()[posicaoDoMonstroAtacanteNoTabuleiro]

                            // Verifique o estado "atacouNestaRodada" antes de permitir o ataque
                            if (!atacante.atacouNestaRodada) {
                                if (!oponente.getTabuleiro().tabuleiroEstaVazio()) {
                                    val targetPosition =
                                        selecionarPosicaoDoMonstro(oponente, "Selecione o monstro alvo:")
                                    if (targetPosition != -1) {
                                        jogadorAtual.atacar(
                                            oponente,
                                            posicaoDoMonstroAtacanteNoTabuleiro,
                                            targetPosition
                                        )

                                        // Marque o estado "atacouNestaRodada" como true após o ataque
                                        atacante.atacouNestaRodada = true
                                        jogadorAtual.getTabuleiro().mostrarTabuleiro()
                                    }
                                } else {
                                    val damage = jogadorAtual.getTabuleiro()
                                        .getPosicoes()[posicaoDoMonstroAtacanteNoTabuleiro].carta.ataque
                                    oponente.pontosJogador -= damage
                                    println("${jogadorAtual.nome} infringiu $damage pontos de dano direto a ${oponente.nome}.")
                                }
                            } else {
                                println("Este monstro já atacou nesta rodada.")
                                jogadorAtual.getTabuleiro().mostrarTabuleiro()
                            }
                        }
                    } else if (jogadorAtual.getTabuleiro().tabuleiroEstaVazio()) {
                        println("Você não tem monstros no tabuleiro.")
                        jogadorAtual.getTabuleiro().mostrarTabuleiro()
                    } else {
                        println("Você só pode atacar com monstros em estado de ataque.")
                    }
                }

                4 -> {
                    val posicaoDoMonstroNoTabuleiro =
                        selecionarPosicaoDoMonstro(jogadorAtual, "Selecione o monstro para alterar o estado:")
                    if (posicaoDoMonstroNoTabuleiro != -1 && !jogadorAtual.getTabuleiro().getPosicoes()[posicaoDoMonstroNoTabuleiro].atacouNestaRodada) {
                        val novoEstadoDoMonstro = selecionarEstadoDoMonstro()
                        jogadorAtual.mudarEstadoMonstro(posicaoDoMonstroNoTabuleiro, novoEstadoDoMonstro)
                        jogadorAtual.getTabuleiro().mostrarTabuleiro()
                    } else {
                        println("Você só pode mudar o estado do monstro na próxima rodada.")
                    }
                }

                5 -> {
                    if (baralho.estaVazio()) {
                        println("Não há cartas disponíveis para comprar.")
                        return
                    } else if (!jogadorAtual.maoEstaCheia()) {
                        receberCartaNaMao(jogadorAtual)
                        println("${jogadorAtual.nome} comprou uma nova carta.")
                    }
                }

                6 -> {
                    val cartaParaDescartar = selecionarCartaDaMão(jogadorAtual, "Selecione uma carta para descartar:")
                    if (cartaParaDescartar != null) {
                        jogadorAtual.discartarCarta(cartaParaDescartar)
                        println("${jogadorAtual.nome} descartou a carta ${cartaParaDescartar.nome}.")
                    }
                }

                7 -> {
                    break
                }

                else -> {
                    println("Escolha inválida. Tente novamente.")
                }
            }
        }
        println("${jogadorAtual.nome}, seu turno terminou.")
    }

    private fun selecionarCartaDaMão(jogador: Jogador, mensagem: String): Carta? {
        println(mensagem)
        val mao = jogador.getMaoJogador()
        for (index in 0..<mao.getCartas().size) {
            val carta = mao.getCarta(index)
            println("${index + 1}. ${carta.nome} (${carta.tipo}) [Descrição: ${carta.descricao}, Ataque: ${carta.ataque}, Defesa: ${carta.defesa}]")
        }
        val index = readInt()
        return if (index >= 1 && index <= mao.getCartas().size) {
            mao.getCarta(index - 1)
        } else {
            null
        }
    }

    private fun selecionarEstadoDoMonstro(): EstadoMonstro {
        println("Selecione o estado do monstro:")
        println("1. Ataque")
        println("2. Defesa")
        val escolha = readInt()
        return if (escolha == 1) EstadoMonstro.ATAQUE else EstadoMonstro.DEFESA
    }

    // Seleciona a posição de um monstro no tabuleiro.
    private fun selecionarPosicaoDoMonstro(jogador: Jogador, mensagem: String): Int {
        println(mensagem)
        jogador.getTabuleiro().getPosicoes().forEachIndexed { index, position ->
            val estado = if (position.estado == EstadoMonstro.ATAQUE) "Ataque" else "Defesa"
            println("${index + 1}. ${position.carta.nome} (${position.carta.tipo}) [Descrição: ${position.carta.descricao}, Ataque: ${position.carta.ataque}, Defesa: ${position.carta.defesa}, Estado: $estado]")
        }
        val index = readInt()
        return if (index >= 1 && index <= jogador.getTabuleiro().getPosicoes().size) {
            index - 1
        } else {
            -1
        }
    }

    // Recebe uma carta do baralho e adiciona à mão do jogador, se possível.
    private fun receberCartaNaMao(jogador: Jogador) {
        val carta = baralho.puxarCarta()
        if (jogador.getMaoJogador().getCartas().size < MAXIMO_CARTAS_NA_MAO) {
            jogador.getMaoJogador().adicionarCarta(carta)
        }
    }

    // Obtém a lista de jogadores.
    fun getJogadores(): MutableList<Jogador> {
        return jogadores
    }

    // Obtém o vencedor do jogo, se houver.
    fun getVencedor(): String? {
        return when {
            jogadores[0].pontosJogador > jogadores[1].pontosJogador -> jogadores[0].nome
            jogadores[1].pontosJogador > jogadores[0].pontosJogador -> jogadores[1].nome
            else -> null
        }
    }

    // Lê um número inteiro da entrada do jogador.
    private fun readInt(): Int {
        return try {
            scanner.nextInt()
        } catch (e: InputMismatchException) {
            println("Entrada inválida. Tente novamente.")
            scanner.next()
            readInt()
        }
    }
}
