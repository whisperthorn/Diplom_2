package site.stellarburgers.api.models.make.order;

import lombok.Data;
import site.stellarburgers.api.models.OrderNumberPojo;

@Data
public class MakeOrderSuccessPojo {
    private String name;
    private OrderNumberPojo order;
    private boolean success;
}
