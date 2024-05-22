package rs.raf.repositories.user;

import org.apache.commons.codec.digest.DigestUtils;
import rs.raf.entities.User;
import rs.raf.entities.UserType;
import rs.raf.repositories.MySqlAbstractRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlUserRepository extends MySqlAbstractRepository implements UserRepository{
    @Override
    public User findUser(String email) {
        User user = null;

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            preparedStatement = connection.prepareStatement("SELECT * FROM users where email = ?");
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                String password = resultSet.getString("password");
                UserType userType = UserType.valueOf(resultSet.getString("user_type").toUpperCase());
                Boolean active = resultSet.getBoolean("active");

                user = new User(id, name, surname, email, password, userType, active);
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

        return user;
    }

    @Override
    public User addUser(User user) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            String[] generatedColumns = {"id"};
            String hashedPassword = DigestUtils.sha256Hex(user.getPassword());

            preparedStatement = connection.prepareStatement("INSERT INTO users (name,surname,email,password,active,user_type) VALUES(?,?,?,?,?,?)", generatedColumns);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, hashedPassword);
            preparedStatement.setBoolean(5, user.getActive());
            preparedStatement.setString(6, user.getUserType().toString());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();

            if(resultSet.next())
            {
                user.setId(resultSet.getLong(1));
                user.setPassword(hashedPassword);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(preparedStatement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return user;
    }

    @Override
    public User changeActivityForUser(String email) {

        User user = findUser(email);
        if(user != null)
        {
            Connection connection = null;
            PreparedStatement preparedStatement = null;
            try {
                connection = this.newConnection();

                preparedStatement = connection.prepareStatement("UPDATE users SET active = ? WHERE email = ?");
                preparedStatement.setBoolean(1, !user.getActive());
                preparedStatement.setString(2, email);
                preparedStatement.executeUpdate();

                user.setActive(!user.getActive());

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                this.closeStatement(preparedStatement);
                this.closeConnection(connection);
            }
        }
        return user;
    }

    @Override
    public List<User> allUsers() {
        List<User> posts = new ArrayList<>();

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = this.newConnection();

            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from users");
            while (resultSet.next()) {
                posts.add(new User(resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        UserType.valueOf(resultSet.getString("user_type").toUpperCase()),
                        resultSet.getBoolean("active")))
                        ;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.closeStatement(statement);
            this.closeResultSet(resultSet);
            this.closeConnection(connection);
        }

        return posts;
    }
}
