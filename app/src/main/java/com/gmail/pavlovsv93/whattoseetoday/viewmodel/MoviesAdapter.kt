package com.gmail.pavlovsv93.whattoseetoday.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.model.Movie
import com.gmail.pavlovsv93.whattoseetoday.model.db.AppDB
import com.gmail.pavlovsv93.whattoseetoday.model.db.MoviesEntity
import com.gmail.pavlovsv93.whattoseetoday.model.repo.RoomInterfaceRepository
import com.gmail.pavlovsv93.whattoseetoday.model.repo.RoomRepository
import com.gmail.pavlovsv93.whattoseetoday.view.BASE_URL_IMAGE
import com.gmail.pavlovsv93.whattoseetoday.view.WhatToSeeActivity
import com.squareup.picasso.Picasso

class MoviesAdapter(private var onClickItem: WhatToSeeActivity.OnClickItem) :
    RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    private var moviesList: MutableList<Movie> = mutableListOf()
    private val repoDB: RoomInterfaceRepository = RoomRepository(AppDB.getMoviesDAO())

    fun setMovie(data: MutableList<Movie>?) {
        if (data != null) {
            moviesList = data
        }
        notifyDataSetChanged()
    }

    fun updateItem(movie: Movie): Int {
        var index = -1
        for (i in 0 until moviesList.size) {
            if (moviesList[i].id == movie.id) {
                index = i
                break
            }
        }
        return index
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_movie, parent, false)
                    as View
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(moviesList[position])
    }

    override fun getItemCount(): Int = moviesList.size

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(movie: Movie) {
            itemView.findViewById<RatingBar>(R.id.item_rating_bar).rating = movie.rating!!.toFloat()
            itemView.findViewById<TextView>(R.id.text_rating).text = movie.rating.toString()
            Picasso.with(itemView.context)
                .load(BASE_URL_IMAGE + movie.poster)
                .centerCrop()
                .resize(600, 1000)
                .placeholder(R.drawable.ic_baseline_image_not_supported_24)
                .into(itemView.findViewById<ImageView>(R.id.item_image))
            itemView.findViewById<TextView>(R.id.item_text_titel).text =
                (movie.name + " (" + movie.date?.dropLast(6) + ")")
            val favorite = itemView.findViewById<ImageButton>(R.id.image_btn_favorite)
            if (findInTheDB(id = movie.id)) {
                favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
            } else {
                favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
            }
            itemView.setOnClickListener {
                onClickItem?.onClick(movie = movie)
            }
            favorite.setOnClickListener {
                onClickItem?.onClickFavorite(movie = movie)
            }
        }
    }

    private fun findInTheDB(id: Int): Boolean = repoDB.findItem(id)


}


