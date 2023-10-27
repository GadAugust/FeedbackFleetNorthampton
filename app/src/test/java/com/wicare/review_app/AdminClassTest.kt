package com.wicare.review_app

import com.wicare.review_app.models.Admin
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class AdminClassTest {
    @Test
    fun testAdmin() {
        var admin = Admin(1, "John", "Doe", "johndoe@gmail.com", "12345");
        assertEquals("John", admin.firstname)
        assertEquals("Doe", admin.lastname)
        assertEquals("johndoe@gmail.com", admin.email);
    }
}