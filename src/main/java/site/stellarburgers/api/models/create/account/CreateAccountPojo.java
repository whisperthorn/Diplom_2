package site.stellarburgers.api.models.create.account;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccountPojo {
    private String email;
    private String password;
    private String name;
}
