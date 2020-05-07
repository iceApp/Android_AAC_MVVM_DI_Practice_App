package com.example.meditation.view.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meditation.MyApplication
import com.example.meditation.R
import com.example.meditation.viewmodel.MainViewModel

class ThemeSelectDialog: DialogFragment() {

    private val appContext = MyApplication.appContext
    private val themeList = MyApplication.themeList
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val recyclerView = RecyclerView(appContext)
        with(recyclerView){
            layoutManager = GridLayoutManager(appContext, 2)
            adapter = ThemeSelectAdapter(themeList, viewModel)
        }

        val dialog =  AlertDialog.Builder(requireActivity()).apply {
            setTitle(R.string.select_theme)
            setView(recyclerView)
        }.create()

        viewModel.txtTheme.observe(requireActivity(), Observer {
            dialog.dismiss()
        })

        return dialog
    }

}