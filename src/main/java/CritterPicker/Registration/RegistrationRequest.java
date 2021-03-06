package CritterPicker.Registration;

import CritterPicker.Enums.Hemisphere;
import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RegistrationRequest {

    @NotNull(message = "This field is required!")
    @Size(min = 5, max = 50, message = "Username should be between 5 to 50 characters long")
    private String username;
    @NotNull(message = "This field is required!")
    @Size(min = 8, max = 50, message = "Password should be between 8 to 50 characters long")
    private String password;
    @NotNull(message = "This field is required!")
    @Email
    private String email;
    private String adminPassword;
    private Hemisphere hemisphere;
}
