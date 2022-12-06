package com.elian.computeit.core.util.extensions

import com.elian.computeit.core.data.Operation
import com.elian.computeit.core.domain.models.OperationData

val OperationData.result get() = Operation.fromName(operationName).invoke(pairOfNumbers.first, pairOfNumbers.second)

val OperationData.isError get() = insertedResult != result 