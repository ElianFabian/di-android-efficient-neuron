package com.elian.computeit.core.domain.states

import com.elian.computeit.core.util.Error

data class NumericFieldState<T : Number>(
    val number: T? = null,
    val error: Error? = null,
)

sealed interface NumericFieldError : Error
{
    object Empty : NumericFieldError
}