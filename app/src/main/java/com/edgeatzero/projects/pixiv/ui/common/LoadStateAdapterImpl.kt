package com.edgeatzero.projects.pixiv.ui.common

import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.edgeatzero.library.base.BaseActivity
import com.edgeatzero.library.base.BaseFragment
import com.edgeatzero.library.common.DataBindingViewHolder
import com.edgeatzero.library.ext.inflateViewHolder
import com.edgeatzero.library.ext.safeUpdateLayoutParams
import com.edgeatzero.library.model.LoadState
import com.edgeatzero.library.paging.LoadStateAdapter
import com.edgeatzero.projects.pixiv.databinding.LayoutLoadStateBinding

open class LoadStateAdapterImpl @JvmOverloads constructor(
    showInitialLoad: Boolean? = null,
    state: LiveData<LoadState>? = null,
    lifecycleOwner: LifecycleOwner? = null,
    fragmentManager: FragmentManager? = null,
    @StringRes private val empty: Int? = null,
    @StringRes private val complete: Int? = null,
    private val retry: () -> Unit
) : LoadStateAdapter<DataBindingViewHolder<LayoutLoadStateBinding>>(
    lifecycleOwner,
    fragmentManager
) {

    @JvmOverloads  constructor(
        showInitialLoad: Boolean? = null,
        state: LiveData<LoadState>? = null,
        activity: BaseActivity<*>,
        @StringRes empty: Int? = null,
        @StringRes  complete: Int? = null,
         retry: () -> Unit
    ) : this(
        showInitialLoad,
        state,
        activity,
        activity.supportFragmentManager,
        empty,
        complete,
        retry
    )

    @JvmOverloads constructor(
        showInitialLoad: Boolean? = null,
        state: LiveData<LoadState>? = null,
        fragment: BaseFragment<*>,
        @StringRes empty: Int? = null,
        @StringRes  complete: Int? = null,
        retry: () -> Unit
    ) : this(
        showInitialLoad,
        state,
        fragment.viewLifecycleOwner,
        fragment.childFragmentManager,
        empty,
        complete,
        retry
    )

    init {
        showInitialLoad?.let { this.showInitialLoad = it }
        if (state != null && lifecycleOwner != null) {
            state.observe(lifecycleOwner) { loadState = it }
        }
    }

    override fun onBindViewHolder(
        holder: DataBindingViewHolder<LayoutLoadStateBinding>,
        loadState: LoadState
    ) {
        holder.binding.state = loadState
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): DataBindingViewHolder<LayoutLoadStateBinding> = parent.inflateViewHolder { binding ->
        binding.root.safeUpdateLayoutParams<StaggeredGridLayoutManager.LayoutParams> {
            isFullSpan = true
        }
        binding.retry = retry
        binding.empty = empty
        binding.complete = complete
        binding.showInitialLoad = showInitialLoad
    }

}
