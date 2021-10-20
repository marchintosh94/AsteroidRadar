package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.utils.Enums
import com.udacity.asteroidradar.viewmodels.MainViewModel

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        // viewModel variable is available only after onViewCreated
        ViewModelProvider(this, MainViewModel.MainViewModelFactory(requireActivity().application)).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        // Loader handler
        viewModel.loader.observe(viewLifecycleOwner){ status ->
            status?.let {
                binding.statusLoadingWheel.visibility = if (status == Enums.LoaderStatus.LOADING) View.VISIBLE else View.GONE
            }
        }

        // Network error handler
        viewModel.networkError.observe(viewLifecycleOwner){ error ->
            error?.let {
                if (error !== ""){
                    Snackbar.make(binding.root, error, Snackbar.LENGTH_LONG).show()
                }
            }
        }

        //init the adapter
        val adapter = AsteroidsAdapter(AsteroidsAdapter.OnClickCallback{
            viewModel.navigateAsteroidDetails(it)
        })


        // Observe the navigateToItemDetail LiveData and Navigate when it is not null
        viewModel.navigateToItemDetail.observe(viewLifecycleOwner){
            it?.let {
                //Navigate to detail fragment
                findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
                viewModel.navigateAsteroidDetailsComplete()
            }
        }

        viewModel.asteroids.observe(viewLifecycleOwner){
            binding.noResults.noResultsView.visibility = if (it.size <= 0) View.VISIBLE else View.GONE
            it?.let {
                adapter.submitList(it)
            }
        }

        //bind adapter
        binding.asteroidRecycler.adapter = adapter


        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.filterAsteroidList(
            when(item.itemId){
                R.id.show_week_menu -> Enums.AsteroidFilter.WEEK
                R.id.show_today_menu -> Enums.AsteroidFilter.TODAY
                else -> Enums.AsteroidFilter.ALL
            }
        )
        return true
    }
}
