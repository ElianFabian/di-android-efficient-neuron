package com.elian.computeit.core.data

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

    operator fun invoke(first: Int, second: Int) = calculate(first, second)
}

val symbolToOperation = Operation.values().associateBy { it.symbol }