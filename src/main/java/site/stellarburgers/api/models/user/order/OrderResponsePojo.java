package site.stellarburgers.api.models.user.order;

import lombok.Data;
import java.util.List;

@Data
public class OrderResponsePojo {
    private boolean success;
    private List<OrderPojo> orders;
    private int total;
    private int totalToday;
}
