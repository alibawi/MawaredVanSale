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

package com.mawared.tableview.layoutmanager

import android.content.Context
import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.mawared.tableview.ITableView
import com.mawared.tableview.adapter.recyclerview.holder.AbstractViewHolder
import com.mawared.tableview.util.TableViewUtils

/**
 * Created by jeremypacabis on February 28, 2018.
 * @author Jeremy Patrick Pacabis <jeremy@ingenuity.ph>
 * com.mawared.tableview.layoutmanager <android-tableview-kotlin>
 */
class ColumnHeaderLayoutManager(
        context: Context,
        private val tableView: ITableView
) : LinearLayoutManager(context) {

    private val cachedWidthList: SparseArray<Int> = SparseArray()

    val firstItemLeft: Int
        get() {
            val firstColumnHeader = findViewByPosition(findFirstVisibleItemPosition())
            return firstColumnHeader!!.left
        }

    val visibleViewHolders: Array<AbstractViewHolder?>
        get() {
            val visibleChildCount = findLastVisibleItemPosition() - findFirstVisibleItemPosition() + 1
            val views = arrayOfNulls<AbstractViewHolder>(visibleChildCount)
            for ((index, i) in (findFirstVisibleItemPosition()
                    until findLastVisibleItemPosition() + 1).withIndex()) {
                views[index] = tableView.columnHeaderRecyclerView
                        .findViewHolderForAdapterPosition(i) as AbstractViewHolder

            }

            return views
        }

    init {
        orientation = HORIZONTAL
    }

    override fun measureChildWithMargins(child: View, widthUsed: Int, heightUsed: Int) {
        super.measureChildWithMargins(child, widthUsed, heightUsed)
        if (tableView.hasFixedWidth) {
            return
        }

        measureChild(child, widthUsed, heightUsed)
    }

    override fun measureChild(child: View, widthUsed: Int, heightUsed: Int) {
        if (tableView.hasFixedWidth) {
            super.measureChild(child, widthUsed, heightUsed)
            return
        }

        val position = getPosition(child)
        val cacheWidth = getCacheWidth(position)

        if (cacheWidth != -1) {
            TableViewUtils.setWidth(child, cacheWidth)
        } else {
            super.measureChild(child, widthUsed, heightUsed)
        }
    }


    fun setCacheWidth(position: Int, width: Int) {
        cachedWidthList.put(position, width)
    }

    fun getCacheWidth(position: Int): Int {
        val cachedWidth = cachedWidthList.get(position)
        return if (cachedWidth != null) {
            cachedWidthList.get(position)
        } else -1
    }

    fun removeCachedWidth(position: Int) {
        cachedWidthList.remove(position)
    }

    fun customRequestLayout() {
        var left = firstItemLeft
        var right: Int
        for (i in findFirstVisibleItemPosition() until findLastVisibleItemPosition() + 1) {
            right = left + getCacheWidth(i)
            val columnHeader = findViewByPosition(i)
            columnHeader?.left = left
            columnHeader?.right = right
            layoutDecoratedWithMargins(
                    columnHeader!!,
                    columnHeader.left,
                    columnHeader.top,
                    columnHeader.right,
                    columnHeader.bottom
            )

            left = right + 1
        }
    }

    fun getViewHolder(xPosition: Int): AbstractViewHolder {
        return tableView.columnHeaderRecyclerView
                .findViewHolderForAdapterPosition(xPosition) as AbstractViewHolder
    }
}
