package models

data class Carta(
    val nome: String,
    val descricao: String,
    val tipo: TipoCarta,
    var ataque: Int,
    var defesa: Int
)
