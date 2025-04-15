package com.wildan.storeapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.inputmethod.EditorInfo
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
import com.wildan.storeapp.databinding.ActivityLoginBinding
import com.wildan.storeapp.model.LoginRequest
import com.wildan.storeapp.utils.Constant
import com.wildan.storeapp.extensions.ViewBindingExt.viewBinding
import com.wildan.storeapp.extensions.isNotEmpty
import com.wildan.storeapp.ui.viewmodel.ProductViewModel
import com.wildan.storeapp.extensions.showToast
import com.wildan.storeapp.utils.handleErrorApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityLoginBinding::inflate)
    private val viewModelAuth: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
        getLiveData()
    }

    private fun setupView() = with(binding) {

        bindProgressButton(loginButton)
        loginButton.attachTextChangeAnimator()

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

        registerButton.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        lifecycleScope.launch {
            val getLocalData = MyApp.getInstance()

            val getAccessToken =
                MyApp.getInstance().readStringDataStore(this@LoginActivity, Constant.SAVE_TOKEN)

            if (getAccessToken.isNotEmpty()) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } else {
                MyApp.getInstance().clearDataStore(this@LoginActivity)
                binding.layoutLogin.visibility = View.VISIBLE
                val isRemember = getLocalData.readRememberDataStore(
                    this@LoginActivity,
                    Constant.IS_REMEMBER_LOGIN
                )
                val getUsername = getLocalData.readAuthDataStore(
                    this@LoginActivity,
                    Constant.SAVE_USERNAME
                )
                val getPassword = getLocalData.readAuthDataStore(
                    this@LoginActivity,
                    Constant.SAVE_PASSWORD
                )
                binding.cbxRememberMe.isChecked = isRemember
                if (isRemember) {
                    binding.inputUsername.setText(getUsername)
                    binding.inputPassword.setText(getPassword)
                }

                binding.loginButton.setOnClickListener {
                    requestLogin()
                }

                binding.inputPassword.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        requestLogin()
                    }
                    true
                }
            }
        }
    }

    private fun requestLogin() = with(binding) {
        lifecycleScope.launch {
            val isRemember = cbxRememberMe.isChecked
            val username = inputUsername.text.toString()
            val password = inputPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                val body = LoginRequest()
                body.username = username
                body.password = password

                viewModelAuth.requestLogin(this@LoginActivity, body, isRemember)
            } else {
                showToast(getString(R.string.message_if_login_empty))
            }
        }
    }

    private fun getLiveData() = with(binding) {
        viewModelAuth.apply {
            getDataLogin.observe(this@LoginActivity) { token ->
                lifecycleScope.launch {
                    withContext(Dispatchers.Default) {
                        MyApp.getInstance().saveStringDataStore(
                            this@LoginActivity, Constant.SAVE_TOKEN,
                            token.toString()
                        )
                    }
                    if (!cbxRememberMe.isChecked) {
                        MyApp.getInstance().clearAuthStore(this@LoginActivity)
                    }
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                }
            }
            error.observe(this@LoginActivity) {
                handleErrorApi(it)
            }
            loading.observe(this@LoginActivity) {
                if (it) {
                    loginButton.isClickable = false
                    loginButton.showProgress {
                        progressColor = "#FFFFFF".toColorInt()
                    }
                } else {
                    loginButton.isClickable = true
                    loginButton.hideProgress("Login")
                }
            }
        }
    }
}