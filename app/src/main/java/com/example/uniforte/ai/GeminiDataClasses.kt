package com.example.uniforte.ai.gemini

import com.google.gson.annotations.SerializedName

data class GeminiRequest(
    @SerializedName("contents") val contents: List<Content>,
    @SerializedName("generationConfig") val generationConfig: GenerationConfig? = null,
    @SerializedName("safetySettings") val safetySettings: List<SafetySetting>? = null
)

data class Content(
    @SerializedName("parts") val parts: List<Part>,
    @SerializedName("role") val role: String
)

data class Part(
    @SerializedName("text") val text: String
)

data class GenerationConfig(
    @SerializedName("temperature") val temperature: Float? = null,
    @SerializedName("maxOutputTokens") val maxOutputTokens: Int? = null
)

data class SafetySetting(
    @SerializedName("category") val category: String,
    @SerializedName("threshold") val threshold: String
)

data class GeminiResponse(
    @SerializedName("candidates") val candidates: List<Candidate>?,
    @SerializedName("promptFeedback") val promptFeedback: PromptFeedback?
)

data class Candidate(
    @SerializedName("content") val content: Content?,
    @SerializedName("finishReason") val finishReason: String?,
    @SerializedName("index") val index: Int?,
    @SerializedName("safetyRatings") val safetyRatings: List<SafetyRating>?
)

data class SafetyRating(
    @SerializedName("category") val category: String,
    @SerializedName("probability") val probability: String
)

data class PromptFeedback(
    @SerializedName("safetyRatings") val safetyRatings: List<SafetyRating>?
)

data class GeminiErrorResponse(
    @SerializedName("error") val error: GeminiErrorDetail?
)

data class GeminiErrorDetail(
    @SerializedName("code") val code: Int?,
    @SerializedName("message") val message: String?,
    @SerializedName("status") val status: String?
)

