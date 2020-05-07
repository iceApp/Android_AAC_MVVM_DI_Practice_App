package com.example.meditation.view.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.meditation.MyApplication
import com.example.meditation.R
import com.example.meditation.data.ThemeData
import com.example.meditation.viewmodel.MainViewModel
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.theme_tetail_card.*

class ThemeSelectAdapter(private val themeList: ArrayList<ThemeData>, private val mainViewModel: MainViewModel) : RecyclerView.Adapter<ThemeSelectAdapter.ViewHolder>() {

    private val appContext: Context =  MyApplication.appContext

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.theme_tetail_card,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            val themeData = themeList[position]
            txtTheme.text = appContext.resources.getString(themeData.themeNameResId)
            Glide.with(appContext).load(themeData.themeSpPicResId).into(imgTheme)
            containerView?.setOnClickListener {
                mainViewModel.setTheme(themeData)
            }
        }
    }

    override fun getItemCount(): Int {
        return themeList.size
    }

    inner class ViewHolder(override val containerView: View?): RecyclerView.ViewHolder(containerView!!), LayoutContainer{

    }

}
