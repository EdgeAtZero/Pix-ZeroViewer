package com.edgeatzero.projects.pixiv.ui.common

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.recyclerview.widget.DiffUtil
import com.edgeatzero.projects.pixiv.BR
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class SelectData<T>(
    @Bindable
    var data: @RawValue T,
    @Bindable
    var selected: Boolean = false
) : BaseObservable(), Parcelable {

    fun toggle() {
        selected = !selected
        notifyPropertyChanged(BR.selected)
    }

}
