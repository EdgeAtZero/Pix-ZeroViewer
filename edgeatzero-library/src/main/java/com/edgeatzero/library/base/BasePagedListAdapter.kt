package com.edgeatzero.library.base

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.edgeatzero.library.common.DataBindingViewHolder
import com.edgeatzero.library.common.WeakProperty
import com.edgeatzero.library.ext.inflate
import androidx.paging.PagedListAdapter as Adapter
import com.edgeatzero.library.interfaces.BaseAdapterBridge as Bridge

abstract class BasePagedListAdapter<T : Any, VH : ViewHolder> : Adapter<T, VH>, Bridge<T> {

    protected var lifecycleOwner by WeakProperty<LifecycleOwner>()
    protected var fragmentManager by WeakProperty<FragmentManager>()

    @JvmOverloads
    constructor(
        diffCallback: DiffUtil.ItemCallback<T>,
        activity: AppCompatActivity? = null,
        fragment: Fragment? = null,
        lifecycleOwner: LifecycleOwner? = activity ?: fragment?.viewLifecycleOwner,
        fragmentManager: FragmentManager? = activity?.supportFragmentManager ?: fragment?.childFragmentManager
    ) : super(diffCallback) {
        this.diffCallback = diffCallback
        this.lifecycleOwner = lifecycleOwner
        this.fragmentManager = fragmentManager
    }

    @JvmOverloads
    constructor(
        config: AsyncDifferConfig<T>,
        activity: AppCompatActivity? = null,
        fragment: Fragment? = null,
        lifecycleOwner: LifecycleOwner? = activity ?: fragment?.viewLifecycleOwner,
        fragmentManager: FragmentManager? = activity?.supportFragmentManager ?: fragment?.childFragmentManager
    ) : super(config) {
        diffCallback = config.diffCallback
        this.lifecycleOwner = lifecycleOwner
        this.fragmentManager = fragmentManager
    }

    override fun lifecycleOwner(): LifecycleOwner? {
        return lifecycleOwner
    }

    override fun fragmentManager(): FragmentManager? {
        return fragmentManager
    }

    protected var view by WeakProperty<RecyclerView>()

    override fun context(): Context = requireNotNull(view?.context)

    protected val notNullCurrentList: List<T>
        get() = currentList ?: emptyList()

    protected val diffCallback: DiffUtil.ItemCallback<T>

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        view = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        if (view == recyclerView) view == null
    }

    open fun updateItem(item: T, callback: (position: Int, oldItem: T, newItem: T) -> Unit) {
        notNullCurrentList.forEachIndexed { index, it ->
            if (diffCallback.areItemsTheSame(it, item)) callback.invoke(index, it, item)
        }
    }

    override operator fun get(position: Int): T? {
        return notNullCurrentList[position]
    }

    override fun current(): List<T> = notNullCurrentList

    override fun clone() : List<T> = notNullCurrentList.toList()

    protected inline fun <reified Binding : ViewDataBinding> inflateBinding(
        container: ViewGroup? = view
    ): Binding = LayoutInflater.from(context()).inflate(container)

    protected inline fun <reified Binding : ViewDataBinding> inflateDataBindingViewHolder(
        container: ViewGroup? = view
    ): DataBindingViewHolder<Binding> = DataBindingViewHolder(inflateBinding(container))

    companion object {

        @JvmStatic
        protected inline fun <reified T : Any, reified Binding : ViewDataBinding> BasePagedListAdapter<T, DataBindingViewHolder<Binding>>.inflateHolder(
            container: ViewGroup? = view
        ): DataBindingViewHolder<Binding> = inflateDataBindingViewHolder(container)

    }

}
