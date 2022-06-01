package com.gmail.pavlovsv93.whattoseetoday.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.gmail.pavlovsv93.whattoseetoday.R
import com.gmail.pavlovsv93.whattoseetoday.model.Movie
import com.gmail.pavlovsv93.whattoseetoday.view.fragment.navigview.JournalFragment


class JournalAdapter(private var onClickCard: JournalFragment.OnClickJournal?) : RecyclerView.Adapter<JournalAdapter.JournalViewHolder>() {

    private var journalList: MutableList<Movie> = mutableListOf()

    fun setJournal(data: MutableList<Movie>?){
        if (data != null) {
            journalList = data
        }
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JournalViewHolder {
        return JournalViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_journal, parent, false)
                        as View
        )
    }

    override fun onBindViewHolder(holder: JournalViewHolder, position: Int) {
        holder.bind(journalList[position])
    }

    override fun getItemCount(): Int = journalList.size

    inner class JournalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            itemView.findViewById<CardView>(R.id.card_journal).setOnClickListener {
                onClickCard?.onClickCardJournal(movie)
            }
            itemView.findViewById<TextView>(R.id.textview_journal).text = (movie.name + " (" + movie.date?.dropLast(6) + ")")
        }

    }
}