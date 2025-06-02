package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.uniforte.data.network.Usuario
import com.example.uniforte.data.network.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class PerfilActivity : AppCompatActivity() {

    // Referências para os TextViews onde os dados serão exibidos
    private lateinit var tvNome: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvTelefone: TextView
    private lateinit var tvEndereco: TextView
    // Adicione outras referências se necessário (ex: ImageView para foto)
    // private lateinit var imgUsuario: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Inicializar as views
        tvNome = findViewById(R.id.textNome) // ID do layout activity_perfil.xml
        tvEmail = findViewById(R.id.tvEmail) // ID do layout activity_perfil.xml
        tvTelefone = findViewById(R.id.tvTelefone) // ID do layout activity_perfil.xml
        tvEndereco = findViewById(R.id.tvEndereco) // ID do layout activity_perfil.xml
        // imgUsuario = findViewById(R.id.imgUsuario)

        val btnEditarInformacoes = findViewById<Button>(R.id.btnEditarInformacoes)
        btnEditarInformacoes.setOnClickListener {
            val intent = Intent(this, EditarInformacoesActivity::class.java)
            intent.putExtra("tipoUsuario", "aluno")
            startActivity(intent)
        }

        val btnSair = findViewById<Button>(R.id.btnSair)
        btnSair.setOnClickListener {
            val sharedPref = getSharedPreferences("AppPrefs", MODE_PRIVATE)
            with (sharedPref.edit()) {
                remove("USER_ID")
                apply()
            }

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Limpar pilha de activities
            startActivity(intent)
            finish()
        }



        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        btnVoltar.setOnClickListener {
            finish()
        }

        setupNavInferior()

    }

    override fun onResume() {
        super.onResume()
        // Buscar (ou re-buscar) dados do usuário sempre que a tela ficar visível
        fetchUserData()
    }

    private fun setupNavInferior() {
        val navInferiorAlunoFragment = NavInferiorAlunoFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_nav_inferior, navInferiorAlunoFragment)
            .commit()

        navInferiorAlunoFragment.onNavItemSelected = { itemId ->
            when (itemId) {
                R.id.navHome -> {
                    startActivity(Intent(this, HomeAlunoActivity::class.java))
                    finish() // Finaliza a atual para não empilhar
                }
                R.id.navFicha -> {
                    startActivity(Intent(this, FichaTreinoActivity::class.java))
                    finish() // Finaliza a atual para não empilhar
                }
                R.id.navPerfil -> {
                    // Página atual, nada a fazer
                }
            }
        }
    }

    private fun fetchUserData() {
        val userId = getSharedPreferences("AppPrefs", MODE_PRIVATE).getString("USER_ID", null)
        if (userId.isNullOrEmpty()) {
            Toast.makeText(this, "Erro: ID do usuário não encontrado.", Toast.LENGTH_LONG).show()
            // TODO: Talvez redirecionar para o login?
            return
        }

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.buscarUsuarioPorId(userId)
                if (response.isSuccessful) {
                    val usuario = response.body()
                    if (usuario != null) {
                        displayUserData(usuario)
                    } else {
                        Log.e("PerfilActivity", "Resposta da API bem-sucedida, mas corpo vazio.")
                        Toast.makeText(this@PerfilActivity, "Não foi possível carregar os dados do usuário.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("PerfilActivity", "Erro na API: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@PerfilActivity, "Erro ao buscar dados: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                Log.e("PerfilActivity", "Erro de rede ou IO: ${e.message}", e)
                Toast.makeText(this@PerfilActivity, "Erro de conexão. Verifique sua internet.", Toast.LENGTH_SHORT).show()
            } catch (e: HttpException) {
                Log.e("PerfilActivity", "Erro HTTP: ${e.code()} - ${e.message()}", e)
                Toast.makeText(this@PerfilActivity, "Erro no servidor: ${e.code()}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("PerfilActivity", "Erro inesperado: ${e.message}", e)
                Toast.makeText(this@PerfilActivity, "Ocorreu um erro inesperado.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayUserData(usuario: Usuario) {
        tvNome.text = usuario.nome ?: "Nome não disponível"
        tvEmail.text = usuario.email ?: "E-mail não disponível"
        tvTelefone.text = usuario.telefone ?: "Telefone não disponível" // Não precisa mais de toString() pois agora é String
        tvEndereco.text = usuario.endereco ?: "Endereço não disponível"

    }
}
