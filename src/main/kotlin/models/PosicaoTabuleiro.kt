package models

// Classe de dados que representa uma posição no tabuleiro do jogo, contendo uma carta,
// estado do monstro (ataque ou defesa), e um indicador se o monstro já atacou nesta rodada.
data class PosicaoTabuleiro(val carta: Carta, var estado: EstadoMonstro, var atacouNestaRodada: Boolean = false)
