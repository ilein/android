package com.pg.homeworknetwork

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class MovieListFragment : Fragment(R.layout.fragment_movie_list) {
    lateinit var recycler: RecyclerView

    private val context: CoroutineContext = SupervisorJob() + Dispatchers.Main
    private val scope = CoroutineScope(context)

    private val goToDetails = object : MovieItemAdapter.IOnItemClick {
        override fun onItemClick(movie: Movie) {
            val manager: FragmentManager = parentFragmentManager
            val transaction: FragmentTransaction = manager.beginTransaction()
            val detailsFragment = MovieDetailFragment()
            detailsFragment.arguments = Bundle().apply { putInt(MovieDetailFragment.ARG_ID, movie.id) }
            transaction.replace(R.id.mainFragment, detailsFragment)
            transaction.commit()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recycler = view.findViewById<RecyclerView?>(R.id.recycler).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = MovieItemAdapter().apply {
                setClickListener(goToDetails)
            }
        }
        val adapter = (recycler.adapter as MovieItemAdapter)
        scope.launch (Dispatchers.IO) {
            val movies: Movies = Api().getMovies()
            adapter.submitList(movies.results)
        }
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        const val TAG = "MovieListFragment"
    }
}