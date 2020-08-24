package com.edgeatzero.library.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.edgeatzero.library.common.CleanableProperty
import com.edgeatzero.library.common.WeakProperty
import com.edgeatzero.library.ext.inflate
import com.edgeatzero.library.interfaces.BaseBridge

abstract class BaseDialogFragment<T : ViewDataBinding> : InjectionDialogFragment(), BaseBridge {

    var container by WeakProperty<ViewGroup>()

    var property: CleanableProperty<T>? = null

    protected open val enable: Boolean = true

    protected abstract val binding: T

    override fun context(): Context = requireContext()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.container = container
        return if (enable) {
            val invoke = ::binding.invoke()
            invoke.lifecycleOwner = try {
                viewLifecycleOwner
            } catch (e: IllegalStateException) {
                this
            }
            invoke.root
        } else null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        property?.clear()
    }

    companion object {

        @JvmStatic
        protected inline fun <reified T : ViewDataBinding> BaseDialogFragment<*>.inflate(
            container: ViewGroup? = null
        ): T = layoutInflater.inflate(container)

        @JvmStatic
        protected inline fun <reified T : ViewDataBinding> BaseDialogFragment<T>.binding(
            crossinline function: () -> ViewGroup? = { container }
        ): Lazy<T> = lazy { layoutInflater.inflate<T>(function.invoke()) }

    }

}
