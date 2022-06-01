package com.gmail.pavlovsv93.whattoseetoday.viewmodel

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.model.DTO.KnownFor
import com.gmail.pavlovsv93.whattoseetoday.model.DTO.ResultDTO
import com.gmail.pavlovsv93.whattoseetoday.utils.BASE_URL_IMAGE_STARS
import com.gmail.pavlovsv93.whattoseetoday.view.fragment.navigview.StarsFragment
import com.squareup.picasso.Picasso

class StarsAdapter(private var onClickToStar:StarsFragment.OnClickToStar) :
    RecyclerView.Adapter<StarsAdapter.StarViewHolder>() {
    private var starsList: MutableList<ResultDTO> = mutableListOf()

    fun setStarsList(data: MutableList<ResultDTO>?) {
        if (data != null) {
            starsList.addAll(data)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StarViewHolder {
        return StarViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_star, parent, false) as View
        )
    }

    override fun onBindViewHolder(holder: StarViewHolder, position: Int) {
        holder.bind(starsList[position])
    }

    override fun getItemCount(): Int {
        return starsList.size
    }
    inner class StarViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(result: ResultDTO) {
            val card = itemView.findViewById<CardView>(R.id.star_card)
            val image = itemView.findViewById<ImageView>(R.id.star_card_image)
            val name = itemView.findViewById<TextView>(R.id.star_card_name)
            val text = itemView.findViewById<TextView>(R.id.star_card_text)
            Picasso.with(itemView.context)
                .load(BASE_URL_IMAGE_STARS + result.profile_path)
                .centerCrop()
                .resize(600, 1000)
                .placeholder(R.drawable.ic_twotone_stars_150)
                .into(image)
            name.text = result.name
            text.text = ("Рейтинг: "+result.popularity.toString())

            card.setOnClickListener {
                onClickToStar.onClickToStar(result)
            }

        }
    }


}