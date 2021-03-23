package com.akdogan.swcharacters.ui.main

import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akdogan.swcharacters.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.prefs.PreferenceChangeEvent
import java.util.prefs.PreferenceChangeListener

class MainFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val viewModel: MainViewModel by viewModels<MainViewModel>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyRecViewAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        recyclerView = view.findViewById(R.id.my_rec_view)
        setupRecyclerViewWithSettings()
        registerPrefListener()
        return view
    }

    override fun onDestroy() {
        unregisterPrefListener()
        super.onDestroy()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MyRecViewAdapter(viewModel::invokeItemClicked)
        //recyclerView = view.findViewById()
        //
        recyclerView.adapter = adapter

        viewModel.navigateWithUrl.observe(viewLifecycleOwner){
            it?.let{
                goToDetailView(it)
                viewModel.onNavigateDone()
            }
        }

        viewModel.characterList.observe(viewLifecycleOwner){ list ->
            adapter.dataset = list
        }

        view.findViewById<FloatingActionButton>(R.id.reload_fab).setOnClickListener {
            viewModel.getCharacters()
        }
    }

    fun registerPrefListener(){
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .registerOnSharedPreferenceChangeListener(this)
    }

    fun unregisterPrefListener(){
        PreferenceManager.getDefaultSharedPreferences(requireContext())
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun setupRecyclerViewWithSettings(){
        val prefMan = PreferenceManager.getDefaultSharedPreferences(requireActivity().application)
        val useList = prefMan.getBoolean(getString(R.string.preference_show_grid), false)
        if (!useList) {
            recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        } else {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    fun goToDetailView(url: String){
        val action = MainFragmentDirections.actionMainFragmentToDetailFragment(url)
        findNavController().navigate(action)
    }



    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        //val prefKey = getString(R.string.preference_show_grid)
        Log.i("PREFERENCE_CHANGE", "got called with ${key}")
        when (key) {
            getString(R.string.preference_show_grid) -> setupRecyclerViewWithSettings()
            getString(R.string.preference_text_color) -> refreshRecyclerView()
        }
    }

    private fun refreshRecyclerView() {
        adapter.notifyDataSetChanged()
    }

}