package com.elian.computeit.core.data.util

import com.elian.computeit.core.data.Divide
import com.elian.computeit.core.data.Multiply
import com.elian.computeit.core.data.Subtract
import com.elian.computeit.core.data.Sum

val symbolToOperation = mapOf(
    Sum.symbol to Sum,
    Subtract.symbol to Subtract,
    Multiply.symbol to Multiply,
    Divide.symbol to Divide,
)