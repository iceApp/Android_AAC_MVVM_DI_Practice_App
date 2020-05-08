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

    private var selectedItemId: Int = 0

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        viewModel.timeId.observe(requireActivity(), Observer {
            selectedItemId = it
        })

        return AlertDialog.Builder(requireActivity()).apply {

            setTitle(R.string.select_time)
            setSingleChoiceItems(R.array.time_list, selectedItemId){ _, which ->

                selectedItemId = which
                viewModel.setTime(selectedItemId)
                dismiss()
            }
        }.create()
    }
}