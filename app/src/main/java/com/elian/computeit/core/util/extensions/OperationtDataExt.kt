package com.elian.computeit.core.util.extensions

import com.elian.computeit.core.data.model.OperationData
import com.elian.computeit.core.domain.models.OperationType

val OperationData.result get() = OperationType.fromName(operationName).invoke(pairOfNumbers.first, pairOfNumbers.second)

val OperationData.isError get() = insertedResult != result 