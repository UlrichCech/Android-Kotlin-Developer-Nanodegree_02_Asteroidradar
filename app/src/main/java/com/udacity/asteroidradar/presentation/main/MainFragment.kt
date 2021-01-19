package com.udacity.asteroidradar.presentation.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.business.asteroids.control.ShowAllAsteroidFilter
import com.udacity.asteroidradar.business.asteroids.control.ShowTodayAsteroidFilter
import com.udacity.asteroidradar.business.asteroids.control.ShowWeekAsteroidFilter
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "viewModel only accessible after onViewCreated()"
        }
        ViewModelProvider(this, MainViewModel.Factory(activity.application)).get(MainViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupNavigationToAsteroidDetails()

        setupRecyclerView(binding)

        setupPictureOfDayContentDescription(binding)

        setHasOptionsMenu(true)

        return binding.root
    }


    private fun setupRecyclerView(binding: FragmentMainBinding) {
        val adapter = AsteroidListAdapter(AsteroidListClickListener { asteroid ->
            viewModel.displayAsteroidDetails(asteroid)
        })
        binding.asteroidRecycler.adapter = adapter
        viewModel.asteroids.observe(viewLifecycleOwner, {
            it?.let {
                adapter.submitList(it)
            }
        })
    }

    private fun setupNavigationToAsteroidDetails() {
        viewModel.navigateToAsteroidDetails.observe(viewLifecycleOwner, { asteroid ->
            asteroid?.let {
                this.findNavController().navigate(
                    MainFragmentDirections
                        .actionShowDetail(asteroid)
                )
                viewModel.doneNavigatingToAsteroidDetails()
            }
        })
    }

    private fun setupPictureOfDayContentDescription(binding: FragmentMainBinding) {
        viewModel.pod.observe(viewLifecycleOwner, {
            it?.let {
                binding.activityMainImageOfTheDay.contentDescription =
                    String.format(
                        context?.resources?.getString(R.string.nasa_picture_of_day_content_description_format)!!,
                        it.title
                    )
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_today_menu -> viewModel.setFilter(ShowTodayAsteroidFilter)
            R.id.show_week_menu -> viewModel.setFilter(ShowWeekAsteroidFilter)
            else -> viewModel.setFilter(ShowAllAsteroidFilter)
        }
        return true
    }

}
