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

class EditarInformacoesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_informacoes)

        val btnVoltar = findViewById<ImageView>(R.id.buttonVoltar)
        btnVoltar.setOnClickListener{
            finish()
        }

        val btnSalvar = findViewById<Button>(R.id.buttonSalvar)
        btnSalvar.setOnClickListener{
            finish()
            Toast.makeText(this, "Informações editadas com sucesso!", Toast.LENGTH_SHORT).show()
        }

        }
    }
