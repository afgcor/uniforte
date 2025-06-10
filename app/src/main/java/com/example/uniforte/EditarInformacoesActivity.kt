package com.example.uniforte.ui

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.TextInputEditText
import com.example.uniforte.R
import com.example.uniforte.data.SupabaseInit
import kotlinx.coroutines.launch

class EditarInformacoesActivity : AppCompatActivity() {

    // usa o SupabaseClient centralizado
    private val supabase = SupabaseInit.supabase

    // id do usuário e campos do formulário
    private lateinit var usuarioId: String
    private lateinit var editNome: TextInputEditText
    private lateinit var editEmail: TextInputEditText
    private lateinit var editTelefone: TextInputEditText
    private lateinit var editEndereco: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_informacoes)

        // 1) Vincula views
        editNome      = findViewById(R.id.editNome)
        editEmail     = findViewById(R.id.editEmail)
        editTelefone  = findViewById(R.id.editTelefone)
        editEndereco  = findViewById(R.id.editEndereco)
        val btnVoltar = findViewById<ImageView>(R.id.buttonVoltar)
        val btnSalvar = findViewById<Button>(R.id.buttonSalvar)

        // 2) Recebe extras da Intent (deve vir pelo menos o ID e valores atuais)
        usuarioId = intent.getStringExtra("USUARIO_ID")
            ?: throw IllegalStateException("ID de usuário não enviado")
        editNome.setText(intent.getStringExtra("USUARIO_NOME"))
        editEmail.setText(intent.getStringExtra("USUARIO_EMAIL"))
        editTelefone.setText(intent.getStringExtra("USUARIO_TELEFONE"))
        editEndereco.setText(intent.getStringExtra("USUARIO_ENDERECO"))

        // 3) Botão voltar
        btnVoltar.setOnClickListener { finish() }

        // 4) Salvar: atualiza somente as colunas que o form exibe
        btnSalvar.setOnClickListener {
            val novoNome     = editNome.text.toString().trim()
            val novoEmail    = editEmail.text.toString().trim()
            val novoTelefone = editTelefone.text.toString().trim()
            val novoEndereco = editEndereco.text.toString().trim()

            lifecycleScope.launch {
                val res = supabase
                    .from("usuarios")
                    .update(
                        mapOf(
                            "nome"     to novoNome,
                            "email"    to novoEmail,
                            "telefone" to novoTelefone,
                            "endereco" to novoEndereco
                        )
                    )
                    .eq("id", usuarioId)
                    .execute()

                if (res.error == null) {
                    Toast.makeText(
                        this@EditarInformacoesActivity,
                        "Informações atualizadas com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(
                        this@EditarInformacoesActivity,
                        "Erro ao atualizar: ${res.error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
