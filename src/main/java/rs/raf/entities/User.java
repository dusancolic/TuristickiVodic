package rs.raf.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.UniqueElements;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class User {
    private Long id;

    @NotNull(message = "Name field is required")
    @NotEmpty(message = "Name field is required")
    private String name;

    @NotNull(message = "Surname field is required")
    @NotEmpty(message = "Surname field is required")
    private String surname;

    @NotNull(message = "Email field is required")
    @NotEmpty(message = "Email field is required")
    @Email(message = "Email field is not valid")
    private String email;

    @NotNull(message = "Password field is required")
    @NotEmpty(message = "Password field is required")
    private String password;

    @NotNull(message = "UserType field is required")
    private UserType userType;
    private Boolean active = true;



}
