package it.pierosilvestri.stopwatch_domain.services

import it.pierosilvestri.stopwatch_domain.utils.StopwatchState
import it.pierosilvestri.stopwatch_domain.utils.nanosecondToCentisecondToTwoDigits
import it.pierosilvestri.stopwatch_domain.utils.twoDigits
import java.util.Timer
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

class StopwatchServiceImpl: StopwatchSimpleService {

    private lateinit var timer: Timer

    private var seconds = "00"
    private var minutes = "00"
    private var centiseconds = "00"
    private var currentState = StopwatchState.Idle

    private var duration: Duration = Duration.ZERO
    private var callback: ((String, String, String, StopwatchState) -> Unit)? = null


    override fun observeTime(callback: (String, String, String, StopwatchState) -> Unit) {
        this.callback = callback
    }

    override fun startStopwatch() {
        timer = fixedRateTimer(initialDelay = 10L, period = 10L) {
            duration = duration.plus(10.milliseconds)
            duration.toComponents { _minutes, _seconds, _centiseconds ->
                minutes = _minutes.twoDigits()
                seconds = _seconds.twoDigits()
                centiseconds = _centiseconds.nanosecondToCentisecondToTwoDigits()
                currentState = StopwatchState.Started
                callback?.invoke(minutes, seconds, centiseconds, currentState)
            }
        }
    }

    override fun stopStopwatch() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        currentState = StopwatchState.Stopped
        callback?.invoke(minutes, seconds, centiseconds, currentState)
    }

    override fun resetStopwatch() {
        stopStopwatch()
        duration = Duration.ZERO
        currentState = StopwatchState.Idle
        callback?.invoke("00", "00", "00", StopwatchState.Idle)

    }
}