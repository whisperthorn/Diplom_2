package site.stellarburgers.api.models.user.authentication;

import lombok.Data;
import site.stellarburgers.api.models.UserPojo;

@Data
public class LoginSuccessPojo {
    private boolean success;
    private String accessToken;
    private String refreshToken;
    private UserPojo user;
}
