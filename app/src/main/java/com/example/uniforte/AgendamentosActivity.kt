package com.example.uniforte
import com.example.uniforte.data.network.AgendamentoRequest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView // Use ImageView for btnBack as per activity_agendamentos.xml
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uniforte.data.model.Agendamento
import com.example.uniforte.data.network.RetrofitClient // Assuming RetrofitClient is your setup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AgendamentosActivity : AppCompatActivity() {

    private lateinit var llAgendamentosContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agendamentos)

        llAgendamentosContainer = findViewById(R.id.llAgendamentosContainer)

        // Corrected: Use R.id.btnBack as per activity_agendamentos.xml
        val btnVoltar: ImageView = findViewById(R.id.btnVoltar)

        btnVoltar.setOnClickListener { // Changed from btnVoltar
            onBackPressedDispatcher.onBackPressed() // Use onBackPressedDispatcher for modern Android
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

    private fun addAgendamentoCard(agendamento: Agendamento) { // <-- CHANGED PARAMETER TYPE TO Agendamento
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
            // Pass the primary key 'id' of the agendamento
            showDeleteConfirmationDialog(agendamento.id) // <-- CORRECTED HERE to use agendamento.id
        }

        llAgendamentosContainer.addView(cardView)
    }

    private fun showDeleteConfirmationDialog(agendamentoIdToDelete: Int) { // Renamed parameter for clarity
        AlertDialog.Builder(this)
            .setTitle("Confirmar exclusão")
            .setMessage("Tem certeza que deseja excluir esta aula?")
            .setPositiveButton("Excluir") { dialog, _ ->
                deleteAgendamento(agendamentoIdToDelete) // Pass the correct ID
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // This function signature and internal call are already correct for converting Int to String for API
    private fun deleteAgendamento(agendamentoID: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // The API expects a String, so .toString() is correctly used here
                val response = RetrofitClient.instance.deleteAgendamento(agendamentoID.toString())

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AgendamentosActivity, "Aula excluída com sucesso!", Toast.LENGTH_SHORT).show()
                        fetchAgendamentos() // Refresh the list after successful deletion
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("AgendamentosActivity", "Error deleting appointment: ${response.code()} - $errorBody")
                        Toast.makeText(this@AgendamentosActivity, "Falha ao excluir aula: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("AgendamentosActivity", "Exception deleting appointment: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AgendamentosActivity, "Erro de conexão ao excluir aula.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}