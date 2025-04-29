package com.example.uniforte

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FeedbackActivity : AppCompatActivity() {

    private lateinit var scrollMessages: ScrollView
    private lateinit var messageContainer: LinearLayout
    private lateinit var etMessage: EditText
    private lateinit var btnSend: ImageButton
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        // Mapear referências de views do layout para manipulação em código
        scrollMessages   = findViewById(R.id.scrollMessages)
        messageContainer = findViewById(R.id.messageContainer)
        etMessage        = findViewById(R.id.etMessage)
        btnSend          = findViewById(R.id.btnSend)
        backButton       = findViewById(R.id.back_button)

        // Configurar navegação inferior: ao tocar em “Ficha de Treino”, abre a activity correspondente
        findViewById<TextView>(R.id.navFicha).setOnClickListener {
            startActivity(Intent(this, FichaTreinoActivity::class.java))
        }
        // Configurar navegação inferior: ao tocar em “Perfil”, abre a activity de perfil
        findViewById<TextView>(R.id.navPerfil).setOnClickListener {
            startActivity(Intent(this, PerfilActivity::class.java))
        }

        // Voltar para a tela inicial do aluno, finalizando esta activity para não ficar no stack
        backButton.setOnClickListener {
            startActivity(Intent(this, HomeAlunoActivity::class.java))
            finish()
        }

        // Envio de mensagem ao tocar no botão de envio
        btnSend.setOnClickListener {
            sendCurrentMessage()
        }

        // Também permitir envio ao pressionar a tecla ENTER (física ou virtual)
        etMessage.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                sendCurrentMessage()
                true
            } else {
                false
            }
        }
    }

    /**
     * Lê o texto do EditText, adiciona ao container de mensagens e mantém o foco
     * no campo de entrada para permitir envio contínuo sem fechar o teclado.
     */
    private fun sendCurrentMessage() {
        val text = etMessage.text.toString().trim()
        if (text.isNotEmpty()) {
            addMessage(text)
            etMessage.text.clear()

            // Garantir que o campo permaneça em foco e o teclado continue visível
            etMessage.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etMessage, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    /**
     * Cria dinamicamente uma TextView formatada como bolha de mensagem,
     * define margem e largura mínima, adiciona ao container e rola até o fim.
     */
    private fun addMessage(message: String) {
        val bubble = TextView(this).apply {
            text = message
            setTextColor(Color.BLACK)
            setBackgroundResource(R.drawable.bg_bubble_sent)
        }

        // Definir largura mínima de bolha (200dp convertido para pixels)
        val minWidthPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            200f,
            resources.displayMetrics
        ).toInt()
        bubble.minWidth = minWidthPx

        // Configurar LayoutParams para alinhamento à direita com margens padrão
        bubble.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = Gravity.END
            setMargins(16, 8, 16, 8)
        }

        messageContainer.addView(bubble)
        // Agendar scroll para garantir que a nova mensagem fique visível
        scrollMessages.post {
            scrollMessages.fullScroll(View.FOCUS_DOWN)
        }
    }
}
