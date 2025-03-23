package site.stellarburgers.api.models.user.authentication;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginPojo {
    private String email;
    private String password;
}
