package com.elian.efficientneuron.util

object EmailUtils
{
    private const val PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@$*?¡\\-_])(?!.*\\s).{8,20}$"

    private val passwordPattern = java.util.regex.Pattern.compile(PASSWORD_PATTERN)

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
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    fun isEmailNotValid(email: String): Boolean
    {
        return !isEmailValid(email)
    }
}