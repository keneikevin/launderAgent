package com.example.launder.adapter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.agent.R
import com.example.agent.databinding.CardBinding
import com.example.launder.data.entities.Cake
import javax.inject.Inject


class CakeAdapter @Inject constructor(
):   PagingDataAdapter<Cake, CakeAdapter.CakeViewHolder>(Companion) {
    class CakeViewHolder(val binding: CardBinding, var cake: Cake? = null) : RecyclerView.ViewHolder(
        binding.root){
        val tvTitle= binding.textName
        val tvPrice= binding.textPrice
        val tvPer= binding.per
        val ivCake = binding.img
    }
    companion object : DiffUtil.ItemCallback<Cake>() {
        override fun areItemsTheSame(oldItem: Cake, newItem: Cake): Boolean {
            return oldItem.mediaId == newItem.mediaId
        }

        override fun areContentsTheSame(oldItem: Cake, newItem: Cake): Boolean {
            return oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: CakeViewHolder, position: Int) {
        val cake = getItem(position) ?:return



        holder.apply {
            Glide.with(itemView).setDefaultRequestOptions(RequestOptions()
                .placeholder(R.drawable.ic_broken_image)
                .error(R.drawable.ic_broken_image)
                .diskCacheStrategy(DiskCacheStrategy.DATA))
                .load(cake.img).into(ivCake)



            tvTitle.text = cake.title
            val sizePice = "Ksh: ${cake.price} "
            tvPrice.text = sizePice
            tvPer.text = cake.per


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CakeViewHolder {
        return CakeViewHolder(
            CardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}







