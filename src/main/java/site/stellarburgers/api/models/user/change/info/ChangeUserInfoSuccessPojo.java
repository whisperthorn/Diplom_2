package site.stellarburgers.api.models.user.change.info;

import lombok.Data;
import site.stellarburgers.api.models.UserPojo;

@Data
public class ChangeUserInfoSuccessPojo {
    private boolean success;
    private UserPojo user;
}
