package com.example.uniforte.ai

import com.example.uniforte.BuildConfig
import com.example.uniforte.ai.gemini.Content
import com.example.uniforte.ai.gemini.GeminiApi
import com.example.uniforte.ai.gemini.GeminiRequest
import com.example.uniforte.ai.gemini.GeminiResponse
import com.example.uniforte.ai.gemini.GeminiErrorResponse
import com.example.uniforte.ai.gemini.Part
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object AIService {

    private const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/"

    private const val GEMINI_API_KEY = "AIzaSyB8mM9KCwa1yB_eRNi5oDOHcwj8CvLpw84"


    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(GEMINI_BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val geminiApi = retrofit.create(GeminiApi::class.java)

    private val conversationHistory = mutableListOf<Content>()

    private const val ACADEMIA_CHATBOT_SYSTEM_PROMPT = """
    **IMPORTANTE: CADA MENSAGEM DE RESPOSTA DEVE TER NO MÁXIMO 12 LINHAS. SEJA CONCISO E DIRETO AO PONTO.**
    Você é o FitBot, um Chatbot da Academia UniForte, amigável e motivador. Seu nome é FitBot. Seu objetivo é ajudar os alunos da academia com informações sobre treinos, uso de equipamentos, dicas de nutrição para performance, horários de aulas, e motivação.
    Forneça respostas claras, objetivas e personalizadas sempre que possível. Mantenha as respostas focadas no essencial para respeitar o limite de linhas. Incentive os alunos a manterem a consistência e a celebrarem os seus progressos. Use uma linguagem positiva e encorajadora.
    Se não souber uma resposta específica sobre a academia (ex: horários exatos de uma aula específica se não fornecido), admita e sugira onde o aluno pode encontrar essa informação (ex: receção, website da academia).
    **RELEMBRE-SE: AS RESPOSTAS NÃO PODEM ULTRAPASSAR 12 LINHAS.** """

    suspend fun getChatResponse(userMessage: String): Result<String> {
        return try {
            val currentMessagesToSend = mutableListOf<Content>()

            if (conversationHistory.isEmpty()) {

                currentMessagesToSend.add(Content(parts = listOf(Part(text = ACADEMIA_CHATBOT_SYSTEM_PROMPT)), role = "user"))

                currentMessagesToSend.add(Content(parts = listOf(Part(text = userMessage)), role = "user"))
            } else {
                currentMessagesToSend.addAll(conversationHistory)
                currentMessagesToSend.add(Content(parts = listOf(Part(text = userMessage)), role = "user"))
            }


            val finalMessagesToSend = if (currentMessagesToSend.size > 10) {

                currentMessagesToSend.takeLast(10)
            } else {
                currentMessagesToSend
            }

            val request = GeminiRequest(
                contents = finalMessagesToSend
            )

            val response = geminiApi.generateContent(GEMINI_API_KEY, request)

            if (response.isSuccessful) {
                val geminiResponse = response.body()
                val botMessageText = geminiResponse?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text

                if (botMessageText != null) {

                    if (conversationHistory.isEmpty() && finalMessagesToSend.size >=2 && finalMessagesToSend[0].parts.first().text == ACADEMIA_CHATBOT_SYSTEM_PROMPT) {

                        conversationHistory.add(finalMessagesToSend[0])
                        conversationHistory.add(finalMessagesToSend[1])
                    } else if (conversationHistory.isNotEmpty()) {

                        conversationHistory.add(Content(parts = listOf(Part(text = userMessage)), role = "user"))
                    } else {

                        conversationHistory.add(Content(parts = listOf(Part(text = ACADEMIA_CHATBOT_SYSTEM_PROMPT)), role = "user"))
                        conversationHistory.add(Content(parts = listOf(Part(text = userMessage)), role = "user"))
                    }

                    conversationHistory.add(Content(parts = listOf(Part(text = botMessageText)), role = "model"))
                    Result.success(botMessageText.trim())
                } else {
                    Result.failure(Exception("Resposta da API Gemini vazia ou em formato inesperado."))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Erro desconhecido da API Gemini"
                val apiError = try {
                    val gson = Gson()
                    val errorResponse = gson.fromJson(errorBody, GeminiErrorResponse::class.java)
                    errorResponse?.error?.message ?: errorBody
                } catch (e: Exception) {
                    errorBody
                }
                Result.failure(Exception("Erro na API Gemini (${response.code()}): $apiError"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Falha ao comunicar com a API Gemini: ${e.message}", e))
        }
    }
}

