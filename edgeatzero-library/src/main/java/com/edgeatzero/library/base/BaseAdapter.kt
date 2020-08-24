package com.edgeatzero.library.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.edgeatzero.library.common.DataBindingViewHolder
import com.edgeatzero.library.common.WeakProperty
import com.edgeatzero.library.ext.inflate
import com.edgeatzero.library.interfaces.BaseBridge

abstract class BaseAdapter<VH : ViewHolder> @JvmOverloads constructor(
    activity: AppCompatActivity? = null,
    fragment: Fragment? = null,
    protected var lifecycleOwner: LifecycleOwner? = activity ?: fragment?.viewLifecycleOwner,
    protected var fragmentManager: FragmentManager? = activity?.supportFragmentManager
        ?: fragment?.childFragmentManager
) : Adapter<VH>(), BaseBridge {

    override fun lifecycleOwner(): LifecycleOwner? {
        return lifecycleOwner
    }

    override fun fragmentManager(): FragmentManager? {
        return fragmentManager
    }

    protected var view by WeakProperty<RecyclerView>()

    override fun context(): Context = requireNotNull(view?.context)

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        view = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        if (view == recyclerView) view == null
    }

    protected inline fun <reified Binding : ViewDataBinding> inflateBinding(
        container: ViewGroup? = view
    ): Binding = LayoutInflater.from(context()).inflate(container)

    protected inline fun <reified Binding : ViewDataBinding> inflateDataBindingViewHolder(
        container: ViewGroup? = view
    ): DataBindingViewHolder<Binding> = DataBindingViewHolder(inflateBinding(container))

    companion object {

        @JvmStatic
        protected inline fun <reified Binding : ViewDataBinding> BaseAdapter<DataBindingViewHolder<Binding>>.inflateHolder(
            container: ViewGroup? = view
        ): DataBindingViewHolder<Binding> = inflateDataBindingViewHolder(container)

    }

}
