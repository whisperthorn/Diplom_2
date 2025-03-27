package site.stellarburgers.api.models.make.order;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MakeOrderPojo {
    private List<String> ingredients;
}
