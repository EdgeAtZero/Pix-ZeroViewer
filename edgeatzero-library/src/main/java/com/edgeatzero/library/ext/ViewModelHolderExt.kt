@file:Suppress("UNCHECKED_CAST")

package com.edgeatzero.library.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.edgeatzero.library.common.ViewModelHolder

inline fun <reified VM : ViewModel> FragmentManager.findViewModelHolder(
    tag: String?
): ViewModelHolder<VM>? = fragments.map {
    if (it.tag == tag && it is ViewModelHolder<*> && it.getViewModel() is VM) it else null
}.firstOrNull() as? ViewModelHolder<VM>

inline fun <reified VM : ViewModel> FragmentManager.saveViewModel(
    tag: String?,
    model: VM
): Unit = (findViewModelHolder(tag) ?: ViewModelHolder<VM>().also {
    beginTransaction().add(it, tag).commit()
}).setViewModel(model)

inline fun <reified VM : ViewModel> FragmentManager.autoOperate(
    tag: String?,
    getter: () -> VM,
    setter: (VM) -> Unit
) {
    val vm = findViewModelHolder<VM>(tag)?.getViewModel()
    if (vm != null) {
        setter.invoke(vm)
    } else {
        saveViewModel(tag, getter.invoke())
    }
}

inline fun <reified VM : ViewModel> AppCompatActivity.findViewModel(
    tag: String?,
    manager: FragmentManager = supportFragmentManager
): VM? = manager.findViewModelHolder<VM>(tag)?.getViewModel()

inline fun <reified VM : ViewModel> Fragment.findViewModel(
    tag: String?,
    manager: FragmentManager = childFragmentManager
): VM? = manager.findViewModelHolder<VM>(tag)?.getViewModel()

inline fun <reified VM : ViewModel> AppCompatActivity.saveViewModel(
    tag: String?,
    model: VM,
    manager: FragmentManager = supportFragmentManager
): Unit = manager.saveViewModel(tag, model)

inline fun <reified VM : ViewModel> Fragment.saveViewModel(
    tag: String?,
    model: VM,
    manager: FragmentManager = childFragmentManager
): Unit = manager.saveViewModel(tag, model)


inline fun <reified VM : ViewModel> AppCompatActivity.autoOperate(
    tag: String?,
    getter: () -> VM,
    setter: (VM) -> Unit,
    manager: FragmentManager = supportFragmentManager
): Unit = manager.autoOperate(tag, getter, setter)

inline fun <reified VM : ViewModel> Fragment.autoOperate(
    tag: String?,
    getter: () -> VM,
    setter: (VM) -> Unit,
    manager: FragmentManager = childFragmentManager
): Unit = manager.autoOperate(tag, getter, setter)
