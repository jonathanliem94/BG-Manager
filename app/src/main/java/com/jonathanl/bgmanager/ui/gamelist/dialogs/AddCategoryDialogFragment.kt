package com.jonathanl.bgmanager.ui.gamelist.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.jonathanl.bgmanager.R

class AddCategoryDialogFragment(private val listener: AddCategoryDialogListener): DialogFragment() {

    interface AddCategoryDialogListener {
        fun onDialogPositiveClick(filterText: String)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            // Build the dialog and set up the button click handlers
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            builder.setView(inflater.inflate(R.layout.dialog_add_category, null))
                .setPositiveButton(R.string.okay) { _, _ ->
                    val textView = dialog?.findViewById<EditText>(R.id.addNewCategoryText)
                    val newCategoryText = textView?.text.toString()
                    if (newCategoryText.isBlank() || newCategoryText == "null"){
                        dismiss()
                    }
                    else {
                        listener.onDialogPositiveClick(newCategoryText)
                    }
                }
                .setNegativeButton(R.string.cancel) { _, _ ->
                    dismiss()
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }

}