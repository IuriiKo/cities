package com.kushyk.test.cities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kushyk.test.R
import com.kushyk.test.cities.adapter.CitiesAdapter
import com.kushyk.test.cities.adapter.ViewHolderAction
import com.kushyk.test.cities.viewmodel.CitiesViewModel
import com.kushyk.test.cities.viewmodel.CitiesViewModel.ViewAction
import com.kushyk.test.cities.viewmodel.CitiesViewModel.ViewState
import com.kushyk.test.cities.viewmodel.CityModel
import com.kushyk.test.databinding.CitiesFragmentBinding
import com.kushyk.test.utils.view.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlin.time.ExperimentalTime

@FlowPreview
@ExperimentalTime
@AndroidEntryPoint
class CitiesFragment : Fragment() {
    private val viewModel: CitiesViewModel by viewModels()
    private var binding: CitiesFragmentBinding? = null
    private var adapter: CitiesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeViewState()
        observeViewAction()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CitiesFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSearchView()
        setupRecyclerView()
    }

    private fun setupSearchView() {
        binding?.apply {
            searchView.isIconified = false
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.onTextChange(newText)
                    return true
                }
            })
        }
    }

    private fun setupRecyclerView() {
        binding?.apply {
            adapter = CitiesAdapter()
            lifecycleScope.launchWhenCreated {
                adapter?.actionFlow?.collect {
                    when (it) {
                        is ViewHolderAction.Click -> {
                            viewModel.onClickItem(it.model)
                        }
                    }
                }
            }
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL,
                false
            )
            recyclerView.addItemDecoration(
                DividerItemDecoration(
                    requireContext().resources.getDimensionPixelSize(
                        R.dimen.medium
                    )
                )
            )
        }
    }

    private fun observeViewState() {
        lifecycleScope.launchWhenStarted {
            viewModel.viewState.collect {
                when (it) {
                    ViewState.StartSearch -> enableStartSearchState()
                    ViewState.NotFound -> enableNotFoundState()
                    is ViewState.Data -> enableDataState(it.cities)
                }
            }
        }
    }

    private fun observeViewAction() {
        lifecycleScope.launchWhenStarted {
            viewModel.viewAction.collect {
                when (it) {
                    is ViewAction.ShowLocation -> showLocation(it)
                }
            }
        }
    }

    private fun showLocation(location: ViewAction.ShowLocation) {
        val gmmIntentUri: Uri = Uri.parse("geo:${location.latitude},${location.longitude}")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(mapIntent)
        } else {
            showMessageLocationAppInMissing()
        }
    }

    private fun showMessageLocationAppInMissing() {
        Toast.makeText(
            requireContext(),
            R.string.location_app_is_missing,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun enableDataState(cities: List<CityModel>) {
        adapter?.submitList(cities)
        showDataLayout()
    }

    private fun enableNotFoundState() {
        binding?.apply {
            notificationIconView.setImageResource(R.drawable.ic_baseline_error_24)
            notificationDescriptionView.setText(R.string.not_found_description)
            adapter?.submitList(emptyList())
        }
        showNotificationLayout()
    }

    private fun enableStartSearchState() {
        binding?.apply {
            notificationIconView.setImageResource(R.drawable.ic_baseline_search_24)
            notificationDescriptionView.setText(R.string.start_search_description)
            adapter?.submitList(emptyList())
        }
        showNotificationLayout()
    }

    private fun showDataLayout() {
        binding?.apply {
            recyclerView.visibility = View.VISIBLE
            notificationGroup.visibility = View.INVISIBLE
        }
    }

    private fun showNotificationLayout() {
        binding?.apply {
            recyclerView.visibility = View.INVISIBLE
            notificationGroup.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}