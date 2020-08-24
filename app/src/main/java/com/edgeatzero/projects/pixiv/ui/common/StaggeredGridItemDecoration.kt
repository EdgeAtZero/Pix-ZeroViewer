package com.edgeatzero.projects.pixiv.ui.common

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class StaggeredGridItemDecoration @JvmOverloads constructor(
    private val context: Context,
    @DimenRes marginRes: Int? = null,
    @DimenRes paddingRes: Int? = null
) : LinearItemDecoration(context, marginRes, paddingRes) {

    var checkFullSpan: OnCheckFullSpan? = null

    fun checkFullSpan(block: ((Int) -> Boolean)) {
        checkFullSpan = OnCheckFullSpanWrapper(block)
    }

    override fun adapter(
        main: ConcatAdapter,
        header: RecyclerView.Adapter<out RecyclerView.ViewHolder>?,
        footer: RecyclerView.Adapter<out RecyclerView.ViewHolder>?
    ): OnFilter {
        val listener = AdapterListener(main, header, footer)
        onFilter = listener
        checkFullSpan = listener
        return listener
    }

    override fun adapter(
        main: ConcatAdapter,
        header: (() -> Int?)?,
        footer: (() -> Int?)?
    ): OnFilter {
        val listener = AdapterListener(main, header, footer)
        onFilter = listener
        checkFullSpan = listener
        return listener
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val padding = padding
        val margin = margin
        val onFilter = onFilter
        val onCheckFullSpan = checkFullSpan
        val extra = extra
        val adapter = parent.adapter ?: return
        val manager = parent.layoutManager as? StaggeredGridLayoutManager ?: return
        val params = view.layoutParams as? StaggeredGridLayoutManager.LayoutParams ?: return
        val index = parent.getChildAdapterPosition(view)
        val count = adapter.itemCount - 1
        val spanCount = manager.spanCount
        val p0: Int /*left*/
        val p1: Int /*top*/
        val p2: Int /*right*/
        val p3: Int /*bottom*/
        if (params.isFullSpan) {
            p0 = margin
            p2 = margin
        } else when (params.spanIndex) {
            0 -> {
                p0 = margin
                p2 = padding
            }
            spanCount.minus(1) -> {
                p0 = padding
                p2 = margin
            }
            else -> {
                p0 = padding
                p2 = padding
            }
        }
        val i0 = index.minus(1).minus(params.spanIndex)
        val i1 = index.plus(spanCount.minus(params.spanIndex))
        val b0 = onCheckFullSpan?.checkFullSpan(i0)
        val b1 = onCheckFullSpan?.checkFullSpan(i1)
        val b2 = index == 0
        val b3 = index == count
        p1 = if (b0 == true || b2) {
            extra ?: margin
        } else padding
        p3 = if (b1 == true || b3) {
            extra ?: margin
        } else padding
//        p1 = padding
//        p3 = padding
//        p1 = if ((layoutParams.isFullSpan && index == 0) || index <= spanCount.minus(1)) margin else padding
//        p3 = if ((layoutParams.isFullSpan && index == 0) || index <= spanCount.minus(1)) margin else padding
//        p1 = if (index <= 0 || filter.invoke(index.minus(1))) {
//            if (index == 0) padding else padding.div(2)
//        } else 0
//        p3 = if (index >= count || filter.invoke(index.plus(1))) {
//            if (index == count) padding else padding.div(2)
//        } else 0
//        val invoke = !filter.invoke(view, index)
//        logd("padding($padding),spanIndex(${params.spanIndex}),index($index),count($count),filter($invoke),i0($i0),i1($i1),b0($b0),b1($b1),b2($b2),b3($b3),p0($p0),p1($p1),p2($p2),p3($p3) ($view)")
        if (!onFilter.filter(view, index)) return
        when (manager.orientation) {
            StaggeredGridLayoutManager.VERTICAL -> outRect.set(
                replaceTop ?: p0,
                replaceLeft ?: p1,
                replaceRight ?: p2,
                replaceBottom ?: p3
            )
            StaggeredGridLayoutManager.HORIZONTAL -> outRect.set(
                replaceTop ?: p1,
                replaceLeft ?: p0,
                replaceRight ?: p3,
                replaceBottom ?: p2
            )
        }
    }

    class AdapterListener : LinearItemDecoration.AdapterListener, OnCheckFullSpan {

        constructor(
            main: ConcatAdapter,
            header: (() -> Int?)? = null,
            footer: (() -> Int?)? = null
        ) : super(main, header, footer)

        constructor(
            main: ConcatAdapter,
            header: RecyclerView.Adapter<out RecyclerView.ViewHolder>? = null,
            footer: RecyclerView.Adapter<out RecyclerView.ViewHolder>? = null
        ) : super(main, header, footer)

        override fun checkFullSpan(index: Int) = index !in range

    }

    interface OnCheckFullSpan {

        fun checkFullSpan(index: Int): Boolean

    }

    class OnCheckFullSpanWrapper(private val block: (Int) -> Boolean) : OnCheckFullSpan {

        override fun checkFullSpan(index: Int) = block.invoke(index)

    }

}
