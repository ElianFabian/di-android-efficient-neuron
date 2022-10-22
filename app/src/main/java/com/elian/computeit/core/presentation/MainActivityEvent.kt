package com.elian.computeit.core.presentation

sealed interface MainActivityEvent
{
    object OnUserNotRegistered : MainActivityEvent
}