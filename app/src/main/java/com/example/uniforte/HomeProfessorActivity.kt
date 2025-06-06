package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.uniforte.data.network.RetrofitInstance
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class HomeProfessorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_professor)

        // Inserir o fragmento da navegação inferior no container
        val navInferiorProfessorFragment = NavInferiorProfessorFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_nav_inferior, navInferiorProfessorFragment)
            .commit()

        // Configurar o callback para tratar cliques na navegação inferior
        navInferiorProfessorFragment.onNavItemSelected = { itemId ->
            when (itemId) {
                R.id.navHome -> {
                    // Página atual, nada a fazer
                }
                R.id.navMeusAlunos -> {
                    startActivity(Intent(this, MeusAlunosActivity::class.java))
                }
                R.id.navPerfilAdmin -> {
                    startActivity(Intent(this, PerfilAdminActivity::class.java))
                }
            }
        }

        val btnEditarAula = findViewById<Button>(R.id.btnEditarAula)
        btnEditarAula.setOnClickListener {
            val intent = Intent(this, EditarAulaActivity::class.java)
            startActivity(intent)
        }

        val verTudo = findViewById<TextView>(R.id.tvVerTudo)
        verTudo.setOnClickListener {
            startActivity(Intent(this, AgendamentosProfessorActivity::class.java))
        }

        val btnAdicionarAula = findViewById<Button>(R.id.btnAdicionarAula)
        btnAdicionarAula.setOnClickListener {
            val intent = Intent(this, AdicionarAulaActivity::class.java)
            startActivity(intent)
        }

        // Chamar a API para buscar aulas do professor
        fetchAulasProfessor()
    }

    private fun fetchAulasProfessor() {
        val sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val professorId = sharedPref.getString("USER_ID", null)

        if (professorId == null) {
            Toast.makeText(this, "ID do professor não encontrado. Faça login novamente.", Toast.LENGTH_LONG).show()
            return
        }

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getAulasByProfessorId(professorId)

                if (response.isSuccessful && response.body() != null) {
                    val responseBodyString = response.body()?.string() ?: "[]"
                    Log.d("HomeProfessorActivity", "Aulas do Professor: $responseBodyString")

                    try {
                        val jsonArray = JSONArray(responseBodyString)
                        // Exemplo de como você pode processar os dados sem criar um modelo
                        // Você pode iterar sobre o jsonArray e extrair as informações necessárias
                        // para exibir na UI, por exemplo, em um RecyclerView ou TextViews.
                        // Para este exemplo, vamos apenas mostrar um Toast com o número de aulas.
                        Toast.makeText(this@HomeProfessorActivity, "Aulas encontradas: ${jsonArray.length()}", Toast.LENGTH_LONG).show()

                        // Exemplo de como acessar o título da primeira aula (se houver)
                        if (jsonArray.length() > 0) {
                            val firstAula = jsonArray.getJSONObject(0)
                            val tituloAula = firstAula.optString("titulo", "Sem título")
                            Log.d("HomeProfessorActivity", "Título da primeira aula: $tituloAula")
                            // Você pode atualizar um TextView aqui, por exemplo:
                            // findViewById<TextView>(R.id.tvTituloAula).text = tituloAula
                        }

                    } catch (e: Exception) {
                        Log.e("HomeProfessorActivity", "Erro ao analisar JSON de aulas: ${e.message}", e)
                        Toast.makeText(this@HomeProfessorActivity, "Erro ao processar dados das aulas.", Toast.LENGTH_LONG).show()
                    }

                } else {
                    val errorBodyString = response.errorBody()?.string() ?: "{\"error\": \"Erro desconhecido\"}"
                    Log.e("HomeProfessorActivity", "Erro da API ao buscar aulas: ${response.code()} - $errorBodyString")
                    try {
                        val jsonObject = JSONObject(errorBodyString)
                        val errorMessage = jsonObject.getString("error")
                        Toast.makeText(this@HomeProfessorActivity, errorMessage, Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@HomeProfessorActivity, "Erro ao buscar aulas. Tente novamente.", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: IOException) {
                Log.e("HomeProfessorActivity", "Erro de rede ao buscar aulas: ${e.message}", e)
                Toast.makeText(this@HomeProfessorActivity, "Erro de conexão. Verifique sua internet.", Toast.LENGTH_LONG).show()
            } catch (e: HttpException) {
                Log.e("HomeProfessorActivity", "Erro HTTP ao buscar aulas: ${e.code()} - ${e.message()}", e)
                Toast.makeText(this@HomeProfessorActivity, "Erro no servidor ao buscar aulas. Tente novamente mais tarde.", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.e("HomeProfessorActivity", "Erro inesperado ao buscar aulas: ${e.message}", e)
                Toast.makeText(this@HomeProfessorActivity, "Ocorreu um erro inesperado ao buscar aulas.", Toast.LENGTH_LONG).show()
            }
        }
    }
}

