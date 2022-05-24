package com.elian.efficientneuron.ui.signup

import com.elian.efficientneuron.data.model.User
import com.elian.efficientneuron.data.repository.FirebaseRepository
import com.elian.efficientneuron.util.EmailUtils

class SignupInteractor(private val listener: SignupContract.OnInteractorListener) :
    SignupContract.Interactor,
    SignupContract.OnRepositoryCallback
{
    private val repository: SignupContract.Repository = FirebaseRepository

    //region SignupContract.Interactor
    override fun signup(user: User)
    {
        repository.signup(this, user)
    }

    override fun validateFields(user: User, repeatedPassword: String): Boolean = when
    {
        EmailUtils.isEmailNotValid(user.email)       -> listener.onEmailInvalidError().run { true }
        EmailUtils.isPasswordNotValid(user.password) -> listener.onPasswordInvalidError().run { true }

        user.password != repeatedPassword            -> listener.onPasswordsDontMatchError().run { true }

        else                                         -> false
    }

    //endregion

    //region SignupContract.OnRepositoryCallback

    override fun onSignupSuccess()
    {
        listener.onSignupSuccess()
    }

    override fun onSignupFailure()
    {
        listener.onSignupFailure()
    }

    //endregion
}
