package com.wildan.storeapp
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.wildan.storeapp.model.LoginRequest
import com.wildan.storeapp.model.LoginResponse
import com.wildan.storeapp.repository.ProductRepository
import com.wildan.storeapp.ui.viewmodel.ProductViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class ProductViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var productViewModel: ProductViewModel
    private lateinit var productRepository: ProductRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        productRepository = mock(ProductRepository::class.java)
        productViewModel = ProductViewModel(productRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test login success`() = runTest {
        val loginRequest = LoginRequest("wildan", "wildan12344")
        val mockResponse = LoginResponse("sample_token")

        `when`(productRepository.requestLogin(loginRequest)).thenReturn(flowOf(mockResponse))

        productViewModel.requestLogin(loginRequest)

        advanceUntilIdle()

        val observer = mock(Observer::class.java) as Observer<String?>
        productViewModel.getDataLogin.observeForever(observer)

        verify(observer).onChanged("sample_token")

        productViewModel.getDataLogin.removeObserver(observer)
    }
}