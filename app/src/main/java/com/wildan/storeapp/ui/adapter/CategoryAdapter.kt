package com.wildan.storeapp.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wildan.storeapp.R
import com.wildan.storeapp.databinding.ItemCategoryBinding

class CategoryAdapter(
    private val action: (String) -> Unit
) : ListAdapter<String, CategoryAdapter.Holder>(MyDiffCallback()) {

    private var selectedCategory: String = "All"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val category = getItem(position)

        with(holder.binding) {
            tvCategory.text = category.replaceFirstChar { it.uppercase() }

            // Highlight jika ini yang sedang dipilih
            if (selectedCategory == category) {
                cardCategory.setCardBackgroundColor(ContextCompat.getColor(root.context, R.color.colorPrimary))
                tvCategory.setTextColor(Color.WHITE)
            } else {
                cardCategory.setCardBackgroundColor(Color.WHITE)
                tvCategory.setTextColor(Color.BLACK)
            }

            cardCategory.setOnClickListener {
                if (selectedCategory != category) {
                    selectedCategory = category
                    notifyDataSetChanged() // trigger UI refresh
                    action(category)
                }
            }
        }
    }

    fun submitCategoryList(list: List<String>) {
        val newList = listOf("All") + list
        submitList(newList)
    }

    class Holder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)

    class MyDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String) = oldItem == newItem
        override fun areContentsTheSame(oldItem: String, newItem: String) = oldItem == newItem
    }
}