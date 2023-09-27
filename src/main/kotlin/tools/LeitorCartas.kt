package tools

import model.Carta
import java.io.File
import java.io.InputStream

class LeitorCartas (){

    companion object{
        private lateinit var cartas:List<Carta>

        fun getCartas():List<Carta>{
            if(!::cartas.isInitialized){
                /*aqui deve ocorrer a carga das cartas
                *
                *Sugiro usar a função map para transformar as String recuperadas do arquivo em objetos do tipo carta
                */
                //cartas = lerCartasCSV()

                cartas = lerCartasCSV().map {
                    val dadosCarta = it.split(";")
                    Carta(dadosCarta[0], dadosCarta[1], dadosCarta[2].toInt(), dadosCarta[3].toInt(), dadosCarta[4])
                }

                //Salvando as cartas em um array


                println(lerCartasCSV())
            }
            return cartas.map { it }  //retorna uma replica das cartas
        }

        private fun lerCartasCSV():List<String>{
            val streamDados:InputStream = File("cartas.csv").inputStream()
            val leitorStream = streamDados.bufferedReader()
            return leitorStream.lineSequence()
                .filter { it.isNotBlank() }.toList()

        }
    }



}