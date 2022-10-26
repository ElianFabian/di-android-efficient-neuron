package com.elian.computeit.core.data.util

import com.elian.computeit.feature_tests.data.models.Divide
import com.elian.computeit.feature_tests.data.models.Multiply
import com.elian.computeit.feature_tests.data.models.Subtract
import com.elian.computeit.feature_tests.data.models.Sum

val symbolToOperation = mapOf(
    Sum.symbol to Sum,
    Subtract.symbol to Subtract,
    Multiply.symbol to Multiply,
    Divide.symbol to Divide,
)