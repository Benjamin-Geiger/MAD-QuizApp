package com.example.quiz_app_starter.datalayer

import com.example.quiz_app_starter.model.Question
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class QuestionRepository(
    private val questionDao: QuestionDao
) {
    fun getQuestions(): Flow<List<Question>> = questionDao.getRandomQuestions()

    suspend fun refreshQuestions() {
        try {
            val response = Api.retrofitService.getQuestions()
            val questions = response.map { it.asEntity() }
            questionDao.insertQuestions(questions)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteAll() = questionDao.clear()
    suspend fun insertQuestion(question: Question) = questionDao.insertQuestion(question)
}