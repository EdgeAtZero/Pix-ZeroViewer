package com.edgeatzero.library.model

@Suppress("DataClassPrivateConstructor")
data class LoadState private constructor(
    val state: State,
    val message: String
) {

    val isNull
        get() = this == NULL

    val isEmpty: Boolean
        get() = this == EMPTY

    val isLoaded: Boolean
        get() = this == LOADED

    val isComplete: Boolean
        get() = this == COMPLETED

    val isLoading: Boolean
        get() = this == LOADING

    val isInitialLoading: Boolean
        get() = this == INITIAL_LOADING

    val isError
        get() = this.state == State.FAILED

    companion object {

        /*
         * 空状态
         **/
        val NULL = LoadState(State.FAILED, "null")

        /*
         * 数据源返回空数据
         **/
        @JvmStatic
        val EMPTY = LoadState(State.SUCCESSFUL, "empty")

        /*
         * 数据源加载完成
         **/
        @JvmStatic
        val LOADED = LoadState(State.SUCCESSFUL, "loaded")

        /*
         * 数据源加载完毕
         **/
        @JvmStatic
        val COMPLETED = LoadState(State.SUCCESSFUL, "complete")

        /*
         * 数据源加载中
         **/
        @JvmStatic
        val LOADING = LoadState(State.LOADING, "loading")

        /*
         * 数据源初始加载完毕
         **/
        @JvmStatic
        val INITIAL_LOADING = LoadState(State.LOADING, "initial_loading")

        /*
         * 数据源初始加载失败
         **/
        @JvmStatic
        fun error(message: String) = LoadState(State.FAILED, message)

    }

}
