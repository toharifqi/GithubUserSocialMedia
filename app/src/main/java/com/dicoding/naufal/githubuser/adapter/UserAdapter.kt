package com.dicoding.naufal.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.naufal.githubuser.R
import com.dicoding.naufal.githubuser.databinding.ItemUserBinding
import com.dicoding.naufal.githubuser.model.UserModel

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
                userPhoto.loadImage(user.avatar)
                itemView.setOnClickListener{
                    onItemClickCallBack?.onItemClicked(user, position)
                }
            }
        }
    }

    fun ImageView.loadImage(url: String?){
        Glide.with(this.context)
            .load(url)
            .placeholder(R.drawable.ic_baseline_person_white)
            .error(R.drawable.ic_baseline_broken_image_white)
            .apply(RequestOptions().override(65, 65))
            .into(this)
    }
}