package com.wicare.review_app.models

class Review(id: Int, firstname: String, lastname: String, email: String, userReview: String, likes: Int, dislike: Int) {
    var id: Int = id
    var firstname: String = firstname
    var lastname: String = lastname
    var email: String = email
    var userReview: String = userReview
    var likes: Int = likes
    var dislike: Int = dislike
}