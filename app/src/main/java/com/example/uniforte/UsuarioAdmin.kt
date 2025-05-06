package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class UsuarioAdmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_usuario_admin)

        // ✅ Voltar para a Home do admin
        findViewById<TextView>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, HomeProfessorActivity::class.java))
        }

        //navegar para tela de Alunos
        findViewById<TextView>(R.id.navFicha).setOnClickListener {
            // Navegação para Ficha de Treino (se necessário)
            startActivity(Intent(this, AlunosActivity::class.java))
        }

        

    }
}