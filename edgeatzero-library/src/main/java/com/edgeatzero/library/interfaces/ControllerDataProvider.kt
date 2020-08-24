package com.edgeatzero.library.interfaces

import androidx.lifecycle.LiveData
import com.edgeatzero.library.model.LoadState
import java.util.concurrent.Executor

interface ControllerDataProvider {

    val state: LiveData<LoadState>

    fun retry()

}
