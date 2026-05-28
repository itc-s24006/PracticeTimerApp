package com.example.practicetimerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.practicetimerapp.ui.theme.PracticeTimerAppTheme

import androidx.compose.foundation.layout.Box
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.viewmodel.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticeTimerAppTheme {
                Main()
            }
        }
    }
}

@Composable
fun Main() {
    // viewModelを取得
    val viewModel: TimerViewModel = viewModel()

    // Scaffold 標準的な画面構成を用意するComposable。TopBarやButtonBarなどの領域をまとめて扱う。
    Scaffold(
        bottomBar = { BottomView(viewModel) }   // フッター部
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.TopCenter
        ) {
            TimerViewModel(viewModel)   // タイマー部
        }
    }

}

@Composable
fun TimerViewModel(viewModel: TimerViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BoxWithConstraints( // 親から与えられたサイズ制約をこの中のコードから参照できるコンポーネント
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            // minOf    これらを使って画面に収まる最大の正方形サイズを計算し、Boxに渡す
            val size = minOf(maxWidth, maxHeight)

            Box(
                modifier = Modifier
                    .size(size)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(  // 進捗グラフ
                    progress = {viewModel.progress},
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 16.dp
                )
                Text(   // 中央のテキスト
                    text = viewModel.timeLeftText,
                    fontSize = 48.sp,
                )
            }
        }

        OutlinedTextField(  // タイマーの時間
            value = viewModel.totalTimeText,
            onValueChange = { },
            label = { Text("指定の時間") },
            readOnly = true,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
            )
        )
    }
}   // TimerView finish

// 画面下部のボタンリスト
@Composable
fun BottomView(viewModel: TimerViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .navigationBarsPadding()
            .padding(16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        FilledIconButton(   // スタート
            onClick = { viewModel.countDown() },
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.img_play),
                contentDescription = "start/pause"
            )
        }
        FilledIconButton(   // リセット
            onClick = { viewModel.resetTimer() },
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.img_reset),
                contentDescription = "reset"
            )
        }
    }
}   // BottomView finish



@Preview(showBackground = true)
@Composable
fun MainPreview() {
    PracticeTimerAppTheme {
        Main()
    }
}