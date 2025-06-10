package com.example.uniforte.ui

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.uniforte.R
import com.example.uniforte.data.SupabaseInit
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class EditarExercicioFichaActivity : AppCompatActivity() {

    // 1) Cliente Supabase centralizado
    private val supabase = SupabaseInit.supabase

    // 2) Variáveis para ID e campos do formulário
    private lateinit var exercicioId: String
    private lateinit var editSeries: TextInputEditText
    private lateinit var editRepeticoes: TextInputEditText
    private lateinit var editCarga: TextInputEditText
    private lateinit var editObservacoes: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_exercicio_ficha)

        // 3) Vincula as views
        editSeries       = findViewById(R.id.editSeries)
        editRepeticoes   = findViewById(R.id.editRepeticoes)
        editCarga        = findViewById(R.id.editCarga)
        editObservacoes  = findViewById(R.id.editObservacoes)
        val btnVoltar    = findViewById<ImageView>(R.id.btnVoltar)
        val btnCancelar  = findViewById<Button>(R.id.btnCancelar)
        val btnSalvar    = findViewById<Button>(R.id.btnSalvar)

        // 4) Lê o ID e valores atuais da Intent
        exercicioId = intent.getStringExtra("EXERCICIO_ID")
            ?: throw IllegalStateException("ID do exercício não enviado")
        editSeries.setText(intent.getStringExtra("EXERCICIO_SERIES"))
        editRepeticoes.setText(intent.getStringExtra("EXERCICIO_REPETICOES"))
        editCarga.setText(intent.getStringExtra("EXERCICIO_CARGA"))
        editObservacoes.setText(intent.getStringExtra("EXERCICIO_OBSERVACOES"))

        // 5) Navegação
        btnVoltar.setOnClickListener   { finish() }
        btnCancelar.setOnClickListener { finish() }

        // 6) Salvar: atualiza na tabela "exercicio_ficha_exercicios"
        btnSalvar.setOnClickListener {
            val series      = editSeries.text.toString().trim()
            val repeticoes  = editRepeticoes.text.toString().trim()
            val carga       = editCarga.text.toString().trim()
            val observacoes = editObservacoes.text.toString().trim()

            lifecycleScope.launch {
                val res = supabase
                    .from("exercicio_ficha_exercicios")
                    .update(
                        mapOf(
                            "series"      to series,
                            "repeticoes"  to repeticoes,
                            "carga"       to carga,
                            "observacoes" to observacoes
                        )
                    )
                    .eq("id", exercicioId)
                    .execute()

                if (res.error == null) {
                    Toast.makeText(
                        this@EditarExercicioFichaActivity,
                        "Exercício atualizado com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(
                        this@EditarExercicioFichaActivity,
                        "Erro ao atualizar: ${res.error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
} 