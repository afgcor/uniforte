package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforte.data.model.ExercicioFichaDetalhes
import com.example.uniforte.ui.EditarExercicioFichaActivity

class DadosTreinoActivity : AppCompatActivity() {
    private lateinit var exercicioAdapter: ExercicioAdapter
    private val REQUEST_EDITAR_EXERCICIO = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dados_treino)

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        btnVoltar.setOnClickListener {
            finish()
        }

        // Configura o RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewExercicios)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializa o adapter com a lista de exercícios e o callback de edição
        exercicioAdapter = ExercicioAdapter(
            exercicios = emptyList(),
            onEditarClick = { exercicio ->
                editarExercicio(exercicio)
            }
        )
        recyclerView.adapter = exercicioAdapter

        // TODO: Carregar os exercícios da ficha de treino
    }

    private fun editarExercicio(exercicio: ExercicioFichaDetalhes) {
        val intent = Intent(this, EditarExercicioFichaActivity::class.java).apply {
            putExtra("EXERCICIO_ID", exercicio.id)
            putExtra("EXERCICIO_SERIES", exercicio.series)
            putExtra("EXERCICIO_REPETICOES", exercicio.repeticoes)
            putExtra("EXERCICIO_CARGA", exercicio.carga)
            putExtra("EXERCICIO_OBSERVACOES", exercicio.observacoes)
        }
        startActivityForResult(intent, REQUEST_EDITAR_EXERCICIO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDITAR_EXERCICIO && resultCode == RESULT_OK) {
            // TODO: Recarregar os exercícios após a edição
        }
    }
}