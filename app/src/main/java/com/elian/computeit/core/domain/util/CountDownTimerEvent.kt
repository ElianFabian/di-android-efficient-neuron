package com.elian.computeit.core.domain.util

sealed interface CountDownTimerEvent
{
    object Started : CountDownTimerEvent
    data class Ticked(val millisUntilFinished: Long) : CountDownTimerEvent
    object Restarted : CountDownTimerEvent
    object Stopped : CountDownTimerEvent
    object Finished : CountDownTimerEvent
}