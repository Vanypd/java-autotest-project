package tests.config.pojo;

import lombok.Getter;
import lombok.Setter;
import tests.config.ConfigData;

@Getter
@Setter
public class Credentials {
    private String username;
    private String email;
    private String password;


    private Credentials() {}


    public static synchronized Credentials getInstance() {
        return ConfigData.getInstance().getCredentials();
    }
}
