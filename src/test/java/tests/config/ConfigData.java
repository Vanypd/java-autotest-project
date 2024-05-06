package tests.config;

import lombok.Getter;
import lombok.Setter;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import tests.config.pojo.Credentials;
import tests.config.pojo.OrderDetails;

import java.io.IOException;
import java.io.InputStream;

@Getter
@Setter
public class ConfigData {
    private Credentials credentials;
    private OrderDetails orderDetails;


    private ConfigData() {}


    public static synchronized ConfigData getInstance() {
        Class<?> clazz = ConfigData.class;
        String path = "config.yaml";
        Yaml yaml = new Yaml(new Constructor(clazz, new LoaderOptions()));

        try (InputStream inputStream = clazz.getClassLoader().getResourceAsStream(path)) {
            return yaml.load(inputStream);
        } catch (IOException e) {
            throw new IllegalArgumentException("");
        }
    }
}
