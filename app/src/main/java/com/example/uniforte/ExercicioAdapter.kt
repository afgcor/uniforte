// com.example.uniforte.adapter.ExercicioAdapter.kt
package com.example.uniforte

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforte.R
import com.example.uniforte.data.model.ExercicioFichaDetalhes
import com.example.uniforte.ui.EditarExercicioFichaActivity

class ExercicioAdapter(
    private val exercicios: List<ExercicioFichaDetalhes>,
    private val onEditarClick: (ExercicioFichaDetalhes) -> Unit
) : RecyclerView.Adapter<ExercicioAdapter.ExercicioViewHolder>() {

    class ExercicioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvExercicioNome: TextView = itemView.findViewById(R.id.tvExercicioNome)
        val tvExercicioMaquina: TextView = itemView.findViewById(R.id.tvExercicioMaquina)
        val tvExercicioSeriesRepeticoesCarga: TextView = itemView.findViewById(R.id.tvExercicioSeriesRepeticoesCarga)
        val tvExercicioObservacoes: TextView = itemView.findViewById(R.id.tvExercicioObservacoes)
        val ivExercicioThumb: ImageView = itemView.findViewById(R.id.ivExercicioThumb)
        val btnEditar: ImageView = itemView.findViewById(R.id.btnEditar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercicioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_exercicio, parent, false)
        return ExercicioViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExercicioViewHolder, position: Int) {
        val exercicio = exercicios[position]
        holder.tvExercicioNome.text = exercicio.nome
        holder.tvExercicioMaquina.text = "Máquina: ${exercicio.maquina ?: "N/A"}"
        holder.tvExercicioSeriesRepeticoesCarga.text =
            "Séries: ${exercicio.series ?: "N/A"} Repetições: ${exercicio.repeticoes ?: "N/A"} Carga: ${exercicio.carga ?: "N/A"}"
        holder.tvExercicioObservacoes.text = "Observações: ${exercicio.observacoes ?: "Nenhuma"}"

        // TODO: Lógica para carregar a imagem/thumbnail do exercício (ex: com Glide/Picasso)
        holder.ivExercicioThumb.setImageResource(R.drawable.ic_ficha) // Placeholder

        // Configura o botão de editar
        holder.btnEditar.setOnClickListener {
            onEditarClick(exercicio)
        }
    }

    override fun getItemCount(): Int = exercicios.size
}