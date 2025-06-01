package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AulasDisponiveisActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aulas_disponiveis)

        // Inserir o fragmento da navegação inferior no container
        val navInferiorAlunoFragment = NavInferiorAlunoFragment   ()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_nav_inferior, navInferiorAlunoFragment)
            .commit()

        // Configurar o callback para tratar cliques na navegação inferior
        navInferiorAlunoFragment.onNavItemSelected = { itemId ->
            when (itemId) {
                R.id.navHome -> {
                    startActivity(Intent(this, HomeAlunoActivity::class.java))
                }
                R.id.navFicha -> {
                    startActivity(Intent(this, FichaTreinoActivity::class.java))
                }
                R.id.navPerfil -> {
                    startActivity(Intent(this, PerfilActivity::class.java))
                }
            }
        }

//        val tituloTextView: TextView = findViewById(R.id.tituloTextView)
//        tituloTextView.text = "Aulas Disponíveis"

        // Configurando os botões de agendar aula
//        val agendarAula1Button: Button = findViewById(R.id.agendarAula1)
//        val agendarAula2Button: Button = findViewById(R.id.agendarAula2)

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        btnVoltar.setOnClickListener{
            finish()
        }

        val btnAgendarAula = findViewById<Button>(R.id.agendarPilates)
        btnAgendarAula.setOnClickListener{
            finish()
            Toast.makeText(this, "Aula agendada com sucesso!", Toast.LENGTH_SHORT).show()
        }

    }
}
