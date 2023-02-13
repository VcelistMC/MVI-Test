package com.soft.myapplication.common

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.InputType
import android.widget.EditText

object AlertDialogHelper {
    fun showInputDialog(context: Context ,title: String, positiveAction: (String) -> Unit){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)

        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)

        builder.setPositiveButton("OK") { _, _ -> positiveAction(input.text.toString()) }

        builder.setNegativeButton("Cancel"){ dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }
}