package com.example.uniforte

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var etLogin: TextInputEditText
    private lateinit var etSenha: TextInputEditText
    private lateinit var btnEntrar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        etLogin = findViewById(R.id.etLogin)
        etSenha = findViewById(R.id.etSenha)
        btnEntrar = findViewById(R.id.btnEntrar)

        btnEntrar.setOnClickListener {
            val email = etLogin.text.toString().trim()
            val senha = etSenha.text.toString().trim()

            if (!email.contains("@")) {
                etLogin.error = "E-mail inválido"
                return@setOnClickListener
            }

            val intent = Intent(this, HomeAlunoActivity::class.java)
            startActivity(intent)
        }

        val tvCadastrar = findViewById<TextView>(R.id.tvCadastrar)
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

        val imgLogo = findViewById<ImageView>(R.id.imgLogo)
        imgLogo.setOnClickListener {
            val intent = Intent(this, HomeProfessorActivity::class.java)
            startActivity(intent)
        }

        val webVLibras = findViewById<DraggableWebView>(R.id.webVLibras)
        val webSettings = webVLibras.settings
        webSettings.javaScriptEnabled = true
        webVLibras.setBackgroundColor(0x00000000)

        val rootView = findViewById<View>(android.R.id.content)
        val textoCompleto = coletarTextosDaTela(rootView)
        atualizarTextoVLibras(textoCompleto)

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
                        if (!isDragging) {
                            view.performClick()
                            return false
                        }
                        return true
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

    private fun gerarHtmlVLibras(texto: String): String {
        return """
            <!DOCTYPE html>
            <html lang="pt-BR">
            <head>
                <meta charset="utf-8" />
                <style>
                    body {
                        margin: 0;
                        padding: 0;
                        background: transparent;
                    }
                    p {
                        font-size: 16px;
                        color: transparent;
                        position: absolute;
                        pointer-events: auto;
                        user-select: text;
                        width: 100%;
                        height: auto;
                    }
                </style>
            </head>
            <body>
                <p>$texto</p>
                <div vw class="enabled">
                    <div vw-access-button class="active"></div>
                    <div vw-plugin-wrapper>
                        <div class="vw-plugin-top-wrapper"></div>
                    </div>
                </div>
                <script src="https://vlibras.gov.br/app/vlibras-plugin.js"></script>
                <script>
                    new window.VLibras.Widget('https://vlibras.gov.br/app');
                </script>
            </body>
            </html>
        """.trimIndent()
    }

    private fun atualizarTextoVLibras(texto: String) {
        val webVLibras = findViewById<DraggableWebView>(R.id.webVLibras)
        val html = gerarHtmlVLibras(texto)
        webVLibras.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
    }
}
