package br.com.igorbag.githubsearch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.igorbag.githubsearch.databinding.RepositoryItemBinding
import br.com.igorbag.githubsearch.domain.Repository

class RepositoryAdapter(private val repositories: List<Repository>) :
    RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {

    var carItemLister: (Repository) -> Unit = {}
    var btnShareLister: (Repository) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RepositoryItemBinding.inflate(inflater, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repository = repositories[position]
        holder.bind(repository, carItemLister, btnShareLister)
    }

    override fun getItemCount(): Int = repositories.count()

    class ViewHolder(private val binding: RepositoryItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            repository: Repository,
            carItemLister: (Repository) -> Unit,
            btnShareLister: (Repository) -> Unit
        ) {
            binding.tvRepositoryName.text = repository.name

            binding.ivFavorite.setOnClickListener {
                btnShareLister(repository)
            }
            binding.clCardContent.setOnClickListener {
                carItemLister(repository)
            }
        }
    }
}


