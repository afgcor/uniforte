package com.example.uniforte.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforte.R
import com.example.uniforte.data.model.AulaProfessor

class AgendamentosProfessorAdapter(
    private var aulas: List<AulaProfessor>
) : RecyclerView.Adapter<AgendamentosProfessorAdapter.AulaViewHolder>() {

    inner class AulaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nomeAula: TextView = view.findViewById(R.id.txtTitulo)
        val descricaoAula: TextView = view.findViewById(R.id.txtDescricao)
        val horarioDataAula: TextView = view.findViewById(R.id.txtDataHora)
        val btnEditar: ImageView = view.findViewById(R.id.btnEditar)
        val btnExcluir: ImageView = view.findViewById(R.id.btnExcluir)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AulaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_aula_professor, parent, false)
        return AulaViewHolder(view)
    }

    override fun onBindViewHolder(holder: AulaViewHolder, position: Int) {
        val aula = aulas[position]

        holder.nomeAula.text = aula.titulo
        holder.descricaoAula.text = aula.descricao
        holder.horarioDataAula.text = "${aula.data} - ${aula.horario}"

        holder.btnEditar.setOnClickListener {
            // Aqui futuramente você pode implementar a lógica de editar a aula
        }

        holder.btnExcluir.setOnClickListener {
            // Aqui futuramente você pode implementar a lógica de excluir a aula
        }
    }

    override fun getItemCount(): Int = aulas.size

    fun updateList(novaLista: List<AulaProfessor>) {
        aulas = novaLista
        notifyDataSetChanged()
    }
}
