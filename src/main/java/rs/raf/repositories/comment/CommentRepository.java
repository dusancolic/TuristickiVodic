package rs.raf.repositories.comment;

import rs.raf.entities.Comment;

import java.util.List;

public interface CommentRepository {

    Comment addComment(Comment comment);

    List<Comment> findCommentWithId(Integer id);

    List<Comment> allComments();
}
