package mtgcollection.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

public class User {

    private int userId;

    @NotBlank(message = "Email cannot be blank.")
    @NotNull(message = "Email cannot be null.")
    @Email(message = "Email must be a valid address.")
    private String email;

    @NotBlank(message = "Password cannot be blank.")
    @NotNull(message = "Password cannot be null.")
    private String password;

    public User(){}

    public User(int userId, String email, String password) {
        this.userId = userId;
        this.email = email;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId && Objects.equals(email, user.email) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email, password);
    }

    public void hashPassword(){
        // Define a cost factor (work factor). Default is 10.
        int logRounds = 10;  // Increasing this value makes it more secure, but slower

        // Generate the salt
        // Salt is part of password helps 
        //  2**log_rounds.
        String salt = BCrypt.gensalt(logRounds);

        // Hash the password with the salt
        password = BCrypt.hashpw(password, salt);
    }

    public boolean checkPassword(String plainText){
        return BCrypt.checkpw(plainText,password);
    }
}
