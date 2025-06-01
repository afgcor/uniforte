package com.example.uniforte

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class MeusAlunosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meus_alunos)

        val cardAluno = findViewById<ConstraintLayout>(R.id.cardDadosAluno)
        cardAluno.setOnClickListener{
            val intent = Intent(this, PerfilAlunoActivity::class.java)
            startActivity(intent)
        }

        val tvAdicionarAluno = findViewById<ImageButton>(R.id.btnAdicionarAluno)
        tvAdicionarAluno.setOnClickListener{
            val intent = Intent(this, BuscarAlunoActivity::class.java)
            startActivity(intent)
        }

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        btnVoltar.setOnClickListener{
            finish()
        }

        val deletarListener = View.OnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirmar exclusão")
                .setMessage("Tem certeza que deseja excluir este aluno?")
                .setPositiveButton("Excluir") { dialog, _ ->
                    Toast.makeText(this, "Aluno excluído com sucesso!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        val btnDeletarAluno = findViewById<ImageButton>(R.id.btnDeletarAluno)
        btnDeletarAluno.setOnClickListener(deletarListener)

        // Inserir o fragmento da navegação inferior no container
        val navInferiorProfessorFragment = NavInferiorProfessorFragment   ()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_nav_inferior, navInferiorProfessorFragment)
            .commit()

        // Configurar o callback para tratar cliques na navegação inferior
        navInferiorProfessorFragment.onNavItemSelected = { itemId ->
            when (itemId) {
                R.id.navHome -> {
                    startActivity(Intent(this, HomeProfessorActivity::class.java))
                }
                R.id.navMeusAlunos -> {
                    // Página atual, nada a fazer
                }
                R.id.navPerfilAdmin -> {
                    startActivity(Intent(this, PerfilAdminActivity::class.java))
                }
            }
        }

    }
}