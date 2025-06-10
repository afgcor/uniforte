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
    private var usuarioAtual: Usuario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        // Inicializar as views
        tvNome = findViewById(R.id.textNome)
        tvEmail = findViewById(R.id.tvEmail)
        tvTelefone = findViewById(R.id.tvTelefone)
        tvEndereco = findViewById(R.id.tvEndereco)

        val btnEditarInformacoes = findViewById<Button>(R.id.btnEditarInformacoes)
        btnEditarInformacoes.setOnClickListener {
            usuarioAtual?.let { usuario ->
                val intent = Intent(this, EditarInformacoesActivity::class.java).apply {
                    putExtra("USUARIO_ID", usuario.id)
                    putExtra("USUARIO_NOME", usuario.nome)
                    putExtra("USUARIO_EMAIL", usuario.email)
                    putExtra("USUARIO_TELEFONE", usuario.telefone)
                    putExtra("USUARIO_ENDERECO", usuario.endereco)
                }
                startActivityForResult(intent, REQUEST_EDITAR_INFORMACOES)
            } ?: run {
                Toast.makeText(this, "Erro: Dados do usuário não carregados.", Toast.LENGTH_SHORT).show()
            }
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
        usuarioAtual = usuario
        tvNome.text = usuario.nome ?: "Nome não disponível"
        tvEmail.text = usuario.email ?: "E-mail não disponível"
        tvTelefone.text = usuario.telefone ?: "Telefone não disponível"
        tvEndereco.text = usuario.endereco ?: "Endereço não disponível"
    }

    companion object {
        private const val REQUEST_EDITAR_INFORMACOES = 1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EDITAR_INFORMACOES && resultCode == RESULT_OK) {
            // Recarrega os dados do usuário após a edição
            fetchUserData()
        }
    }
}
