package com.example.uniforte

import android.content.Context import android.content.Intent import android.content.SharedPreferences import android.os.Bundle import android.util.Log import android.view.LayoutInflater import android.widget.LinearLayout import android.widget.TextView import android.widget.Button import android.widget.ImageButton import android.widget.ImageView import android.widget.Toast import androidx.appcompat.app.AppCompatActivity import androidx.lifecycle.lifecycleScope import com.example.uniforte.data.model.FichaTreino import com.example.uniforte.data.network.RetrofitInstance import kotlinx.coroutines.launch import retrofit2.HttpException import java.io.IOException

class FichaTreinoActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPreferences
    private lateinit var containerTreinos: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ficha_treino)

        sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        containerTreinos = findViewById(R.id.containerTreinos)

        // Inserir o fragmento da navegação inferior no container
        val navInferiorAlunoFragment = NavInferiorAlunoFragment   ()
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

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        btnVoltar.setOnClickListener{
            finish()
        }

        // Remover o listener do llCardTreino1, pois os cards serão gerados dinamicamente
        // val llCardTreino = findViewById<LinearLayout>(R.id.llCardTreino1)
        // llCardTreino.setOnClickListener {
        //     val intent = Intent(this, DadosTreinoActivity::class.java)
        //     startActivity(intent)
        // }

        fetchFichasTreino()
    }

    private fun fetchFichasTreino() {
        val userId = sharedPref.getString("USER_ID", null)
        if (userId == null) {
            Toast.makeText(this, "ID do usuário não encontrado. Faça login novamente.", Toast.LENGTH_LONG).show()
            // Opcional: Redirecionar para a tela de login
            // startActivity(Intent(this, MainActivity::class.java))
            // finish()
            return
        }

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getFichasTreinoByUserId(userId)
                if (response.isSuccessful) {
                    val ficha = response.body() // Agora é um único FichaTreino
                    ficha?.let { displayFichasTreino(listOf(it)) } // Envolver em uma lista
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
            }
        }
    }

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
            val cardView = LayoutInflater.from(this).inflate(R.layout.activity_ficha_treino, containerTreinos, false) as LinearLayout

            cardView.findViewById<TextView>(R.id.textTituloFicha).text = ficha.titulo
            cardView.findViewById<TextView>(R.id.textDescricaoFicha).text = ficha.descricao
            cardView.findViewById<TextView>(R.id.textListaExercicios).text = ficha.exercicios.joinToString("\n")
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
}