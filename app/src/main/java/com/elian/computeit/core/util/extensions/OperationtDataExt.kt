package com.elian.computeit.core.util.extensions

import com.elian.computeit.core.domain.models.OperationData

val OperationData.isError get() = expectedResult != insertedResult