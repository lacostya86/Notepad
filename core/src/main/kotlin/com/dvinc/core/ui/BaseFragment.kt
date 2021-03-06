/*
 * Copyright (c) 2018 by Denis Verentsov (decsent@Yandex.ru)
 * All rights reserved.
 */

package com.dvinc.core.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dvinc.core.R
import com.dvinc.core.di.DaggerApplication
import com.dvinc.core.di.provider.ApplicationProvider
import com.dvinc.core.snackbar.SnackbarFactory

abstract class BaseFragment(@LayoutRes layoutResId: Int) : Fragment(layoutResId), BaseView {

    val appComponent: ApplicationProvider by lazy {
        (requireActivity().applicationContext as DaggerApplication).getApplicationProvider()
    }

    private val decorView by lazy { requireActivity().window.decorView }

    abstract fun injectDependencies()

    override fun onAttach(context: Context) {
        injectDependencies()
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Important fix for window insets
        // See info at https://medium.com/androiddevelopers/windows-insets-fragment-transitions-9024b239a436
        ViewCompat.requestApplyInsets(view)
    }

    override fun showMessage(messageResId: Int, containerResId: Int, anchorView: View?, duration: Int) {
        val backgroundColorId = R.color.black
        val textColorId = R.color.white
        showSnackBar(messageResId, containerResId, anchorView?.id, duration, backgroundColorId, textColorId)
    }

    override fun showErrorMessage(messageResId: Int, containerResId: Int, anchorView: View?, duration: Int) {
        val backgroundColorId = R.color.red
        val textColorId = R.color.white
        showSnackBar(messageResId, containerResId, anchorView?.id, duration, backgroundColorId, textColorId)
    }

    fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    protected open fun handleViewCommand(viewCommand: ViewCommand) {
        when (viewCommand) {
            is NavigateTo -> {
                findNavController()
                    .navigate(viewCommand.direction)
            }
            is NavigateUp -> {
                findNavController()
                    .navigateUp()
            }
        }
    }

    private fun showSnackBar(
        messageResId: Int,
        containerResId: Int,
        anchorViewId: Int?,
        duration: Int,
        @ColorRes backgroundColor: Int,
        @ColorRes textColor: Int
    ) {
        val message = getString(messageResId)
        val snackbar = SnackbarFactory.create(
            mainView = decorView,
            messageText = message,
            containerResId = containerResId,
            duration = duration,
            backgroundColor = backgroundColor,
            textColor = textColor
        )
        anchorViewId?.let { snackbar?.setAnchorView(it) }
        snackbar?.show()
    }
}
