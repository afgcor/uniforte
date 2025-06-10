package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import com.example.uniforte.data.Aula
import com.example.uniforte.adapters.AulaAdapter
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.uniforte.data.network.RetrofitInstance
import retrofit2.HttpException
import java.io.IOException
import com.example.uniforte.data.network.AgendamentoRequest
import android.content.Context

class AulasDisponiveisActivity : AppCompatActivity() {

    private lateinit var aulasRecyclerView: RecyclerView
    private lateinit var aulaAdapter: AulaAdapter
    private val REQUEST_EDITAR_AULA = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aulas_disponiveis)

        val navInferiorAlunoFragment = NavInferiorAlunoFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_nav_inferior, navInferiorAlunoFragment)
            .commit()

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

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        btnVoltar.setOnClickListener{
            finish()
        }

        // Initialize AulaAdapter with click listeners for both agendar and editar buttons
        aulaAdapter = AulaAdapter(
            mutableListOf(),
            onAgendarClick = { aula -> agendarAula(aula) },
            onEditarClick = { aula -> editarAula(aula) }
        )

        aulasRecyclerView = findViewById(R.id.aulasRecyclerView)
        aulasRecyclerView.layoutManager = LinearLayoutManager(this)
        aulasRecyclerView.adapter = aulaAdapter

        fetchAulasComProfessor()
    }

    private fun editarAula(aula: Aula) {
        val intent = Intent(this, EditarAulaActivity::class.java).apply {
            putExtra("AULA_ID", aula.id)
            putExtra("AULA_TITULO", aula.nome)
            putExtra("AULA_DESCRICAO", aula.descricao)
            putExtra("AULA_DATA", aula.data)
            putExtra("AULA_HORARIO", aula.horario)
        }
        startActivityForResult(intent, REQUEST_EDITAR_AULA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDITAR_AULA && resultCode == RESULT_OK) {
            // Recarrega a lista de aulas após a edição
            fetchAulasComProfessor()
        }
    }

    private fun fetchAulasComProfessor() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.getAulasComProfessor()

                if (response.isSuccessful) {
                    val aulas = response.body()
                    if (aulas != null) {
                        launch(Dispatchers.Main) {
                            aulaAdapter.updateAulas(aulas)
                            Log.d("AulasDisponiveisActivity", "Aulas carregadas: ${aulas.size}")
                        }
                    } else {
                        Log.e("AulasDisponiveisActivity", "Corpo da resposta nulo")
                        launch(Dispatchers.Main) {
                            Toast.makeText(this@AulasDisponiveisActivity, "Erro ao carregar aulas: Resposta vazia.", Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("AulasDisponiveisActivity", "Erro na requisição: ${response.code()} - $errorBody")
                    launch(Dispatchers.Main) {
                        Toast.makeText(this@AulasDisponiveisActivity, "Erro ao carregar aulas: ${response.code()} - $errorBody", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: IOException) {
                Log.e("AulasDisponiveisActivity", "Erro de rede: ${e.message}", e)
                launch(Dispatchers.Main) {
                    Toast.makeText(this@AulasDisponiveisActivity, "Erro de conexão. Verifique sua internet ou o endereço do servidor.", Toast.LENGTH_LONG).show()
                }
            } catch (e: HttpException) {
                Log.e("AulasDisponiveisActivity", "Erro HTTP: ${e.code()} - ${e.message()}", e)
                launch(Dispatchers.Main) {
                    Toast.makeText(this@AulasDisponiveisActivity, "Erro no servidor ao buscar aulas: ${e.code()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e("AulasDisponiveisActivity", "Erro inesperado: ${e.message}", e)
                launch(Dispatchers.Main) {
                    Toast.makeText(this@AulasDisponiveisActivity, "Ocorreu um erro inesperado ao buscar aulas.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun agendarAula(aula: Aula) {
        val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val alunoId = sharedPref.getString("USER_ID", null)

        if (alunoId == null) {
            Toast.makeText(this, "Erro: ID do aluno não encontrado. Por favor, faça login novamente.", Toast.LENGTH_LONG).show()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val agendamentosResponse = RetrofitInstance.api.getAgendamentosByAlunoId(alunoId)

                if (agendamentosResponse.isSuccessful) {
                    val agendamentos = agendamentosResponse.body()
                    val aulaJaAgendada = agendamentos?.any { it.aulaId == aula.id.toInt() } ?: false

                    if (aulaJaAgendada) {
                        launch(Dispatchers.Main) {
                            Toast.makeText(this@AulasDisponiveisActivity, "Você já possui essa aula agendada!", Toast.LENGTH_LONG).show()
                        }
                        return@launch
                    }
                } else {
                    val errorBody = agendamentosResponse.errorBody()?.string()
                    Log.e("AulasDisponiveisActivity", "Erro ao verificar agendamentos: ${agendamentosResponse.code()} - $errorBody")
                    launch(Dispatchers.Main) {
                        Toast.makeText(this@AulasDisponiveisActivity, "Erro ao verificar agendamentos: ${agendamentosResponse.code()} - $errorBody", Toast.LENGTH_LONG).show()
                    }
                    return@launch
                }

                val agendamentoRequest = AgendamentoRequest(
                    aulaId = aula.id.toInt(),
                    alunoId = alunoId,
                    nome = aula.nome,
                    data = aula.data,
                    descricao = aula.descricao,
                    horario = aula.horario
                )

                val response = RetrofitInstance.api.agendarAula(agendamentoRequest)

                if (response.isSuccessful) {
                    launch(Dispatchers.Main) {
                        Toast.makeText(this@AulasDisponiveisActivity, "Aula agendada com sucesso!", Toast.LENGTH_SHORT).show()
                        // Redirect to HomeAlunoActivity
                        val intent = Intent(this@AulasDisponiveisActivity, HomeAlunoActivity::class.java)
                        startActivity(intent)
                        finish() // Finish current activity
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("AulasDisponiveisActivity", "Erro ao agendar aula: ${response.code()} - $errorBody")
                    launch(Dispatchers.Main) {
                        Toast.makeText(this@AulasDisponiveisActivity, "Erro ao agendar aula: ${response.code()} - $errorBody", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: IOException) {
                Log.e("AulasDisponiveisActivity", "Erro de rede ao agendar aula: ${e.message}", e)
                launch(Dispatchers.Main) {
                    Toast.makeText(this@AulasDisponiveisActivity, "Erro de conexão. Verifique sua internet ou o endereço do servidor.", Toast.LENGTH_LONG).show()
                }
            } catch (e: HttpException) {
                Log.e("AulasDisponiveisActivity", "Erro HTTP ao agendar aula: ${e.code()} - ${e.message()}", e)
                launch(Dispatchers.Main) {
                    Toast.makeText(this@AulasDisponiveisActivity, "Erro no servidor ao agendar aula: ${e.code()}", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e("AulasDisponiveisActivity", "Erro inesperado ao agendar aula: ${e.message}", e)
                launch(Dispatchers.Main) {
                    Toast.makeText(this@AulasDisponiveisActivity, "Ocorreu um erro inesperado ao agendar aula.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

