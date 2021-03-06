/*
 * Copyright (c) 2019 by Denis Verentsov (decsent@yandex.ru)
 * All rights reserved.
 */

package com.dvinc.core.ui

import androidx.navigation.NavDirections

data class ShowMessage(val messageResId: Int) : ViewCommand

data class ShowErrorMessage(val messageResId: Int) : ViewCommand

data class NavigateTo(val direction: NavDirections) : ViewCommand

object NavigateUp : ViewCommand
