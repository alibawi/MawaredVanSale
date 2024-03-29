/*
 * Copyright 2018 Jeremy Patrick Pacabis
 * Copyright 2017-2018 Evren Coşkun
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.mawared.tableview.listener.click

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.mawared.tableview.ITableView
import com.mawared.tableview.adapter.recyclerview.CellRecyclerView
import com.mawared.tableview.adapter.recyclerview.holder.AbstractViewHolder

/**
 * Created by jeremypacabis on February 28, 2018.
 * @author Jeremy Patrick Pacabis <jeremy@ingenuity.ph>
 * com.mawared.tableview.listener.click <android-tableview-kotlin>
 */
class RowHeaderRecyclerViewItemClickListener(
        recyclerView: CellRecyclerView,
        tableView: ITableView
) : AbstractItemClickListener(recyclerView, tableView) {

    override fun clickAction(view: RecyclerView, e: MotionEvent): Boolean {
        val childView = view.findChildViewUnder(e.x, e.y)
        if (childView != null && gestureDetector.onTouchEvent(e)) {
            val holder = recyclerView.getChildViewHolder(childView) as AbstractViewHolder
            val row = holder.adapterPosition
            if (!tableView.ignoreSelectionColors) {
                selectionHandler.setSelectedRowPosition(holder, row)
            }

            tableViewListener?.onRowHeaderClicked(holder, row)
            return true
        }

        return false
    }

    override fun longPressAction(e: MotionEvent) {
        if (recyclerView.scrollState != RecyclerView.SCROLL_STATE_IDLE) {
            return
        }

        val child = recyclerView.findChildViewUnder(e.x, e.y)
        if (child != null && tableViewListener != null) {
            val holder = recyclerView.getChildViewHolder(child)
            tableViewListener!!.onRowHeaderLongPressed(holder, holder.adapterPosition)
        }
    }
}
