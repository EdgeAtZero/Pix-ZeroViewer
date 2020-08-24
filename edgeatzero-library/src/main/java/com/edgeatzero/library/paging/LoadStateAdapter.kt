package com.edgeatzero.library.paging

import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.edgeatzero.library.base.BaseAdapter
import com.edgeatzero.library.ext.logd
import com.edgeatzero.library.model.LoadState

abstract class LoadStateAdapter<VH : RecyclerView.ViewHolder> : BaseAdapter<VH> {

    open var showInitialLoad: Boolean = false

    @JvmOverloads
    constructor(
        lifecycleOwner: LifecycleOwner? = null,
        fragmentManager: FragmentManager? = null
    ) : super(lifecycleOwner = lifecycleOwner, fragmentManager = fragmentManager)

    constructor(activity: AppCompatActivity) : super(activity = activity)

    constructor(fragment: Fragment) : super(fragment = fragment)

    var loadState: LoadState = LoadState.NULL
        set(value) {
            if (field != value) {
                val oldItem = displayLoadStateAsItem(field)
                val newItem = displayLoadStateAsItem(value)

                if (oldItem && !newItem) {
                    notifyItemRemoved(0)
                } else if (newItem && !oldItem) {
                    notifyItemInserted(0)
                } else if (oldItem && newItem) {
                    notifyItemChanged(0)
                }
                field = value
            }
        }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return onCreateViewHolder(parent, loadState)
    }

    final override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, loadState)
    }

    final override fun getItemViewType(position: Int): Int = getStateViewType(loadState)

    final override fun getItemCount(): Int = if (displayLoadStateAsItem(loadState)) 1 else 0

    abstract fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): VH

    abstract fun onBindViewHolder(holder: VH, loadState: LoadState)

    open fun getStateViewType(loadState: LoadState): Int = 0

    open fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return (showInitialLoad || !loadState.isInitialLoading) && !loadState.isNull
    }

}
