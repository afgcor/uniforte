package com.example.uniforte

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar // Adicione ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uniforte.adapter.FichaTreinoAdapter // Importe o novo adapter
import com.example.uniforte.data.model.FichaTreino
import com.example.uniforte.data.network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class FichaTreinoActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var rvFichasTreino: RecyclerView // Mude para RecyclerView
    private lateinit var fichaTreinoAdapter: FichaTreinoAdapter
    private lateinit var progressBar: ProgressBar // Declare ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ficha_treino)

        sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        rvFichasTreino = findViewById(R.id.rvFichasTreino) // Inicialize o RecyclerView
        progressBar = findViewById(R.id.progressBar) // Inicialize a ProgressBar (adicione no XML se não tiver)

        setupRecyclerView() // Configura o RecyclerView

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
                    // Página atual, nada a fazer
                }
                R.id.navPerfil -> {
                    startActivity(Intent(this, PerfilActivity::class.java))
                }
            }
        }

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        btnVoltar.setOnClickListener {
            finish()
        }

        fetchFichasTreino()
    }

    private fun setupRecyclerView() {
        fichaTreinoAdapter = FichaTreinoAdapter(
            emptyList(),
            onItemClick = { ficha ->
                // Ação ao clicar em um card de ficha de treino
                Toast.makeText(this, "Ficha de Treino: ${ficha.id ?: ficha.objetivo}", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, DadosTreinoActivity::class.java).apply {
                    // Passe os dados da ficha para a próxima atividade
                    putExtra("FICHA_ID", ficha.id)
                    // ... outros dados que DadosTreinoActivity precisa
                }
                startActivity(intent)
            }
        )
        rvFichasTreino.layoutManager = LinearLayoutManager(this)
        rvFichasTreino.adapter = fichaTreinoAdapter
    }

    private fun fetchFichasTreino() {
        progressBar.visibility = View.VISIBLE // Mostra a ProgressBar
        val userId = sharedPref.getString("USER_ID", null)
        val userToken = sharedPref.getString("USER_TOKEN", null) // Obtenha o token

        if (userId == null) {
            Toast.makeText(this, "ID do usuário não encontrado. Faça login novamente.", Toast.LENGTH_LONG).show()
            progressBar.visibility = View.GONE
            return
        }
        if (userToken == null) {
            Toast.makeText(this, "Token de autenticação não encontrado. Faça login novamente.", Toast.LENGTH_LONG).show()
            progressBar.visibility = View.GONE
            return
        }

        lifecycleScope.launch {
            try {
                // A API de listarFichasTreinoPorAlunoId retorna uma LISTA de FichaTreino
                val response = RetrofitInstance.api.getFichasTreinoByUserId(userId, "Bearer $userToken")

                if (response.isSuccessful && response.body() != null) {
                    val fichas = response.body()!!
                    fichaTreinoAdapter.updateFichas(fichas)
                    if (fichas.isEmpty()) {
                        Toast.makeText(this@FichaTreinoActivity, "Nenhuma ficha de treino encontrada.", Toast.LENGTH_SHORT).show()
                    }
                    Log.d("FichaTreinoActivity", "Fichas carregadas: ${fichas.size}")
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("FichaTreinoActivity", "Erro ao buscar fichas: ${response.code()} - $errorBody")
                    Toast.makeText(this@FichaTreinoActivity, "Erro ao carregar fichas de treino.", Toast.LENGTH_LONG).show()
                }
            } catch (e: IOException) {
                Log.e("FichaTreinoActivity", "Erro de rede: ${e.message}", e)
                Toast.makeText(this@FichaTreinoActivity, "Erro de conexão. Verifique sua internet.", Toast.LENGTH_LONG).show()
            } catch (e: HttpException) {
                Log.e("FichaTreinoActivity", "Erro HTTP: ${e.code()} - ${e.message()}", e)
                Toast.makeText(this@FichaTreinoActivity, "Erro no servidor ao buscar fichas.", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.e("FichaTreinoActivity", "Erro inesperado: ${e.message}", e)
                Toast.makeText(this@FichaTreinoActivity, "Ocorreu um erro inesperado ao buscar fichas.", Toast.LENGTH_LONG).show()
            } finally {
                progressBar.visibility = View.GONE // Esconde a ProgressBar
            }
        }
    }

    // `displayFichasTreino` não é mais necessário com o RecyclerView e Adapter
    /*
    private fun displayFichasTreino(fichas: List<FichaTreino>) {
        containerTreinos.removeAllViews() // Limpa os cards existentes

        if (fichas.isEmpty()) {
            val noDataTextView = TextView(this).apply {
                text = "Nenhuma ficha de treino encontrada para este usuário."
                textSize = 16f
                setPadding(16, 16, 16, 16)
            }
            containerTreinos.addView(noDataTextView)
            return
        }

        fichas.forEach { ficha ->

            // val cardView = LayoutInflater.from(this).inflate(R.layout.activity_ficha_treino, containerTreinos, false) as LinearLayout

            val cardView = LayoutInflater.from(this).inflate(R.layout.item_ficha_treino, containerTreinos, false) as LinearLayout


            cardView.findViewById<TextView>(R.id.textTituloFicha).text = ficha.titulo
            cardView.findViewById<TextView>(R.id.textDescricaoFicha).text = ficha.descricao
            cardView.findViewById<TextView>(R.id.textListaExercicios).text = ficha.exercicios?.joinToString("\n") // Use `?` para segurança
            cardView.findViewById<TextView>(R.id.btnProfessor1).text = ficha.nomeProfessor

            cardView.setOnClickListener {
                val intent = Intent(this, DadosTreinoActivity::class.java)
                // Passar dados da ficha para a próxima atividade, se necessário
                // intent.putExtra("FICHA_ID", ficha.id)
                // intent.putExtra("FICHA_TITULO", ficha.titulo)
                startActivity(intent)
            }
            containerTreinos.addView(cardView)
        }
    }
    */
}