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

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.kiwix.kiwixmobile.core.R
import org.kiwix.kiwixmobile.core.extensions.toast
import org.kiwix.kiwixmobile.core.utils.dialog.AlertDialogShower
import org.kiwix.kiwixmobile.core.utils.dialog.KiwixDialog

internal class ExternalLinkOpenerTest {
  private val sharedPreferenceUtil: SharedPreferenceUtil = mockk()
  private val alertDialogShower: AlertDialogShower = mockk(relaxed = true)
  private val intent: Intent = mockk()
  private val activity: Activity = mockk()

  @Test
  internal fun `alertDialogShower opens link if confirm-button is clicked`() {
    every { intent.resolveActivity(activity.packageManager) } returns mockk()
    every { sharedPreferenceUtil.prefExternalLinkPopup } returns true
    val lambdaSlot = slot<() -> Unit>()
    val externalLinkOpener = ExternalLinkOpener(activity, sharedPreferenceUtil, alertDialogShower)
    externalLinkOpener.openExternalUrl(intent)
    verify {
      alertDialogShower.show(KiwixDialog.ExternalLinkPopup, capture(lambdaSlot), any(), any())
    }
    lambdaSlot.captured.invoke()
    verify { activity.startActivity(intent) }
  }

  @Test
  internal fun `alertDialogShower does not open link if negative-button is clicked`() {
    every { intent.resolveActivity(activity.packageManager) } returns mockk()
    every { sharedPreferenceUtil.prefExternalLinkPopup } returns true
    val lambdaSlot = slot<() -> Unit>()
    val externalLinkOpener = ExternalLinkOpener(activity, sharedPreferenceUtil, alertDialogShower)
    externalLinkOpener.openExternalUrl(intent)
    verify {
      alertDialogShower.show(KiwixDialog.ExternalLinkPopup, any(), capture(lambdaSlot), any())
    }
    lambdaSlot.captured.invoke()
    verify(exactly = 0) { activity.startActivity(intent) }
  }

  @Test
  internal fun `alertDialogShower opens link and saves preferences if neutral-button is clicked`() {
    every { intent.resolveActivity(activity.packageManager) } returns mockk()
    every { sharedPreferenceUtil.prefExternalLinkPopup } returns true
    val lambdaSlot = slot<() -> Unit>()
    val externalLinkOpener = ExternalLinkOpener(activity, sharedPreferenceUtil, alertDialogShower)
    externalLinkOpener.openExternalUrl(intent)
    verify {
      alertDialogShower.show(KiwixDialog.ExternalLinkPopup, any(), any(), capture(lambdaSlot))
    }
    lambdaSlot.captured.invoke()
    verify {
      sharedPreferenceUtil.putPrefExternalLinkPopup(false)
      activity.startActivity(intent)
    }
  }

  @Test
  internal fun `intent is started if external link popup preference is false`() {
    every { intent.resolveActivity(activity.packageManager) } returns mockk()
    every { sharedPreferenceUtil.prefExternalLinkPopup } returns false
    val externalLinkOpener = ExternalLinkOpener(activity, sharedPreferenceUtil, alertDialogShower)
    externalLinkOpener.openExternalUrl(intent)
    verify { activity.startActivity(intent) }
  }

  @Test
  internal fun `toast if packageManager is null`() {
    every { intent.resolveActivity(activity.packageManager) } returns null
    val externalLinkOpener = ExternalLinkOpener(activity, sharedPreferenceUtil, alertDialogShower)
    mockkStatic(Toast::class)
    justRun {
      Toast.makeText(activity, R.string.no_reader_application_installed, Toast.LENGTH_LONG).show()
    }
    externalLinkOpener.openExternalUrl(intent)
    verify { activity.toast(R.string.no_reader_application_installed) }
  }
}
