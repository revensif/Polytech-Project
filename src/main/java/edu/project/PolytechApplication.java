package edu.project;

import com.jpro.webapi.JProApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PolytechApplication extends JProApplication {

    private Parent rootNode;

    @Override
    public void init() throws Exception {
        ConfigurableApplicationContext springContext = SpringApplication.run(PolytechApplication.class);
        FXMLLoader fxmlLoader = new FXMLLoader(PolytechApplication.class.getResource("polytech-project.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        rootNode = fxmlLoader.load();
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(rootNode, 600, 600);
        stage.setTitle("PolyTech Project!");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(true);
    }

    public static void main(String[] args) {
        launch();
    }
}