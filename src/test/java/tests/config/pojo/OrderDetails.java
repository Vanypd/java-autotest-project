package tests.config.pojo;

import lombok.Getter;
import lombok.Setter;
import tests.config.ConfigData;

@Getter
@Setter
public class OrderDetails {
    private String firstName;
    private String lastName;
    private String country;
    private String address;
    private String city;
    private String state;
    private String postcode;
    private String phone;


    private OrderDetails() {}


    public static synchronized OrderDetails getInstance() {
        return ConfigData.getInstance().getOrderDetails();
    }
}
