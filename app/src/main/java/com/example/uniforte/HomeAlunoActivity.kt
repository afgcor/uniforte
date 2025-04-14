package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeAlunoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_aluno)

        findViewById<Button>(R.id.btnAgendamentos).setOnClickListener {
            startActivity(Intent(this, AgendamentosActivity::class.java))
        }
        findViewById<Button>(R.id.btnFeedback).setOnClickListener {
            startActivity(Intent(this, FeedbackActivity::class.java))
        }

        findViewById<Button>(R.id.btnAulas).setOnClickListener {
            startActivity(Intent(this, AulasDisponiveisActivity::class.java))
        }

        findViewById<Button>(R.id.btnFicha).setOnClickListener {
            startActivity(Intent(this, FichaTreinoActivity::class.java))
        }

        findViewById<Button>(R.id.btnPerfil).setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }
    }
}
