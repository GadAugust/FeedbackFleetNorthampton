package com.wicare.review_app
import com.wicare.review_app.models.Message
import org.junit.Test

import org.junit.Assert.*
class messageClassTest {
    @Test
    fun testMessage() {
        var message = Message(1, 1, 3,"Hello Kotlin!");
        assertEquals(1, message.id);
    }
}