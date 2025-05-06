package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
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

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar2)
        btnVoltar.setOnClickListener{
            finish()
        }

        val llCardTreino = findViewById<LinearLayout>(R.id.llCardTreino)
        llCardTreino.setOnClickListener {
            val intent = Intent(this, DadosTreinoActivity::class.java)
            startActivity(intent)
        }

        val navHome = findViewById<TextView>(R.id.navHome)
        navHome.setOnClickListener {
            val intent = Intent(this, HomeAlunoActivity::class.java)
            startActivity(intent)
        }

        val navFicha = findViewById<TextView>(R.id.navFicha)
        navFicha.setOnClickListener {
            val intent = Intent(this, FichaTreinoActivity::class.java)
            startActivity(intent)
        }

        val navPerfil = findViewById<TextView>(R.id.navPerfil)
        navPerfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }

        val btnProfessor = findViewById<Button>(R.id.btnProfessor)
        btnProfessor.setOnClickListener {
            val intent = Intent(this, PerfilAdminActivity::class.java)
            startActivity(intent)
        }

    }
}
