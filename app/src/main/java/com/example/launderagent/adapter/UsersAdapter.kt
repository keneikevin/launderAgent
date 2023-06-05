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
import com.example.agent.databinding.UserBinding
import com.example.launderagent.data.entities.Service
import com.example.launderagent.data.entities.User
import com.example.launderagent.ui.home.customer.UsersFragmentDirections
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class UsersAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<UsersAdapter.PostViewHolder>() {

    class PostViewHolder(val binding: UserBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivPostImage: ImageView = binding.img
        val tvPostAuthor: TextView = binding.textName
        val tvPostText: TextView = binding.per2
        val cad = binding.cad
    }

    private val diffCallback = object : DiffUtil.ItemCallback<User>() {
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uid == newItem.uid
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var posts: List<User>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            UserBinding.inflate(
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
        val post = posts[position]
        holder.apply {
            glide.load(post.profilePictureUrl).into(ivPostImage)
            tvPostAuthor.text = post.username
            tvPostText.text = post.time
            cad.setOnClickListener {
                Snackbar.make(this.itemView, "Swipe ..", Snackbar.LENGTH_SHORT).show()
        //        val directions= CustomersServiceFragmentDirections.actionCustomersServiceFragmentToDetailFragment(post)
                val directions= UsersFragmentDirections.actionUsersFragmentToCustomersServiceFragment(post)
                it.findNavController().navigate(directions)

            }

        }
    }

    private var onDeletePostClickListener: ((Service) -> Unit)? = null



    fun setOnDeletePostClickListener(listener: (Service) -> Unit) {
        onDeletePostClickListener = listener
    }


}
























































