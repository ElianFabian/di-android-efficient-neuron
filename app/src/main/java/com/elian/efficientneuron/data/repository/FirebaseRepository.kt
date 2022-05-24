package com.elian.efficientneuron.data.repository

import com.elian.efficientneuron.data.model.User
import com.elian.efficientneuron.ui.login.LoginContract
import com.elian.efficientneuron.ui.signup.SignupContract
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object FirebaseRepository :
    LoginContract.Repository,
    SignupContract.Repository
{
    override fun login(callback: LoginContract.OnLoginCallback, user: User)
    {
        Firebase.auth.signInWithEmailAndPassword(user.email, user.password)
            .addOnSuccessListener { callback.onLoginSuccess() }
            .addOnFailureListener { callback.onLoginFailure() }
    }

    override fun signup(callback: SignupContract.OnSignupCallback, user: User)
    {
        Firebase.auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnSuccessListener { callback.onSignupSuccess() }
            .addOnFailureListener { callback.onSignupFailure() }
    }
}