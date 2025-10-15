package com.example.w05 // 패키지 이름은 프로젝트 구조에 맞게 유지합니다.

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.w05.ui.theme.SparrowprojectTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    // 1. 'override' 키워드를 추가하여 부모 클래스의 메서드를 재정의하도록 수정했습니다.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SparrowprojectTheme {
                // 상태 호이스팅: 상태와 관련된 로직을 상위 컴포저블로 이동시킵니다.
                var count by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(0) }
                var timeInMillis by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(0L) }
                var isRunning by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

                // 스톱워치 로직을 담당하는 LaunchedEffect
                androidx.compose.runtime.LaunchedEffect(isRunning) {
                    while (isRunning) {
                        delay(10L)
                        timeInMillis += 10L
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    androidx.compose.foundation.layout.Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                    ) {
                        // 2. 상태와 이벤트 핸들러를 파라미터로 전달하여 재사용성을 높였습니다.
                        CounterApp(
                            count = count,
                            onIncrease = { count++ },
                            onReset = { count = 0 }
                        )
                        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(32.dp))
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
    }
}

/**
 * 카운터 UI를 표시하는 Composable. 상태를 직접 소유하지 않고 UI 표시에만 집중합니다.
 * @param count 표시할 현재 숫자.
 * @param onIncrease 숫자를 증가시키는 이벤트.
 * @param onReset 숫자를 초기화하는 이벤트.
 */
@androidx.compose.runtime.Composable
fun CounterApp(
    count: Int,
    onIncrease: () -> Unit,
    onReset: () -> Unit
) {
    androidx.compose.foundation.layout.Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Count: $count",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )
        // 3. 버튼 사이에 간격을 추가하여 UI를 개선했습니다.
        androidx.compose.foundation.layout.Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
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
@androidx.compose.runtime.Composable
fun StopWatchApp(
    timeInMillis: Long,
    isRunning: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onReset: () -> Unit
) {
    androidx.compose.foundation.layout.Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = formatTime(timeInMillis),
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        androidx.compose.foundation.layout.Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)) {
            // 4. isRunning 상태에 따라 버튼의 활성화/비활성화 상태를 제어합니다.
            Button(onClick = onStart, enabled = !isRunning) { Text("Start") }
            Button(onClick = onStop, enabled = isRunning) { Text("Stop") }
            Button(onClick = onReset) { Text("Reset") }
        }
    }
}

/**
 * 밀리초 시간을 "분:초:밀리초" 형식의 문자열로 변환합니다.
 */
private fun formatTime(timeInMillis: Long): String {
    val minutes = (timeInMillis / 1000) / 60
    val seconds = (timeInMillis / 1000) % 60
    val millis = (timeInMillis % 1000) / 10
    return String.format("%02d:%02d:%02d", minutes, seconds, millis)
}


// --- Previews ---

@Preview(showBackground = true)
@androidx.compose.runtime.Composable
fun CounterAppPreview() {
    SparrowprojectTheme {
        CounterApp(count = 5, onIncrease = {}, onReset = {})
    }
}

@Preview(showBackground = true)
@androidx.compose.runtime.Composable
fun StopWatchPreview() {
    SparrowprojectTheme {
        // 프리뷰에서는 다양한 상태를 테스트할 수 있습니다.
        StopWatchApp(
            timeInMillis = 12345L,
            isRunning = false,
            onStart = {},
            onStop = {},
            onReset = {}
        )
    }
}

// 5. 사용되지 않는 StopwatchScreen, ColorToggleButtonApp 및 관련 프리뷰는 코드에서 제거하여 간결하게 만들었습니다.
