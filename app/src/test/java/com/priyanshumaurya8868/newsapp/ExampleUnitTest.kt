package com.priyanshumaurya8868.newsapp

import com.priyanshumaurya8868.newsapp.api.RetrofitInstance
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun headLine() {
        runBlocking{
            val response =  RetrofitInstance.api.getHeadLines("in",
                1,
            )
            assertNotNull(response)
        }
    }

    @Test
    fun `testing everything`()= runBlocking{
        val   response = RetrofitInstance.api.searchFor("Dehradun")
        assertNotNull(response)
    }
}