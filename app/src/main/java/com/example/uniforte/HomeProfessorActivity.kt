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

        // navegar para Home
        findViewById<TextView>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, HomeProfessorActivity::class.java))
        }
        //navegar para tela de Alunos
        findViewById<TextView>(R.id.navFicha).setOnClickListener {

            startActivity(Intent(this, AlunosActivity::class.java))
        }

        findViewById<TextView>(R.id.navPerfil).setOnClickListener {
            // Navegação para Perfil (se necessário)
            startActivity(Intent(this, PerfilAdminActivity::class.java))
        }

        // Botões para editar e adicionar aula
        findViewById<Button>(R.id.btnEditarAula1).setOnClickListener {
            startActivity(Intent(this, EditarAulaActivity::class.java))
        }
        // Botões para editar e adicionar aula
        findViewById<Button>(R.id.btnEditarAula2).setOnClickListener {
            startActivity(Intent(this, EditarAulaActivity::class.java))
        }

        findViewById<Button>(R.id.btnAdicionarAula).setOnClickListener {
            startActivity(Intent(this, AdicionarAulaActivity::class.java))
        }
    }
}
