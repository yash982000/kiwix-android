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
package org.kiwix.kiwixmobile.localFileTransfer

import android.net.wifi.p2p.WifiP2pInfo
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import org.kiwix.kiwixmobile.core.BuildConfig
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket

/**
 * Helper class for the local file sharing module.
 *
 * Once two peer devices are connected through wifi direct, this task is executed to perform a
 * handshake between the two devices. The purpose of the handshake is to allow the file sending
 * device to obtain the IP address of the file receiving device (When the file sending device
 * is the wifi direct group owner, it doesn't have the IP address of the peer device by default).
 *
 * After obtaining the IP address, the sender also shares metadata regarding the file transfer
 * (no. of files & their names) with the receiver.
 */
abstract class PeerGroupHandshake(private var groupInfo: WifiP2pInfo) {
  private val HANDSHAKE_MESSAGE = "Request Kiwix File Sharing"
  suspend fun handshake(): InetAddress? = withContext(Dispatchers.IO) {
    if (BuildConfig.DEBUG) {
      Log.d(TAG, "Handshake in progress")
    }
    when {
      groupInfo.groupFormed && groupInfo.isGroupOwner && isActive ->
        readHandshakeAndExchangeMetaData()
      groupInfo.groupFormed && isActive -> // && !groupInfo.isGroupOwner
        writeHandshakeAndExchangeMetaData()
      else -> null
    }
  }

  private fun writeHandshakeAndExchangeMetaData(): InetAddress? {
    return try {
      Socket().use { client ->
        client.reuseAddress = true
        client.connect(
          InetSocketAddress(
            groupInfo.groupOwnerAddress.hostAddress,
            PEER_HANDSHAKE_PORT
          ), 15000
        )
        val objectOutputStream = ObjectOutputStream(client.getOutputStream())
        // Send message for the peer device to verify
        objectOutputStream.writeObject(HANDSHAKE_MESSAGE)
        exchangeFileTransferMetadata(client.getInputStream(), client.getOutputStream())
        groupInfo.groupOwnerAddress
      }
    } catch (ex: Exception) {
      ex.printStackTrace()
      null
    }
  }

  private fun readHandshakeAndExchangeMetaData(): InetAddress? {
    return try {
      ServerSocket(PEER_HANDSHAKE_PORT)
        .use { serverSocket ->
          serverSocket.reuseAddress = true
          val server = serverSocket.accept()
          val objectInputStream = ObjectInputStream(server.getInputStream())
          val kiwixHandShakeMessage = objectInputStream.readObject()

          // Verify that the peer trying to communicate is a kiwix app intending to transfer files
          if (isKiwixHandshake(kiwixHandShakeMessage)) {
            if (BuildConfig.DEBUG) {
              Log.d(TAG, "Client IP address: " + server.inetAddress)
            }
            exchangeFileTransferMetadata(server.getInputStream(), server.getOutputStream())
            server.inetAddress
          } else {
            // Selected device is not accepting wifi direct connections through the kiwix app
            null
          }
        }
    } catch (ex: Exception) {
      ex.printStackTrace()
      null
    }
  }

  companion object {
    private const val TAG = "PeerGroupHandshake"
    private const val PEER_HANDSHAKE_PORT = 8009
  }

  abstract fun exchangeFileTransferMetadata(inputStream: InputStream, outputStream: OutputStream)

  private fun isKiwixHandshake(handshakeMessage: Any): Boolean =
    HANDSHAKE_MESSAGE == handshakeMessage
}
