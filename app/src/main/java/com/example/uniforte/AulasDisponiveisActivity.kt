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

class AulasDisponiveisActivity : AppCompatActivity() {

    private lateinit var aulasRecyclerView: RecyclerView
    private lateinit var aulaAdapter: AulaAdapter

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

//        val btnAgendarAula = findViewById<Button>(R.id.agendarPilates)
//        btnAgendarAula.setOnClickListener{
//            finish()
//            Toast.makeText(this, "Aula agendada com sucesso!", Toast.LENGTH_SHORT).show()
//        }

        aulasRecyclerView = findViewById(R.id.aulasRecyclerView)
        aulasRecyclerView.layoutManager = LinearLayoutManager(this)
        aulaAdapter = AulaAdapter(mutableListOf())
        aulasRecyclerView.adapter = aulaAdapter

        fetchAulasComProfessor()
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
}


