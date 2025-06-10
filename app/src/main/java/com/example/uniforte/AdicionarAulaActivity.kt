package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.uniforte.data.SupabaseInit
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class AdicionarAulaActivity : AppCompatActivity() {

    // Cliente Supabase centralizado
    private val supabase = SupabaseInit.supabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_aula)

        // Inserir o fragmento da navegação inferior no container
        val navInferiorProfessorFragment = NavInferiorProfessorFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_nav_inferior, navInferiorProfessorFragment)
            .commit()

        // Configurar o callback para tratar cliques na navegação inferior
        navInferiorProfessorFragment.onNavItemSelected = { itemId ->
            when (itemId) {
                R.id.navHome -> {
                    startActivity(Intent(this, HomeProfessorActivity::class.java))
                }
                R.id.navMeusAlunos -> {
                    startActivity(Intent(this, MeusAlunosActivity::class.java))
                }
                R.id.navPerfilAdmin -> {
                    startActivity(Intent(this, PerfilAdminActivity::class.java))
                }
            }
        }

        val btnVoltar = findViewById<ImageView>(R.id.buttonVoltar)
        btnVoltar.setOnClickListener {
            finish()
        }

        val btnCancelar = findViewById<Button>(R.id.buttonCancelar)
        btnCancelar.setOnClickListener {
            finish()
        }

        val inputTituloAula = findViewById<TextInputEditText>(R.id.editTitulo)
        val inputDescricaoAula = findViewById<TextInputEditText>(R.id.editDescricao)
        val inputDataAula = findViewById<TextInputEditText>(R.id.editData)
        val inputHorarioAula = findViewById<TextInputEditText>(R.id.editHorario)

        // Lógica do botão salvar
        val btnSalvar = findViewById<Button>(R.id.buttonSalvar)
        btnSalvar.setOnClickListener {
            val titulo = inputTituloAula.text?.toString()?.trim() ?: ""
            val descricao = inputDescricaoAula.text?.toString()?.trim() ?: ""
            val data = inputDataAula.text?.toString()?.trim() ?: ""
            val horario = inputHorarioAula.text?.toString()?.trim() ?: ""

            if (titulo.isBlank() || descricao.isBlank() || data.isBlank() || horario.isBlank()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val res = supabase
                    .from("adicionar_aula")
                    .insert(
                        mapOf(
                            "titulo" to titulo,
                            "descricao" to descricao,
                            "data" to data,
                            "horario" to horario
                        )
                    )
                    .execute()

                if (res.error == null) {
                    Toast.makeText(
                        this@AdicionarAulaActivity,
                        "Aula adicionada com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(
                        this@AdicionarAulaActivity,
                        "Erro ao adicionar aula: ${res.error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}