package com.kushyk.test.cities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kushyk.test.R
import com.kushyk.test.databinding.CitiesFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CitiesFragment : Fragment() {
    private val viewModel: CitiesViewModel by viewModels()
    private var binding: CitiesFragmentBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CitiesFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}