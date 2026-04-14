package com.example.quiz_app_starter.datalayer

import com.example.quiz_app_starter.model.Question
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://the-trivia-api.com/v2/"
private val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build()

interface ApiService {
    @GET("questions")
    suspend fun getQuestions(): List<QuestionDto>
}

data class QuestionDto(
    val category: String,
    val id: String,
    val correctAnswer: String,
    val incorrectAnswers: List<String>,
    val question: QuestionText,
    val tags: List<String>,
    val type: String,
    val difficulty: String,
    val regions: List<String>?,
    val isNiche: Boolean
)

data class QuestionText(
    val text: String
)

fun QuestionDto.asEntity(): Question {
    return Question(
        category = category,
        id = id,
        correctAnswer = correctAnswer,
        answers = (incorrectAnswers + correctAnswer).shuffled(),
        tags = tags,
        question = question.text,
        type = type,
        difficulty = difficulty,
        regions = regions,
        isNiche = isNiche
    )
}

object Api {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}