/*
 * Copyright (c) 2018 by Denis Verentsov (decsent@Yandex.ru)
 * All rights reserved.
 */

package com.dvinc.notepad.presentation.ui.base

@Deprecated("replace by viewModel")
interface ErrorView {

    fun showError(errorMessage: String)
}
