package site.stellarburgers.api.models.user.change.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUserInfoPojo {
    private String email;
    private String name;
}
