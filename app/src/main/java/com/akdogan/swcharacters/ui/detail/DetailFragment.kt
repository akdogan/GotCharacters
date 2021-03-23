package com.akdogan.swcharacters.ui.detail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.akdogan.swcharacters.R
import com.akdogan.swcharacters.datasource.DomainGotCharacter

class DetailFragment : Fragment() {

    val args: DetailFragmentArgs by navArgs()
    lateinit var adapter : DetailAdapter

    private val viewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = DetailAdapter()
        view.findViewById<RecyclerView>(R.id.detail_rec_view).adapter = adapter

        viewModel.getItem(args.url)

        viewModel.character.observe(viewLifecycleOwner){ item: DomainGotCharacter? ->
            Log.i("DETAIL_VIEW_TRACING", "observer: $item")
            item?.let {
                adapter.submitData(it)
            }

        }
    }



}