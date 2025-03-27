package site.stellarburgers.api.models.user.order;

import lombok.Data;
import java.util.List;

@Data
public class OrderPojo {
    private List<String> ingredients;
    private String _id;
    private String status;
    private int number;
    private String createdAt;
    private String updatedAt;
}

