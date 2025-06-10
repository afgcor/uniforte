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
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.uniforte.data.network.RetrofitClient
import com.example.uniforte.data.network.RetrofitInstance
import com.example.uniforte.data.network.Usuario



class HomeAlunoActivity : AppCompatActivity() {

    private lateinit var tvOlaUsuario: TextView
    private lateinit var webVLibras: DraggableWebView
    private lateinit var progressBarNome: ProgressBar
    private lateinit var editarPerfilLauncher: ActivityResultLauncher<Intent>

    private lateinit var tvTituloObjetivo: TextView
    private lateinit var tvDescricaoObjetivo: TextView
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

        fetchUserObjectives()
        // progressBarObjetivos = findViewById(R.id.progressBarObjetivos) // Se existir
        // progressBarObjetivos.visibility = View.GONE // Começa invisível

        editarPerfilLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            atualizarNomeUsuario(true)

        }



        atualizarNomeUsuario(false)

        val navInferiorAlunoFragment = NavInferiorAlunoFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container_nav_inferior, navInferiorAlunoFragment)
            .commit()

        navInferiorAlunoFragment.onNavItemSelected = { itemId ->
            when (itemId) {
                R.id.navHome -> {

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
        atualizarNomeUsuario(false)
    }

    private fun fetchUserObjectives() {
        // Obter o ID do usuário autenticado (assumindo que está salvo em SharedPreferences)
        val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getString("USER_ID", null) // Assumindo que o ID do usuário é salvo como "USER_ID"

        if (userId.isNullOrEmpty()) {
            Log.e("HomeAlunoActivity", "User ID not found in SharedPreferences.")
            tvTituloObjetivo.text = "Erro"
            tvDescricaoObjetivo.text = "ID do usuário não encontrado."
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.instance.buscarUsuarioPorId(userId)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val user = response.body()!!
                        tvTituloObjetivo.text = user.titulo_objetivo ?: "Bem vindo a Uniforte!"
                        tvDescricaoObjetivo.text = user.descricao_objetivo ?: "Voce ainda não possui um objetivo... :("
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("HomeAlunoActivity", "Erro na resposta da API: ${response.code()} - $errorBody")
                        tvTituloObjetivo.text = "Erro"
                        tvDescricaoObjetivo.text = "Falha ao carregar objetivos."
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeAlunoActivity", "Erro ao buscar objetivos do usuário: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    tvTituloObjetivo.text = "Erro"
                    tvDescricaoObjetivo.text = "Erro de conexão ou dados."
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