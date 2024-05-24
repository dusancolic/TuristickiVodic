package rs.raf.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class LoginRequest {

    @NotNull(message = "Email is required")
    @NotEmpty(message = "Email is required")
    private String email;

    @NotNull(message = "Password is required")
    @NotEmpty(message = "Password is required")
    private String password;

    public LoginRequest() {
    }

}
