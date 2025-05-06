package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditarFichaTreinoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_ficha_treino)

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        btnVoltar.setOnClickListener{
            finish()
        }

        val btnNomeAluno = findViewById<TextView>(R.id.tvNomeAlunoFicha)
        btnNomeAluno.setOnClickListener{
            val intent = Intent(this, PerfilAlunoActivity::class.java)
            startActivity(intent)
        }

        val btnSalvarFicha = findViewById<Button>(R.id.btnSalvarFicha)
        btnSalvarFicha.setOnClickListener{
            finish()
            Toast.makeText(this, "Ficha editada com sucesso!", Toast.LENGTH_SHORT).show()
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