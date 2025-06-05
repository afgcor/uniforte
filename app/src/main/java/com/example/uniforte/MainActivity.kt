package com.example.uniforte

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.uniforte.data.network.RetrofitInstance
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch // Import launch
import okhttp3.ResponseBody
import org.json.JSONObject
import java.io.IOException
import retrofit2.HttpException

class MainActivity : AppCompatActivity() {

    // Views da UI
    private lateinit var etLogin: TextInputEditText
    private lateinit var etSenha: TextInputEditText
    private lateinit var btnEntrar: Button
    private lateinit var tvCadastrar: TextView
    private lateinit var imgLogo: ImageView
    private lateinit var webVLibras: DraggableWebView
    private lateinit var progressBar: ProgressBar

    // SharedPreferences para guardar dados da sessão
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Inicialização das Views
        etLogin = findViewById(R.id.etLogin)
        etSenha = findViewById(R.id.etSenha)
        btnEntrar = findViewById(R.id.btnEntrar)
        tvCadastrar = findViewById(R.id.tvCadastrar)
        imgLogo = findViewById(R.id.imgLogo)
        webVLibras = findViewById(R.id.webVLibras)
        progressBar = findViewById(R.id.progressBar)

        // Inicializar SharedPreferences
        sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

        // Listener do Botão Entrar (com chamada de API)
        btnEntrar.setOnClickListener {
            val email = etLogin.text.toString().trim()
            val senha = etSenha.text.toString().trim()

            // Validações básicas
            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etLogin.error = "E-mail inválido"
                return@setOnClickListener
            }
            if (senha.isEmpty()) {
                etSenha.error = "Senha obrigatória"
                return@setOnClickListener
            }
            etLogin.error = null
            etSenha.error = null

            fazerLogin(email, senha)
        }

        val textoOriginal = "Não possui conta? Cadastre-se :)"
        val spannableString = SpannableString(textoOriginal)
        spannableString.setSpan(
            UnderlineSpan(), 0,
            textoOriginal.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tvCadastrar.text = spannableString
        tvCadastrar.setOnClickListener {
            val intent = Intent(this, CadastroActivity::class.java)
            startActivity(intent)
        }

        imgLogo.setOnClickListener {
//             Ação: navegar para HomeProfessorActivity
             val intent = Intent(this, HomeProfessorActivity::class.java)
             startActivity(intent)
        }

        // Configuração do VLibras
        setupVLibras()
    }

    // Função para realizar o Login via API (abordagem simplificada)
    private fun fazerLogin(email: String, senha: String) {
        progressBar.visibility = View.VISIBLE // Mostrar ProgressBar
        btnEntrar.isEnabled = false // Desabilitar botão

        lifecycleScope.launch {
            try {
                val requestBody = mapOf("email" to email, "senha" to senha)

                // Chama a API via Retrofit
                val response = RetrofitInstance.api.login(requestBody)

                if (response.isSuccessful && response.body() != null) {
                    // Sucesso na chamada
                    val responseBody: ResponseBody? = response.body()
                    val responseBodyString: String = responseBody?.string() ?: "{}" // Usar elvis operator
                    Log.d("MainActivity", "Resposta JSON: $responseBodyString")
                    try {
                        val jsonObject = JSONObject(responseBodyString)
                        val token = jsonObject.getString("token")
                        val userObject = jsonObject.getJSONObject("user")
                        val userId = userObject.getString("id") // Ou getInt, etc.
                        val userEmail = userObject.getString("email")
                        val userName = userObject.optString("nome", userEmail) // Usa optString para evitar erro se "nome" não existir

                        Log.i("MainActivity", "Login bem-sucedido! Token: $token, UserID: $userId, Email: $userEmail, Nome: $userName")

                        with (sharedPref.edit()) {
                            putString("USER_ID", userId)
                            putString("USER_TOKEN", token)
                            putString("USER_NAME", userName)
                            putString("USER_EMAIL", userEmail)
                            apply() // Salva de forma assíncrona
                        }
                        Log.i("MainActivity", "Dados da sessão guardados em SharedPreferences.")

                        val intent = Intent(this@MainActivity, HomeAlunoActivity::class.java)

                        startActivity(intent)
                        finish()

                    } catch (e: Exception) {
                        Log.e("MainActivity", "Erro ao analisar JSON de sucesso: ${e.message}", e)
                        Toast.makeText(this@MainActivity, "Erro ao processar a resposta do servidor.", Toast.LENGTH_LONG).show()
                    }
                } else {
                    // Erro retornado pela API (ex: 401, 400)
                    val errorBodyString = response.errorBody()?.string() ?: "{\"error\": \"Erro desconhecido\"}" // Corpo de erro padrão
                    Log.e("MainActivity", "Erro da API ${response.code()}: $errorBodyString")
                    try {
                        // Tentar analisar o JSON de erro
                        val jsonObject = JSONObject(errorBodyString)
                        val errorMessage = jsonObject.getString("error")
                        Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
                    } catch (e: Exception) {
                        // Falha ao analisar JSON de erro
                        Toast.makeText(this@MainActivity, "Credenciais inválidas ou erro no servidor.", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: IOException) {
                Log.e("MainActivity", "Erro de rede: ${e.message}", e)
                Toast.makeText(this@MainActivity, "Erro de conexão. Verifique sua internet.", Toast.LENGTH_LONG).show()
            } catch (e: HttpException) {
                // Erro HTTP (ex: 500 Internal Server Error)
                Log.e("MainActivity", "Erro HTTP: ${e.code()} - ${e.message()}", e)
                Toast.makeText(this@MainActivity, "Erro no servidor. Tente novamente mais tarde.", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                // Outros erros inesperados
                Log.e("MainActivity", "Erro inesperado: ${e.message}", e)
                Toast.makeText(this@MainActivity, "Ocorreu um erro inesperado.", Toast.LENGTH_LONG).show()
            } finally {
                // Executado sempre ao final do try/catch
                progressBar.visibility = View.GONE // Esconder ProgressBar
                btnEntrar.isEnabled = true // Reabilitar botão
            }
        }
    }

    // --- Funções Originais Mantidas ---

    private fun setupVLibras() {
        val webSettings = webVLibras.settings
        webSettings.javaScriptEnabled = true
        webVLibras.setBackgroundColor(0x00000000) // Fundo transparente

        val rootView = findViewById<View>(android.R.id.content)
        val textoCompleto = coletarTextosDaTela(rootView)
        atualizarTextoVLibras(textoCompleto)

        // Listener de toque para tornar o WebView arrastável
        webVLibras.setOnTouchListener(object : View.OnTouchListener {
            var downRawX = 0f
            var downRawY = 0f
            var dX = 0f
            var dY = 0f
            var isDragging = false
            val CLICK_DRAG_TOLERANCE = 10f

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        downRawX = event.rawX
                        downRawY = event.rawY
                        dX = view.x - downRawX
                        dY = view.y - downRawY
                        isDragging = false
                        return false
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val newX = event.rawX + dX
                        val newY = event.rawY + dY

                        // Limitar o movimento dentro dos limites do pai
                        val parentView = view.parent as View
                        val maxX = parentView.width - view.width
                        val maxY = parentView.height - view.height
                        view.x = newX.coerceIn(0f, maxX.toFloat())
                        view.y = newY.coerceIn(0f, maxY.toFloat())

                        // Verificar se é um movimento de arrasto significativo
                        val deltaX = kotlin.math.abs(event.rawX - downRawX)
                        val deltaY = kotlin.math.abs(event.rawY - downRawY)
                        if (deltaX > CLICK_DRAG_TOLERANCE || deltaY > CLICK_DRAG_TOLERANCE) {
                            isDragging = true
                        }
                        // Retorna true para consumir o evento de movimento (arrasto)
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        return isDragging
                    }

                    else -> return false
                }
            }
        })
    }

    // Coleta textos visíveis na tela para o VLibras
    private fun coletarTextosDaTela(view: View): String {
        val builder = StringBuilder()
        if (view is TextView && view.visibility == View.VISIBLE) {
            val texto = view.text.toString().trim()
            if (texto.isNotEmpty()) {
                builder.append(texto).append(". ")
            }
        } else if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                builder.append(coletarTextosDaTela(view.getChildAt(i)))
            }
        }
        return builder.toString()
    }

    // Gera o HTML para carregar o plugin VLibras
    private fun gerarHtmlVLibras(texto: String): String {
        // Escapar caracteres especiais no texto para evitar problemas no HTML/JS
        val textoEscapado = android.text.Html.escapeHtml(texto)
        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
                <meta charset="utf-8" />
                <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">
                <style>
                    body { margin: 0; padding: 0; background: transparent; overflow: hidden; }
                    /* Esconde o parágrafo, usado apenas para passar o texto */
                    p { display: none; }
                    /* Ajustes para o botão VLibras */
                    div[vw-access-button] { width: 50px !important; height: 50px !important; }
                </style>
            </head>
            <body>
                <p id="content">$textoEscapado</p>
                <div vw class="enabled">
                    <div vw-access-button class="active"></div>
                    <div vw-plugin-wrapper>
                        <div class="vw-plugin-top-wrapper"></div>
                    </div>
                </div>
                <script src="https://vlibras.gov.br/app/vlibras-plugin.js"></script>
                <script>
                    new window.VLibras.Widget("https://vlibras.gov.br/app");
                    // Opcional: Tentar passar o texto para o widget após inicialização
                    // document.addEventListener('DOMContentLoaded', function() {
                    //     const widget = window.VLibras.widget;
                    //     if (widget) {
                    //         const textContent = document.getElementById('content').innerText;
                    //         widget.setText(textContent);
                    //     }
                    // });
                </script>
            </body>
            </html>
        """.trimIndent()
    }

    private fun atualizarTextoVLibras(texto: String) {
        val html = gerarHtmlVLibras(texto)
        webVLibras.loadDataWithBaseURL("https://vlibras.gov.br/app", html, "text/html", "UTF-8", null)
    }
}

