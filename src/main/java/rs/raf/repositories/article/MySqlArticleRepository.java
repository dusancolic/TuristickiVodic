package rs.raf.repositories.article;

import rs.raf.entities.Article;
import rs.raf.repositories.MySqlAbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MySqlArticleRepository extends MySqlAbstractRepository implements ArticleRepository{
    @Override
    public Article addArticle(Article article) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            String[] generatedColumns = {"id"};
            preparedStatement = connection.prepareStatement("INSERT INTO articles (destination_id,title,text,date,number_of_visits,author) VALUES(?,?,?,?,?,?)", generatedColumns);
            preparedStatement.setLong(1, article.getDestinationId());
            preparedStatement.setString(2, article.getTitle());
            preparedStatement.setString(3, article.getText());
            preparedStatement.setString(4, article.getDate());
            preparedStatement.setInt(5, article.getNumberOfVisits());
            preparedStatement.setString(6, article.getAuthor());

            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if(resultSet.next())
                article.setId(resultSet.getLong(1));

            Set<Long> activities = new HashSet<>(article.getActivities());
            for(Long activityId : activities) {

                preparedStatement = connection.prepareStatement("SELECT * FROM activities where id = ?");
                preparedStatement.setLong(1, activityId);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {

                    preparedStatement = connection.prepareStatement("INSERT INTO articles_activities (article_id,activity_id) VALUES(?,?)");
                    preparedStatement.setLong(1, article.getId());
                    preparedStatement.setLong(2, activityId);
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return article;
    }

    @Override
    public Article findArticleWithId(Long id) {
        Article article = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM articles where id = ?");
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                String name = resultSet.getString("title");
                String text = resultSet.getString("text");
                String author = resultSet.getString("author");
                String date = resultSet.getString("date");
                int numberOfVisits = resultSet.getInt("number_of_visits");
                Long destinationId = resultSet.getLong("destination_id");

                List<Long> activities = new ArrayList<>();
                preparedStatement = connection.prepareStatement("select activity_id from articles_activities where article_id = ?");
                preparedStatement.setLong(1, id);
                resultSet = preparedStatement.executeQuery();
                while(resultSet.next()) {
                    activities.add(resultSet.getLong("activity_id"));
                }


                article = new Article(id, destinationId, name, text, author, date, numberOfVisits, activities);
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

        return article;
    }

    @Override
    public List<Article> allArticles() {
        List<Article> articles = new ArrayList<>();

        try (Connection connection = this.newConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM articles ORDER BY date DESC")) {

            while (resultSet.next()) {
                try (Statement activityStatement = connection.createStatement();
                     ResultSet activityResultSet = activityStatement.executeQuery("SELECT activity_id FROM articles_activities WHERE article_id = " + resultSet.getLong("id"))) {

                    List<Long> activities = new ArrayList<>();
                    while (activityResultSet.next()) {
                        activities.add(activityResultSet.getLong("activity_id"));
                    }

                    articles.add(new Article(
                            resultSet.getLong("id"),
                            resultSet.getLong("destination_id"),
                            resultSet.getString("title"),
                            resultSet.getString("text"),
                            resultSet.getString("author"),
                            resultSet.getString("date"),
                            resultSet.getInt("number_of_visits"),
                            activities
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return articles;
    }
}
