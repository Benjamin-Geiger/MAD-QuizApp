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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.quiz_app_starter.R
import com.example.quiz_app_starter.model.Question
import com.example.quiz_app_starter.model.getDummyQuestions
import com.example.quiz_app_starter.navigation.Screen
import com.example.quiz_app_starter.ui.theme.QuizappstarterTheme
import kotlinx.coroutines.delay

@Composable
fun progressBar(countdown: Float, timerDurationSeconds: Float){
    LinearProgressIndicator(modifier =
        Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp)
            .size(0.dp, 140.dp),
        progress = {countdown / timerDurationSeconds}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    questions: List<Question> = getDummyQuestions(),
    navController: NavHostController
){

    var currentQuestionIndex: Int = 0

    var selected: String by remember {
        mutableStateOf("")
    }
    val timerDurationSeconds: Float = 3000f

    var countdown by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(timerDurationSeconds) {
        while (countdown < timerDurationSeconds) {
            delay(10)
            countdown++
        }
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
                onClick = {if (currentQuestionIndex < questions.size - 1) {
                    currentQuestionIndex++
                } else {
                    navController.navigate(Screen.MainScreen.route)
                }
                }) {
                Text("Submit")
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier.padding(innerPadding)) {
            progressBar(countdown, timerDurationSeconds)

            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp, 0.dp, 16.dp, 12.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = questions[currentQuestionIndex].question,
                    modifier = Modifier
                        .padding(10.dp),
                )
            }

            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            )
            { items(questions[currentQuestionIndex].answers) { answer ->
                AnswerCard(
                    answer,
                    isSelected = selected == answer,
                    onSelect = {
                       selected = answer
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
