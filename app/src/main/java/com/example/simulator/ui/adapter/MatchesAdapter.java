package com.example.simulator.ui.adapter

import com.example.simulator.domain.Match.homeTeam
import com.example.simulator.domain.Team.image
import com.example.simulator.domain.Team.name
import com.example.simulator.domain.Team.score
import com.example.simulator.domain.Match.awayTeam
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import android.content.Intent
import android.view.View
import com.example.simulator.databinding.MatchItemBinding
import com.example.simulator.domain.Match
import com.example.simulator.ui.DetailActivity

class MatchesAdapter(val matches: List<Match>) : RecyclerView.Adapter<MatchesAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MatchItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val match = matches[position]

        // Adapta os dados da partida (recuperada da API) para o nosso layout
        Glide.with(context).load(match.homeTeam.image).circleCrop().into(holder.binding.ivHomeTeam)
        holder.binding.tvHomeTeamName.text = match.homeTeam.name
        if (match.homeTeam.score != null) {
            holder.binding.tvHomeTeamScore.text = match.homeTeam.score.toString()
        }
        Glide.with(context).load(match.awayTeam.image).circleCrop().into(holder.binding.ivAwayTeam)
        holder.binding.tvAwayTeamName.text = match.awayTeam.name
        if (match.awayTeam.score != null) {
            holder.binding.tvAwayTeamScore.text = match.awayTeam.score.toString()
        }
        holder.itemView.setOnClickListener { view: View? ->
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.Extras.MATCH, match)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return matches.size
    }

    class ViewHolder(private val binding: MatchItemBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}