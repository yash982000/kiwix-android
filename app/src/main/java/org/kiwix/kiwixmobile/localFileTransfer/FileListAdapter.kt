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
@file:Suppress("PackageNaming")

package org.kiwix.kiwixmobile.localFileTransfer

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_transfer_list.image_view_file_transferred
import kotlinx.android.synthetic.main.item_transfer_list.progress_bar_transferring_file
import kotlinx.android.synthetic.main.item_transfer_list.text_view_file_item_name
import org.kiwix.kiwixmobile.R
import org.kiwix.kiwixmobile.core.base.adapter.BaseViewHolder
import org.kiwix.kiwixmobile.core.extensions.ViewGroupExtensions.inflate
import org.kiwix.kiwixmobile.localFileTransfer.FileItem.FileStatus.ERROR
import org.kiwix.kiwixmobile.localFileTransfer.FileItem.FileStatus.SENDING
import org.kiwix.kiwixmobile.localFileTransfer.FileItem.FileStatus.SENT
import org.kiwix.kiwixmobile.localFileTransfer.FileListAdapter.FileViewHolder

/**
 * Helper class, part of the local file sharing module.
 *
 * Defines the Adapter for the list of file-items displayed in {TransferProgressFragment}
 */
class FileListAdapter(private val fileItems: List<FileItem>) :
  RecyclerView.Adapter<FileViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder =
    FileViewHolder(parent.inflate(R.layout.item_transfer_list, false), this)

  override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
    holder.bind(fileItems[position])
  }

  override fun getItemCount(): Int = fileItems.size

  inner class FileViewHolder(itemView: View, val fileListAdapter: FileListAdapter) :
    BaseViewHolder<FileItem>(itemView) {
    override fun bind(item: FileItem) {
      text_view_file_item_name.text = item.fileName
      image_view_file_transferred.isVisible = item.fileStatus != SENDING
      progress_bar_transferring_file.isVisible = item.fileStatus == SENDING
      if (item.fileStatus != FileItem.FileStatus.TO_BE_SENT) {
        // Icon for TO_BE_SENT is assigned by default in the item layout
        progress_bar_transferring_file.visibility = View.GONE
        when (item.fileStatus) {
          SENDING -> progress_bar_transferring_file.visibility = View.VISIBLE
          SENT -> {
            image_view_file_transferred.setImageResource(R.drawable.ic_baseline_check_24px)
            progress_bar_transferring_file.visibility = View.GONE
          }
          ERROR -> {
            image_view_file_transferred.setImageResource(R.drawable.ic_baseline_error_24px)
            progress_bar_transferring_file.visibility = View.GONE
          }
          else -> {
          }
        }
      }
    }
  }
}
