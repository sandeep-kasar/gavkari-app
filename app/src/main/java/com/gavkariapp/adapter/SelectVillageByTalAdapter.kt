package com.gavkariapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.gavkariapp.Model.Village
import com.gavkariapp.R
import com.gavkariapp.constant.AppConstant
import com.gavkariapp.data.PreferenceHelper
import com.gavkariapp.data.PreferenceHelper.get
import com.gavkariapp.viewholder.VillageSelectionViewHolder
import java.net.URLDecoder

class SelectVillageByTalAdapter(var villageList: List<Village>,
                                var addTheVillage: addVillage)
    : androidx.recyclerview.widget.RecyclerView.Adapter<VillageSelectionViewHolder>() {

    companion object {
        private const val TYPE_CONNECTION = 0
        private var context: Context? = null
        private lateinit var parentView : Context
    }

    override fun onBindViewHolder(viewHolder: VillageSelectionViewHolder, position: Int) {

        if (getItemViewType(position) == TYPE_CONNECTION) {
            showVillageList(viewHolder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VillageSelectionViewHolder {

        context = parent.context

        var viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder
        val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        viewHolder = when (viewType) {

            TYPE_CONNECTION -> {
                val viewEvent = inflater.inflate(R.layout.layout_select_village, parent, false)
                VillageSelectionViewHolder(viewEvent)
            }

            else -> {
                val view = inflater.inflate(R.layout.layout_select_village, parent, false)
                VillageSelectionViewHolder(view)
            }
        }

        parentView = viewHolder.itemView.context.applicationContext

        return viewHolder
    }

    override fun getItemCount(): Int {
        return villageList.size
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_CONNECTION
    }

    private fun showVillageList(viewHolder: VillageSelectionViewHolder, position: Int) {

        viewHolder.tvAdd.text = parentView.getString(R.string.lbl_select)

        val prefs = PreferenceHelper.customPrefs(context!!, "user_info")
        var userLang = prefs[AppConstant.LANGUAGE, "-1"]

        var data = villageList[position]

        var villageName = ""
        if (userLang == AppConstant.ENGLISH) {
            villageName = URLDecoder.decode(data.english, "utf-8")
        }

        if (userLang == AppConstant.MARATHI) {
            villageName = URLDecoder.decode(data.marathi, "utf-8")
        }

        viewHolder.tvVillageName.text = villageName

        viewHolder.tvAdd.setOnClickListener {
            addTheVillage.onClickAddButton(data)
        }

        viewHolder.itemView.setOnClickListener {
            addTheVillage.onClickAddButton(data)
        }
    }

    interface addVillage {
        fun onClickAddButton(villages: Village)
    }
}