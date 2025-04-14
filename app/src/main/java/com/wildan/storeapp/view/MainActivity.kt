package com.wildan.storeapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.TranslateAnimation
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.wildan.storeapp.databinding.ActivityMainBinding
import com.wildan.storeapp.utils.Constant
import com.wildan.storeapp.utils.ViewBindingExt.viewBinding
import com.wildan.storeapp.viewmodel.DatabaseViewModel
import com.wildan.storeapp.viewmodel.LocalDataViewModelFactory
import com.wildan.storeapp.viewmodel.ProductViewModel
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private var mAdapterProduct by Delegates.notNull<ProductAdapter>()
    private var mAdapterCategory by Delegates.notNull<CategoryAdapter>()
    private val viewModel: ProductViewModel by viewModels()
    private lateinit var viewModelDatabase: DatabaseViewModel
    private lateinit var translateAnimation: TranslateAnimation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
        requestContent()
        getLiveData()
    }

    private fun setupView() = with(binding){
        translateAnimation = TranslateAnimation(0F, 0F, 0F, 0F).apply {
            duration = 200
            fillAfter = true
            isFillEnabled = true
        }

        val factory = LocalDataViewModelFactory.getInstance(this@MainActivity)

        viewModelDatabase =
            ViewModelProvider(this@MainActivity, factory)[DatabaseViewModel::class.java]

        mAdapterProduct = ProductAdapter()
        mAdapterCategory = CategoryAdapter()

        rvProduct.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = mAdapterProduct
        }

        rvCategory.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = mAdapterCategory
        }

        swipeRefresh.setOnRefreshListener {
            requestContent()
        }
        btnCart.setOnClickListener {
            startActivity(Intent(this@MainActivity, CartActivity::class.java))
        }
    }

    private fun requestContent(){
        viewModel.getProductList()
        viewModel.getCategoryList()
    }

    private fun getLiveData() = with(binding) {
        viewModel.apply {
            getProductList.observe(this@MainActivity) { data ->
                Constant.handleData(
                    1, false, data, mAdapterProduct,
                    rvProduct, textMessageNoData
                )
            }
            getCategoryList.observe(this@MainActivity) { data ->
                mAdapterCategory.submitList(data)
            }
            error.observe(this@MainActivity) {
                Constant.handleErrorApi(this@MainActivity, it)
            }
            loading.observe(this@MainActivity) {
                swipeRefresh.isRefreshing = it
            }
        }

        viewModelDatabase.getTotalItemCount.observe(this@MainActivity) { quantity ->
            lifecycleScope.launch {
                val itemCount = quantity > 0
                tvCartBadge.visibility = if(itemCount) View.VISIBLE else View.GONE
                tvCartBadge.text = quantity.toString()
            }
        }
    }
}