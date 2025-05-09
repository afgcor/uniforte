
package com.example.uniforte

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class BuscarAlunoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_aluno)

        val editTextSearch = findViewById<EditText>(R.id.editTextSearch)

        val btnVoltar = findViewById<ImageButton>(R.id.back_button)
        btnVoltar.setOnClickListener{
            finish()
        }

    }
}