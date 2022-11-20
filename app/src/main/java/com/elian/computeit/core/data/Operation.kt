package com.elian.computeit.core.data

import com.elian.computeit.core.domain.models.NumberPair

enum class Operation(
    val symbol: String,
    val calculate: (a: Int, b: Int) -> Int,
)
{
    SUM(
        symbol = "+",
        calculate = { a, b -> a + b },
    ),
    SUBTRACT(
        symbol = "−",
        calculate = { a, b -> a - b },
    ),
    MULTIPLY(
        symbol = "×",
        calculate = { a, b -> a * b },
    ),
    DIVIDE(
        symbol = "÷",
        calculate = { a, b -> a / b },
    );

    operator fun invoke(pairOfNumbers: NumberPair) = calculate(pairOfNumbers.first, pairOfNumbers.second)
}

val symbolToOperation = Operation.values().associateBy { it.symbol }