package ru.ellaid.comment.data.repository

import org.springframework.data.mongodb.repository.MongoRepository
import ru.ellaid.comment.data.entity.Comment

interface CommentRepository: MongoRepository<Comment, String> {

    fun findAllByTrackId(trackId: String): List<Comment>
}
