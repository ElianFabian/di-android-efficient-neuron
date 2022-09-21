package com.elian.computeit.data.repository

import com.elian.computeit.data.model.User
import com.elian.computeit.ui.login.LogInContract
import com.elian.computeit.ui.signup.SignUpContract
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object FirebaseRepository :
    LogInContract.Repository,
    SignUpContract.Repository
{
    override fun logIn(callback: LogInContract.OnLogInCallback, user: User)
    {
        Firebase.auth.signInWithEmailAndPassword(user.email, user.password)
            .addOnSuccessListener { callback.onLogInSuccess() }
            .addOnFailureListener { callback.onLogInFailure() }
    }

    override fun signUp(callback: SignUpContract.OnSignUpCallback, user: User)
    {
        Firebase.auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnSuccessListener { callback.onSignUpSuccess() }
            .addOnFailureListener { callback.onSignUpFailure() }
    }
}