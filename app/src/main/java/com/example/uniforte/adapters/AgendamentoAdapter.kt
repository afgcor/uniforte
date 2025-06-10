package com.example.uniforte.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforte.R
import com.example.uniforte.models.Aula

class AgendamentoAdapter(
    private var items: List<Aula>,
    private val onEdit: (Aula) -> Unit,
    private val onDelete: (String) -> Unit
) : RecyclerView.Adapter<AgendamentoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitulo: TextView = view.findViewById(R.id.tvTitulo)
        val tvDescricao: TextView = view.findViewById(R.id.tvDescricao)
        val tvData: TextView = view.findViewById(R.id.tvData)
        val tvHorario: TextView = view.findViewById(R.id.tvHorario)
        val btnEditar: Button = view.findViewById(R.id.btnEditar)
        val btnExcluir: Button = view.findViewById(R.id.btnExcluir)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_aula, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val aula = items[position]
        holder.tvTitulo.text = aula.titulo
        holder.tvDescricao.text = aula.descricao
        holder.tvData.text = aula.data
        holder.tvHorario.text = aula.horario

        holder.btnEditar.setOnClickListener { onEdit(aula) }
        holder.btnExcluir.setOnClickListener { onDelete(aula.id) }
    }

    override fun getItemCount() = items.size

    fun update(newItems: List<Aula>) {
        items = newItems
        notifyDataSetChanged()
    }
} 