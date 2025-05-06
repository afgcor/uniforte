package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AulasDisponiveisActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aulas_disponiveis)

//        val tituloTextView: TextView = findViewById(R.id.tituloTextView)
//        tituloTextView.text = "Aulas Disponíveis"

        // Configurando os botões de agendar aula
//        val agendarAula1Button: Button = findViewById(R.id.agendarAula1)
//        val agendarAula2Button: Button = findViewById(R.id.agendarAula2)

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        btnVoltar.setOnClickListener{
            finish()
        }

        val btnAgendarAula = findViewById<Button>(R.id.agendarAula1)
        btnAgendarAula.setOnClickListener{
            finish()
            Toast.makeText(this, "Aula agendada com sucesso!", Toast.LENGTH_SHORT).show()
        }

        val navHome = findViewById<TextView>(R.id.navHome)
        navHome.setOnClickListener {
            val intent = Intent(this, HomeAlunoActivity::class.java)
            startActivity(intent)
        }

        val navFicha = findViewById<TextView>(R.id.navFicha)
        navFicha.setOnClickListener {
            val intent = Intent(this, FichaTreinoActivity::class.java)
            startActivity(intent)
        }

        val navPerfil = findViewById<TextView>(R.id.navPerfil)
        navPerfil.setOnClickListener {
            val intent = Intent(this, PerfilActivity::class.java)
            startActivity(intent)
        }
    }
}
