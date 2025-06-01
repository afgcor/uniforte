package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class HomeAlunoActivity : AppCompatActivity() {


    private lateinit var tvOlaUsuario: TextView
    private lateinit var webVLibras: DraggableWebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_aluno)


        tvOlaUsuario = findViewById(R.id.tvOlaUsuario)
        val nomeUsuario = intent.getStringExtra("USER_NAME")
        if (!nomeUsuario.isNullOrEmpty()) {
            tvOlaUsuario.text = "Olá, $nomeUsuario!"
        } else {
            tvOlaUsuario.text = "Olá!"
        }

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
                    startActivity(Intent(this, PerfilActivity::class.java))
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

        webVLibras = findViewById<DraggableWebView>(R.id.webVLibras) // Inicializa webVLibras
        val webSettings = webVLibras.settings
        webSettings.javaScriptEnabled = true
        webVLibras.setBackgroundColor(0x00000000) // Transparente

        val btnAtivarVLibras = findViewById<MaterialButton>(R.id.btnAtivarVLibras)

        btnAtivarVLibras.setOnClickListener {
            if (webVLibras.visibility == View.VISIBLE) {
                webVLibras.visibility = View.GONE
            } else {
                webVLibras.visibility = View.VISIBLE
            }
        }

        // Coletar todos os textos visíveis da tela
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

    // Gera o HTML invisível acessível ao VLibras
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
            </script>
        </body>
        </html>
        """.trimIndent()
    }

    // Atualiza o WebView com o novo texto para VLibras
    private fun atualizarTextoVLibras(texto: String) {
        // A variável webVLibras já deve estar inicializada no onCreate
        val html = gerarHtmlVLibras(texto)
        webVLibras.loadDataWithBaseURL("https://vlibras.gov.br/app", html, "text/html", "UTF-8", null)
    }
}

