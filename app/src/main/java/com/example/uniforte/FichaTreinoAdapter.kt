// com.example.uniforte.adapter.FichaTreinoAdapter.kt
package com.example.uniforte.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforte.R
import com.example.uniforte.data.model.FichaTreino

class FichaTreinoAdapter(
    private var fichas: List<FichaTreino>,
    private val onItemClick: (FichaTreino) -> Unit
) : RecyclerView.Adapter<FichaTreinoAdapter.FichaTreinoViewHolder>() {

    class FichaTreinoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTituloFicha: TextView = itemView.findViewById(R.id.textTituloFicha)
        val textObjetivoFicha: TextView = itemView.findViewById(R.id.textObjetivoFicha) // Adicione este ID no XML
        val textProfessor: TextView = itemView.findViewById(R.id.textProfessor) // Adicione este ID no XML
        val textExercicios: TextView = itemView.findViewById(R.id.textExercicios) // Adicione este ID no XML
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FichaTreinoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ficha_treino, parent, false)
        return FichaTreinoViewHolder(view)
    }

    override fun onBindViewHolder(holder: FichaTreinoViewHolder, position: Int) {
        val ficha = fichas[position]
        holder.textTituloFicha.text = "Ficha de Treino #${ficha.id ?: "N/A"}" // Exemplo de título
        holder.textObjetivoFicha.text = "Descricao: ${ficha.objetivo ?: "Não definido"}"
        holder.textProfessor.text = "Professor: ${ficha.nomeProfessor ?: "Não informado"}"

        // Formata e exibe os exercícios
        val exerciciosText = ficha.exercicios?.joinToString("\n") { ex ->
            "${ex.nome} - ${ex.series}x${ex.repeticoes} (Carga: ${ex.carga ?: "N/A"})"
        } ?: "Nenhum exercício."
        holder.textExercicios.text = exerciciosText

        holder.itemView.setOnClickListener { onItemClick(ficha) }
    }

    override fun getItemCount(): Int = fichas.size

    fun updateFichas(newFichas: List<FichaTreino>) {
        fichas = newFichas
        notifyDataSetChanged()
    }
}