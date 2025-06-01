package com.example.uniforte

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FichaTreinoProfessorActivity : AppCompatActivity() {

    data class Treino(
        val titulo: String,
        val descricao: String,
        val exercicios: String,
        val nomeProfessor: String
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ficha_treino_professor)

        // Inserir o fragmento da navegação inferior no container
        val navInferiorProfessorFragment = NavInferiorAlunoFragment()
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
                    startActivity(Intent(this, MeusAlunosActivity::class.java))
                }
                R.id.navPerfilAdmin -> {
                    startActivity(Intent(this, PerfilAdminActivity::class.java))
                }
            }
        }

        val btnEditarFicha = findViewById<ImageView>(R.id.btnEditarFicha)
        btnEditarFicha.setOnClickListener {
            startActivity(Intent(this, EditarFichaTreinoActivity::class.java))
        }

        val deletarListener = View.OnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirmar exclusão")
                .setMessage("Tem certeza que deseja excluir esta ficha?")
                .setPositiveButton("Excluir") { dialog, _ ->
                    Toast.makeText(this, "Ficha excluída com sucesso!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        val btnDeletarFicha = findViewById<ImageView>(R.id.btnDeletarFicha)
        btnDeletarFicha.setOnClickListener(deletarListener)

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        btnVoltar.setOnClickListener{
            finish()
        }

    }
}
