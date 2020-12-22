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

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import org.kiwix.kiwixmobile.core.downloader.model.Base64String

fun ImageView.setBitmap(base64String: Base64String) {
  base64String.toBitmap()
    ?.let(::setImageBitmap)
}

// methods that accept inline classes as parameters are not allowed to be called from java
// hence this facade
fun ImageView.setBitmapFromString(string: String?) {
  setBitmap(Base64String(string))
}

fun ImageView.setImageDrawableCompat(@DrawableRes id: Int) {
  setImageDrawable(ContextCompat.getDrawable(context, id))
}

fun ImageView.tint(@ColorInt colorId: Int) {
  ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(colorId))
}
