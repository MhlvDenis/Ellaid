package ru.ellaid.comment.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.ellaid.comment.data.entity.Comment
import ru.ellaid.comment.exception.CommentNotFoundException
import ru.ellaid.comment.rest.form.CreateCommentForm
import ru.ellaid.comment.rest.form.EditCommentForm
import ru.ellaid.comment.service.CommentService

@RestController
class CommentController(
    private val commentService: CommentService
) {

    @PostMapping("/comment")
    fun createComment(
        @RequestBody createCommentForm: CreateCommentForm
    ): ResponseEntity<Comment> =
        ResponseEntity(
            commentService.createComment(
                createCommentForm.trackId,
                createCommentForm.userId,
                createCommentForm.content
            ),
            HttpStatus.CREATED
        )

    @PatchMapping("/comment")
    fun editComment(
        @RequestBody editCommentForm: EditCommentForm
    ): ResponseEntity<Comment> = try {
        ResponseEntity(
            commentService.editComment(
                editCommentForm.commentId,
                editCommentForm.newContent
            ),
            HttpStatus.OK
        )
    } catch (e: CommentNotFoundException) {
        ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @DeleteMapping("/comment")
    fun deleteComment(
        @RequestParam id: String
    ): ResponseEntity<Comment> = try {
        ResponseEntity(commentService.deleteComment(id), HttpStatus.OK)
    } catch (e: CommentNotFoundException) {
        ResponseEntity(HttpStatus.NOT_FOUND)
    }

    @GetMapping("/comments")
    fun getComments(
        @RequestParam trackId: String
    ): ResponseEntity<List<Comment>> =
        ResponseEntity(commentService.getComments(trackId), HttpStatus.OK)
}
