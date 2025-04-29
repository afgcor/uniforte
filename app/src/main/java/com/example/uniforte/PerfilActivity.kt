package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
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

    }
}
