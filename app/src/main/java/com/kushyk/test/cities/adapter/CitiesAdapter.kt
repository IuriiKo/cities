package com.kushyk.test.cities.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kushyk.test.cities.viewmodel.CityModel
import com.kushyk.test.databinding.CityItemBinding
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class CitiesAdapter : RecyclerView.Adapter<CityViewHolder>() {
    private var items: List<CityModel> = emptyList()
    private val _actionFlow = MutableSharedFlow<ViewHolderAction>(extraBufferCapacity = 1)
    val actionFlow = _actionFlow.asSharedFlow()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder =
        CityViewHolder(
            CityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            _actionFlow
        )

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(items: List<CityModel>) {
        this.items = items
        notifyDataSetChanged()
    }
}