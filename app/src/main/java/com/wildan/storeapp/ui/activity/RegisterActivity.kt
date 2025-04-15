package com.wildan.storeapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.wildan.storeapp.R
import com.wildan.storeapp.databinding.ActivityRegisterBinding
import com.wildan.storeapp.utils.Constant
import com.wildan.storeapp.utils.extensions.ViewBindingExt.viewBinding
import com.wildan.storeapp.ui.viewmodel.ProductViewModel

class RegisterActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityRegisterBinding::inflate)
    private val viewModelAuth: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
        getLiveData()
    }

    private fun setupView() = with(binding) {

        bindProgressButton(registerButton)
        registerButton.attachTextChangeAnimator()

        layoutPassword.setEndIconDrawable(R.drawable.filled_visibility_off)
        layoutPassword.setEndIconOnClickListener {
            if (inputPassword.transformationMethod is PasswordTransformationMethod) {
                // Show Password
                inputPassword.transformationMethod = null
                layoutPassword.setEndIconDrawable(R.drawable.filled_visibility_on)
            } else {
                // Hide Password
                inputPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                layoutPassword.setEndIconDrawable(R.drawable.filled_visibility_off)
            }

            inputPassword.setSelection(inputPassword.text?.length ?: 0)
        }

        binding.registerButton.setOnClickListener {
            val username = inputUsername.text.toString()
            val password = inputPassword.text.toString()
            val email = inputEmail.text.toString()

            val usernameEmpty = Constant.isTextEmpty(username)
            val passEmpty = Constant.isTextEmpty(password)
        }
    }

    private fun getLiveData() = with(binding) {
        viewModelAuth.apply {
            successRegister.observe(this@RegisterActivity) {
                startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
                finish()
            }
            error.observe(this@RegisterActivity) {
                Constant.handleErrorApi(this@RegisterActivity, it)
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