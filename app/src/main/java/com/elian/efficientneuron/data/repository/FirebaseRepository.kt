package com.elian.efficientneuron.data.repository

import com.elian.efficientneuron.data.model.User
import com.elian.efficientneuron.ui.login.LoginContract
import com.elian.efficientneuron.ui.signup.SignupContract

object FirebaseRepository :
    LoginContract.Repository,
    SignupContract.Repository
{
    override fun login(callback: LoginContract.OnLoginCallback, user: User)
    {

    }

    override fun signup(callback: SignupContract.OnSignupCallback, user: User)
    {
        
    }
}