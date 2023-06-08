package com.example.launderagent.adapterpackage

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.agent.databinding.OrderBinding
import com.example.launderagent.data.entities.Order
import com.example.launderagent.ui.auth.OrdersFragmentDirections
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class OrdersAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<OrdersAdapter.PostViewHolder>() {

    class PostViewHolder(val binding: OrderBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvPostAuthor: TextView = binding.textName
        val bt: Button = binding.img
        val tvPostText: TextView = binding.per2
        val cad = binding.cad
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Order>() {
        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.oderUid == newItem.oderUid
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var posts: List<Order>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(
            OrderBinding.inflate(
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
          //  glide.load(post.profilePictureUrl).into(ivPostImage)
            tvPostAuthor.text = post.code

            var tt = "${post.bookTime}   |   3 items   |   ${post.price}"
            when (post.status) {
                "Pending" -> {
                    binding.img.setBackgroundColor(Color.parseColor("#0000FF"))
                }

                "Accepted" -> {
                binding.img.setBackgroundColor(Color.parseColor("#800000"))
            }
                "Processing" -> {
                binding.img.setBackgroundColor(Color.parseColor("#808080"))
            }
                "Complete" -> {
                    binding.img.setBackgroundColor(Color.parseColor("#006400"))}
                else ->  {
                    binding.img.setBackgroundColor(Color.RED)
            }
            }
            tvPostText.text = tt
           binding.img.text = post.status
            cad.setOnClickListener {
                Snackbar.make(this.itemView, "Swipe ..", Snackbar.LENGTH_SHORT).show()
                val directions = OrdersFragmentDirections.actionOrdersFragmentToEditOrderFragment(post)
             //   val directions= UsersFragmentDirections.actionUsersFragmentToCustomersServiceFragment(post)
                it.findNavController().navigate(directions)
               //     it.findNavController().navigate(R.id.action_ordersFragment_to_editOrderFragment)
            }

        }
    }



}
























































