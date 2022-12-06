package com.saneet.demo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import com.saneet.demo.data.SchoolRepository
import com.saneet.demo.models.SchoolPreviewModel
import com.saneet.demo.schoollist.SchoolListViewModel
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.observers.TestObserver
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.MockitoAnnotations

class SchoolViewModelTest {
    private val repository: SchoolRepository = mock()
    private val schoolPreviewModel: SchoolPreviewModel = mock()
    private val viewModel = SchoolListViewModel()
    private val observable: Single<List<SchoolPreviewModel>> = mock()

    private val captor: KArgumentCaptor<Consumer<List<SchoolPreviewModel>>> = argumentCaptor()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val schedulers = RxImmediateSchedulerRule()

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel.setRepository(repository)
    }

    @Test
    fun fetchList_noMoreItems() {
        whenever(
            observable.subscribe(
                any(),
                any()
            )
        ) doReturn TestObserver.create<List<SchoolPreviewModel>>()
        whenever(repository.getSchoolsList(any(), any())) doReturn observable
        whenever(observable.subscribeOn(any())) doReturn observable
        whenever(observable.observeOn(any())) doReturn observable

        viewModel.fetchNext()

        verify(observable).subscribe(captor.capture(), any())
        captor.firstValue.accept(listOf(schoolPreviewModel))

        assertEquals(viewModel.schoolsList.value!!.size, 1)
        val model = viewModel.schoolsList.value!![0]
        assertEquals(model, schoolPreviewModel)
        assertEquals(viewModel.moreItemsAvailable, false)
        assertEquals(viewModel.clearAndShowLoading.value, false)
    }

    @Test
    fun searchList_noMoreItems() {
        whenever(
            observable.subscribe(
                any(),
                any()
            )
        ) doReturn TestObserver.create<List<SchoolPreviewModel>>()
        whenever(repository.searchSchools(any(), any(), any())) doReturn observable
        whenever(observable.subscribeOn(any())) doReturn observable
        whenever(observable.observeOn(any())) doReturn observable

        viewModel.setSearchTerm("searchTerm")

        verify(repository).searchSchools(eq("searchTerm"), any(), any())
        verify(observable).subscribe(captor.capture(), any())
        captor.firstValue.accept(listOf(schoolPreviewModel))

        assertEquals(viewModel.schoolsList.value!!.size, 1)
        val model = viewModel.schoolsList.value!![0]
        assertEquals(model, schoolPreviewModel)
        assertEquals(viewModel.moreItemsAvailable, false)
        assertEquals(viewModel.clearAndShowLoading.value, true)
    }
}