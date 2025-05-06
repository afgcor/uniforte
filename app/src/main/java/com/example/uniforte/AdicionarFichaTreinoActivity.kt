package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AdicionarFichaTreinoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_ficha_treino)

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        btnVoltar.setOnClickListener{
            finish()
        }

        val btnSalvarFicha = findViewById<Button>(R.id.btnSalvarFicha)
        btnSalvarFicha.setOnClickListener{
            finish()
            Toast.makeText(this, "Ficha adicionada com sucesso!", Toast.LENGTH_SHORT).show()
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