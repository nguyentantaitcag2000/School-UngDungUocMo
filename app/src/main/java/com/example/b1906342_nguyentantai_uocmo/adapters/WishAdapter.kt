package com.example.b1906342_nguyentantai_uocmo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.b1906342_nguyentantai_uocmo.R
import com.example.b1906342_nguyentantai_uocmo.databinding.ItemWishBinding
import com.example.b1906342_nguyentantai_uocmo.models.Wish
import com.example.b1906342_nguyentantai_uocmo.sharedpreferences.AppSharedPreferences

class WishAdapter (
    private val context: Context,
    private val wishList: List<Wish>,// Ban đầu là List
    private val appSharedPreferences: AppSharedPreferences,
    private val iClickItemWish: IClickItemWish
): RecyclerView.Adapter<WishAdapter.WishViewHolder>() // Ban đầu là RecyclerView.Adapter()
{
    class WishViewHolder(val binding: ItemWishBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishViewHolder{
        return WishViewHolder(ItemWishBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int{
        return wishList.size
    }

    override fun onBindViewHolder(holder: WishViewHolder, position: Int){
//        val wish: Wish =  wishList(position)
        val wish: Wish =  wishList[position]
        holder.binding.tvName.text = wish.name
        holder.binding.tvContent.text = wish.content
        Glide.with(context).load(wish.owner.avatar)
            .error(R.drawable.icon)
            .into(holder.binding.imgAvatar)

        if(appSharedPreferences.getIdUser("idUser").toString() == wish.owner._id){
            holder.binding.imgUpdate.visibility = View.VISIBLE
            holder.binding.imgDelete.visibility = View.VISIBLE
        }

        holder.binding.imgDelete.setOnClickListener{
            iClickItemWish.onClickRemove(wish._id)
        }

        holder.binding.imgUpdate.setOnClickListener{
            iClickItemWish.onClickUpdate(wish._id,wish.name,wish.content)
        }
    }

    interface IClickItemWish {
        fun onClickUpdate(idWish: String, fullName: String, content: String)
        fun onClickRemove(idWish: String)
    }
}

