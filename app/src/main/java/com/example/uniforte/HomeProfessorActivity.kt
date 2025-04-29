package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeProfessorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_professor)


        findViewById<Button>(R.id.btnEditarAula2).setOnClickListener {
            startActivity(Intent(this, EditarAulaActivity::class.java))
        }

        findViewById<Button>(R.id.btnAdicionarAula).setOnClickListener {
            startActivity(Intent(this, AdicionarAulaActivity::class.java))
        }

        findViewById<Button>(R.id.navHome).setOnClickListener {
            startActivity(Intent(this, HomeProfessorActivity::class.java))
        }


    }
}
