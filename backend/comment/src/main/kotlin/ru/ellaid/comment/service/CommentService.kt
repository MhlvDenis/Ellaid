package ru.ellaid.comment.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import ru.ellaid.comment.data.entity.Comment
import ru.ellaid.comment.data.repository.CommentRepository
import ru.ellaid.comment.exception.CommentNotFoundException

private val logger = KotlinLogging.logger { }

@Service
class CommentService(
    private val repository: CommentRepository
) {

    fun getComment(id: String): Comment =
        repository.findById(id).orElseThrow {
            logger.error { "Comment $id not found" }
            CommentNotFoundException()
        }

    fun createComment(
        trackId: String,
        userId: String,
        content: String
    ): Comment = repository.save(Comment(trackId, userId, content))

    fun editComment(
        commentId: String,
        newContent: String,
    ): Comment =
        getComment(commentId).apply {
            content = newContent
        }.also {
            repository.save(it)
        }

    fun deleteComment(commentId: String): Comment =
        getComment(commentId).also {
            repository.deleteById(commentId)
        }

    fun getComments(trackId: String): List<Comment> = repository.findAllByTrackId(trackId)
}
