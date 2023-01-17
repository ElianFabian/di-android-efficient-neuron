package com.elian.computeit.core.util.extensions

import com.elian.computeit.core.domain.models.OperationType
import com.elian.computeit.core.data.model.OperationData

val OperationData.result get() = OperationType.fromName(operationName).invoke(pairOfNumbers.first, pairOfNumbers.second)

val OperationData.isError get() = insertedResult != result 