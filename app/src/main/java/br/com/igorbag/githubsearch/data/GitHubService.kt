package br.com.igorbag.githubsearch.data

import br.com.igorbag.githubsearch.domain.Repository
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface GitHubService {

    @GET("users/{user}/repos")
    fun getAllRepositoriesByUser(@Path("user") user: String): Call<List<Repository>>

    companion object {

        private val retrofitService: GitHubService by lazy {

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(GitHubService::class.java)

        }

        fun getInstance() : GitHubService {
            return retrofitService
        }
    }

}
