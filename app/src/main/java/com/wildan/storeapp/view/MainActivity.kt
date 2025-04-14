package com.wildan.storeapp.view

import android.os.Bundle
import android.view.View
import android.view.animation.TranslateAnimation
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.wildan.storeapp.databinding.ActivityMainBinding
import com.wildan.storeapp.utils.Constant
import com.wildan.storeapp.utils.ViewBindingExt.viewBinding
import com.wildan.storeapp.viewmodel.ProductViewModel
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private var mAdapterProduct by Delegates.notNull<ProductAdapter>()
    private var mAdapterCategory by Delegates.notNull<CategoryAdapter>()
    private val viewModel: ProductViewModel by viewModels()
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
//                tvCartBadge.visibility = if(data.isNotEmpty()) View.VISIBLE else View.GONE
//                tvCartBadge.text = data.size.toString()
            }
            error.observe(this@MainActivity) {
                Constant.handleErrorApi(this@MainActivity, it)
            }
            loading.observe(this@MainActivity) {
                swipeRefresh.isRefreshing = it
            }
        }
    }
}