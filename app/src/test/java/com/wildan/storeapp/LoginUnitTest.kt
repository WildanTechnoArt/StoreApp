package com.wildan.storeapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.wildan.storeapp.model.LoginRequest
import com.wildan.storeapp.model.LoginResponse
import com.wildan.storeapp.repository.AuthRepository
import com.wildan.storeapp.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AuthViewModel
    private lateinit var repository: AuthRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())

        repository = mock(AuthRepository::class.java)
        viewModel = AuthViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when login success then token is emmit`() = runTest {
        val loginRequest = LoginRequest("wildan", "wildan12344")
        val expectResponse = LoginResponse("sample_token")

        `when`(repository.requestLogin(loginRequest)).thenReturn(flowOf(expectResponse))

        viewModel.requestLogin(loginRequest)

        advanceUntilIdle()

        assertEquals(expectResponse.token, viewModel.getDataLogin.value)
    }
}