/*
 * Kiwix Android
 * Copyright (c) 2020 Kiwix <android.kiwix.org>
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

package org.kiwix.kiwixmobile.core.utils

import android.text.Editable
import android.text.TextWatcher

class SimpleTextWatcher(
  private val onTextWatcherChangeAction: (CharSequence?, Int, Int, Int) -> Unit
) : TextWatcher {
  @SuppressWarnings("EmptyFunctionBlock")
  override fun afterTextChanged(p0: Editable?) {
  }

  @SuppressWarnings("EmptyFunctionBlock")
  override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
  }

  override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    onTextWatcherChangeAction.invoke(s, start, before, count)
  }
}
