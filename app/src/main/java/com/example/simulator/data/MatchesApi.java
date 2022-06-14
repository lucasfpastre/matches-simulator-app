package com.example.simulator.data

import com.example.simulator.domain.Match
import retrofit2.Call
import retrofit2.http.GET

interface MatchesApi {
    @get:GET("matches.json")
    val matches: Call<List<Match?>?>?
}