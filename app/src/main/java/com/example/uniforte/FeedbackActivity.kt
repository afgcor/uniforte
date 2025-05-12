package com.example.uniforte

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.uniforte.ai.AIService
import kotlinx.coroutines.launch

class FeedbackActivity : AppCompatActivity() {

    private lateinit var scrollMessages: ScrollView
    private lateinit var messageContainer: LinearLayout
    private lateinit var etMessage: EditText
    private lateinit var btnSend: ImageButton
    private lateinit var backButton: ImageButton

    private val TAG = "FeedbackActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        scrollMessages   = findViewById(R.id.scrollMessages)
        messageContainer = findViewById(R.id.messageContainer)
        etMessage        = findViewById(R.id.etMessage)
        btnSend          = findViewById(R.id.btnSend)
        backButton       = findViewById(R.id.back_button)


        backButton.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()
        }

        btnSend.setOnClickListener {
            sendCurrentMessageAndGetResponse()
        }

        etMessage.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                sendCurrentMessageAndGetResponse()
                true
            } else {
                false
            }
        }
        addMessageFromAI("Olá! Eu sou o FitBot, seu assistente virtual da UniFort. Como posso te ajudar hoje? ✨💪")
    }

    private fun sendCurrentMessageAndGetResponse() {
        val userText = etMessage.text.toString().trim()
        if (userText.isNotEmpty()) {
            addMessageFromUser(userText)
            etMessage.text.clear()

            // Ocultar teclado após enviar
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(etMessage.windowToken, 0)

            // Mostrar uma mensagem de "FitBot está a escrever..."
            val thinkingMessage = "FitBot está a pensar... 🤔"
            addMessageFromAI(thinkingMessage, isThinking = true)

            lifecycleScope.launch {
                Log.d(TAG, "Enviando para IA: $userText")
                val result = AIService.getChatResponse(userText)

                removeLastMessageIfThinking()

                result.fold(
                    onSuccess = {
                            aiResponse ->
                        Log.d(TAG, "Resposta da IA: $aiResponse")
                        addMessageFromAI(aiResponse)
                    },
                    onFailure = {
                            error ->
                        Log.e(TAG, "Erro da IA: ${error.message}", error)
                        addMessageFromAI("Desculpe, não consegui processar sua mensagem agora. Tente novamente mais tarde. 😟")
                        Toast.makeText(this@FeedbackActivity, "Erro: ${error.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }
    }

    private fun addMessageFromUser(message: String) {
        addMessage(message, true)
    }

    private fun addMessageFromAI(message: String, isThinking: Boolean = false) {
        addMessage(message, false, isThinking)
    }

    private fun removeLastMessageIfThinking() {
        val lastView = messageContainer.getChildAt(messageContainer.childCount - 1)
        if (lastView != null && lastView.tag == "thinking_message") {
            messageContainer.removeView(lastView)
        }
    }

    private fun addMessage(message: String, isUserMessage: Boolean, isThinking: Boolean = false) {
        val bubble = TextView(this).apply {
            text = message
            setTextColor(Color.BLACK)
            setBackgroundResource(if (isUserMessage) R.drawable.bg_bubble_sent else R.drawable.bg_bubble_received)
            if (isThinking) {
                tag = "thinking_message"
            }
        }

        val minWidthPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            150f,
            resources.displayMetrics
        ).toInt()
        bubble.minWidth = minWidthPx

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            gravity = if (isUserMessage) Gravity.END else Gravity.START
            // Ajustar margens para melhor visualização
            val horizontalMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics).toInt()
            val verticalMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4f, resources.displayMetrics).toInt()
            setMargins(horizontalMargin, verticalMargin, horizontalMargin, verticalMargin)
        }
        bubble.layoutParams = params

        val paddingPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics).toInt()
        bubble.setPadding(paddingPx, paddingPx, paddingPx, paddingPx)


        messageContainer.addView(bubble)
        scrollMessages.post {
            scrollMessages.fullScroll(View.FOCUS_DOWN)
        }
    }
}

