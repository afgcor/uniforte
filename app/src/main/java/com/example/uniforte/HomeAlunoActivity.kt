// src/main/java/com/example/uniforte/HomeAlunoActivity.kt
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
import android.widget.LinearLayout // Import LinearLayout
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
import com.example.uniforte.data.network.RetrofitClient // Assuming RetrofitClient is correctly set up
import com.example.uniforte.data.network.AgendamentoRequest // Import the new AgendamentoRequest data class
import com.example.uniforte.data.network.Usuario

// Make sure DraggableWebView is defined or replace it with a standard WebView
// For example:
// import android.webkit.WebView
// class DraggableWebView : WebView { /* ... */ }

class HomeAlunoActivity : AppCompatActivity() {

    private lateinit var tvOlaUsuario: TextView
    private lateinit var webVLibras: DraggableWebView // Or WebView if DraggableWebView is not custom
    private lateinit var progressBarNome: ProgressBar
    private lateinit var editarPerfilLauncher: ActivityResultLauncher<Intent>

    private lateinit var tvTituloObjetivo: TextView
    private lateinit var tvDescricaoObjetivo: TextView

    // Declare TextViews for the first appointment card
    private lateinit var tvTituloAula1: TextView
    private lateinit var tvDescricaoAula1: TextView
    private lateinit var tvDataAula1: TextView
    private lateinit var tvHoraAula1: TextView
    private lateinit var llCardAula1: LinearLayout // To hide/show the card

    // Declare TextViews for the second appointment card
    private lateinit var tvTituloAula2: TextView
    private lateinit var tvDescricaoAula2: TextView
    private lateinit var tvDataAula2: TextView
    private lateinit var tvHoraAula2: TextView
    private lateinit var llCardAula2: LinearLayout // To hide/show the card

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_aluno)

        // Initialize views
        tvOlaUsuario = findViewById(R.id.tvOlaUsuario)
        webVLibras = findViewById(R.id.webVLibras)
        progressBarNome = findViewById(R.id.progressBarNome)
        progressBarNome.visibility = View.GONE

        // Initialize objective TextViews
        tvTituloObjetivo = findViewById(R.id.tvTituloObjetivo)
        tvDescricaoObjetivo = findViewById(R.id.tvDescricaoObjetivo)

        // Initialize appointment card 1 TextViews and LinearLayout
        tvTituloAula1 = findViewById(R.id.tvTituloAula1)
        tvDescricaoAula1 = findViewById(R.id.tvDescricaoAula1)
        tvDataAula1 = findViewById(R.id.tvDataAula1)
        tvHoraAula1 = findViewById(R.id.tvHoraAula1)
        llCardAula1 = findViewById(R.id.llCardAula1)
        llCardAula1.visibility = View.GONE // Start as GONE until data is loaded

        // Initialize appointment card 2 TextViews and LinearLayout
        tvTituloAula2 = findViewById(R.id.tvTituloAula2)
        tvDescricaoAula2 = findViewById(R.id.tvDescricaoAula2)
        tvDataAula2 = findViewById(R.id.tvDataAula2)
        tvHoraAula2 = findViewById(R.id.tvHoraAula2)
        llCardAula2 = findViewById(R.id.llCardAula2)
        llCardAula2.visibility = View.GONE // Start as GONE until data is loaded


        fetchUserObjectives()
        fetchUserAgendamentos() // Call to fetch appointments

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
                    // Current activity, no action needed
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

        // DraggableWebView setup (assuming you have this custom class)
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

        // Enable WebView VLibras movement
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
        fetchUserAgendamentos() // Refresh appointments when activity resumes
    }

    private fun fetchUserObjectives() {
        val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getString("USER_ID", null)

        if (userId.isNullOrEmpty()) {
            Log.e("HomeAlunoActivity", "User ID not found in SharedPreferences.")
            tvTituloObjetivo.text = "Erro"
            tvDescricaoObjetivo.text = "ID do usuário não encontrado."
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Ensure RetrofitClient.instance.api.buscarUsuarioPorId is called if your RetrofitClient requires '.api'
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

    // New function to fetch and display appointments
    private fun fetchUserAgendamentos() {
        val sharedPref = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getString("USER_ID", null)

        if (userId.isNullOrEmpty()) {
            Log.e("HomeAlunoActivity", "User ID not found for fetching appointments.")
            llCardAula1.visibility = View.GONE
            llCardAula2.visibility = View.GONE
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Call the getAgendamentosByAlunoId method from your ApiService
                // Ensure RetrofitClient.instance.api.getAgendamentosByAlunoId is called if your RetrofitClient requires '.api'
                val response = RetrofitClient.instance.getAgendamentosByAlunoId(userId)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        val agendamentos = response.body()!!
                        if (agendamentos.isNotEmpty()) {
                            // Display the first appointment
                            val agendamento1 = agendamentos[0]
                            tvTituloAula1.text = agendamento1.nome // Use 'nome' for the title
                            tvDescricaoAula1.text = agendamento1.descricao // Use 'descricao' for the description
                            tvDataAula1.text = "Data: ${agendamento1.data}" // Use 'data' for the date
                            tvHoraAula1.text = "Hora: ${agendamento1.horario}" // Use 'horario' for the time
                            llCardAula1.visibility = View.VISIBLE // Uncommented

                            // Display the second appointment if available
                            if (agendamentos.size > 1) {
                                val agendamento2 = agendamentos[1]
                                tvTituloAula2.text = agendamento2.nome
                                tvDescricaoAula2.text = agendamento2.descricao
                                tvDataAula2.text = "Data: ${agendamento2.data}"
                                tvHoraAula2.text = "Hora: ${agendamento2.horario}"
                                llCardAula2.visibility = View.VISIBLE // Uncommented
                            } else {
                                llCardAula2.visibility = View.GONE // Hide the second card if only one appointment
                            }
                        } else {
                            // No appointments found
                            llCardAula1.visibility = View.GONE // Uncommented
                            llCardAula2.visibility = View.GONE // Uncommented
                            // Optionally, display a message like "Nenhum agendamento para a semana."
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        Log.e("HomeAlunoActivity", "Error fetching appointments: ${response.code()} - $errorBody")
                        llCardAula1.visibility = View.GONE // Uncommented
                        llCardAula2.visibility = View.GONE // Uncommented
                        // Optionally, display an error message
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeAlunoActivity", "Exception fetching appointments: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    llCardAula1.visibility = View.GONE
                    llCardAula2.visibility = View.GONE
                    // Optionally, display a connection error message
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
                body { margin: 0; padding: 0; background: transparent; overflow: hidden;}
                /* Esconde o parágrafo, usado apenas para passar o texto */
                p { display: none;}
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