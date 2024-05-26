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
    public List<Article> findArticlesWithDestinationName(String name) {
        List<Article> articles = new ArrayList<>();

        String articleQuery = "SELECT a.id, a.destination_id, a.title, a.text, a.author, a.date, a.number_of_visits " +
                "FROM articles a JOIN destinations d ON a.destination_id = d.id WHERE d.name = ?";
        String activityQuery = "SELECT activity_id FROM articles_activities WHERE article_id = ?";

        try (Connection connection = this.newConnection();
             PreparedStatement articleStatement = connection.prepareStatement(articleQuery))
        {
            articleStatement.setString(1, name);

            try (ResultSet resultSet = articleStatement.executeQuery()) {
                while (resultSet.next()) {
                    Long id = resultSet.getLong("id");
                    Long destinationId = resultSet.getLong("destination_id");
                    String title = resultSet.getString("title");
                    String text = resultSet.getString("text");
                    String author = resultSet.getString("author");
                    String date = resultSet.getString("date");
                    int numberOfVisits = resultSet.getInt("number_of_visits");

                    List<Long> activities = new ArrayList<>();
                    try (PreparedStatement activityStatement = connection.prepareStatement(activityQuery)) {
                        activityStatement.setLong(1, id);
                        try (ResultSet activityResultSet = activityStatement.executeQuery()) {
                            while (activityResultSet.next()) {
                                activities.add(activityResultSet.getLong("activity_id"));
                            }
                        }
                    }
                    articles.add(new Article(id, destinationId, title, text, author, date, numberOfVisits, activities));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return articles;
    }

    @Override
    public String removeArticle(Article article) {
        Long id = article.getId();
        String deleteArticleQuery = "DELETE FROM articles WHERE id = ?";
        try (Connection connection = this.newConnection();
            PreparedStatement deleteStatement = connection.prepareStatement(deleteArticleQuery)) {

            deleteStatement.setLong(1, id);
            deleteStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error while removing article";
        }
        return "Article removed";
    }

    @Override
    public int incrementVisits(Article article) {
        Long id = article.getId();
        int visits = article.getNumberOfVisits();
        String incrementVisitsQuery = "UPDATE articles SET number_of_visits = number_of_visits + 1 WHERE id = ?";
        try (Connection connection = this.newConnection();
             PreparedStatement incrementStatement = connection.prepareStatement(incrementVisitsQuery)) {

            incrementStatement.setLong(1, id);
            incrementStatement.executeUpdate();
            visits = visits + 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
        return visits;
    }

    @Override
    public Article updateArticle(Article article) {
        Long id = article.getId();

        String updateArticleQuery = "UPDATE articles SET destination_id = ?, title = ?, text = ?, date = ?, number_of_visits = ?, author = ? WHERE id = ?";
        String deleteArticlesActivitiesQuery = "DELETE FROM articles_activities WHERE article_id = ?";
        String checkActivityQuery = "SELECT * FROM activities WHERE id = ?";
        String insertArticleActivityQuery = "INSERT INTO articles_activities (article_id, activity_id) VALUES(?, ?)";

        try (Connection connection = this.newConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateArticleQuery);
             PreparedStatement deleteStatement = connection.prepareStatement(deleteArticlesActivitiesQuery);
             PreparedStatement checkActivityStatement = connection.prepareStatement(checkActivityQuery);
             PreparedStatement insertStatement = connection.prepareStatement(insertArticleActivityQuery)) {

            updateStatement.setLong(1, article.getDestinationId());
            updateStatement.setString(2, article.getTitle());
            updateStatement.setString(3, article.getText());
            updateStatement.setString(4, article.getDate());
            updateStatement.setInt(5, article.getNumberOfVisits());
            updateStatement.setString(6, article.getAuthor());
            updateStatement.setLong(7, id);
            updateStatement.executeUpdate();

            deleteStatement.setLong(1, id);
            deleteStatement.executeUpdate();

            Set<Long> activities = new HashSet<>(article.getActivities());
            for (Long activityId : activities) {
                checkActivityStatement.setLong(1, activityId);
                try (ResultSet resultSet = checkActivityStatement.executeQuery()) {
                    if (resultSet.next()) {
                        insertStatement.setLong(1, id);
                        insertStatement.setLong(2, activityId);
                        insertStatement.executeUpdate();
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
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
