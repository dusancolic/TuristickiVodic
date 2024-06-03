package rs.raf.repositories.user;

import rs.raf.entities.User;
import rs.raf.request.UserForChangeRequest;

import java.util.List;

public interface UserRepository {

    User findUser(String email);
    User addUser(User user);
    User changeActivityForUser(String email);
    List<User> allUsers(int page, int size);

    long countUsers();

    User changeUserData(UserForChangeRequest userForChangeRequest);

}
