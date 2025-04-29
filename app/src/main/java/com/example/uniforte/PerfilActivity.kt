package com.example.uniforte

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.uniforte.EditarPerfilActivity

class PerfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Configuração do botão de editar perfil
        val btnEditarPerfil = findViewById<Button>(R.id.editarPerfilBtn)
        btnEditarPerfil.setOnClickListener(object : android.view.View.OnClickListener {
            override fun onClick(view: android.view.View) {
                startActivity(android.content.Intent(this@PerfilActivity  , EditarPerfilActivity::class.java))
                finish()
            }
        })

    }
}
