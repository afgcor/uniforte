package com.example.uniforte

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import android.util.Log // Importe para logs

// Imports para Coroutines e Retrofit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.uniforte.data.network.RetrofitClient


class HomeAlunoActivity : AppCompatActivity() {

    private lateinit var tvOlaUsuario: TextView
    private lateinit var webVLibras: DraggableWebView
    private lateinit var progressBarNome: ProgressBar
    private lateinit var editarPerfilLauncher: ActivityResultLauncher<Intent>

    // Novos TextViews para exibir o objetivo e nome do professor da ficha de treino
    private lateinit var tvTituloObjetivo: TextView
    private lateinit var tvDescricaoObjetivo: TextView
    // Se você tiver uma ProgressBar para a seção de objetivos, descomente e inicialize
    // private lateinit var progressBarObjetivos: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_aluno)

        // Inicializar views
        tvOlaUsuario = findViewById(R.id.tvOlaUsuario)
        webVLibras = findViewById(R.id.webVLibras)
        progressBarNome = findViewById(R.id.progressBarNome)
        progressBarNome.visibility = View.GONE

        // Inicialize os novos TextViews
        tvTituloObjetivo = findViewById(R.id.tvTituloObjetivo)
        tvDescricaoObjetivo = findViewById(R.id.tvDescricaoObjetivo)
        // progressBarObjetivos = findViewById(R.id.progressBarObjetivos) // Se existir
        // progressBarObjetivos.visibility = View.GONE // Começa invisível

        editarPerfilLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            atualizarNomeUsuario(true)
            // Atualize os objetivos também, caso o perfil tenha alterado algo relacionado
            fetchFichasTreino()
        }

        atualizarNomeUsuario(false)

        val navInferiorAlunoFragment = NavInferiorAlunoFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_nav_inferior, navInferiorAlunoFragment)
            .commit()

        navInferiorAlunoFragment.onNavItemSelected = { itemId ->
            when (itemId) {
                R.id.navHome -> {
                    // Já está na Home, pode querer forçar um refresh dos dados
                    fetchFichasTreino()
                }
                R.id.navFicha -> {
                    startActivity(Intent(this, FichaTreinoActivity::class.java))
                }
                R.id.navPerfil -> {
                    val intent = Intent(this, PerfilActivity::class.java)
                    editarPerfilLauncher.launch(intent)
                }
            }
        }

        findViewById<TextView>(R.id.tvVerTudo).setOnClickListener {
            startActivity(Intent(this, AgendamentosActivity::class.java))
        }

        findViewById<Button>(R.id.btnAgendarNovaAula).setOnClickListener {
            startActivity(Intent(this, AulasDisponiveisActivity::class.java))
        }

        findViewById<ImageView>(R.id.ivFeedbackCalorico).setOnClickListener {
            startActivity(Intent(this, FeedbackActivity::class.java))
        }

        webVLibras = findViewById(R.id.webVLibras)
        val webSettings = webVLibras.settings
        webSettings.javaScriptEnabled = true
        webVLibras.setBackgroundColor(0x00000000)

        val btnAtivarVLibras = findViewById<MaterialButton>(R.id.btnAtivarVLibras)

        btnAtivarVLibras.setOnClickListener {
            if (webVLibras.visibility == View.VISIBLE) {
                webVLibras.visibility = View.GONE
            } else {
                webVLibras.visibility = View.VISIBLE
            }
        }

        val rootView = findViewById<View>(android.R.id.content)
        val textoCompleto = coletarTextosDaTela(rootView)
        atualizarTextoVLibras(textoCompleto)

        // Habilitar movimentação do WebView VLibras
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
                        val maxX = (view.parent as View).width - view.width
                        val maxY = (view.parent as View).height - view.height
                        view.x = newX.coerceIn(0f, maxX.toFloat())
                        view.y = newY.coerceIn(0f, maxY.toFloat())

                        val deltaX = kotlin.math.abs(event.rawX - downRawX)
                        val deltaY = kotlin.math.abs(event.rawY - downRawY)
                        if (deltaX > CLICK_DRAG_TOLERANCE || deltaY > CLICK_DRAG_TOLERANCE) {
                            isDragging = true
                        }
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        return isDragging
                    }

                    else -> return false
                }
            }
        })

        // CHAMADA AO BACKEND PARA BUSCAR FICHAS DE TREINO
        fetchFichasTreino()
    }

    private fun atualizarNomeUsuario(mostrarCarregamento: Boolean) {
        if (mostrarCarregamento) {
            progressBarNome.visibility = View.VISIBLE
            tvOlaUsuario.visibility = View.INVISIBLE

            Handler(Looper.getMainLooper()).postDelayed({
                val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                // Supondo que você salva o nome do usuário como "USER_NAME"
                val nomeUsuario = sharedPref.getString("USER_NAME", "")

                if (!nomeUsuario.isNullOrEmpty()) {
                    tvOlaUsuario.text = "Olá, $nomeUsuario!"
                } else {
                    tvOlaUsuario.text = "Olá!"
                }

                progressBarNome.visibility = View.GONE
                tvOlaUsuario.visibility = View.VISIBLE
            }, 500)
        } else {
            val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val nomeUsuario = sharedPref.getString("USER_NAME", "")

            if (!nomeUsuario.isNullOrEmpty()) {
                tvOlaUsuario.text = "Olá, $nomeUsuario!"
            } else {
                tvOlaUsuario.text = "Olá!"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Atualiza o nome do usuário e busca as fichas de treino sempre que a Activity volta ao foco
        atualizarNomeUsuario(false)
        fetchFichasTreino()
    }

    private fun fetchFichasTreino() {
        // progressBarObjetivos.visibility = View.VISIBLE // Mostra ProgressBar enquanto carrega

        val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val alunoId = sharedPref.getString("USER_ID", null)

        // LOG 1: Verifica qual alunoId está sendo enviado
        Log.d("FichaTreinoDebug", "Aluno ID sendo buscado: $alunoId")

        if (alunoId == null) {
            Log.e("HomeAlunoActivity", "Erro: Aluno ID não encontrado nas SharedPreferences.")
            tvTituloObjetivo.text = "Erro"
            tvDescricaoObjetivo.text = "ID do aluno não encontrado."
            // progressBarObjetivos.visibility = View.GONE
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.getFichasTreinoByAlunoId(alunoId)

                withContext(Dispatchers.Main) {
                    // progressBarObjetivos.visibility = View.GONE // Esconde ProgressBar

                    // LOG 2: Verifica o status da resposta
                    Log.d("FichaTreinoDebug", "API Response Code: ${response.code()}")
                    Log.d("FichaTreinoDebug", "API Response Message: ${response.message()}")

                    if (response.isSuccessful) {
                        val fichas = response.body()

                        // LOG 3: Verifica o corpo da resposta (se não for nulo)
                        Log.d("FichaTreinoDebug", "API Response Body (Sucesso): $fichas")

                        if (!fichas.isNullOrEmpty()) {
                            val primeiraFicha = fichas[0]
                            tvTituloObjetivo.text = primeiraFicha.objetivo
//                            tvDescricaoObjetivo.text = "Professor(a): ${primeiraFicha.nomeProfessor ?: "Não informado"}"
                        } else {
                            // Este é o caminho que está sendo executado atualmente
                            tvTituloObjetivo.text = "Nenhum objetivo encontrado"
                            tvDescricaoObjetivo.text = "Crie uma nova ficha de treino."
                            Log.d("FichaTreinoDebug", "Fichas de treino vazias ou nulas para o aluno ID: $alunoId")
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("HomeAlunoActivity", "Erro ao buscar fichas de treino: ${response.code()} - ${response.message()} - $errorBody")
                        tvTituloObjetivo.text = "Erro ao carregar objetivos"
                        tvDescricaoObjetivo.text = "Código: ${response.code()}"
                        // LOG 4: Log do corpo do erro se a resposta não for bem-sucedida
                        Log.e("FichaTreinoDebug", "API Error Body: $errorBody")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // progressBarObjetivos.visibility = View.GONE // Esconde ProgressBar
                    Log.e("HomeAlunoActivity", "Erro inesperado ao buscar fichas de treino: ${e.message}", e)
                    tvTituloObjetivo.text = "Erro de conexão"
                    tvDescricaoObjetivo.text = "Verifique sua internet ou servidor."
                }
            }
        }
    }

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

    private fun gerarHtmlVLibras(texto: String): String {
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