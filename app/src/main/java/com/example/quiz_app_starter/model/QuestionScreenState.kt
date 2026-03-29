package com.example.quiz_app_starter.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class QuestionScreenState(
    val currentQuestionIndex: Int = 0,
    val selectedAnswer: String = "",
    val questionList: List<Question> = emptyList(),
    val timerProgress: Float = 0.0f,
    val resultDialog: String = "",
    val dialogMessage: String = "",
    val pointsAchieved: Int = 0
)

class QuestionViewModel(private val questions: List<Question>) : ViewModel() {
    private val _uiState = MutableStateFlow(
        QuestionScreenState(questionList = questions)
    )

    val uiState: StateFlow<QuestionScreenState> = _uiState.asStateFlow()
}
