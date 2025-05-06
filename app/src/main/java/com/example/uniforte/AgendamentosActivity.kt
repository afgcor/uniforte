package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.TextView

class AgendamentosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendamentos)

        val backButton: ImageButton = findViewById(R.id.back_button)

        backButton.setOnClickListener {
            val intent = Intent(this, HomeAlunoActivity::class.java)
            startActivity(intent)

            finish()
        }

        val navHome = findViewById<TextView>(R.id.navHome)
        navHome.setOnClickListener {
            val intent = Intent(this, HomeProfessorActivity::class.java)
            startActivity(intent)
        }

        val navFicha = findViewById<TextView>(R.id.navFicha)
        navFicha.setOnClickListener {
            val intent = Intent(this, FichaTreinoActivity::class.java)
            startActivity(intent)
        }

        val navPerfilAdmin = findViewById<TextView>(R.id.navPerfilAdmin)
        navPerfilAdmin.setOnClickListener {
            val intent = Intent(this, PerfilAdminActivity::class.java)
            startActivity(intent)
        }
    }
}
