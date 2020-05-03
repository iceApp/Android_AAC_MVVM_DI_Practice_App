package com.example.meditation.view.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.meditation.R
import com.example.meditation.viewmodel.MainViewModel

class TimeSelectDialog: DialogFragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private var selectedItemId: Int = 0



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireActivity()).apply {

            setTitle(R.string.select_time)
            selectedItemId = viewModel.getTimeId()
            setSingleChoiceItems(R.array.time_list,selectedItemId){dialog, which ->

                selectedItemId = which
                viewModel.setTime(selectedItemId)
                dismiss()
            }
        }.create()
    }
}