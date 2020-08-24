@file:Suppress("UNCHECKED_CAST")

package com.edgeatzero.library.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel


class ViewModelHolder<VM : ViewModel> : Fragment() {

    private var model: VM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun setViewModel(viewModel: VM) {
        this.model = viewModel
    }

    fun getViewModel(): VM? {
        return model
    }

    companion object {

        fun <VM : ViewModel> createContainer(viewModel: VM): ViewModelHolder<*> {
            val container: ViewModelHolder<VM> = ViewModelHolder()
            container.setViewModel(viewModel)
            return container
        }

        fun <T : ViewModel> findViewModel(
            fragmentManager: FragmentManager,
            viewModelTag: String?
        ): T? {
            return (fragmentManager.findFragmentByTag(viewModelTag) as? ViewModelHolder<*>)?.getViewModel() as? T
        }

    }

}
