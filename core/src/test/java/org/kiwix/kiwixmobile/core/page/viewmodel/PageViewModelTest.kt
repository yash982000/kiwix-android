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

package org.kiwix.kiwixmobile.core.page.viewmodel

import com.jraska.livedata.test
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import io.reactivex.schedulers.TestScheduler
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.kiwix.kiwixmobile.core.dao.PageDao
import org.kiwix.kiwixmobile.core.page.PageImpl
import org.kiwix.kiwixmobile.core.page.adapter.Page
import org.kiwix.kiwixmobile.core.page.pageState
import org.kiwix.kiwixmobile.core.page.viewmodel.Action.Exit
import org.kiwix.kiwixmobile.core.page.viewmodel.Action.ExitActionModeMenu
import org.kiwix.kiwixmobile.core.page.viewmodel.Action.Filter
import org.kiwix.kiwixmobile.core.page.viewmodel.Action.OnItemClick
import org.kiwix.kiwixmobile.core.page.viewmodel.Action.OnItemLongClick
import org.kiwix.kiwixmobile.core.page.viewmodel.Action.UpdatePages
import org.kiwix.kiwixmobile.core.page.viewmodel.Action.UserClickedDeleteButton
import org.kiwix.kiwixmobile.core.page.viewmodel.Action.UserClickedDeleteSelectedPages
import org.kiwix.kiwixmobile.core.page.viewmodel.Action.UserClickedShowAllToggle
import org.kiwix.kiwixmobile.core.page.viewmodel.effects.OpenPage
import org.kiwix.kiwixmobile.core.reader.ZimReaderContainer
import org.kiwix.kiwixmobile.core.search.viewmodel.effects.PopFragmentBackstack
import org.kiwix.kiwixmobile.core.utils.SharedPreferenceUtil
import org.kiwix.sharedFunctions.InstantExecutorExtension
import org.kiwix.sharedFunctions.setScheduler

@ExtendWith(InstantExecutorExtension::class)
internal class PageViewModelTest {
  private val pageDao: PageDao = mockk()
  private val zimReaderContainer: ZimReaderContainer = mockk()
  private val sharedPreferenceUtil: SharedPreferenceUtil = mockk()

  private lateinit var viewModel: TestablePageViewModel
  private val testScheduler = TestScheduler()
  private val itemsFromDb: PublishProcessor<List<Page>> =
    PublishProcessor.create()

  init {
    setScheduler(testScheduler)
    RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
  }

  @BeforeEach
  fun init() {
    clearAllMocks()
    every { zimReaderContainer.id } returns "id"
    every { zimReaderContainer.name } returns "zimName"
    every { sharedPreferenceUtil.showHistoryAllBooks } returns true
    every { pageDao.pages() } returns itemsFromDb
    viewModel = TestablePageViewModel(zimReaderContainer, sharedPreferenceUtil, pageDao)
  }

  @Test
  fun `initial state is Initialising`() {
    viewModel.state.test().assertValue(pageState())
  }

  @Test
  fun `Exit calls PopFragmentBackstack`() {
    viewModel.effects.test().also { viewModel.actions.offer(Exit) }
      .assertValue(PopFragmentBackstack)
    viewModel.state.test().assertValue(pageState())
  }

  @Test
  fun `ExitActionModeMenu calls deslectAllPages`() {
    viewModel.actions.offer(ExitActionModeMenu)
    viewModel.state.test().assertValue(TestablePageState(searchTerm = "deselectAllPagesCalled"))
  }

  @Test
  fun `UserClickedShowAllToggle calls offerUpdateToShowAllToggle`() {
    val action = UserClickedShowAllToggle(true)
    viewModel.actions.offer(action)
    viewModel.state.test()
      .assertValue(TestablePageState(searchTerm = "offerUpdateToShowAllToggleCalled"))
  }

  @Test
  fun `UserClickedDeleteButton calls createDeletePageDialogEffect`() {
    viewModel.actions.offer(UserClickedDeleteButton)
    assertThat(viewModel.createDeletePageDialogEffectCalled).isEqualTo(true)
  }

  @Test
  fun `UserClickedDeleteSelectedPages calls createDeletePageDialogEffect`() {
    viewModel.actions.offer(UserClickedDeleteSelectedPages)
    assertThat(viewModel.createDeletePageDialogEffectCalled).isEqualTo(true)
  }

  @Test
  internal fun `OnItemClick selects item if one is selected`() {
    val page = PageImpl(isSelected = true)
    viewModel.state.postValue(TestablePageState(listOf(page)))
    viewModel.actions.offer(OnItemClick(page))
    viewModel.state.test().assertValue(TestablePageState(listOf(PageImpl())))
  }

  @Test
  internal fun `OnItemClick offers OpenPage if none is selected`() {
    viewModel.state.postValue(TestablePageState(listOf(PageImpl())))
    viewModel.effects.test().also { viewModel.actions.offer(OnItemClick(PageImpl())) }
      .assertValue(OpenPage(PageImpl(), zimReaderContainer))
    viewModel.state.test().assertValue(TestablePageState(listOf(PageImpl())))
  }

  @Test
  internal fun `OnItemLongClick selects item if none is selected`() {
    val page = PageImpl()
    viewModel.state.postValue(TestablePageState(listOf(page)))
    viewModel.actions.offer(OnItemLongClick(page))
    viewModel.state.test().assertValue(TestablePageState(listOf(PageImpl(isSelected = true))))
  }

  @Test
  fun `Filter calls updatePagesBasedOnFilter`() {
    viewModel.actions.offer(Filter("Called"))
    viewModel.state.test()
      .assertValue(TestablePageState(searchTerm = "updatePagesBasedOnFilterCalled"))
  }

  @Test
  fun `UpdatePages calls updatePages`() {
    viewModel.actions.offer(UpdatePages(emptyList()))
    viewModel.state.test()
      .assertValue(TestablePageState(searchTerm = "updatePagesCalled"))
  }
}
