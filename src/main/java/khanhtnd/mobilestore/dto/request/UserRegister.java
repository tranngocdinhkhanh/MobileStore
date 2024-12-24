package khanhtnd.mobilestore.dto.request;

import khanhtnd.mobilestore.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegister {
    private String username;
    private String password;

    public User mapToUser() {
        User user = new User();
        user.setUsername(this.getUsername());
        user.setPasswordHash(this.getPassword());
        return user;
    }
}
