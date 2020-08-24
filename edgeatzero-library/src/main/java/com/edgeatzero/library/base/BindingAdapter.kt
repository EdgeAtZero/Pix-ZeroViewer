package com.edgeatzero.library.base


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.edgeatzero.library.ext.inflate
import kotlin.reflect.KClass
import com.edgeatzero.library.base.BaseAdapter as Adapter
import com.edgeatzero.library.common.DataBindingViewHolder as ViewHolder

abstract class BindingAdapter @JvmOverloads constructor(
    activity: AppCompatActivity? = null,
    fragment: Fragment? = null,
    lifecycleOwner: LifecycleOwner? = activity ?: fragment?.viewLifecycleOwner,
    fragmentManager: FragmentManager? = activity?.supportFragmentManager
        ?: fragment?.childFragmentManager
) : Adapter<ViewHolder<out ViewDataBinding>>(activity, fragment, lifecycleOwner, fragmentManager) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder<*> {
        val holder = viewTypeToBindingKClass(viewType).inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).let { ViewHolder(it) }
        onInitViewHolder(holder, viewType)
        return holder
    }

    open fun onInitViewHolder(holder: ViewHolder<*>, viewType: Int) {

    }


    abstract fun viewTypeToBindingKClass(viewType: Int): KClass<out ViewDataBinding>

}
