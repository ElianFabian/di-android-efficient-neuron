package com.elian.computeit.view_model

import com.elian.computeit.data.model.User
import com.elian.computeit.feature_auth.data.repository.AuthRepositoryImpl

class SignUpInteractor(private val listener: SignUpContract.OnInteractorListener) :
    SignUpContract.Interactor,
    SignUpContract.OnRepositoryCallback
{
    private val repository: SignUpContract.Repository = AuthRepositoryImpl() as SignUpContract.Repository

    //region SignUpContract.Interactor
    override fun signUp(user: User)
    {
        repository.signUp(this, user)
    }

    override fun validateFields(user: User, repeatedPassword: String): Boolean = when
    {
//        !ValidationUtil.isEmailValid(user.email)       -> listener.onEmailInvalidError().run { true }
//        !ValidationUtil.isValidPassword(user.password) -> listener.onPasswordInvalidError().run { true }

        user.password != repeatedPassword               -> listener.onPasswordsDontMatchError().run { true }

        else                                            -> false
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
