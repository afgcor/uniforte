package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText // Import EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.uniforte.data.network.UpdateUsuarioRequest
import com.example.uniforte.data.network.Usuario
import com.example.uniforte.data.network.RetrofitInstance
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import com.google.gson.JsonSyntaxException

class EditarInformacoesActivity : AppCompatActivity() {

    // Referências para os EditTexts
    private lateinit var inputNome: TextInputEditText
    private lateinit var inputEmail: TextInputEditText
    private lateinit var inputTelefone: TextInputEditText
    private lateinit var inputEndereco: TextInputEditText

    private var currentUserId: String? = null // Para armazenar o ID do usuário

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_informacoes)

        // Inicializar as views
        inputNome = findViewById(R.id.inputNome)
        inputEmail = findViewById(R.id.inputEmail)
        inputTelefone = findViewById(R.id.inputTelefone)
        inputEndereco = findViewById(R.id.inputEndereco)

        // CORREÇÃO: Permitir edição do nome
        // inputNome.isEnabled = false // Removido para permitir edição do nome

        val tipoUsuario = intent.getStringExtra("tipoUsuario") ?: "aluno" // Default para aluno se nulo

        setupNavInferior(tipoUsuario)
        setupButtons()

        // CORREÇÃO: Atribuir o valor de userId para currentUserId
        currentUserId = getSharedPreferences("AppPrefs", MODE_PRIVATE).getString("USER_ID", null)

        if (!currentUserId.isNullOrEmpty()) {
            fetchUserData(currentUserId!!)
        } else {
            Toast.makeText(this, "Erro: ID do usuário não encontrado.", Toast.LENGTH_LONG).show()
            finish() // Fecha a activity se não houver ID
        }
    }

    private fun setupNavInferior(tipoUsuario: String) {
        when (tipoUsuario) {
            "aluno" -> {
                val navInferiorAlunoFragment = NavInferiorAlunoFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container_nav_inferior, navInferiorAlunoFragment)
                    .commit()

                navInferiorAlunoFragment.onNavItemSelected = { itemId ->
                    when (itemId) {
                        R.id.navHome -> startActivity(Intent(this, HomeAlunoActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP })
                        R.id.navFicha -> startActivity(Intent(this, FichaTreinoActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP })
                        R.id.navPerfil -> {
                            finish()
                        }
                    }
                }
            }
            "professor" -> {
                // Manter a lógica original para professor
                val navInferiorProfessorFragment = NavInferiorProfessorFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container_nav_inferior, navInferiorProfessorFragment)
                    .commit()

                navInferiorProfessorFragment.onNavItemSelected = { itemId ->
                    when (itemId) {
                        R.id.navHome -> startActivity(Intent(this, HomeProfessorActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP })
                        R.id.navMeusAlunos -> startActivity(Intent(this, MeusAlunosActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP })
                        R.id.navPerfilAdmin -> startActivity(Intent(this, PerfilAdminActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP })
                    }
                }
            }
        }
    }

    private fun setupButtons() {
        findViewById<ImageButton>(R.id.btnVoltar).setOnClickListener {
            finish()
        }

        findViewById<Button>(R.id.btnSalvar).setOnClickListener {
            if (currentUserId != null) {
                saveUserData(currentUserId!!)
            } else {
                Toast.makeText(this, "Erro: ID do usuário inválido.", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<Button>(R.id.btnCancelar).setOnClickListener {
            finish()
        }

        // TODO: Adicionar listener para o botão de editar foto (iconEditarFoto) se necessário
    }

    private fun fetchUserData(userId: String) {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.buscarUsuarioPorId(userId)
                if (response.isSuccessful) {
                    val usuario = response.body()
                    if (usuario != null) {
                        populateFields(usuario)
                    } else {
                        Log.e("EditarInfoActivity", "Resposta da API bem-sucedida, mas corpo vazio.")
                        Toast.makeText(this@EditarInformacoesActivity, "Não foi possível carregar os dados.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("EditarInfoActivity", "Erro na API ao buscar: ${response.code()} - ${response.message()}")
                    Toast.makeText(this@EditarInformacoesActivity, "Erro ao carregar dados: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            } catch (e: IOException) {
                Log.e("EditarInfoActivity", "Erro de rede/IO ao buscar: ${e.message}", e)
                Toast.makeText(this@EditarInformacoesActivity, "Erro de conexão.", Toast.LENGTH_SHORT).show()
            } catch (e: HttpException) {
                Log.e("EditarInfoActivity", "Erro HTTP ao buscar: ${e.code()} - ${e.message()}", e)
                Toast.makeText(this@EditarInformacoesActivity, "Erro no servidor: ${e.code()}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Log.e("EditarInfoActivity", "Erro inesperado ao buscar: ${e.message}", e)
                Toast.makeText(this@EditarInformacoesActivity, "Ocorreu um erro inesperado.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun populateFields(usuario: Usuario) {
        inputNome.setText(usuario.nome ?: "")
        inputEmail.setText(usuario.email ?: "")
        inputTelefone.setText(usuario.telefone ?: "") // Já corrigido para String
        inputEndereco.setText(usuario.endereco ?: "")
        // Preencher CPF se houver o campo: findViewById<TextInputEditText>(R.id.inputCpf).setText(usuario.cpf ?: "")
        // TODO: Carregar imagem do usuário se houver
    }

    private fun saveUserData(userId: String) {
        // CORREÇÃO: Incluir nome na requisição
        val nome = inputNome.text.toString().trim()
        val email = inputEmail.text.toString().trim()
        val telefone = inputTelefone.text.toString().trim()
        val endereco = inputEndereco.text.toString().trim()

        // Validação simples (pode ser mais robusta)
        if (nome.isEmpty()) {
            inputNome.error = "Nome não pode estar vazio"
            return
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.error = "E-mail inválido"
            return
        }
        // Adicione validações para telefone e endereço se necessário

        // CORREÇÃO: Incluir nome na requisição de atualização
        val updateRequest = UpdateUsuarioRequest(
            nome = nome,
            email = email,
            telefone = telefone,
            endereco = endereco
        )

        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.atualizarUsuario(userId, updateRequest)
                if (response.isSuccessful) {
                    Toast.makeText(this@EditarInformacoesActivity, "Informações salvas com sucesso!", Toast.LENGTH_SHORT).show()
                    finish() // Fecha a activity após salvar
                } else {
                    Log.e("EditarInfoActivity", "Erro na API ao salvar: ${response.code()} - ${response.message()}")
                    // Tentar ler a mensagem de erro do corpo, se houver
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = if (!errorBody.isNullOrEmpty()) errorBody else "Erro ${response.code()}"
                    Toast.makeText(this@EditarInformacoesActivity, "Erro ao salvar: $errorMessage", Toast.LENGTH_LONG).show()
                }
            } catch (e: IOException) {
                Log.e("EditarInfoActivity", "Erro de rede/IO ao salvar: ${e.message}", e)
                Toast.makeText(this@EditarInformacoesActivity, "Erro de conexão ao salvar.", Toast.LENGTH_SHORT).show()
            } catch (e: HttpException) {
                Log.e("EditarInfoActivity", "Erro HTTP ao salvar: ${e.code()} - ${e.message()}", e)
                Toast.makeText(this@EditarInformacoesActivity, "Erro no servidor ao salvar: ${e.code()}", Toast.LENGTH_SHORT).show()
            } catch (e: JsonSyntaxException) {
                // CORREÇÃO: Tratamento específico para o erro de formato JSON
                Log.e("EditarInfoActivity", "Erro de formato JSON ao salvar: ${e.message}", e)
                Toast.makeText(this@EditarInformacoesActivity, "Erro no formato da resposta do servidor. Contate o administrador.", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.e("EditarInfoActivity", "Erro inesperado ao salvar: ${e.message}", e)
                Toast.makeText(this@EditarInformacoesActivity, "Ocorreu um erro inesperado ao salvar.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
