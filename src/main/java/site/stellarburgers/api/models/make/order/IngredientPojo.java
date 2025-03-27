package site.stellarburgers.api.models.make.order;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class IngredientPojo {
    @SerializedName("_id")
    private String id;
    private String type;
}



