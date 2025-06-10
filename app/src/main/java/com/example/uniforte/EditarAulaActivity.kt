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

class EditarAulaActivity : AppCompatActivity() {

    // usa o cliente Supabase já inicializado em SupabaseInit
    private val supabase = SupabaseInit.supabase

    private lateinit var aulaId: String
    private lateinit var editTitulo: TextInputEditText
    private lateinit var editDescricao: TextInputEditText
    private lateinit var editData: TextInputEditText
    private lateinit var editHorario: TextInputEditText
    private lateinit var btnSalvar: Button
    private lateinit var btnVoltar: ImageView
    private lateinit var btnCancelar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_aula)

        // 1) vincula os campos do layout
        editTitulo     = findViewById(R.id.editTitulo)
        editDescricao  = findViewById(R.id.editDescricao)
        editData       = findViewById(R.id.editData)
        editHorario    = findViewById(R.id.editHorario)
        btnVoltar      = findViewById(R.id.buttonVoltar)
        btnSalvar      = findViewById(R.id.buttonSalvar)
        btnCancelar    = findViewById(R.id.buttonCancelar)

        // 2) carrega dados que vieram na Intent
        aulaId = intent.getStringExtra("AULA_ID")
            ?: throw IllegalStateException("ID da aula não enviado")
        editTitulo.setText(intent.getStringExtra("AULA_TITULO"))
        editDescricao.setText(intent.getStringExtra("AULA_DESCRICAO"))
        editData.setText(intent.getStringExtra("AULA_DATA"))
        editHorario.setText(intent.getStringExtra("AULA_HORARIO"))

        // 3) configura botões
        btnVoltar.setOnClickListener { finish() }
        btnCancelar.setOnClickListener { finish() }

        btnSalvar.setOnClickListener {
            val novoTitulo  = editTitulo.text.toString().trim()
            val novaDesc    = editDescricao.text.toString().trim()
            val novaData    = editData.text.toString().trim()
            val novoHorario = editHorario.text.toString().trim()

            // 4) faz o update na tabela "adicionar_aula" usando as colunas corretas
            lifecycleScope.launch {
                val response = supabase
                    .from("adicionar_aula")
                    .update(
                        mapOf(
                            "titulo"    to novoTitulo,
                            "descricao" to novaDesc,
                            "data"      to novaData,
                            "horario"   to novoHorario
                        )
                    )
                    .eq("id", aulaId)
                    .execute()

                if (response.error == null) {
                    Toast.makeText(
                        this@EditarAulaActivity,
                        "Aula atualizada com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(
                        this@EditarAulaActivity,
                        "Erro ao atualizar: ${response.error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}