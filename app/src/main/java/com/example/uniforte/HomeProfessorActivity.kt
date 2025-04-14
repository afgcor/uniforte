package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeProfessorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_professor)

        findViewById<Button>(R.id.btnAlunos).setOnClickListener {
            startActivity(Intent(this, AlunosActivity::class.java))
        }
        findViewById<Button>(R.id.btnPerfilAluno).setOnClickListener {
            startActivity(Intent(this, PerfilAlunosActivity::class.java))
        }


    }
}
