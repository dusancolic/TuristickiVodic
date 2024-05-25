package rs.raf.repositories.activity;

import rs.raf.entities.Activity;
import rs.raf.entities.Article;
import rs.raf.repositories.MySqlAbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MySqlActivityRepository extends MySqlAbstractRepository implements ActivityRepository {
    @Override
    public Activity addActivity(Activity activity) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            String[] generatedColumns = {"id"};

            preparedStatement = connection.prepareStatement("INSERT INTO activities (name) VALUES(?)", generatedColumns);
            preparedStatement.setString(1, activity.getName());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if(resultSet.next())
                activity.setId(resultSet.getLong(1));

            Set<Long> articles = new HashSet<>(activity.getArticles());
            for(Long articleId : articles) {

                preparedStatement = connection.prepareStatement("SELECT * FROM articles where id = ?");
                preparedStatement.setLong(1, articleId);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {

                    preparedStatement = connection.prepareStatement("INSERT INTO articles_activities (article_id,activity_id) VALUES(?,?)");
                    preparedStatement.setLong(1, articleId);
                    preparedStatement.setLong(2, activity.getId());
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

        return activity;
    }

    @Override
    public Activity findActivity(String name) {
        Activity activity = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM activities where name = ?");
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                long id = resultSet.getLong("id");
                List<Long> articles = new ArrayList<>();
                preparedStatement = connection.prepareStatement("select article_id from articles_activities where activity_id = ?");
                preparedStatement.setLong(1, id);
                resultSet = preparedStatement.executeQuery();
                while(resultSet.next()) {
                    articles.add(resultSet.getLong("article_id"));
                }

                activity = new Activity(id, name, articles);
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

        return activity;
    }

    @Override
    public List<Activity> allActivities() {
        List<Activity> activities = new ArrayList<>();

        try (Connection connection = this.newConnection();
             Statement statement = connection.createStatement();
             Statement statement2 = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM activities")) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                Long id = resultSet.getLong("id");
                try (ResultSet articleResultSet = statement2.executeQuery("SELECT article_id FROM articles_activities WHERE activity_id = " + id)) {
                    List<Long> articles = new ArrayList<>();
                    while (articleResultSet.next()) {
                        articles.add(articleResultSet.getLong("article_id"));
                    }
                    activities.add(new Activity(id, name, articles));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return activities;
    }
}
