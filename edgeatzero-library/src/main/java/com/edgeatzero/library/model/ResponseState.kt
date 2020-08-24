package com.edgeatzero.library.model

@Suppress("DataClassPrivateConstructor")
data class ResponseState private constructor(
    val state: State,
    val message: String = "",
    val retryable: Boolean = false
) {

    val isLoading: Boolean = state == State.LOADING

    val isSuccessful: Boolean = state == State.SUCCESSFUL

    val isFailed: Boolean = state == State.FAILED

    companion object {

        @JvmStatic
        val NULL = ResponseState(State.FAILED, "null")

        @JvmStatic
        val LOADING =
            ResponseState(State.LOADING)

        @JvmStatic
        val SUCCESSFUL =
            ResponseState(State.SUCCESSFUL)

        @JvmStatic
        fun failed(
            message: String,
            retryable: Boolean = true
        ) = ResponseState(State.FAILED, message, retryable)

    }

}
