package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AlunosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alunos)

        findViewById<TextView>(R.id.navHome).setOnClickListener {
            // Navegação para Ficha de Treino (se necessário)
            startActivity(Intent(this, HomeProfessorActivity::class.java))
        }

        findViewById<TextView>(R.id.navAlunos).setOnClickListener {
            // Navegação para Ficha de Treino (se necessário)
            startActivity(Intent(this, AlunosActivity::class.java))
        }

        findViewById<TextView>(R.id.navPerfil).setOnClickListener {
            // Navegação para Ficha de Treino (se necessário)
            startActivity(Intent(this, PerfilAdminActivity::class.java))
        }

        findViewById<TextView>(R.id.scrollAlunos).setOnClickListener {
            // Navegação para Ficha de Treino (se necessário)
            startActivity(Intent(this, PerfilAlunoActivity::class.java))
        }
        findViewById<TextView>(R.id.scrollAlunos2).setOnClickListener {
            // Navegação para Ficha de Treino (se necessário)
            startActivity(Intent(this, PerfilAlunoActivity::class.java))
        }

        findViewById<TextView>(R.id.scrollAlunos3).setOnClickListener {
            // Navegação para Ficha de Treino (se necessário)
            startActivity(Intent(this, PerfilAlunoActivity::class.java))
        }

        findViewById<TextView>(R.id.addAluno).setOnClickListener {
            // Navegação para Ficha de Treino (se necessário)
            startActivity(Intent(this, EditarInformacoesActivity::class.java))
        }
    }
}
