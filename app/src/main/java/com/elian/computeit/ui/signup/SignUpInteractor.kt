package com.elian.computeit.ui.signup

import com.elian.computeit.data.model.User
import com.elian.computeit.data.repository.FirebaseRepository
import com.elian.computeit.util.EmailUtils

class SignUpInteractor(private val listener: SignUpContract.OnInteractorListener) :
    SignUpContract.Interactor,
    SignUpContract.OnRepositoryCallback
{
    private val repository: SignUpContract.Repository = FirebaseRepository

    //region SignUpContract.Interactor
    override fun signUp(user: User)
    {
        repository.signUp(this, user)
    }

    override fun validateFields(user: User, repeatedPassword: String): Boolean = when
    {
        EmailUtils.isEmailNotValid(user.email)       -> listener.onEmailInvalidError().run { true }
        EmailUtils.isPasswordNotValid(user.password) -> listener.onPasswordInvalidError().run { true }

        user.password != repeatedPassword            -> listener.onPasswordsDontMatchError().run { true }

        else                                         -> false
    }

    //endregion

    //region SignUpContract.OnRepositoryCallback

    override fun onSignUpSuccess()
    {
        listener.onSignUpSuccess()
    }

    override fun onSignUpFailure()
    {
        listener.onSignUpFailure()
    }

    //endregion
}
