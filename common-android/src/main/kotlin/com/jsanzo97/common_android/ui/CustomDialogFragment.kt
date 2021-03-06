package com.jsanzo97.common_android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.jsanzo97.recipemanager.common.R

class CustomDialogFragment(
    private val title: String = "",
    private val message: String,
    private val positiveButtonAction: () -> Unit = {},
    private val showOkButton: Boolean = true,
    private val showCancelButton: Boolean = false
) : DialogFragment() {

    private lateinit var dialogOkButton: MaterialButton
    private lateinit var dialogCancelButton: MaterialButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.custom_dialog_fragment, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<MaterialTextView>(R.id.custom_dialog_title).text = title
        view.findViewById<MaterialTextView>(R.id.custom_dialog_message).text = message

        dialogOkButton = view.findViewById(R.id.custom_dialog_fragment_accept_button)
        dialogCancelButton = view.findViewById(R.id.custom_dialog_fragment_cancel_button)

        dialogOkButton.visibility = if (showOkButton) View.VISIBLE else View.GONE
        dialogCancelButton.visibility = if (showCancelButton) View.VISIBLE else View.GONE

        dialogOkButton.setOnClickListener {
            positiveButtonAction()
            this.dismiss()
        }

        dialogCancelButton.setOnClickListener {
            this.dismiss()
        }
    }

}
