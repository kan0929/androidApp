package com.example.w05

import android.content.pm.LauncherApps
import android.graphics.Paint
import android.os.Bundle
import android.widget.Button
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.Font
import com.example.w05.ui.theme.SparrowprojectTheme
import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

class MainActivity: ComponentActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SparrowprojectTheme {
                val count = remember { mutableStateOf(0) }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    CounterApp(count)
                    Spacer(modifier = Modifier.height(32.dp))
                    StopWatchApp()
                }
            }
        }
    }
}

@Composable
fun CounterApp(count: MutableState<Int>) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Count: ${count.value}",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold)
        Row {
            Button(onClick = {count.value++}) {
                Text("Increase")
            }
            Button(onClick = {count.value = 0}) {
                Text("Reset")
            }
        }
    }
}

@Composable
fun StopWatchApp() {
    var timeInMillis by remember { mutableStateOf(1234L) }
    var isRunning by remember { mutableStateOf(false) }
    LaunchedEffect(isRunning) {
        while (isRunning) {
                delay(10L)
                timeInMillis += 10L
            }

        }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(text = formatTime(timeInMillis),
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold)
        Row{
            Button(onClick = {isRunning = true }) { Text("Start")}
            Button(onClick = {isRunning = false }) { Text("Stop")}
            Button(onClick = {
                isRunning = false
                timeInMillis = 0L
            }) { Text("Reset")}
        }
    }
}

@Composable
fun StopwatchScreen(
    timeInMillis: Long,
    onStartClick: () -> Unit,
    onStopClick: () -> Unit,
    onResetClick: () -> Unit
){
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Text(
            text = formatTime(timeInMillis),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier =
                Modifier.align(Alignment.CenterHorizontally))
        {
                    Button(onClick = onStartClick) {Text("Start")}
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = onStopClick) {Text("Stop")}
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = onResetClick) {Text("Reset")}
                }

    }
}

@Composable
fun ColorToggleButtonApp(){
    var currentColor by remember { mutableStateOf(Color.Red) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(currentColor)
            .clickable {
                currentColor = if (currentColor == Color.Red) Color.Blue else Color.Red
            }
            .padding(30.dp),
        contentAlignment = Alignment.Center
        ){
            Text(
                text = "Click Me",
                color = Color.White,
                fontSize = 30.sp
            )
        }
    }
}

private fun formatTime(timeInMillis: Long): String{
    val minutes = (timeInMillis / 1000) / 60
    val seconds = (timeInMillis / 1000) % 60
    val millis = (timeInMillis % 1000) / 10
    return String.format("%02d:%02d:%02d", minutes,seconds,millis)
}

@Preview(showBackground = true, widthDp = 300, heightDp = 300)
@Composable
fun ColorToggleButtonAppPreview() {
    ColorToggleButtonApp()
}

@Preview(showBackground = true)
@Composable
fun CounterAppPreview() {
    CounterApp(count = remember { mutableStateOf(0) })
}

@Preview(showBackground = true)
@Composable
fun StopWatchPreview(){
    StopWatchApp()
}