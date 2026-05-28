package com.example.practicetimerapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.Locale
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// タイマー状態を表すenum
enum class TimerState {
    STOPPED,
    RUNNING,
    PAUSED  // 一時停止状態
}

class TimerViewModel : ViewModel() {

    // タイマーの初期値(default 60 second)
    private var initTime = 60_000L

    // カウント時間
    private var totalTime by mutableLongStateOf(initTime)

    // 残り時間
    private var timeLeft by mutableLongStateOf(initTime)

    // タイマー用job
    private var timer: Job? = null

    // 現在の動作状態
    private var state by mutableStateOf(TimerState.STOPPED)

    // 実行中を判定するカスタムゲッター
    /* 現在のstateがRUNNINGであればtrueを返す
       カスタムゲッターにすることで、stateが変化したときにisRunningの値も自動的に反映する
     */
    val isRunning get() = state == TimerState.RUNNING

    // 進捗状況を表すカスタムゲッター
    val progress get() = timeLeft / totalTime.toFloat()

    // 残り時間を導出するカスタムゲッター
    val timeLeftText: String
        get() {
            val seconds = (timeLeft / 1000) % 60
            val minutes = (timeLeft / 1000) / 60
            return String.format(Locale.JAPANESE, "%02d:%02d", minutes, seconds)
        }

    // 合計時間を導出するカスタムゲッター
    val totalTimeText: String
        get() {
            val seconds = (totalTime / 1000) % 60
            val minutes = (totalTime / 1000) / 60
            return String.format(Locale.JAPANESE, "%02d:%02d", minutes, seconds)
        }

    // カウントダウン
    fun countDown() {
        // 起動中のタイマーがあれば停止する
        timer?.cancel()
        // 実行中に変更
        state = TimerState.RUNNING

        // .launch  コルーチン起動
        timer = viewModelScope.launch {
            // 実行中かつ残り時間がある間ループ
            while (timeLeft > 0 && isRunning) {
                delay(100)  // 100ms待つ（スレッドは塞がないため、待機中も画面が固まることはない）
                timeLeft -= 100 // 100ms(0.1秒)減らす
            }
            // カウンドダウンが終わっていれば、終了状態に遷移
            if (timeLeft <= 0) {
                timeLeft = 0
                state = TimerState.STOPPED
            }
        }   // launch finish
    }   // countDown finish

    // タイマーリセット
    fun resetTimer() {
        // 各データを初期化する
        totalTime = initTime
        timeLeft = initTime
    }
}   // TimerViewModel finish