package com.example.launderagent.adapterpackage

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.agent.databinding.ServiceCustomerBinding
import com.example.launderagent.data.entities.Service
import com.example.launderagent.ui.home.customer.CustomersServiceFragmentDirections
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class ServiceCustomerAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<ServiceCustomerAdapter.PostViewHolder>() {

    class PostViewHolder(val binding: ServiceCustomerBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivPostImage: ImageView = binding.img
        val tvPostAuthor: TextView = binding.textName
        val tvPostText: TextView = binding.textPrice
       // val tvPer: TextView = binding.per
        val cad = binding.cad
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Service>() {
        override fun areContentsTheSame(oldItem: Service, newItem: Service): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areItemsTheSame(oldItem: Service, newItem: Service): Boolean {
            return oldItem.mediaId == newItem.mediaId
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var posts: List<Service>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            ServiceCustomerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val service = posts[position]
        holder.apply {
            glide.load(service.img).into(ivPostImage)
            tvPostAuthor.text = service.title
            var pr = "Ksh: ${service.price} "+"/ "+"${service.per}"
            tvPostText.text = pr
           // tvPer.text = post.per
            cad.setOnClickListener {
                val directions= CustomersServiceFragmentDirections.actionCustomersServiceFragmentToDetailFragment(service)
                it.findNavController().navigate(directions)
                Snackbar.make(this.itemView, "Swipe", Snackbar.LENGTH_SHORT).show()
            }

        }
    }

    private var onDeletePostClickListener: ((Service) -> Unit)? = null



    fun setOnDeletePostClickListener(listener: (Service) -> Unit) {
        onDeletePostClickListener = listener
    }


}
























































