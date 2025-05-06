package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EditarAulaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_aula)

        val btnVoltar = findViewById<ImageView>(R.id.buttonVoltar)
        btnVoltar.setOnClickListener{
            finish()
        }

        val btnSalvar = findViewById<Button>(R.id.buttonSalvar)
        btnSalvar.setOnClickListener{
            finish()
            Toast.makeText(this, "Aula editada com sucesso!", Toast.LENGTH_SHORT).show()
        }

        val btnCancelar = findViewById<Button>(R.id.buttonCancelar)
        btnCancelar.setOnClickListener{
            finish()
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