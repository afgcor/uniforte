package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView


class PerfilAlunoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_aluno)

        // navegar para Home
        findViewById<TextView>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, HomeProfessorActivity::class.java))
        }

        findViewById<TextView>(R.id.navFicha).setOnClickListener {
            // Navegação para Ficha de Treino (se necessário)
            startActivity(Intent(this, AlunosActivity::class.java))
        }

        findViewById<TextView>(R.id.navPerfil).setOnClickListener {
            // Navegação para Perfil (se necessário)
            startActivity(Intent(this, PerfilAdminActivity::class.java))
        }

        // Navegação adicionar nova ficha
        findViewById<TextView>(R.id.btnAdicionarFicha).setOnClickListener {
            startActivity(Intent(this, AdicionarFichaTreinoActivity::class.java))
        }

        // Navegação Alterar nova ficha
        findViewById<Button>(R.id.btnEditarFicha).setOnClickListener {
            startActivity(Intent(this, AlterarFichaTreinoActivity::class.java))
        }

    }


}
