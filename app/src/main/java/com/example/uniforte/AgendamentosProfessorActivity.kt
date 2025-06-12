package com.example.uniforte

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AgendamentosProfessorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendamentos_professor)

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
                    startActivity(Intent(this, MeusAlunosActivity::class.java))
                }
                R.id.navPerfil -> {
                    startActivity(Intent(this, PerfilAdminActivity::class.java))
                }
            }
        }

        val deletarListener = View.OnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirmar exclusão")
                .setMessage("Tem certeza que deseja excluir esta aula?")
                .setPositiveButton("Excluir") { dialog, _ ->
                    Toast.makeText(this, "Aula excluída com sucesso!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        val btnExcluir = findViewById<ImageView>(R.id.btnExcluir)
        btnExcluir.setOnClickListener(deletarListener)

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        btnVoltar.setOnClickListener{
            finish()
        }

        val btnEditar = findViewById<ImageView>(R.id.btnEditar)
        btnEditar.setOnClickListener{
            val intent = Intent(this, EditarAulaActivity::class.java)
            startActivity(intent)
        }

    }
}