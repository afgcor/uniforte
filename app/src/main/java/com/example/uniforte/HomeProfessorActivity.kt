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
import retrofit2.HttpException
import java.io.IOException
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.uniforte.data.Aula // Importar a classe Aula

class HomeProfessorActivity : AppCompatActivity() {

    private lateinit var tvOlaProfessor: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_professor)

        tvOlaProfessor = findViewById(R.id.tvOlaProfessor)

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
        atualizarNomeProfessor(false)
    }

    override fun onResume() {
        super.onResume()
        atualizarNomeProfessor(false)
    }

    private fun atualizarNomeProfessor(mostrarCarregamento: Boolean) {
        // Não há ProgressBar no layout do professor, então ignoramos o mostrarCarregamento por enquanto
        val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val nomeProfessor = sharedPref.getString("USER_NAME", "")

        if (!nomeProfessor.isNullOrEmpty()) {
            tvOlaProfessor.text = "Olá professor, $nomeProfessor!"
        } else {
            tvOlaProfessor.text = "Olá professor!"
        }
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
                // Usar getAulasByProfessorId que aceita o ID do professor
                val response = RetrofitInstance.api.getAulasByProfessorId(professorId)

                if (response.isSuccessful && response.body() != null) {
                    val aulas: List<Aula> = response.body()!!
                    Log.d("HomeProfessorActivity", "Aulas do Professor: ${aulas.size} encontradas")

                    if (aulas.isNotEmpty()) {
                        Toast.makeText(this@HomeProfessorActivity, "Aulas encontradas: ${aulas.size}", Toast.LENGTH_LONG).show()
                        val firstAula = aulas[0]
                        val tituloAula = firstAula.nome // Assumindo que 'nome' é o título da aula
                        Log.d("HomeProfessorActivity", "Título da primeira aula: $tituloAula")
                        // Você pode atualizar um TextView aqui, por exemplo:
                        // findViewById<TextView>(R.id.tvTituloAula).text = tituloAula
                    } else {
                        Toast.makeText(this@HomeProfessorActivity, "Nenhuma aula encontrada para este professor.", Toast.LENGTH_LONG).show()
                    }

                } else {
                    val errorBodyString = response.errorBody()?.string() ?: "{\"error\": \"Erro desconhecido\"}"
                    Log.e("HomeProfessorActivity", "Erro da API ao buscar aulas: ${response.code()} - $errorBodyString")
                    Toast.makeText(this@HomeProfessorActivity, "Erro ao buscar aulas. Tente novamente.", Toast.LENGTH_LONG).show()
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


