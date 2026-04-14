package com.example.quiz_app_starter.model

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewModelScope
import com.example.quiz_app_starter.datalayer.QuestionRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuestionScreenState(
    val currentQuestionIndex: Int = 0,
    val selectedAnswer: String = "",
    val questionList: List<Question> = emptyList(),
    val timerProgress: Float = 0.0f,
    val resultDialog: String = "",
    val dialogMessage: String = "",
    val pointsAchieved: Int = 0,
    val showDialog: Boolean = false
)

class QuestionViewModel(private val repository: QuestionRepository) : ViewModel(), DefaultLifecycleObserver {
    private val _uiState = MutableStateFlow(
        QuestionScreenState()
    )

    private fun fetchQuestions() {
        viewModelScope.launch {
            repository.refreshQuestions() // Fetch from API first
            repository.getQuestions().collect { questions ->
                if (questions.isNotEmpty()) {
                    _uiState.update { it.copy(
                        questionList = questions,
                        currentQuestionIndex = 0
                    ) }
                }
            }
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        pauseTimer()
    }

    override fun onStart(owner: LifecycleOwner) {
        resumeTimer()
    }

    val uiState: StateFlow<QuestionScreenState> = _uiState.asStateFlow()
    private var timerJob: Job? = null
    private val timerDurationSeconds: Float = 3000f

    private var isTimerRunning: Boolean = true

    init {
        fetchQuestions()
        startTimer()
    }

    fun onAnswerSelected(answer: String) {
        _uiState.update { it.copy(selectedAnswer = answer) }
    }

    fun submitAnswer() {
        val currentState = _uiState.value
        if (currentState.selectedAnswer.isEmpty()) return
        
        timerJob?.cancel()
        val currentQuestion = currentState.questionList[currentState.currentQuestionIndex]
        val isCorrect = currentState.selectedAnswer == currentQuestion.correctAnswer
        
        _uiState.update { 
            it.copy(
                pointsAchieved = if (isCorrect) it.pointsAchieved + 1 else it.pointsAchieved,
                resultDialog = if (isCorrect) "Corretto!" else "Falso!",
                showDialog = true
            )
        }
    }

    fun onNextClicked(onQuizFinished: (Int) -> Unit) {
        val currentState = _uiState.value
        if (currentState.currentQuestionIndex < currentState.questionList.size - 1) {
            _uiState.update {
                it.copy(
                    currentQuestionIndex = it.currentQuestionIndex + 1,
                    selectedAnswer = "",
                    showDialog = false,
                    timerProgress = 0f
                )
            }
            startTimer()
        } else {
            onQuizFinished(currentState.pointsAchieved)
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var countdown = 0f
            while (countdown < timerDurationSeconds) {
                delay(10)
                if(isTimerRunning) {
                    countdown++
                }
                _uiState.update { it.copy(timerProgress = countdown / timerDurationSeconds) }
            }
            onTimerFinished()
        }
    }

    private fun pauseTimer() {
        isTimerRunning = false
    }

    private fun resumeTimer() {
        isTimerRunning = true
    }

    private fun onTimerFinished() {
        _uiState.update {
            it.copy(
                resultDialog = "TEMPO SCADUTO!",
                showDialog = true
            )
        }
    }
}
