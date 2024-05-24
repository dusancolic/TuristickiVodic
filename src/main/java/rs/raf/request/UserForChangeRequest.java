package rs.raf.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import rs.raf.entities.UserType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
@Data
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class UserForChangeRequest {

    @NotNull(message = "Old email is required")
    @NotEmpty(message = "Old email is required")
    @Email
    private String oldEmail;
    @NotNull(message = "New email is required")
    @NotEmpty(message = "New email is required")
    @Email
    private String newEmail;
    @NotNull(message = "Name is required")
    @NotEmpty(message = "Name is required")
    private String name;
    @NotNull(message = "Surname is required")
    @NotEmpty(message = "Surname is required")
    private String surname;

    private UserType userType;
}
