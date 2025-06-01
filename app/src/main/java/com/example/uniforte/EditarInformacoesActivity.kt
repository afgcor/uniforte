package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditarInformacoesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_informacoes)

        val tipoUsuario = intent.getStringExtra("tipoUsuario")!!

        when (tipoUsuario) {
            "aluno" -> {
                val navInferiorAlunoFragment = NavInferiorAlunoFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container_nav_inferior, navInferiorAlunoFragment)
                    .commit()

                navInferiorAlunoFragment.onNavItemSelected = { itemId ->
                    when (itemId) {
                        R.id.navHome -> startActivity(Intent(this, HomeAlunoActivity::class.java))
                        R.id.navFicha -> startActivity(Intent(this, FichaTreinoActivity::class.java))
                        R.id.navPerfil -> startActivity(Intent(this, PerfilActivity::class.java))
                    }
                }
            }

            "professor" -> {
                val navInferiorProfessorFragment = NavInferiorProfessorFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container_nav_inferior, navInferiorProfessorFragment)
                    .commit()

                navInferiorProfessorFragment.onNavItemSelected = { itemId ->
                    when (itemId) {
                        R.id.navHome -> startActivity(Intent(this, HomeProfessorActivity::class.java))
                        R.id.navMeusAlunos -> startActivity(Intent(this, MeusAlunosActivity::class.java))
                        R.id.navPerfilAdmin -> startActivity(Intent(this, PerfilAdminActivity::class.java))
                    }
                }
            }
        }

        findViewById<ImageButton>(R.id.btnVoltar).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnSalvar).setOnClickListener {
            Toast.makeText(this, "Informações editadas com sucesso!", Toast.LENGTH_SHORT).show()
            finish()
        }

        findViewById<Button>(R.id.btnCancelar).setOnClickListener{
            finish()
        }
    }
}
