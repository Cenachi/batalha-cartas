package models

class Tabuleiro {
    private val posicoes = mutableListOf<PosicaoTabuleiro>()
    private val MAXIMO_MONSTROS_NO_TABULEIRO = 5

    // Método para adicionar um monstro ao tabuleiro com uma carta e um estado especificados.
    fun adicionarMonstro(carta: Carta, estado: EstadoMonstro) {
        // Verifica se o tabuleiro não está cheio antes de adicionar um monstro.
        if (posicoes.size < MAXIMO_MONSTROS_NO_TABULEIRO) {
            posicoes.add(PosicaoTabuleiro(carta, estado))
        }
    }

    // Método para remover um monstro do tabuleiro com base na posição.
    fun removerMonstro(posicao: Int) {
        // Verifica se a posição fornecida está dentro dos limites do tabuleiro antes de remover um monstro.
        if (posicao in 0 until posicoes.size) {
            posicoes.removeAt(posicao)
        }
    }

    // Método para verificar se o tabuleiro está cheio (atingiu o número máximo de monstros).
    fun tabuleiroEstaCheio(): Boolean {
        return posicoes.size >= MAXIMO_MONSTROS_NO_TABULEIRO
    }

    // Método para verificar se o tabuleiro está vazio (não possui monstros).
    fun tabuleiroEstaVazio(): Boolean {
        return posicoes.isEmpty()
    }

    // Método para exibir o estado atual do tabuleiro, mostrando as cartas, descrições, ataques, defesas e estados dos monstros.
    fun mostrarTabuleiro() {
        println("\nTabuleiro:")
        val separador = "+----------------+"
        val linhaVazia = "|                |"

        // Cabeçalho do tabuleiro
        println(separador)

        // Loop para as cinco posições do tabuleiro
        for (i in 0..<5) {
            val posicao = posicoes.getOrElse(i) { PosicaoTabuleiro(Carta("", "", TipoCarta.MONSTRO, 0, 0), EstadoMonstro.ATAQUE) }
            val carta = posicao.carta

            // Verifica se todos os campos da carta estão preenchidos
            if (carta.nome.isNotEmpty() && carta.descricao.isNotEmpty() && carta.ataque != 0 && carta.defesa != 0) {
                val estado = if (posicao.estado == EstadoMonstro.ATAQUE) "Ataque" else "Defesa"
                val linha1 = "| ${carta.nome}"
                val linha2 = "| Descrição: ${carta.descricao}"
                val linha3 = "| Ataque: ${carta.ataque}   Defesa: ${carta.defesa}"
                val linha4 = "| Estado: $estado"

                // Imprime as linhas da posição atual
                println(separador)
                println(linhaVazia)
                println(linha1.padEnd(18) + "|")
                println(linha2.padEnd(18) + "|")
                println(linha3.padEnd(18) + "|")
                println(linha4.padEnd(18) + "|")
            } else {
                // Se algum campo da carta estiver vazio/nulo, imprima uma linha vazia
                println(separador)
                println(linhaVazia)
                println(linhaVazia)
                println(linhaVazia)
                println(linhaVazia)
            }
        }

        // Rodapé do tabuleiro
        println(separador)
    }

    // Método para obter todas as posições atuais do tabuleiro.
    fun getPosicoes(): MutableList<PosicaoTabuleiro> {
        return posicoes
    }

}
