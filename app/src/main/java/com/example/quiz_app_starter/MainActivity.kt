package com.example.quiz_app_starter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.quiz_app_starter.navigation.Navigation
import com.example.quiz_app_starter.navigation.Screen
import com.example.quiz_app_starter.ui.theme.QuizappstarterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizappstarterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(bestScore = 3, modifier = Modifier.padding(innerPadding))

//                    MainMenuScreen(
//                        bestScore = 3,
//                        modifier = Modifier.padding(innerPadding)
//                    )
                }
            }
        }
    }
}

@Composable
fun MainMenuScreen(
    bestScore: Int = 0,
    modifier: Modifier,
    navController: NavHostController
) {

    Column(
        modifier = modifier
            .padding(16.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.bleh),
            contentDescription = "App Logo",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "QuizApp",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = buildAnnotatedString {
                append("Test your \n")
                withStyle(SpanStyle(brush = Brush.horizontalGradient(
                    0.3f to Color.Red,
                    0.6f to Color.Red,
                    0.6f to Color.Yellow,
                    0.8f to Color.Yellow,
                    0.8f to Color.Green,
                    1.0f to Color.Green
                ))){
                    append("knowledge")
                }
            },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(16.dp)
                )
                .background(MaterialTheme.colorScheme.primary.copy(0.5f))
                .padding(16.dp)

        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Best of all time",
                    color = MaterialTheme.colorScheme.onBackground.copy(0.7f)
                )
                Text(text = bestScore.toString(), fontSize = 64.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {navController.navigate(Screen.QuestionScreen.route)},
            modifier = Modifier.fillMaxWidth(0.6f),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = "Play!", style = MaterialTheme.typography.labelLarge)
        }
    }
}

//@Preview(showBackground = true, name = "MainMenuPreview")
//@Composable
//fun MainMenuScreenPreview() {
//    QuizappstarterTheme {
//        MainMenuScreen(67, Modifier,)
//    }
//}
