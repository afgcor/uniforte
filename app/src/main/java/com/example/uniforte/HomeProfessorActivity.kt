package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeProfessorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_professor)

        // Inserir o fragmento da navegação inferior no container
        val navInferiorProfessorFragment = NavInferiorProfessorFragment   ()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_nav_inferior, navInferiorProfessorFragment)
            .commit()

        // Configurar o callback para tratar cliques na navegação inferior
        navInferiorProfessorFragment.onNavItemSelected = { itemId ->
            when (itemId) {
                R.id.navHome -> {
                    // Página atual, nada a fazer
                }
                R.id.navMeusAlunos -> {
                    startActivity(Intent(this, MeusAlunosActivity::class.java))
                }
                R.id.navPerfilAdmin -> {
                    startActivity(Intent(this, PerfilAdminActivity::class.java))
                }
            }
        }

        val btnEditarAula = findViewById<Button>(R.id.btnEditarAula)
        btnEditarAula.setOnClickListener {
            val intent = Intent(this, EditarAulaActivity::class.java)
            startActivity(intent)
        }

        val verTudo = findViewById<TextView>(R.id.tvVerTudo)
        verTudo.setOnClickListener {
            startActivity(Intent(this, AgendamentosProfessorActivity::class.java))
        }

        val btnAdicionarAula = findViewById<Button>(R.id.btnAdicionarAula)
        btnAdicionarAula.setOnClickListener {
            val intent = Intent(this, AdicionarAulaActivity::class.java)
            startActivity(intent)
        }

    }
}
