package com.example.uniforte

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PerfilAlunoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_aluno)

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        btnVoltar.setOnClickListener {
            finish()
        }

        val btnEditarFicha = findViewById<ImageView>(R.id.btnEditarFicha)
        btnEditarFicha.setOnClickListener {
            val intent = Intent(this, EditarFichaTreinoActivity::class.java)
            startActivity(intent)
        }

        val btnAdicionarFicha = findViewById<TextView>(R.id.tvAdicionarFicha)
        btnAdicionarFicha.setOnClickListener {
            val intent = Intent(this, AdicionarFichaTreinoActivity::class.java)
            startActivity(intent)
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

        val btnVerFichas = findViewById<TextView>(R.id.btnVerFichas)
        btnVerFichas.setOnClickListener {
            val intent = Intent(this, FichaTreinoProfessorActivity::class.java)
            startActivity(intent)
        }

        val llCardTreino = findViewById<LinearLayout>(R.id.llCardTreino1)
        llCardTreino.setOnClickListener {
            val intent = Intent(this, DadosTreinoActivity::class.java)
            startActivity(intent)
        }

        val llCardObjetivo = findViewById<LinearLayout>(R.id.llCardObjetivo)
        val tvTituloObjetivo = findViewById<TextView>(R.id.tvTituloObjetivo)
        val tvDescricaoObjetivo = findViewById<TextView>(R.id.tvDescricaoObjetivo)

        llCardObjetivo.setOnLongClickListener {
            // Inflar o layout do diálogo
            val dialogView = layoutInflater.inflate(R.layout.dialog_editar_objetivo, null)
            val etTituloObjetivo = dialogView.findViewById<EditText>(R.id.etTituloObjetivo)
            val etDescricaoObjetivo = dialogView.findViewById<EditText>(R.id.etDescricaoObjetivo)

            // Preencher campos com os valores atuais
            etTituloObjetivo.setText(tvTituloObjetivo.text.toString())
            etDescricaoObjetivo.setText(tvDescricaoObjetivo.text.toString())

            // Criar o diálogo
            val dialog = AlertDialog.Builder(this)
                .setTitle("Editar Objetivo")
                .setView(dialogView)
                .setPositiveButton("Salvar") { dialog, _ ->
                    val novoTitulo = etTituloObjetivo.text.toString()
                    val novaDescricao = etDescricaoObjetivo.text.toString()

                    tvTituloObjetivo.text = novoTitulo
                    tvDescricaoObjetivo.text = novaDescricao

                    Toast.makeText(this, "Objetivo editado com sucesso!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setNegativeButton("Cancelar", null)
                .create()

            dialog.show()
            true // Indica que o evento foi tratado
        }

        // Inserir o fragmento da navegação inferior no container
        val navInferiorProfessorFragment = NavInferiorProfessorFragment()
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
