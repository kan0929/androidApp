package com.example.www

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // 상태 호이스팅: 상태와 로직을 최상위 수준으로 올립니다.
            var count by remember { mutableStateOf(0) }
            var timeInMillis by remember { mutableStateOf(0L) }
            var isRunning by remember { mutableStateOf(false) }

            // 스톱워치 로직
            LaunchedEffect(isRunning) {
                while (isRunning) {
                    delay(10L)
                    timeInMillis += 10L
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // 상태와 이벤트 핸들러를 파라미터로 전달합니다.
                CounterApp(
                    count = count,
                    onIncrease = { count++ },
                    onReset = { count = 0 }
                )
                Spacer(modifier = Modifier.height(32.dp))
                StopWatchApp(
                    timeInMillis = timeInMillis,
                    isRunning = isRunning,
                    onStart = { isRunning = true },
                    onStop = { isRunning = false },
                    onReset = {
                        isRunning = false
                        timeInMillis = 0L
                    }
                )
            }
        }
    }
}

/**
 * 카운터 UI를 표시하는 Composable. 상태를 직접 소유하지 않습니다.
 * @param count 표시할 현재 숫자.
 * @param onIncrease 숫자를 증가시키는 이벤트 핸들러.
 * @param onReset 숫자를 초기화하는 이벤트 핸들러.
 */
@Composable
fun CounterApp(
    count: Int,
    onIncrease: () -> Unit,
    onReset: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Count: $count",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onIncrease) {
                Text("Increase")
            }
            Button(onClick = onReset) {
                Text("Reset")
            }
        }
    }
}

/**
 * 스톱워치 UI를 표시하는 Composable. 상태를 직접 소유하지 않습니다.
 * @param timeInMillis 표시할 시간 (밀리초).
 * @param isRunning 스톱워치 실행 여부.
 * @param onStart 시작 이벤트 핸들러.
 * @param onStop 중지 이벤트 핸들러.
 * @param onReset 초기화 이벤트 핸들러.
 */
@Composable
fun StopWatchApp(
    timeInMillis: Long,
    isRunning: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = formatTime(timeInMillis),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = onStart, enabled = !isRunning) { Text("Start") }
            Button(onClick = onStop, enabled = isRunning) { Text("Stop") }
            Button(onClick = onReset) { Text("Reset") }
        }
    }
}

/**
 * 밀리초 시간을 "분:초:밀리초" 형식의 문자열로 변환합니다.
 */
@Composable()
private fun formatTime(timeInMillis: Long): String {
    val minutes = (timeInMillis / 1000) / 60
    val seconds = (timeInMillis / 1000) % 60
    val millis = (timeInMillis % 1000) / 10
    return String.format("%02d:%02d:%02d", minutes, seconds, millis)
}


// --- Previews ---

@Composable
fun StopWatchPreview() {
    // 프리뷰는 다양한 시나리오를 테스트할 수 있습니다.
    StopWatchApp(
        timeInMillis = 12345L,
        isRunning = false,
        onStart = {},
        onStop = {},
        onReset = {}
    )
}

@Composable
fun StopWatchRunningPreview() {
    // 실행 중일 때의 UI를 테스트하는 프리뷰
    StopWatchApp(
        timeInMillis = 54321L,
        isRunning = true,
        onStart = {},
        onStop = {},
        onReset = {}
    )
}

