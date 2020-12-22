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

package org.kiwix.kiwixmobile.core.page.bookmark.viewmodel

import org.kiwix.kiwixmobile.core.page.adapter.PageRelated
import org.kiwix.kiwixmobile.core.page.bookmark.adapter.BookmarkItem
import org.kiwix.kiwixmobile.core.page.viewmodel.PageState

data class BookmarkState(
  override val pageItems: List<BookmarkItem>,
  override val showAll: Boolean,
  override val currentZimId: String?,
  override val searchTerm: String = ""
) : PageState<BookmarkItem>() {
  override val visiblePageItems: List<PageRelated> = filteredPageItems

  override fun copyWithNewItems(newItems: List<BookmarkItem>): PageState<BookmarkItem> =
    copy(pageItems = newItems)
}
