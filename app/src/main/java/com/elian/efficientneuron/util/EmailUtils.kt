package com.elian.efficientneuron.util

import android.util.Patterns
import java.util.regex.Pattern

object EmailUtils
{
    private const val PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@$*?¡\\-_])(?!.*\\s).{8,20}$"

    private val passwordPattern = Pattern.compile(PASSWORD_PATTERN)

    fun isValidPassword(password: String): Boolean
    {
        return passwordPattern.matcher(password).matches()
    }
    
    fun isPasswordNotValid(password: String): Boolean
    {
        return !isValidPassword(password)
    }

    fun isEmailValid(email: String): Boolean
    {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    fun isEmailNotValid(email: String): Boolean
    {
        return !isEmailValid(email)
    }
}