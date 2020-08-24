package com.edgeatzero.library.model

enum class State(
    val isLoading: Boolean = false,
    val isSuccessful: Boolean = false,
    val isFailed: Boolean = false
) {

    LOADING(isLoading = true),

    SUCCESSFUL(isSuccessful = true),

    FAILED(isFailed = true);

}
