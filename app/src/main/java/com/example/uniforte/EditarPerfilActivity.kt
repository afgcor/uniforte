package com.example.uniforte

import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uniforte.AulasDisponiveisActivity

class EditarPerfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_editar_perfil)


        // Configuração do botão de voltar
        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        btnVoltar.setOnClickListener(object : android.view.View.OnClickListener {
            override fun onClick(view: android.view.View) {
                startActivity(
                    android.content.Intent(
                        this@EditarPerfilActivity,
                        PerfilActivity::class.java
                    )
                )
                finish()
            }
        })
    }
}