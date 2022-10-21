package com.elian.computeit.core.domain.util

sealed interface TimerEvent
{
    object Started : TimerEvent
    data class Ticked(val millisUntilFinished: Long) : TimerEvent
    object Restarted : TimerEvent
    object Stopped : TimerEvent
    object Resumed : TimerEvent
    object Finished : TimerEvent
}