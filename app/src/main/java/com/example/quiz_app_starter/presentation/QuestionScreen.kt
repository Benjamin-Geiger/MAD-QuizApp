package com.example.quiz_app_starter.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.quiz_app_starter.R
import com.example.quiz_app_starter.model.QuestionViewModel
import com.example.quiz_app_starter.navigation.Screen

@Composable
fun progressBar(progress: Float){
    LinearProgressIndicator(modifier =
        Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp)
            .size(0.0.dp, 140.0.dp),
        progress = { progress }
    )
}

@Composable
fun AlertDialogExample(
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    AlertDialog(
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = { },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Next")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    navController: NavHostController,
    viewModel: QuestionViewModel = viewModel()
){
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by viewModel.uiState.collectAsState()
    val questions = uiState.questionList

    DisposableEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(viewModel)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(viewModel)
        }
    }

    if (questions.isEmpty()) return

    val currentQuestion = questions[uiState.currentQuestionIndex]

    if (uiState.showDialog) {
        AlertDialogExample(
            onConfirmation = {
                viewModel.onNextClicked { finalPoints ->
                    navController.navigate(Screen.EndScreen.route + "/$finalPoints")
                }
            },
            dialogTitle = "Quiz Result",
            dialogText = uiState.resultDialog
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color.Gray
                ),
                title = {Text("QuizApp")},
                actions = {
                    IconButton(onClick = { navController.popBackStack() }){
                        Image(
                            painter = painterResource(id = R.drawable.leave),
                            contentDescription = "Leave",
                            modifier = Modifier.size(90.dp)
                        )
                    }
                }
            )
        },
        bottomBar = {
            Button(
                modifier = Modifier.fillMaxWidth().padding(16.dp, 0.dp).navigationBarsPadding(),
                enabled = uiState.selectedAnswer.isNotEmpty(),
                onClick = {
                    viewModel.submitAnswer()
                }) {
                Text("Submit")
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier.padding(innerPadding)) {
            progressBar(uiState.timerProgress)

            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp, 0.dp, 16.dp, 12.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = currentQuestion.question,
                    modifier = Modifier
                        .padding(10.dp),
                )
            }

            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            )
            { items(currentQuestion.answers) { answer ->
                AnswerCard(
                    answer,
                    isSelected = uiState.selectedAnswer == answer,
                    onSelect = {
                       viewModel.onAnswerSelected(answer)
                    }
                )
            } }
        }
    }
}

@Composable
fun AnswerCard(
    answer: String,
    isSelected: Boolean,
    onSelect:() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.primary
        )
    ){
        Row(
            Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(answer)
            RadioButton(
                colors = RadioButtonColors(
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.secondary,
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.primary
                ),
                selected = isSelected,
                onClick = onSelect

            )
        }
    }
}
