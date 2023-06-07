package com.example.launderagent.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.agent.R
import com.example.agent.databinding.ItemShoppingBinding
import com.example.launderagent.data.entities.ShoppingItem
import javax.inject.Inject

class ShoppingAdapter @Inject constructor(
):RecyclerView.Adapter<ShoppingAdapter.ShoppingViewHolder>() {
    private lateinit var binding: ItemShoppingBinding
    class ShoppingViewHolder(binding: ItemShoppingBinding) : RecyclerView.ViewHolder(binding.root){
        val tvName= binding.tvName
        val tvSize =binding.tvShoppingItemSize
        val tvPrice = binding.tvPrice
        val image = binding.ivCake
    }

    private val diffCallback = object : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var shoppingItems: List<ShoppingItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        return ShoppingViewHolder(
            ItemShoppingBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val shoppingItem = shoppingItems[position]
        holder.apply {
            Glide.with(itemView).setDefaultRequestOptions(RequestOptions()
                .placeholder(R.drawable.place)
                .error(R.drawable.ic_broken_image)
                .diskCacheStrategy(DiskCacheStrategy.DATA))
                .load(shoppingItem.imageUrl).into(image)
            tvName.text = shoppingItem.name
            val sizeText = "${shoppingItem.size}KG"
            tvSize.text = sizeText
            val priceText = "${shoppingItem.price}0Ksh"
            tvPrice.text = priceText
        }
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }

}