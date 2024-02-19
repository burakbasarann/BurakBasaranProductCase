package com.basaran.burakbasarancase.common

import androidx.annotation.StringRes


sealed class UIState<out Int> {
    object Success : UIState<Nothing>()
    class Loading(@StringRes val loadingMessageId: Int) : UIState<Int>()
    class Error(@StringRes val errorMessageId: Int) : UIState<Int>()
}