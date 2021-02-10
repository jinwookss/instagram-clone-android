package com.jinwookss.wookstagram.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.jinwookss.wookstagram.MainActivity
import com.jinwookss.wookstagram.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    companion object {
        private val TAG = LoginActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@LoginActivity
            viewModel = this@LoginActivity.viewModel
        }
        setContentView(binding.root)

        initialize()
    }

    private fun initialize() {
        viewModel.signUpEvent.observe(this, {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.onSignUpByEmail(email, password)
            }
        })
        viewModel.signInEvent.observe(this, {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                viewModel.onSignInByEmail(email, password)
            }
        })
        viewModel.navigateEvent.observe(this, { user ->
            moveToMain(user)
        })
        viewModel.message.observe(this, { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun moveToMain(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}