package com.akdogan.swcharacters.ui.detail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.akdogan.swcharacters.R
import com.akdogan.swcharacters.datasource.DomainGotCharacter

class DetailAdapter : RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {

    private var dataSet : List<DetailEntity> = emptyList()

    fun submitData(character: DomainGotCharacter){
        val tempList = mutableListOf<DetailEntity>()

        fun addIfNotEmpty(title: String, values: List<String>){
            if (values.isNotEmpty() && !values[0].isNullOrBlank()){
                tempList.add(DetailEntity(title, values))
            }
        }
        addIfNotEmpty("Name", listOf(character.name))
        addIfNotEmpty("Gender", listOf(character.gender))
        addIfNotEmpty("Culture", listOf(character.culture))
        addIfNotEmpty("Born", listOf(character.born))
        addIfNotEmpty("Titles", character.titles)
        addIfNotEmpty("Aliases", character.aliases)
        addIfNotEmpty("Father", listOf(character.father))
        addIfNotEmpty("Mother", listOf(character.mother))
        addIfNotEmpty("Spouse", listOf(character.spouse))
        dataSet = tempList
        notifyDataSetChanged()
        Log.i("DETAIL_VIEW_TRACING", "dataset: $dataSet")
    }

    // Wrapper Class
    data class DetailEntity(
        val title: String,
        val values: List<String>,
        val expandable: Boolean = values.size > 1
    )

    class DetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title = view.findViewById<TextView>(R.id.detail_list_title_text)
        val subText = view.findViewById<TextView>(R.id.detail_list_item_secondary_text)

        fun bind(item: DetailEntity){
            title.text = item.title
            subText.text = item.values.firstOrNull() ?: ""
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.detail_list_view_item,
            parent,
            false
        )
        return DetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(dataSet[position])
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}

