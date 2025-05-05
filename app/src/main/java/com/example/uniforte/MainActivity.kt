package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private var modoAdmin = false  // Flag para indicar se é modo admin

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Referências aos elementos da tela
        val btnEntrar = findViewById<Button>(R.id.btnEntrar)
        val tvCadastrar = findViewById<TextView>(R.id.tvCadastrar)
        val imgLogo = findViewById<ImageView>(R.id.imgLogo)
        val campoLogin = findViewById<EditText>(R.id.etLogin)
        val campoSenha = findViewById<EditText>(R.id.etSenha)

        // Ativa o segredo ao clicar na imagem
        imgLogo.setOnClickListener {
            modoAdmin = !modoAdmin
            Toast.makeText(this@MainActivity, if (modoAdmin) "Modo Admin ATIVADO" else "Modo Admin DESATIVADO", Toast.LENGTH_SHORT).show()
        }

        // Clique em "Entrar"
        btnEntrar.setOnClickListener {
            val login = campoLogin.text.toString()
            val senha = campoSenha.text.toString()

            if (login.isNotEmpty() && senha.isNotEmpty()) {
                val destino = if (modoAdmin) {
                    HomeProfessorActivity::class.java
                } else {
                    HomeAlunoActivity::class.java
                }
                startActivity(Intent(this, destino))
            } else {
                campoLogin.error = "Por favor, insira um login válido"
                campoSenha.error = "Por favor, insira uma senha válida"
            }
        }




        // Sublinha o texto "Não Possui Conta? Cadastre-se"
        val textoOriginal = "Não Possui Conta? Cadastre-se"
        val spannableString = SpannableString(textoOriginal)
        spannableString.setSpan(
            UnderlineSpan(),
            0,
            textoOriginal.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvCadastrar.text = spannableString

        // Clique em "Cadastre-se"
        tvCadastrar.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }

        // Clique em "Entrar"
        btnEntrar.setOnClickListener {
            val login = campoLogin.text.toString()
            val senha = campoSenha.text.toString()

            // Verifica se a senha e o login são válidos
            if (login.isNotEmpty() && senha.isNotEmpty()) {
                val destino = if (modoAdmin) {
                    // Se o modo admin estiver ativado, vai para a tela de admin
                    HomeProfessorActivity::class.java
                } else {
                    // Caso contrário, vai para a tela de aluno
                    HomeAlunoActivity::class.java
                }
                startActivity(Intent(this, destino))
            } else {
                // Caso os campos não sejam preenchidos, mostre um erro
                campoLogin.error = "Por favor, insira um login válido"
                campoSenha.error = "Por favor, insira uma senha válida"
            }
        }

        // Ajuste visual opcional para compatibilidade com gestos/áreas do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }




    }
}
