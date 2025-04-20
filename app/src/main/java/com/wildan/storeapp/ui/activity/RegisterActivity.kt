package com.wildan.storeapp.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.lifecycle.lifecycleScope
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.wildan.storeapp.MyApp
import com.wildan.storeapp.R
import com.wildan.storeapp.databinding.ActivityRegisterBinding
import com.wildan.storeapp.extensions.ViewBindingExt.viewBinding
import com.wildan.storeapp.extensions.isNotEmpty
import com.wildan.storeapp.extensions.isValidEmail
import com.wildan.storeapp.extensions.showToast
import com.wildan.storeapp.model.RegisterRequest
import com.wildan.storeapp.ui.viewmodel.ProductViewModel
import com.wildan.storeapp.utils.Constant
import com.wildan.storeapp.utils.handleErrorApi
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityRegisterBinding::inflate)
    private val viewModelAuth: ProductViewModel by viewModels()
    private var mUsername: String? = null
    private var mPassword: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
        getLiveData()
    }

    private fun setupView() = with(binding) {
        bindProgressButton(registerButton)
        registerButton.attachTextChangeAnimator()

        binding.registerButton.setOnClickListener {
            mUsername = inputUsername.text.toString()
            mPassword = inputPassword.text.toString()
            val email = inputEmail.text.toString()

            if (mUsername.isNotEmpty() && mPassword.isNotEmpty()) {
                if (email.isValidEmail()) {
                    val body = RegisterRequest()
                    body.username = mUsername
                    body.password = mPassword
                    body.email = email

                    viewModelAuth.registerUser(body)
                } else {
                    showToast("Email not valid")
                }
            } else {
                showToast(getString(R.string.msg_if_empty))
            }
        }
    }

    private fun getLiveData() = with(binding) {
        val database = MyApp.getInstance()
        viewModelAuth.apply {
            successRegister.observe(this@RegisterActivity) { message ->
                lifecycleScope.launch {
                    database.saveStringDataStore(this@RegisterActivity, Constant.SAVE_USERNAME, mUsername)
                    database.saveStringDataStore(this@RegisterActivity, Constant.SAVE_PASSWORD, mPassword)
                    showToast(message.toString())
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                }
            }
            error.observe(this@RegisterActivity) {
                handleErrorApi(it)
            }
            loading.observe(this@RegisterActivity) {
                if (it) {
                    registerButton.isClickable = false
                    registerButton.showProgress {
                        progressColor = "#FFFFFF".toColorInt()
                    }
                } else {
                    registerButton.isClickable = true
                    registerButton.hideProgress("Register")
                }
            }
        }
    }
}