package br.com.igorbag.githubsearch.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import br.com.igorbag.githubsearch.R
import br.com.igorbag.githubsearch.data.GitHubService
import br.com.igorbag.githubsearch.databinding.ActivityMainBinding
import br.com.igorbag.githubsearch.domain.Repository
import br.com.igorbag.githubsearch.ui.adapter.RepositoryAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val sharedPref by lazy {
        this.getSharedPreferences(
            getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
    }
    private val retrofitService by lazy { GitHubService.getInstance() }
    private lateinit var adapter: RepositoryAdapter

    companion object {
         val USERNAME_KEY = "userName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupListeners()
        showUserName()
    }

    private fun setupListeners() {
        binding.btnConfirmar.setOnClickListener {
            saveUserLocal()
        }
    }

    private fun saveUserLocal() {
        sharedPref.edit().apply {
            putString(USERNAME_KEY, binding.etNomeUsuario.text.toString())
            apply()
        }
        showUserName()
    }

    private fun showUserName() {
        val userName = sharedPref.getString(USERNAME_KEY, null)

        if (userName != null) {
            binding.etNomeUsuario.setText(userName)
            getAllReposByUserName(userName)
        }
    }


    //Metodo responsavel por buscar todos os repositorios do usuario fornecido
    fun getAllReposByUserName(userName: String) {
        val response = retrofitService.getAllRepositoriesByUser(userName)

        response.enqueue(object : Callback<List<Repository>> {
            override fun onResponse(
                call: Call<List<Repository>>,
                response: Response<List<Repository>>
            ) {
                setupAdapter(response.body())
            }
            override fun onFailure(call: Call<List<Repository>>, t: Throwable) {
                println(t.message)
            }
        })

    }

    // Metodo responsavel por realizar a configuracao do adapter
    fun setupAdapter(list: List<Repository>?) {

        if (list != null) {
            adapter = RepositoryAdapter(list)
            binding.rvListaRepositories.adapter = adapter

            adapter.btnShareLister = {
                shareRepositoryLink(it.htmlUrl)
            }
            adapter.carItemLister = {
                openBrowser(it.htmlUrl)
            }
        } else {
           Toast.makeText(this, getString(R.string.erro_requisicao_retrofit), Toast.LENGTH_LONG).show()
    }
}

    private fun shareRepositoryLink(urlRepository: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, urlRepository)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    // Metodo responsavel por abrir o browser com o link informado do repositorio

    // @Todo 12 - Colocar esse metodo no click item do adapter
    fun openBrowser(urlRepository: String) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(urlRepository)
            )
        )

    }

}