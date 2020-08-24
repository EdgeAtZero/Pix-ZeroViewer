package com.edgeatzero.projects.pixiv.ui.like

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.edgeatzero.library.common.WeakProperty

class LikeProItemTouchCallback(
    private val model: LikeProViewModel,
    private val adapter: LikeProAdapter
) : ItemTouchHelper.Callback() {

    private var recyclerView by WeakProperty<RecyclerView>()

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        this.recyclerView = recyclerView
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val from = viewHolder.bindingAdapterPosition
        val to = target.bindingAdapterPosition
        val swap = model.swap(from, to)
        if (swap) adapter.notifyItemMoved(from, to)
        return swap
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val index = viewHolder.bindingAdapterPosition
        if (model.deleteAt(index)) adapter.notifyItemRemoved(index)
    }

//    private val icon =
//        requireNotNull(ContextCompat.getDrawable(context, R.drawable.ic_delete_forever))
//    private val background =
//        ColorDrawable(context.resolveAttribute(R.attr.colorBackgroundInverse).data)
//
//    override fun onChildDraw(
//        c: Canvas,
//        recyclerView: RecyclerView,
//        viewHolder: RecyclerView.ViewHolder,
//        dX: Float,
//        dY: Float,
//        actionState: Int,
//        isCurrentlyActive: Boolean
//    ) {
//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//        val itemView = viewHolder.itemView
//        val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
//
//        val backTop = itemView.top
//        val backBottom = itemView.bottom
//        val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
//        val iconBottom = iconTop + icon.intrinsicHeight
//        when {
//            dX > 0 -> {
//                val backLeft = itemView.left
//                val backRight = itemView.left + dX.toInt()
//                background.setBounds(backLeft, backTop, backRight, backBottom)
//                val iconLeft = itemView.left + iconMargin
//                val iconRight = iconLeft + icon.intrinsicWidth
//                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
//            }
//            dX < 0 -> {
//                val backRight = itemView.right
//                val backLeft = itemView.right + dX.toInt()
//                background.setBounds(backLeft, backTop, backRight, backBottom)
//                val iconRight = itemView.right - iconMargin
//                val iconLeft = iconRight - icon.intrinsicWidth
//                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
//            }
//            else -> {
//                background.setBounds(0, 0, 0, 0)
//                icon.setBounds(0, 0, 0, 0)
//            }
//        }
//        background.draw(c)
//        icon.draw(c)
//    }

}
