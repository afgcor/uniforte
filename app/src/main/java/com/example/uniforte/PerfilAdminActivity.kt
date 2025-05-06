package com.example.uniforte

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.TextView
import android.content.Intent
import android.widget.Button
import android.widget.ImageButton


class PerfilAdminActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil_admin)
        
        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar3)
        btnVoltar.setOnClickListener{
            finish()
        }

        val btnSair = findViewById<Button>(R.id.btnSair)
        btnSair.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
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

        val btnEditarInformacoes = findViewById<Button>(R.id.btnEditarInformacoes)
        btnEditarInformacoes.setOnClickListener{
            val intent = Intent(this, EditarInformacoesActivity::class.java)
            startActivity(intent)
        }

    }
}