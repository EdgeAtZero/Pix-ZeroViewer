/*
 * Copyright 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.edgeatzero.library.ext

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.edgeatzero.library.common.ViewModelProperty

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.viewModels(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): ViewModelProperty<VM> {
    return ViewModelProperty(
        kClass = VM::class,
        store = { viewModelStore },
        factory = factoryProducer ?: { defaultViewModelProviderFactory }
    )
}

@MainThread
inline fun <reified VM : ViewModel> Fragment.viewModels(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): ViewModelProperty<VM> {
    return ViewModelProperty(
        kClass = VM::class,
        store = { viewModelStore },
        factory = factoryProducer ?: { defaultViewModelProviderFactory }
    )
}

@MainThread
inline fun <reified VM : ViewModel> Fragment.activityViewModels(
    noinline factoryProducer: (() -> ViewModelProvider.Factory)? = null
): ViewModelProperty<VM> = ViewModelProperty(
    kClass = VM::class,
    store = { requireActivity().viewModelStore },
    factory = factoryProducer ?: { requireActivity().defaultViewModelProviderFactory }
)
