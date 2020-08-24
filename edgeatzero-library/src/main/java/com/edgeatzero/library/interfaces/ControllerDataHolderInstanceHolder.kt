package com.edgeatzero.library.interfaces

import androidx.lifecycle.LiveData

interface ControllerDataHolderInstanceHolder {

    val provider : LiveData<ControllerDataProvider>

}
