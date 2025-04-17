package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class FichaTreinoActivity : AppCompatActivity() {

    data class Treino(
        val titulo: String,
        val descricao: String,
        val exercicios: String,
        val nomeProfessor: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ficha_treino)

        val listaTreinos = listOf(
            Treino(
                "Treino AB",
                "Treino com foco em bíceps e costas",
                "Supino 3x14\nPuxada Frente 3x10\nElevação Lateral 4x12\nRosca Scott 4x12",
                "Nome do Professor"
            ),
            Treino(
                "Treino CD",
                "Treino com foco em bíceps e costas",
                "Supino 3x14\nPuxada Frente 3x10\nElevação Lateral 4x12\nRosca Scott 4x12",
                "Nome do Professor"
            )
        )

        //navegação home
        findViewById<TextView>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, HomeAlunoActivity::class.java))
        }

        //navegação perfil
        findViewById<TextView>(R.id.navPerfil).setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }


    }
}
