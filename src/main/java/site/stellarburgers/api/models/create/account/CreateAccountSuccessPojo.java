package site.stellarburgers.api.models.create.account;

import lombok.Data;
import lombok.AllArgsConstructor;
import site.stellarburgers.api.models.UserPojo;

@Data
@AllArgsConstructor
public class CreateAccountSuccessPojo {
    private boolean success;
    private UserPojo user;
    private String accessToken;
    private String refreshToken;
}
