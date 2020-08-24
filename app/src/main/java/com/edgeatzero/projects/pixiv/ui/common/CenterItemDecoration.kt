package com.edgeatzero.projects.pixiv.ui.common

import android.graphics.Rect
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.edgeatzero.library.util.DisplayUtils
import com.edgeatzero.projects.pixiv.databinding.LayoutLoadStateBinding

class CenterItemDecoration(
    private val dip: Int? = null,
    private val px: Int? = null,
    private val lambda: (position: Int, child: View, parent: RecyclerView) -> Boolean = { _, _, _ -> true }
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (DataBindingUtil.findBinding<LayoutLoadStateBinding>(view) != null) return
        val adapter = parent.adapter ?: return
        val layoutParams = view.layoutParams ?: return
        val layoutManager = parent.layoutManager ?: return
        val padding = dip?.let { DisplayUtils.dip2px(dip = it) } ?: px ?: 0
        if (!lambda.invoke(parent.getChildAdapterPosition(view), view, parent)) return
        when {
            layoutParams is StaggeredGridLayoutManager.LayoutParams && layoutManager is StaggeredGridLayoutManager -> {
                if (layoutParams.isFullSpan) return
                val spanCount = layoutManager.spanCount
                when (layoutManager.orientation) {
                    StaggeredGridLayoutManager.VERTICAL -> {
                        outRect.top = padding.div(2)
                        outRect.bottom = padding.div(2)
                        when {
                            layoutParams.spanIndex % spanCount == 0 -> {
                                outRect.left = padding.times(2)
                                outRect.right = padding.div(2)
                            }
                            layoutParams.spanIndex % spanCount == spanCount - 1 -> {
                                outRect.left = padding.div(2)
                                outRect.right = padding.times(2)
                            }
                            else -> {
                                outRect.left = padding.div(2)
                                outRect.right = padding.div(2)
                            }
                        }
                    }
                    StaggeredGridLayoutManager.HORIZONTAL -> {
                        outRect.left = padding.div(2)
                        outRect.right = padding.div(2)
                        when {
                            layoutParams.spanIndex.rem(spanCount) == 0 -> {
                                outRect.top = padding.times(2)
                                outRect.bottom = padding.div(2)
                            }
                            layoutParams.spanIndex.rem(spanCount) == spanCount.minus(1) -> {
                                outRect.top = padding.div(2)
                                outRect.bottom = padding.times(2)
                            }
                            else -> {
                                outRect.top = padding.div(2)
                                outRect.bottom = padding.div(2)
                            }
                        }
                    }
                }
            }
            layoutManager is LinearLayoutManager -> {
                val b1 = parent.getChildAdapterPosition(view) == 0
                val b2 = parent.getChildAdapterPosition(view) == adapter.itemCount.minus(1)
                when (layoutManager.orientation) {
                    LinearLayoutManager.VERTICAL -> {
                        outRect.top = padding
                        outRect.bottom = padding
                        outRect.top = if (b1) padding.times(2) else padding.div(2)
                        outRect.bottom = if (b2) padding.times(2) else padding.div(2)
                    }
                    LinearLayoutManager.HORIZONTAL -> {
                        outRect.left = padding
                        outRect.right = padding
                        outRect.left = if (b1) padding.times(2) else padding.div(2)
                        outRect.right = if (b2) padding.times(2) else padding.div(2)
                    }
                }
            }
//            layoutManager is FlexboxLayoutManager && layoutParams is FlexboxLayoutManager.LayoutParams -> {
//                when (layoutManager.flexDirection) {
//                    FlexDirection.ROW -> {
//                    }
//                    FlexDirection.ROW_REVERSE -> {
//                    }
//                    FlexDirection.COLUMN -> {
//                    }
//                    FlexDirection.COLUMN_REVERSE -> {
//                    }
//                }
//            }
        }

    }
}
