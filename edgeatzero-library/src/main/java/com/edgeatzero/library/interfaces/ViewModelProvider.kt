package com.edgeatzero.library.interfaces

import androidx.lifecycle.ViewModel

interface ViewModelProvider<T : ViewModel> {

    val model: T

}
