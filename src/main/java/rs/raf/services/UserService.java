package rs.raf.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.apache.commons.codec.digest.DigestUtils;
import rs.raf.entities.User;
import rs.raf.repositories.user.UserRepository;
import rs.raf.request.UserForChangeRequest;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class UserService {
    @Inject
    private UserRepository userRepository;

    public String login(String email, String password)
    {
        String hashedPassword = DigestUtils.sha256Hex(password);

        User user = this.userRepository.findUser(email);
        if (user == null || !user.getPassword().equals(hashedPassword) || !user.getActive()) {
            System.out.println("User not found or password incorrect");
            return null;
        }

        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + 24*60*60*1000); // One day

        Algorithm algorithm = Algorithm.HMAC256("secret");

        // JWT-om mozete bezbedono poslati informacije na FE
        // Tako sto sve sto zelite da posaljete zapakujete u claims mapu
        return JWT.create()
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withSubject(email)
                .withClaim("user_type", user.getUserType().ordinal())
                .sign(algorithm);
    }

    public User register(User user)
    {
        return this.userRepository.addUser(user);
    }

    public User findUser(String email)
    {
        return this.userRepository.findUser(email);
    }

    public User changeActivityForUser(String email)
    {
        return this.userRepository.changeActivityForUser(email);
    }

    public List<User> allUsers()
    {
        return this.userRepository.allUsers();
    }

    public User changeUserData(UserForChangeRequest userForChangeRequest)
    {
        return this.userRepository.changeUserData(userForChangeRequest);
    }

    public boolean isAuthorized(String token){
        Algorithm algorithm = Algorithm.HMAC256("secret");
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);

        String email = jwt.getSubject();
//        jwt.getClaim("role").asString();

        User user = this.userRepository.findUser(email);

        if (user == null){
            return false;
        }

        return true;
    }
}
