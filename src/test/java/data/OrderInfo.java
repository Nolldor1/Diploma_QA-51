package data;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class OrderInfo {
    private String id;
    private String created;
    private String credit_id;
    private String payment_id;
}
