package site.stellarburgers.api.models.make.order;

import lombok.Data;

import java.util.List;

@Data
public class IngredientListPojo {
    private boolean success;
    private List<IngredientPojo> data;
}
