package com.saneet.demo

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.saneet.demo.data.SchoolRepository
import com.saneet.demo.models.SchoolDetailsModel
import com.saneet.demo.network.NetworkService
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.junit.Test

class RepositoryTest {
    private val networkService: NetworkService = mock()
    private val schoolDetailsModel: SchoolDetailsModel = mock()
    private val repository = SchoolRepository(networkService)

    @Test
    fun school_details() {
        whenever(networkService.fetchSchoolDetails(any())) doReturn Single.just(
            listOf(
                schoolDetailsModel
            )
        )
        val response = repository.getSchoolDetails("").blockingGet()
        assertEquals(schoolDetailsModel, response)
    }
}