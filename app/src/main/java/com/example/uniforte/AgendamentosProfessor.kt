package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforte.adapters.AgendamentoAdapter
import com.example.uniforte.data.SupabaseInit
import com.example.uniforte.models.Aula
import kotlinx.coroutines.launch

class AgendamentosProfessor : AppCompatActivity() {

    companion object {
        const val REQUEST_EDITAR_AULA = 1001
    }

    private val supabase = SupabaseInit.supabase
    private lateinit var adapter: AgendamentoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendamentos_professor)

        // configura RecyclerView e Adapter
        val rv = findViewById<RecyclerView>(R.id.rvAgendamentos)
        adapter = AgendamentoAdapter(
            items = emptyList(),
            onEdit = { aula ->
                // abre tela de edição com dados da aula
                Intent(this, EditarAulaActivity::class.java).apply {
                    putExtra("AULA_ID", aula.id)
                    putExtra("AULA_TITULO", aula.titulo)
                    putExtra("AULA_DESCRICAO", aula.descricao)
                    putExtra("AULA_DATA", aula.data)
                    putExtra("AULA_HORARIO", aula.horario)
                }.also { startActivityForResult(it, REQUEST_EDITAR_AULA) }
            },
            onDelete = { id ->
                deleteAula(id)
            }
        )
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        fetchAulas()
    }

    private fun fetchAulas() {
        lifecycleScope.launch {
            val res = supabase
                .from("adicionar_aula")
                .select<Aula>()
                .execute()
            if (res.error == null) {
                adapter.update(res.data ?: emptyList())
            } else {
                Log.e("AgendamentoProf", "Erro fetchAulas: ${res.error.message}")
            }
        }
    }

    private fun deleteAula(id: String) {
        lifecycleScope.launch {
            val res = supabase
                .from("adicionar_aula")
                .delete()
                .eq("id", id)
                .execute()
            if (res.error == null) {
                fetchAulas()
            } else {
                Log.e("AgendamentoProf", "Erro deleteAula: ${res.error.message}")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDITAR_AULA && resultCode == RESULT_OK) {
            fetchAulas()
        }
    }
} 