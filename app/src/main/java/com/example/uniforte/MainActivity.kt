package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btnEntrar = findViewById<Button>(R.id.btnEntrar)
        btnEntrar.setOnClickListener {
            val intent = Intent(this, HomeAlunoActivity::class.java)
            startActivity(intent)
        }

        val tvCadastrar = findViewById<TextView>(R.id.tvCadastrar)
        val textoOriginal = "Não Possui Conta? Cadastre-se"
        val spannableString = SpannableString(textoOriginal)
        spannableString.setSpan(UnderlineSpan(), 0, textoOriginal.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvCadastrar.text = spannableString
        tvCadastrar.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }

        val imgLogo = findViewById<ImageView>(R.id.imgLogo)
        imgLogo.setOnClickListener {
            val intent = Intent(this, HomeProfessorActivity::class.java)
            startActivity(intent)
        }
    }

}