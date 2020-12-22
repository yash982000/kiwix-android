/*
 * Kiwix Android
 * Copyright (c) 2019 Kiwix <android.kiwix.org>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.kiwix.kiwixmobile.core.extensions

import android.graphics.Color
import android.view.View
import androidx.annotation.ColorInt
import com.google.android.material.snackbar.Snackbar

fun View.snack(
  stringId: Int,
  actionStringId: Int? = null,
  actionClick: (() -> Unit)? = null,
  @ColorInt actionTextColor: Int = Color.WHITE
) {
  Snackbar.make(
    this, stringId, Snackbar.LENGTH_LONG
  ).apply {
    actionStringId?.let { setAction(it) { actionClick?.invoke() } }
    setActionTextColor(actionTextColor)
  }.show()
}

fun View.snack(
  message: String,
  actionStringId: Int? = null,
  actionClick: (() -> Unit)? = null,
  @ColorInt actionTextColor: Int = Color.WHITE
) {
  Snackbar.make(
    this, message, Snackbar.LENGTH_LONG
  ).apply {
    actionStringId?.let { setAction(it) { actionClick?.invoke() } }
    setActionTextColor(actionTextColor)
  }.show()
}

fun View.snack(stringId: Int) {
  snack(stringId, null, null)
}
