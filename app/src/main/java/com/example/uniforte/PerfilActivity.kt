package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class PerfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val btnEditarInformacoes = findViewById<Button>(R.id.btnEditarInformacoes)
        btnEditarInformacoes.setOnClickListener {
            val intent = Intent(this, EditarInformacoesActivity::class.java)
            intent.putExtra("tipoUsuario", "aluno")
            startActivity(intent)
        }

        val btnSair = findViewById<Button>(R.id.btnSair)
        btnSair.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        btnVoltar.setOnClickListener{
            finish()
        }

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
                    // Página atual, nada a fazer
                }
            }
        }
    }
}
