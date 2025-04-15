package com.example.uniforte

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AulasDisponiveisActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aulas_disponiveis)


        // Configuração do botão de voltar
        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        btnVoltar.setOnClickListener(object : android.view.View.OnClickListener {
            override fun onClick(view: android.view.View) {
                startActivity(android.content.Intent(this@AulasDisponiveisActivity , HomeAlunoActivity::class.java))
                finish()
            }
        })

        val tituloTextView: TextView = findViewById(R.id.tituloTextView)
        tituloTextView.text = "Aulas Disponíveis"

        // Configurando os botões de agendar aula
        val agendarAula1Button: Button = findViewById(R.id.agendarAula1)
        val agendarAula2Button: Button = findViewById(R.id.agendarAula2)
    }
}
