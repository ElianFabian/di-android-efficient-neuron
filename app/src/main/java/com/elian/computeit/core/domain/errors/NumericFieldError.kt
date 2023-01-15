package com.elian.computeit.core.domain.errors

import com.elian.computeit.core.util.Error

sealed interface NumericFieldError : Error
{
	object Empty : NumericFieldError
}