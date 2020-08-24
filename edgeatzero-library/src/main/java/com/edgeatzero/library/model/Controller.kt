package com.edgeatzero.library.model

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class Controller<T>(
    val data: LiveData<PagedList<T>>,
    val state: () -> LiveData<LoadState>,
    val retry: () -> Unit,
    val refresh: () -> Unit
)
