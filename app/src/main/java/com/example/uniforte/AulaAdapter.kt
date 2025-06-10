package com.example.uniforte.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforte.R
import com.example.uniforte.data.Aula
import com.example.uniforte.EditarAulaActivity

class AulaAdapter(
    private val aulas: MutableList<Aula>, 
    private val onAgendarClick: (Aula) -> Unit,
    private val onEditarClick: (Aula) -> Unit
) : RecyclerView.Adapter<AulaAdapter.AulaViewHolder>() {

    class AulaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val aulaNomeTextView: TextView = itemView.findViewById(R.id.aulaNomeTextView)
        val aulaDescricaoTextView: TextView = itemView.findViewById(R.id.aulaDescricaoTextView)
        val aulaHorarioDataTextView: TextView = itemView.findViewById(R.id.aulaHorarioDataTextView)
        val aulaProfessorTextView: TextView = itemView.findViewById(R.id.aulaProfessorTextView)
        val btnAgendar: Button = itemView.findViewById(R.id.btnAgendar)
        val btnEditar: Button = itemView.findViewById(R.id.btnEditar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AulaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_aula, parent, false)
        return AulaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AulaViewHolder, position: Int) {
        val aula = aulas[position]
        holder.aulaNomeTextView.text = aula.nome
        holder.aulaDescricaoTextView.text = aula.descricao
        holder.aulaHorarioDataTextView.text = " Data: ${aula.data} | Horário: ${aula.horario} "
        holder.aulaProfessorTextView.text = "Professor: ${aula.professor.usuarios.nome}"

        holder.btnAgendar.setOnClickListener { onAgendarClick(aula) }
        holder.btnEditar.setOnClickListener { onEditarClick(aula) }
    }

    override fun getItemCount(): Int = aulas.size

    fun updateAulas(newAulas: List<Aula>) {
        aulas.clear()
        aulas.addAll(newAulas)
        notifyDataSetChanged()
    }
}