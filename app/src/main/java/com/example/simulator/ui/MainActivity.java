package com.example.simulator.ui

import android.animation.Animator
import com.example.simulator.domain.Match.homeTeam
import com.example.simulator.domain.Team.score
import com.example.simulator.domain.Team.stars
import com.example.simulator.domain.Match.awayTeam
import androidx.appcompat.app.AppCompatActivity
import com.example.simulator.data.MatchesApi
import com.example.simulator.ui.adapter.MatchesAdapter
import android.os.Bundle
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import android.animation.AnimatorListenerAdapter
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.example.simulator.R
import com.example.simulator.databinding.ActivityMainBinding
import com.example.simulator.domain.Match
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    private var matchesApi: MatchesApi? = null
    private var matchesAdapter: MatchesAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setupHttpClint()
        setupMatchesList()
        setupMatchesRefresh()
        setupFloatingActionButton()
    }

    private fun setupHttpClint() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://lucasfpastre.github.io/matches-simulator-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        matchesApi = retrofit.create(MatchesApi::class.java)
    }

    private fun setupMatchesList() {
        binding!!.rvMatches.setHasFixedSize(true)
        binding!!.rvMatches.layoutManager = LinearLayoutManager(this)
        findMatchesFromApi()
    }

    private fun setupMatchesRefresh() {
        binding!!.srlMatches.setOnRefreshListener { findMatchesFromApi() }
    }

    private fun setupFloatingActionButton() {
        binding!!.fabSimulate.setOnClickListener { view: View ->
            view.animate().rotationBy(360f).setDuration(500)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        val random = Random()
                        for (i in 0 until matchesAdapter!!.itemCount) {
                            val (_, _, homeTeam, awayTeam) = matchesAdapter!!.matches[i]
                            homeTeam.score = random.nextInt(homeTeam.stars + 1)
                            awayTeam.score = random.nextInt(awayTeam.stars + 1)
                            matchesAdapter!!.notifyItemChanged(i)
                        }
                    }
                })
        }
    }

    private fun showErrorMessage() {
        Snackbar.make(binding!!.fabSimulate, R.string.error_api, Snackbar.LENGTH_LONG).show()
    }

    private fun findMatchesFromApi() {
        binding!!.srlMatches.isRefreshing = true
        matchesApi!!.matches.enqueue(object : Callback<List<Match?>?> {
            override fun onResponse(call: Call<List<Match?>?>, response: Response<List<Match?>?>) {
                if (response.isSuccessful) {
                    val matches = response.body()
                    matchesAdapter = MatchesAdapter(matches)
                    binding!!.rvMatches.adapter = matchesAdapter
                } else {
                    showErrorMessage()
                }
                binding!!.srlMatches.isRefreshing = false
            }

            override fun onFailure(call: Call<List<Match?>?>, t: Throwable) {
                showErrorMessage()
                binding!!.srlMatches.isRefreshing = false
            }
        })
    }
}