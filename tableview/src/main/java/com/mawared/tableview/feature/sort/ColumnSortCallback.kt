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

package com.mawared.tableview.feature.sort

//import android.support.v7.util.DiffUtil
import androidx.recyclerview.widget.DiffUtil

/**
 * Created by jeremypacabis on March 01, 2018.
 * @author Jeremy Patrick Pacabis <jeremy@ingenuity.ph>
 * com.mawared.tableview.feature.sort <android-tableview-kotlin>
 */
class ColumnSortCallback(
        private val oldCellItems: List<List<Sortable>>,
        private val newCellItems: List<List<Sortable>>,
        private val columnPosition: Int
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldCellItems.size

    override fun getNewListSize(): Int = newCellItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldCellItems.size > oldItemPosition && newCellItems.size > newItemPosition) {
            if (
                    oldCellItems[oldItemPosition].size > columnPosition &&
                    newCellItems[newItemPosition].size > columnPosition
            ) {
                val oldId = oldCellItems[oldItemPosition][columnPosition].id
                val newId = newCellItems[newItemPosition][columnPosition].id
                return oldId == newId
            }
        }

        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldCellItems.size > oldItemPosition && newCellItems.size > newItemPosition) {
            if (
                    oldCellItems[oldItemPosition].size > columnPosition &&
                    newCellItems[newItemPosition].size > columnPosition
            ) {
                val oldContent = oldCellItems[oldItemPosition][columnPosition].content
                val newContent = newCellItems[newItemPosition][columnPosition].content
                return oldContent == newContent
            }
        }

        return false
    }
}
