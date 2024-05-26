package rs.raf.repositories.destination;

import rs.raf.entities.Destination;
import rs.raf.entities.User;
import rs.raf.entities.UserType;
import rs.raf.repositories.MySqlAbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlDestinationRepository extends MySqlAbstractRepository implements DestinationRepository {
    @Override
    public Destination addDestination(Destination destination) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            String[] generatedColumns = {"id"};
            preparedStatement = connection.prepareStatement("INSERT INTO destinations (name,description) VALUES(?,?)", generatedColumns);
            preparedStatement.setString(1, destination.getName());
            preparedStatement.setString(2, destination.getDescription());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if(resultSet.next())
                destination.setId(resultSet.getLong(1));


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return destination;
    }

    @Override
    public Destination findDestination(String name) {
        Destination destination = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM destinations where name = ?");
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                Long id = resultSet.getLong("id");
                String description = resultSet.getString("description");
                destination = new Destination(id, name, description);
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

        return destination;
    }

    @Override
    public List<Destination> allDestinations() {
        List<Destination> destinations = new ArrayList<>();

        String query = "SELECT * FROM destinations";
        try (Connection connection = this.newConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                destinations.add(new Destination(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return destinations;
    }

    @Override
    public String removeDestination(Destination destination) {

        String checkQuery = "SELECT 1 FROM articles WHERE destination_id = ?";
        String deleteQuery = "DELETE FROM destinations WHERE name = ?";
        try (Connection connection = this.newConnection();
             PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
             PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {

            checkStatement.setLong(1, destination.getId());
            try (ResultSet resultSet = checkStatement.executeQuery()) {
                if (resultSet.next()) {
                    return "Destination is used in articles and can't be removed";
                }
            }

            deleteStatement.setString(1, destination.getName());
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error occurred while removing the destination";
        }
        return "Destination removed";
    }

    @Override
    public Destination updateDestination(Destination destination) {
        Long id = destination.getId();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("UPDATE destinations SET name = ?, description = ? WHERE id = ?");
            preparedStatement.setString(1, destination.getName());
            preparedStatement.setString(2, destination.getDescription());
            preparedStatement.setLong(3, id);
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeConnection(connection);
        }

        return destination;
    }
}
