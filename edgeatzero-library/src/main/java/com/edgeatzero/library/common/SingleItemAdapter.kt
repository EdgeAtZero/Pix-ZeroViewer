package com.edgeatzero.library.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.edgeatzero.library.base.BaseAdapter

abstract class SingleItemAdapter<T : ViewDataBinding> @JvmOverloads constructor(
    activity: AppCompatActivity? = null,
    fragment: Fragment? = null,
    lifecycleOwner: LifecycleOwner? = activity ?: fragment?.viewLifecycleOwner,
    fragmentManager: FragmentManager? = activity?.supportFragmentManager
        ?: fragment?.childFragmentManager
) : BaseAdapter<DataBindingViewHolder<T>>(activity, fragment, lifecycleOwner, fragmentManager) {

    open val display: Boolean = true

    override fun getItemCount() = if (display) 1 else 0

    abstract fun onCreateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): T

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataBindingViewHolder<T> = DataBindingViewHolder(
        onCreateBinding(
            LayoutInflater.from(parent.context),
            parent
        ).also(::onBindingCreate)
    )

    protected open fun onBindBinding(binding: T) {}

    open fun onBindingCreate(binding: T) {}

    override fun onBindViewHolder(
        holder: DataBindingViewHolder<T>,
        position: Int,
        payloads: MutableList<Any>
    ): Unit = onBindBinding(holder.binding)

    override fun onBindViewHolder(
        holder: DataBindingViewHolder<T>,
        position: Int
    ): Unit = onBindBinding(holder.binding)

    protected inline fun <reified Binding : ViewDataBinding> SingleItemAdapter<Binding>.inflate(
        container: ViewGroup? = view
    ): Binding = inflateBinding(container)

}
