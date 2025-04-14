package com.wildan.storeapp.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.wildan.storeapp.R
import com.wildan.storeapp.databinding.ActivityDetailProductBinding
import com.wildan.storeapp.model.ProductResponse
import com.wildan.storeapp.utils.Constant
import com.wildan.storeapp.utils.ViewBindingExt.viewBinding
import com.wildan.storeapp.viewmodel.DatabaseViewModel
import com.wildan.storeapp.viewmodel.LocalDataViewModelFactory
import com.wildan.storeapp.viewmodel.ProductViewModel

class DetailProductActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityDetailProductBinding::inflate)
    private val viewModel: ProductViewModel by viewModels()
    private lateinit var viewModelDatabase: DatabaseViewModel
    private var productId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
        getLiveData()
    }

    private fun setupView() = with(binding) {
        productId = intent.getStringExtra(Constant.PRODUCT_ID)
        val factory = LocalDataViewModelFactory.getInstance(this@DetailProductActivity)
        viewModelDatabase =
            ViewModelProvider(this@DetailProductActivity, factory)[DatabaseViewModel::class.java]

        viewModel.getProductDetail(productId.toString())
        swipeRefresh.setOnRefreshListener {
            viewModel.getProductDetail(productId.toString())
        }

        viewModelDatabase.checkIfAddCart(productId.toString())

        viewModelDatabase.isAddCart.observe(this@DetailProductActivity) { exist ->
            btnAddToCart.text = if (exist) "Remove from Cart" else "Add to Cart"
        }
    }

    private fun getLiveData() = with(binding) {
        viewModel.apply {
            getProductDetail.observe(this@DetailProductActivity) { data ->
                val productName = data.title ?: "-"
                Glide.with(applicationContext)
                    .load(data.image)
                    .placeholder(R.drawable.baseline_image_100)
                    .into(imageProduct)
                tvTitle.text = productName
                tvPrice.text = Constant.formatRupiah(data?.price ?: 0.0)
                tvCategory.text = data.category ?: "None category"
                tvDescription.text = data.description ?: "None description"
                tvRating.text = "${data.rating?.rate} (${data.rating?.count})"

                val product = ProductResponse()
                product.title = productName
                product.price = data?.price ?: 0.0
                product.count = 0
                product.image = data.image

                viewModelDatabase.toggleChart(product) { isSaved ->
                    val message = if (isSaved) "Add to Cart" else "Remove from Cart"
                    Toast.makeText(this@DetailProductActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
            error.observe(this@DetailProductActivity) {
                Constant.handleErrorApi(this@DetailProductActivity, it)
            }
            loading.observe(this@DetailProductActivity) {
                swipeRefresh.isRefreshing = it
            }
        }
    }
}