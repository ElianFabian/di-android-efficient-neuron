package com.elian.computeit.core.data

enum class Operation(
    val symbol: String,
    val calculate: (a: Int, b: Int) -> Int,
)
{
    Sum(
        symbol = "+",
        calculate = { a, b -> a + b },
    ),
    Subtract(
        symbol = "−",
        calculate = { a, b -> a - b },
    ),
    Multiply(
        symbol = "×",
        calculate = { a, b -> a * b },
    ),
    Divide(
        symbol = "÷",
        calculate = { a, b -> a / b },
    );

    operator fun invoke(pair: Pair<Int, Int>) = calculate(pair.first, pair.second)
}

val symbolToOperation = Operation.values().associateBy { it.symbol }