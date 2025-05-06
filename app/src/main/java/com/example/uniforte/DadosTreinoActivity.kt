package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DadosTreinoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dados_treino)

        val tvNomeProfessor = findViewById<TextView>(R.id.tvNomeProfessor)
        tvNomeProfessor.setOnClickListener{
            val intent = Intent(this, PerfilAdminActivity::class.java)
            startActivity(intent)
        }

        val navHome = findViewById<TextView>(R.id.navHome)
        navHome.setOnClickListener {
            val intent = Intent(this, HomeAlunoActivity::class.java)
            startActivity(intent)
        }

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        btnVoltar.setOnClickListener{
            finish()
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

        }
}