package com.edgeatzero.library.common

import androidx.recyclerview.widget.RecyclerView

class AdapterDataObserverProxy(
    private val observer: RecyclerView.AdapterDataObserver,
    private val callback: () -> Int = { 1 }
) : RecyclerView.AdapterDataObserver() {

    override fun onChanged() {
        observer.onChanged()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        observer.onItemRangeChanged(positionStart + callback.invoke(), itemCount)
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        observer.onItemRangeChanged(positionStart + callback.invoke(), itemCount, payload)
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        observer.onItemRangeInserted(positionStart + callback.invoke(), itemCount)
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        observer.onItemRangeRemoved(positionStart + callback.invoke(), itemCount)
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        super.onItemRangeMoved(
            fromPosition + callback.invoke(),
            toPosition + callback.invoke(),
            itemCount
        )
    }

}
