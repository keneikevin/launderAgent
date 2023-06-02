package com.example.launderagent.data.other

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.agent.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DeletePostDialog : DialogFragment() {

    private var positiveListener: (() -> Unit)? = null



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_post_dialog_title)
            .setMessage(R.string.delete_post_dialog_message)
            .setIcon(R.drawable.delete)
            .setPositiveButton(R.string.delete_post_dialog_positive) { _, _ ->
                positiveListener?.let { click ->
                    click()
                }
            }
            .setNegativeButton(R.string.delete_post_dialog_negative) { dialogInterface, _ ->
                dialogInterface.cancel()
            }
            .create()
    }
}


