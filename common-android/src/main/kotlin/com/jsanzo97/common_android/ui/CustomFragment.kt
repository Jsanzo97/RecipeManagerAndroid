package com.jsanzo97.common_android.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.jsanzo97.common_android.DIALOG_FRAGMENT_TAG
import com.jsanzo97.common_android.ui.extensions.hideKeyboard
import com.jsanzo97.common_android.ui.extensions.observe
import com.jsanzo97.common_android.viewmodel.UiConfigurationViewModel
import com.jsanzo97.common_android.viewmodel.UiConfigurationViewState
import com.jsanzo97.recipemanager.common.R
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

abstract class CustomFragment(private val contentLayoutId: Int) : Fragment() {

    private val uiConfigurationViewModel by sharedViewModel<UiConfigurationViewModel>()
    protected abstract val uiConfigurationViewState: UiConfigurationViewState

    private lateinit var progressView: View
    private lateinit var progressDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        uiConfigurationViewModel.updateViewState(uiConfigurationViewState)

        return inflater.inflate(contentLayoutId, container, false)
    }

    protected abstract fun handleUiConfigurationViewState(uiConfigurationViewState: UiConfigurationViewState)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(uiConfigurationViewModel.states, ::handleUiConfigurationViewState)
    }

    fun showError(message: String) {
        hideKeyboard()
        hideProgressDialog()
        CustomDialogFragment(
            message = message,
            title = getString(R.string.error)
        ).show(requireFragmentManager(), DIALOG_FRAGMENT_TAG)
    }

    fun showSuccess(message: String) {
        hideKeyboard()
        hideProgressDialog()
        CustomDialogFragment(
            message = message,
            title = getString(R.string.success)
        ).show(requireFragmentManager(), DIALOG_FRAGMENT_TAG)
    }

    fun showDialogWithFunction(message: String, title: String = "", f: () -> Unit) {
        hideKeyboard()
        hideProgressDialog()
        CustomDialogFragment(
            message = message,
            title = title,
            showOkButton = true,
            showCancelButton = true,
            positiveButtonAction = { f() }
        ).show(requireFragmentManager(), DIALOG_FRAGMENT_TAG)
    }

    fun showProgressDialog() {
        if (!::progressDialog.isInitialized) {
            progressView = layoutInflater.inflate(R.layout.custom_progress_dialog, null)
            progressDialog = AlertDialog.Builder(requireContext())
                .setCancelable(false)
                .setView(progressView)
                .create()
        }

        progressDialog.show()
    }

    fun hideProgressDialog() {
        if (::progressDialog.isInitialized) {
            progressDialog.dismiss()
        }
    }

}
