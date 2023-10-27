package com.wicare.review_app.models

class Message(id: Int, reviewId: Int, adminId: Int, message: String) {
    var id: Int = id
    var reviewId: Int = reviewId
    var adminId: Int = adminId
    var message: String = message
}