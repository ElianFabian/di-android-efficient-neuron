package com.elian.computeit.core.domain.states

import com.elian.computeit.core.util.Error

data class TextFieldState(
    val text: String = "",
    val error: Error? = null,
)