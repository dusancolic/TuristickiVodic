package rs.raf.repositories.comment;

import rs.raf.entities.Comment;
import rs.raf.repositories.MySqlAbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlCommentRepository extends MySqlAbstractRepository implements CommentRepository{
    @Override
    public Comment addComment(Comment comment) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            String[] generatedColumns = {"id"};
            preparedStatement = connection.prepareStatement("INSERT INTO comments (article_id,author,text,date) VALUES(?,?,?,?)", generatedColumns);
            preparedStatement.setLong(1, comment.getArticleId());
            preparedStatement.setString(2, comment.getAuthor());
            preparedStatement.setString(3, comment.getText());
            preparedStatement.setString(4, comment.getDate());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if(resultSet.next())
                comment.setId(resultSet.getLong(1));


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return comment;
    }

    @Override
    public Comment findCommentWithId(Long id) {
        Comment comment = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM comments where id = ?");
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                String text = resultSet.getString("text");
                String author = resultSet.getString("author");
                String date = resultSet.getString("date");
                Long articleId = resultSet.getLong("article_id");

                comment = new Comment(id, articleId, text, author, date);
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return comment;
    }

    @Override
    public List<Comment> findCommentsWithArticleId(Long id) {
        List<Comment> comments = new ArrayList<>();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("select * from comments where article_id = ? order by date desc");
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                comments.add(new Comment(resultSet.getLong("id"),
                        resultSet.getLong("article_id"),
                        resultSet.getString("text"),
                        resultSet.getString("author"),
                        resultSet.getString("date")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return comments;
    }


    @Override
    public List<Comment> allComments() {
        List<Comment> comments = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM comments ORDER BY date DESC");
            while (resultSet.next()) {
                comments.add(new Comment(resultSet.getLong("id"),
                        resultSet.getLong("article_id"),
                        resultSet.getString("text"),
                        resultSet.getString("author"),
                        resultSet.getString("date")));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return comments;
    }
}
