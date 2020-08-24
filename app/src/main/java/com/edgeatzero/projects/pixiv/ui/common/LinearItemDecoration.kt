package com.edgeatzero.projects.pixiv.ui.common

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.annotation.Dimension
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

open class LinearItemDecoration @JvmOverloads constructor(
    private val context: Context,
    @DimenRes marginRes: Int? = null,
    @DimenRes paddingRes: Int? = null
) : RecyclerView.ItemDecoration() {

    @Dimension(unit = Dimension.PX)
    var extra: Int? = null
        private set

    fun extra(@DimenRes res: Int? = null, @Dimension(unit = Dimension.PX) px: Int? = null) {
        extra = res?.let { context.resources.getDimensionPixelOffset(it) } ?: px
    }

    @Dimension(unit = Dimension.PX)
    var margin: Int = 0
        private set

    fun margin(@DimenRes res: Int? = null, @Dimension(unit = Dimension.PX) px: Int? = null) {
        margin = res?.let { context.resources.getDimensionPixelOffset(it) } ?: px ?: return
    }

    @Dimension(unit = Dimension.PX)
    var padding: Int = 0
        private set

    fun padding(@DimenRes res: Int? = null, @Dimension(unit = Dimension.PX) px: Int? = null) {
        padding = res?.let { context.resources.getDimensionPixelOffset(it) } ?: px ?: return
    }

    @Dimension(unit = Dimension.PX)
    var replaceLeft: Int? = null
        private set

    fun replaceLeft(
        @DimenRes res: Int? = null,
        @Dimension(unit = Dimension.PX) px: Int? = null
    ) {
        replaceLeft = res?.let { context.resources.getDimensionPixelOffset(it) } ?: px
    }

    @Dimension(unit = Dimension.PX)
    var replaceTop: Int? = null
        private set

    fun replaceTop(
        @DimenRes res: Int? = null,
        @Dimension(unit = Dimension.PX) px: Int? = null
    ) {
        replaceTop = res?.let { context.resources.getDimensionPixelOffset(it) } ?: px
    }

    @Dimension(unit = Dimension.PX)
    var replaceRight: Int? = null
        private set

    fun replaceRight(
        @DimenRes res: Int? = null,
        @Dimension(unit = Dimension.PX) px: Int? = null
    ) {
        replaceRight = res?.let { context.resources.getDimensionPixelOffset(it) } ?: px
    }

    @Dimension(unit = Dimension.PX)
    var replaceBottom: Int? = null
        private set

    fun replaceBottom(
        @DimenRes res: Int? = null,
        @Dimension(unit = Dimension.PX) px: Int? = null
    ) {
        replaceBottom = res?.let { context.resources.getDimensionPixelOffset(it) } ?: px
    }

    var onFilter: OnFilter = OnFilter.default

    fun filter(block: View.(Int) -> Boolean) {
        onFilter = OnFilterWrapper(block)
    }

    open fun adapter(
        main: ConcatAdapter,
        header: RecyclerView.Adapter<out RecyclerView.ViewHolder>?,
        footer: RecyclerView.Adapter<out RecyclerView.ViewHolder>?
    ): OnFilter {
        val listener = AdapterListener(main, header, footer)
        onFilter = listener
        return listener
    }

    open fun adapter(
        main: ConcatAdapter,
        header: (() -> Int?)?,
        footer: (() -> Int?)?
    ): OnFilter {
        val listener = AdapterListener(main, header, footer)
        onFilter = listener
        return listener
    }

    init {
        marginRes?.let { margin(res = it) }
        paddingRes?.let { padding(res = it) }
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
        val extra = extra
        val adapter = parent.adapter ?: return
        val manager = parent.layoutManager as? LinearLayoutManager ?: return
        val index = parent.getChildAdapterPosition(view)
        val count = adapter.itemCount - 1
        if (!onFilter.filter(view, index)) return
        //top
        val p1 = if (index == 0) extra ?: margin else padding
        //bottom
        val p3 = if (index == count) extra ?: margin else padding
        when (manager.orientation) {
            RecyclerView.VERTICAL -> outRect.set(
                replaceLeft ?: margin,
                replaceTop ?: p1,
                replaceRight ?: margin,
                replaceBottom ?: p3
            )
            RecyclerView.HORIZONTAL -> outRect.set(
                replaceLeft ?: p1,
                replaceTop ?: margin,
                replaceRight ?: p3,
                replaceBottom ?: margin
            )
        }
    }

    open class AdapterListener(
        private val main: ConcatAdapter,
        private val header: (() -> Int?)? = null,
        private val footer: (() -> Int?)? = null
    ) : OnFilter {

        constructor(
            main: ConcatAdapter,
            header: RecyclerView.Adapter<out RecyclerView.ViewHolder>? = null,
            footer: RecyclerView.Adapter<out RecyclerView.ViewHolder>? = null
        ) : this(
            main = main,
            header = { header?.itemCount ?: 0 },
            footer = { footer?.itemCount ?: 0 }
        )

        protected open val range
            get() = (header?.invoke() ?: 0).until(main.itemCount.minus(footer?.invoke() ?: 0))

        override fun filter(view: View, index: Int) = index in range

    }

    interface OnFilter {

        companion object {

            @JvmStatic
            val default = OnFilterWrapper { true }

        }

        fun filter(view: View, index: Int): Boolean

    }

    class OnFilterWrapper(private val block: View.(Int) -> Boolean) : OnFilter {

        override fun filter(view: View, index: Int) = block.invoke(view, index)

    }

}
