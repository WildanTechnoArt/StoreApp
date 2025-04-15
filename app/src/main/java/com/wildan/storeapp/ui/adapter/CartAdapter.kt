package com.wildan.storeapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wildan.storeapp.R
import com.wildan.storeapp.databinding.ItemCartBinding
import com.wildan.storeapp.model.ProductResponse
import com.wildan.storeapp.ui.activity.DetailProductActivity
import com.wildan.storeapp.utils.Constant

class CartAdapter : RecyclerView.Adapter<CartAdapter.Holder>() {

    private val dataList = mutableListOf<ProductResponse>() // Dataset adapter

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newData: List<ProductResponse>) {
        dataList.clear()
        dataList.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val data = dataList[position]
        val context = holder.itemView.context

        with(holder.binding) {
            val productName = data.title ?: "-"
            Glide.with(context)
                .load(data.image)
                .placeholder(R.drawable.baseline_image_100)
                .into(imgProduct)
            tvProductName.text = productName
            tvProductPrice.text = Constant.formatRupiah(data.price ?: 0.0)
            tvProductQuantity.text = "Quantity: ${data.count} pcs"

            cardProduct.setOnClickListener {
                toDetailFragment(
                    data,
                    DetailProductActivity::class.java,
                    it.context)
            }
        }
    }

    private fun toDetailFragment(
        data: ProductResponse,
        activity: Class<out AppCompatActivity>,
        context: Context
    ) {
        val bundle = Bundle().apply {
            putString(Constant.PRODUCT_ID, data.id.toString())
        }
        context.startActivity(Intent(context, activity).putExtras(bundle))
    }

    override fun getItemCount(): Int = dataList.size

    class Holder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)
}
