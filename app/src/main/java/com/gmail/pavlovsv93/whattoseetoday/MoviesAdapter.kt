package com.gmail.pavlovsv93.whattoseetoday

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.gmail.pavlovsv93.whattoseetoday.model.Movie

class MoviesAdapter : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {
    interface OnClickCard{
        fun onClick(movie: Movie)
    }
    private var moviesList: MutableList<Movie> = mutableListOf()

    fun setMovie(data: MutableList<Movie>?) {
        if (data != null) {
            moviesList = data
        }
        notifyDataSetChanged()
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
//            itemView.findViewById<RatingBar>(R.id.item_rating_bar).rating(movie.rating)
//            if (movie.poster != null) {
//                itemView.findViewById<ImageView>(R.id.item_image).setImageDrawable(movie.poster)
//            }else{
//                itemView.findViewById<ImageView>(R.id.item_image).setImageDrawable(R.drawable.ic_baseline_image_not_supported_24.toDrawable())
//            }
            itemView.findViewById<TextView>(R.id.item_text_titel).text = movie.name
            itemView.setOnClickListener { View ->

                //обработка нажатия на карту фильма
                Toast.makeText(itemView.context, movie.description, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

