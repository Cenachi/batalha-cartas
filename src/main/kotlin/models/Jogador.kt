package models

class Jogador(val nome: String, private val tabuleiro: Tabuleiro, private val mao: Mao) {
    private val MAXIMO_MONSTROS_NO_TABULEIRO = 5
    private val MAXIMO_CARTAS_NA_MAO = 10
    var pontosJogador = 10000

    // Método para descartar uma carta da mão do jogador
    fun discartarCarta(carta: Carta) {
        mao.removerCarta(carta)
    }

    // Método para colocar um monstro no tabuleiro
    fun colocarMonstroNoTabuleiro(carta: Carta, estado: EstadoMonstro) {
        // Verifica se o tabuleiro não está cheio
        if (tabuleiro.getPosicoes().size < MAXIMO_MONSTROS_NO_TABULEIRO) {
            tabuleiro.adicionarMonstro(carta, estado)
            mao.removerCarta(carta)
        }
    }

    // Método para equipar um monstro com um equipamento
    fun equiparMonstroComEquipamento(monsterPosition: Int, equipamento: Carta) {
        // Verifica se a posição do monstro é válida
        if (monsterPosition in 0..<tabuleiro.getPosicoes().size) {
            val boardPosition = tabuleiro.getPosicoes()[monsterPosition]
            if (boardPosition.carta.tipo == TipoCarta.MONSTRO) {
                // Atualiza os atributos de ataque e defesa do monstro
                boardPosition.carta.ataque += equipamento.ataque
                boardPosition.carta.defesa += equipamento.defesa

                // Remove a carta equipada da mão do jogador
                mao.removerCarta(equipamento)
            }
        }
    }

    // Método para realizar um ataque contra o oponente
    fun atacar(oponente: Jogador, posicaoMonstroAtacante: Int, posicaoOponente: Int) {
        val atacante = tabuleiro.getPosicoes()[posicaoMonstroAtacante]
        val alvo = oponente.tabuleiro.getPosicoes()[posicaoOponente]

        if (atacante.estado == EstadoMonstro.ATAQUE) {
            if (alvo.estado == EstadoMonstro.DEFESA) {
                if (atacante.carta.ataque > alvo.carta.defesa) {
                    // Atacante derrotou o alvo
                    oponente.tabuleiro.removerMonstro(posicaoOponente)
                    println("${atacante.carta.nome} derrotou ${alvo.carta.nome}.")
                } else {
                    // O atacante não conseguiu derrotar o alvo
                    pontosJogador -= (alvo.carta.defesa - atacante.carta.ataque)
                    println("${alvo.carta.nome} defendeu o ataque de ${atacante.carta.nome}.")
                }
            } else if (atacante.carta.ataque > alvo.carta.ataque) {
                // Atacante derrotou o alvo
                oponente.pontosJogador -= (atacante.carta.ataque - alvo.carta.ataque)
                println("${atacante.carta.nome} derrotou ${alvo.carta.nome}.")
            } else if (atacante.carta.ataque == alvo.carta.ataque) {
                // Ambos os monstros foram derrotados
                tabuleiro.removerMonstro(posicaoMonstroAtacante)
                oponente.tabuleiro.removerMonstro(posicaoOponente)
                println("${atacante.carta.nome} e ${alvo.carta.nome} foram destruídos.")
            } else {
                // Atacante não conseguiu derrotar o alvo
                tabuleiro.removerMonstro(posicaoMonstroAtacante)
                println("${atacante.carta.nome} não conseguiu derrotar ${alvo.carta.nome} e foi destruído.")
            }
        } else {
            if (alvo.estado == EstadoMonstro.DEFESA && atacante.carta.ataque == alvo.carta.ataque) {
                // Atacante e alvo estão em estado de defesa com o mesmo poder de ataque
                tabuleiro.removerMonstro(posicaoMonstroAtacante)
                println("${atacante.carta.nome} e ${alvo.carta.nome} estão em estado de defesa e não há vencedor.")
            }
        }
    }

    // Método para mudar o estado de um monstro no tabuleiro
    fun mudarEstadoMonstro(posicaoMonstro: Int, novoEstado: EstadoMonstro) {
        // Verifica se a posição do monstro é válida
        if (posicaoMonstro in 0..<tabuleiro.getPosicoes().size) {
            tabuleiro.getPosicoes()[posicaoMonstro].estado = novoEstado
        }
    }

    // Método para verificar se a mão do jogador está cheia
    fun maoEstaCheia(): Boolean {
        return mao.getCartas().size >= MAXIMO_CARTAS_NA_MAO
    }

    // Método para verificar se o jogador tem cartas do tipo monstro na mão
    fun temCartaTipoMonstro(): Boolean {
        return mao.getCartas().any { it.tipo == TipoCarta.MONSTRO }
    }

    // Método para verificar se o jogador tem cartas do tipo equipamento na mão
    fun temCartaTipoEquipamento(): Boolean {
        return mao.getCartas().any { it.tipo == TipoCarta.EQUIPAMENTO }
    }

    // Método para reiniciar o estado de ataque dos monstros no tabuleiro
    fun reiniciarEstadoAtaqueMonstros() {
        tabuleiro.getPosicoes().forEach { it.atacouNestaRodada = false }
    }

    // Método para verificar se o jogador pode atacar na rodada atual
    fun podeAtacar(): Boolean {
        return tabuleiro.getPosicoes().any { it.estado == EstadoMonstro.ATAQUE }
    }

    // Método para obter a mão do jogador
    fun getMaoJogador(): Mao {
        return mao
    }

    // Método para obter o tabuleiro do jogador
    fun getTabuleiro(): Tabuleiro {
        return tabuleiro
    }
}
