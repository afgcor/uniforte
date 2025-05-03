package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PerfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val buttonEditarInfo = findViewById<Button>(R.id.buttonEditarInfo)
        buttonEditarInfo.setOnClickListener {
            val intent = Intent(this, EditarInformacoesActivity::class.java)
            startActivity(intent)
        }

        // ✅ Voltar para a Home do aluno
        val navHome = findViewById<TextView>(R.id.navHome)
        navHome.setOnClickListener {
            val intent = Intent(this, HomeAlunoActivity::class.java)
            // Limpa a pilha anterior e evita múltiplas cópias da mesma activity
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

        val navFicha = findViewById<TextView>(R.id.navFicha)
        navFicha.setOnClickListener {
            val intent = Intent(this, FichaTreinoActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

    }
}
