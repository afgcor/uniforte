package com.example.uniforte


import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MeusAlunosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meus_alunos)

        val navHome = findViewById<TextView>(R.id.navHome)
        navHome.setOnClickListener {
            val intent = Intent(this, HomeProfessorActivity::class.java)
            startActivity(intent)
        }

        val navMeusAlunos = findViewById<TextView>(R.id.navMeusAlunos)
        navMeusAlunos.setOnClickListener {
            val intent = Intent(this, HomeProfessorActivity::class.java)
            startActivity(intent)
        }

        val tvAdicionarAluno = findViewById<TextView>(R.id.tvAdicionarAluno)
        tvAdicionarAluno.setOnClickListener{
            val intent = Intent(this, BuscarAlunoActivity::class.java)
            startActivity(intent)
        }

        val navPerfilAdmin = findViewById<TextView>(R.id.navPerfilAdmin)
        navPerfilAdmin.setOnClickListener {
            val intent = Intent(this, PerfilAdminActivity::class.java)
            startActivity(intent)
        }

        val btnVoltar = findViewById<ImageButton>(R.id.back_button)
        btnVoltar.setOnClickListener{
            finish()
        }

        val aluno1 = findViewById<TextView>(R.id.bntEditarAluno)
        aluno1.setOnClickListener{
            val intent = Intent(this, PerfilAlunoActivity::class.java)
            startActivity(intent)
        }

//        val btnEditarAluno = findViewById<Button>(R.id.btnEditarAluno)
//        btnEditarAluno.setOnClickListener {
//            val intent = Intent(this, EditarAlunoActivity::class.java)
//        }
//
//        val btnDeletarAluno = findViewById<ImageView>(R.id.btnDeletarMA)
//        btnDeletarAluno.setOnClickListener{
//            val intent = Intent(this, xsdkjnsfsf::class.java)
//        }
//
//        val tvAdicionarAluno = findViewById<TextView>(R.id.tvAdicionarAluno)
//        tvAdicionarAluno.setOnClickListener{
//            val intent = Intent(this, xxxxxxxxxx::class.java)
//        }

    }
}