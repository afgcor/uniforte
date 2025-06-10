package com.example.uniforte

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uniforte.data.network.AgendamentoRequest // Import your AgendamentoRequest data class
import com.example.uniforte.data.network.RetrofitClient // Import your RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log // Import Log for debugging

class AgendamentosActivity : AppCompatActivity() {

    private lateinit var llAgendamentosContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendamentos)

        llAgendamentosContainer = findViewById(R.id.llAgendamentosContainer)

        val btnVoltar: ImageButton = findViewById(R.id.btnVoltar)

        btnVoltar.setOnClickListener {
            finish()
        }

        // Inserir o fragmento da navegação inferior no container
        val navInferiorAlunoFragment = NavInferiorAlunoFragment()
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

        // Fetch appointments when the activity is created
        fetchAgendamentos()
    }

    override fun onResume() {
        super.onResume()
        // Refresh appointments every time the activity resumes
        fetchAgendamentos()
    }

    private fun fetchAgendamentos() {
        val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getString("USER_ID", null)

        if (userId.isNullOrEmpty()) {
            Log.e("AgendamentosActivity", "User ID not found in SharedPreferences.")
            Toast.makeText(this, "Erro: ID do usuário não encontrado.", Toast.LENGTH_SHORT).show()
            llAgendamentosContainer.removeAllViews() // Clear any old views
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Ensure your ApiService has this method: @GET("agendamentos/aluno/{alunoId}") suspend fun getAgendamentosByAlunoId(@Path("alunoId") alunoId: String): Response<List<AgendamentoRequest>>
                val response = RetrofitClient.instance.getAgendamentosByAlunoId(userId)

                withContext(Dispatchers.Main) {
                    llAgendamentosContainer.removeAllViews() // Clear existing cards before adding new ones

                    if (response.isSuccessful && response.body() != null) {
                        val agendamentos = response.body()!!
                        if (agendamentos.isNotEmpty()) {
                            for (agendamento in agendamentos) {
                                addAgendamentoCard(agendamento)
                            }
                        } else {
                            // No appointments found
                            val noAppointmentsText = TextView(this@AgendamentosActivity)
                            noAppointmentsText.text = "Nenhum agendamento encontrado para esta semana."
                            noAppointmentsText.textAlignment = View.TEXT_ALIGNMENT_CENTER
                            noAppointmentsText.setPadding(0, 50, 0, 50)
                            noAppointmentsText.textSize = 16f
                            noAppointmentsText.setTextColor(resources.getColor(R.color.unifor_marinho, null))
                            llAgendamentosContainer.addView(noAppointmentsText)
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("AgendamentosActivity", "Error fetching appointments: ${response.code()} - $errorBody")
                        Toast.makeText(this@AgendamentosActivity, "Falha ao carregar agendamentos.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("AgendamentosActivity", "Exception fetching appointments: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AgendamentosActivity, "Erro de conexão ao carregar agendamentos.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun addAgendamentoCard(agendamento: AgendamentoRequest) {
        val inflater = LayoutInflater.from(this)
        val cardView = inflater.inflate(R.layout.item_agendamento_card, llAgendamentosContainer, false)

        val tvTitle: TextView = cardView.findViewById(R.id.tvAgendamentoTitle)
        val tvDescription: TextView = cardView.findViewById(R.id.tvAgendamentoDescription)
        val tvDate: TextView = cardView.findViewById(R.id.tvAgendamentoDate)
        val tvTime: TextView = cardView.findViewById(R.id.tvAgendamentoTime)
        val ivDelete: ImageView = cardView.findViewById(R.id.ivDeleteAgendamento)

        tvTitle.text = agendamento.nome
        tvDescription.text = agendamento.descricao
        tvDate.text = "Data: ${agendamento.data}"
        tvTime.text = "Hora: ${agendamento.horario}"

        ivDelete.setOnClickListener {
            showDeleteConfirmationDialog(agendamento.aulaId)
        }

        llAgendamentosContainer.addView(cardView)
    }

    private fun showDeleteConfirmationDialog(aulaId: Int) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar exclusão")
            .setMessage("Tem certeza que deseja excluir esta aula?")
            .setPositiveButton("Excluir") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

//    private fun deleteAgendamento(aulaId: Int) {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                // Ensure your ApiService has this method: @DELETE("agendamentos/{aulaId}") suspend fun deleteAgendamento(@Path("aulaId") aulaId: Int): Response<ResponseBody>
//                val response = RetrofitClient.instance.deleteAgendamento(aulaId)
//
//                withContext(Dispatchers.Main) {
//                    if (response.isSuccessful) {
//                        Toast.makeText(this@AgendamentosActivity, "Aula excluída com sucesso!", Toast.LENGTH_SHORT).show()
//                        fetchAgendamentos() // Refresh the list after successful deletion
//                    } else {
//                        val errorBody = response.errorBody()?.string()
//                        Log.e("AgendamentosActivity", "Error deleting appointment: ${response.code()} - $errorBody")
//                        Toast.makeText(this@AgendamentosActivity, "Falha ao excluir aula: ${response.code()}", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            } catch (e: Exception) {
//                Log.e("AgendamentosActivity", "Exception deleting appointment: ${e.message}", e)
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(this@AgendamentosActivity, "Erro de conexão ao excluir aula.", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
}