package rs.raf.repositories.comment;

import rs.raf.entities.Comment;

import java.util.List;

public interface CommentRepository {

    Comment addComment(Comment comment);

    Comment findCommentWithId(Long id);

    List<Comment> findCommentsWithArticleId(Long id, int page, int size);

    long countCommentsWithArticleId(Long id);

    List<Comment> allComments();
}
