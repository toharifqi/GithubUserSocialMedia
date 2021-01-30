package com.dicoding.naufal.myfavoritegithubusers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.naufal.myfavoritegithubusers.R
import com.dicoding.naufal.myfavoritegithubusers.databinding.ItemUserBinding
import com.dicoding.naufal.myfavoritegithubusers.model.UserModel

class UserAdapter: RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    val mData = ArrayList<UserModel>()
    private var onItemClickCallBack: OnItemClickCallBack? = null

    fun setOnItemClickCallback(onItemClickCallBack: OnItemClickCallBack){
        this.onItemClickCallBack = onItemClickCallBack
    }

    interface OnItemClickCallBack {
        fun onItemClicked(userModel: UserModel?, position: Int)
    }

    fun setData(items: ArrayList<UserModel>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = mData.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    inner class ViewHolder(private val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: UserModel){
            with(binding){
                txtUsername.text = user.username
                txtIduser.text = itemView.resources.getString(R.string.user_id, user.userId)
                Glide.with(itemView.context).load(user.avatar)
                    .apply(RequestOptions().override(65, 65))
                    .into(userPhoto)
                itemView.setOnClickListener{
                    onItemClickCallBack?.onItemClicked(user, position)
                }
            }
        }
    }

    fun addItem(user: UserModel){
        this.mData.add(user)
        notifyItemInserted(this.mData.size - 1)
    }

    fun removeItem(position: Int){
        this.mData.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.mData.size)
    }
}