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


class PerfilAdminActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil_admin)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ✅ Voltar para a Home do admin
        findViewById<TextView>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, HomeProfessorActivity::class.java))
        }



        //navegar para tela de Alunos
        findViewById<TextView>(R.id.navFicha).setOnClickListener {
            // Navegação para Ficha de Treino (se necessário)
            startActivity(Intent(this, AlunosActivity::class.java))
        }

        //navegar para tela editar informações do admin (usuarioAdmin)
        findViewById<Button>(R.id.EditarInfo).setOnClickListener {
            startActivity(Intent(this, UsuarioAdmin::class.java))
        }




    }
}