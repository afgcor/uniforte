package com.example.uniforte

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast

class AgendamentosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendamentos)

        val btnVoltar : ImageButton = findViewById(R.id.btnVoltar)

        btnVoltar.setOnClickListener {
            finish()
        }

        // Inserir o fragmento da navegação inferior no container
        val navInferiorAlunoFragment = NavInferiorAlunoFragment   ()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_nav_inferior, navInferiorAlunoFragment)
            .commit()

        // Configurar o callback para tratar cliques na navegação inferior
        navInferiorAlunoFragment.onNavItemSelected = { itemId ->
            when (itemId) {
                R.id.navHome -> {
                    startActivity(Intent(this, HomeAlunoActivity::class.java))
                }
                R.id.navFicha -> {
                    startActivity(Intent(this, FichaTreinoActivity::class.java))
                }
                R.id.navPerfil -> {
                    startActivity(Intent(this, PerfilActivity::class.java))
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

        val btnDeletarAula = findViewById<ImageView>(R.id.btnDeletarAula1)
        btnDeletarAula.setOnClickListener(deletarListener)

    }
}
