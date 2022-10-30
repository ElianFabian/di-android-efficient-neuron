package com.elian.computeit.core.presentation

sealed interface MainActivityEvent
{
    object OnUserNotLoggedIn : MainActivityEvent
}