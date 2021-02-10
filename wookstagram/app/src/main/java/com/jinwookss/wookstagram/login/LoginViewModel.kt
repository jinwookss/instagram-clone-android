package com.jinwookss.wookstagram.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private val TAG = LoginViewModel::class.java.simpleName
    }

    private val _dataLoading = MutableLiveData(false)
    val dataLoading: LiveData<Boolean>
        get() = _dataLoading

    private val _signInEvent = MutableLiveData<Unit>()
    val signInEvent: LiveData<Unit>
        get() = _signInEvent

    private val _signUpEvent = MutableLiveData<Unit>()
    val signUpEvent: LiveData<Unit>
        get() = _signUpEvent

    private val _navigateEvent = MutableLiveData<FirebaseUser?>()
    val navigateEvent: LiveData<FirebaseUser?>
        get() = _navigateEvent

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    private val context by lazy { application.applicationContext }
    private val auth by lazy { FirebaseAuth.getInstance() }

    fun signUp() {
        _signUpEvent.value = Unit
    }

    fun signIn() {
        _signInEvent.value = Unit
    }

    fun onSignUpByEmail(email: String, password: String) {
        _dataLoading.value = true
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onSignInByEmail(email, password)
            } else {
                Log.w(TAG, "signUpByEmail: failure", task.exception)
                _message.value = "Authentication failed."
            }
            _dataLoading.value = false
        }
    }

    fun onSignInByEmail(email: String, password: String) {
        _dataLoading.value = true
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _navigateEvent.value = task.result?.user
            } else {
                Log.w(TAG, "signInByEmail: failure", task.exception)
                _message.value = "Authentication failed."
            }
            _dataLoading.value = false
        }
    }
}