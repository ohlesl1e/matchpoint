package com.lesliezhou.matchpoint.casual

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.lesliezhou.matchpoint.R

class CardsAdapter : RecyclerView.Adapter<CardsAdapter.ViewHolder>() {
    private var index = 0
    var data = listOf<ProfileCards>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var profilePic: ImageView
        var title: TextView
        var nextPic: View

        init {
            profilePic = itemView.findViewById(R.id.profilePicture)
            title = itemView.findViewById(R.id.nameAndAge)
            nextPic = itemView.findViewById(R.id.nextPic)

            nextPic.setOnClickListener {
                if ((index + 1) == data[adapterPosition].pics.value?.size) {
                    index = 0
                } else {
                    index++
                }
                if (data[adapterPosition].pics.value?.isEmpty() == false) {
                    Glide.with(itemView.context).load(data[adapterPosition].pics.value?.get(index))
                        .centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL).into(profilePic)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsAdapter.ViewHolder {
        val inflater =
            LayoutInflater.from(parent.context).inflate(R.layout.profile_card, parent, false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: CardsAdapter.ViewHolder, position: Int) {
        if (data[position].pics.value?.isEmpty() == false) {
            Glide.with(holder.itemView).load(data[position].pics.value?.get(index)).centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.profilePic)
        }
        holder.title.text = "${data[position].name.value}, ${data[position].age.value}"
    }

    override fun getItemCount(): Int {
        return data.size
    }
}