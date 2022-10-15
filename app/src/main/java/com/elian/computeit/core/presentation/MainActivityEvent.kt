package com.elian.computeit.core.presentation

sealed interface MainActivityEvent
{
    object UserNotRegistered : MainActivityEvent
}