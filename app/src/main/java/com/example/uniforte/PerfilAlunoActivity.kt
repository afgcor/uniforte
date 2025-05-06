package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PerfilAlunoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_aluno)

        val btnVoltar = findViewById<ImageButton>(R.id.back_button)
        btnVoltar.setOnClickListener{
            finish()
        }

        val btnEditarFicha = findViewById<Button>(R.id.btnEditarFicha)
        btnEditarFicha.setOnClickListener {
            val intent = Intent(this, EditarFichaTreinoActivity::class.java)
            startActivity(intent)
        }

        val btnAdicionarFicha = findViewById<TextView>(R.id.tvAdicionarFicha)
        btnAdicionarFicha.setOnClickListener {
            val intent = Intent(this, AdicionarFichaTreinoActivity::class.java)
            startActivity(intent)
        }

        val navHome = findViewById<TextView>(R.id.navHome)
        navHome.setOnClickListener {
            val intent = Intent(this, HomeProfessorActivity::class.java)
            startActivity(intent)
        }

        val navMeusAlunos = findViewById<TextView>(R.id.navMeusAlunos)
        navMeusAlunos.setOnClickListener {
            val intent = Intent(this, MeusAlunosActivity::class.java)
            startActivity(intent)
        }

        val navPerfilAdmin = findViewById<TextView>(R.id.navPerfilAdmin)
        navPerfilAdmin.setOnClickListener {
            val intent = Intent(this, PerfilAdminActivity::class.java)
            startActivity(intent)
        }

    }
}
