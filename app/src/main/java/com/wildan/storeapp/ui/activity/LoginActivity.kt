package com.wildan.storeapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.wildan.storeapp.R
import com.wildan.storeapp.databinding.ActivityLoginBinding
import com.wildan.storeapp.extensions.ViewBindingExt.viewBinding
import com.wildan.storeapp.extensions.clearAuthDataStore
import com.wildan.storeapp.extensions.getBooleanData
import com.wildan.storeapp.extensions.getStringData
import com.wildan.storeapp.extensions.initProgressButton
import com.wildan.storeapp.extensions.isNotEmpty
import com.wildan.storeapp.extensions.saveDataStore
import com.wildan.storeapp.extensions.setLoading
import com.wildan.storeapp.extensions.show
import com.wildan.storeapp.extensions.showToast
import com.wildan.storeapp.model.LoginRequest
import com.wildan.storeapp.ui.viewmodel.ProductViewModel
import com.wildan.storeapp.utils.Constant
import com.wildan.storeapp.utils.handleErrorApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityLoginBinding::inflate)
    private val viewModelAuth: ProductViewModel by viewModels()
    private var loginUsername: String? = null
    private var loginPassword: String? = null
    private var mUsername: String? = null
    private var mPassword: String? = null
    private var isRemember = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
        checkLoginCondition()
        getLiveData()
    }

    private fun setupView() = with(binding) {
        lifecycleScope.launch {
            loginUsername = getStringData(Constant.SAVE_USERNAME)
            loginPassword = getStringData(Constant.SAVE_PASSWORD)
        }

        registerButton.initProgressButton(this@LoginActivity)
        registerButton.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        loginButton.setOnClickListener {
            setupLogin()
        }

        inputPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                setupLogin()
            }
            true
        }
    }

    private fun checkLoginCondition() = with(binding) {
        lifecycleScope.launch {
            val getAccessToken = getStringData(Constant.SAVE_TOKEN)

            if (getAccessToken != "-") {
                navigateToMain()
            } else {
                layoutLogin.show()
                isRemember = getBooleanData(Constant.IS_REMEMBER_LOGIN)
                val getUsername = getStringData(Constant.SAVE_USERNAME, true)
                val getPassword = getStringData(Constant.SAVE_PASSWORD, true)
                cbxRememberMe.isChecked = isRemember
                if (isRemember) {
                    inputUsername.setText(getUsername)
                    inputPassword.setText(getPassword)
                }
            }
        }
    }

    private fun setupLogin() = with(binding) {
        lifecycleScope.launch {
            mUsername = inputUsername.text.toString()
            mPassword = inputPassword.text.toString()

            if (mUsername.isNotEmpty() && mPassword.isNotEmpty()) {
                if (mUsername == loginUsername && mPassword == loginPassword) {
                    loginSuccess("xx1234_sample_token")
                } else {
                    loginWithServer(mUsername, mPassword)
                }
            } else {
                showToast(getString(R.string.message_if_login_empty))
            }
        }
    }

    private fun loginWithServer(username: String?, password: String?) {
        isRemember = binding.cbxRememberMe.isChecked
        val body = LoginRequest()
        body.username = username
        body.password = password

        viewModelAuth.requestLogin(body)
    }

    private fun loginSuccess(token: String?) {
        lifecycleScope.launch {
            withContext(Dispatchers.Default) {
                saveDataStore(Constant.SAVE_TOKEN, token.toString())
                saveDataStore(Constant.SAVE_USERNAME, mUsername, true)
                saveDataStore(Constant.SAVE_PASSWORD, mPassword, true)
                saveDataStore(Constant.IS_REMEMBER_LOGIN, isRemember, true)
            }
            if (!binding.cbxRememberMe.isChecked) {
                clearAuthDataStore()
            }
            navigateToMain()
        }
    }

    private fun getLiveData() = with(binding) {
        viewModelAuth.apply {
            getDataLogin.observe(this@LoginActivity) { token ->
                loginSuccess(token)
            }
            error.observe(this@LoginActivity) {
                handleErrorApi(it)
            }
            loading.observe(this@LoginActivity) {
                loginButton.setLoading(it)
            }
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}