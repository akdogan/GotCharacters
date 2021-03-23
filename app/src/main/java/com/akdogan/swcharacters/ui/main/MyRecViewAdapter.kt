package com.akdogan.swcharacters.ui.main

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.getColor
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.akdogan.swcharacters.R
import com.akdogan.swcharacters.datasource.DomainGotCharacter

class MyRecViewAdapter(
    val clickListener: (url: String) -> Unit
) : RecyclerView.Adapter<MyRecViewAdapter.MyRecViewHolder>() {

    var dataset = emptyList<DomainGotCharacter>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class MyRecViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val cardView = itemView.findViewById<CardView>(R.id.list_item_cardview)
        val nameView = itemView.findViewById<TextView>(R.id.list_item_name)
        val aliasesView = itemView.findViewById<TextView>(R.id.list_item_aliases)
        val titlesView = itemView.findViewById<TextView>(R.id.list_item_titles)
        val bornView = itemView.findViewById<TextView>(R.id.list_item_born)
        val diedView = itemView.findViewById<TextView>(R.id.list_item_died)

        fun onBind(item: DomainGotCharacter){
            nameView.text = item.name
            nameView.setColorWithPreference()
            aliasesView.text = item.aliases.firstOrNull() ?: "NODATA"
            titlesView.text = item.titles.firstOrNull() ?: "NODATA"
            bornView.text = "Born: ${item.born}"
            diedView.text = "Died: ${item.died}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_view_item, parent, false)
        return MyRecViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyRecViewHolder, position: Int) {
        val item = dataset[position]
        holder.cardView.setOnClickListener {
            clickListener(item.url)
        }
        holder.onBind(item)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}

private fun TextView.setColorWithPreference(){
    val resources = context.resources
    @ColorInt
    fun resolvePreferenceColor(str: String): Int{
        return with (resources){
            when (str) {
                getString(R.string.preference_color_values_red) -> getColor(R.color.pref_red, null)
                getString(R.string.preference_color_values_blue) -> getColor(R.color.pref_blue, null)
                getString(R.string.preference_color_values_orange) -> getColor(R.color.pref_orange, null)
                else -> throw IllegalArgumentException("Color is not defined")
            }
        }
    }
    val prefMan = PreferenceManager.getDefaultSharedPreferences(context)
    val selectedColor = prefMan.getString(
        resources.getString(R.string.preference_text_color),
        ""
        )
    val validColors = resources.getStringArray(R.array.color_values)
    if (selectedColor != null && selectedColor in validColors){
        this.setTextColor(resolvePreferenceColor(selectedColor))
    } else {
        Log.i("COLOR_SETTINGS", "selected Color is not in array of values")
    }
}