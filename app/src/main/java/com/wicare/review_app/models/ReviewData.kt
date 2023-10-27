package com.wicare.review_app.models

data class ReviewData (val id: String, val fullname: String, val email: String, val review: String, val image: Int, val likes: String, var dislikes: String) {
}