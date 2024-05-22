package rs.raf.repositories.user;

import rs.raf.entities.User;

import java.util.List;

public interface UserRepository {

    public User findUser(String email);
    public User addUser(User user);
    public User changeActivityForUser(String email);
    public List<User> allUsers();


}
