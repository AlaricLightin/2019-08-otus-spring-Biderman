package ru.biderman.tetris;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.biderman.tetris.gui.MainFrame;

import java.awt.*;

@SpringBootApplication
@IntegrationComponentScan
@ConfigurationPropertiesScan
@EnableScheduling
public class TetrisApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = new SpringApplicationBuilder(TetrisApplication.class)
                .headless(false).run(args);

        EventQueue.invokeLater(() -> {
                    MainFrame ex = ctx.getBean(MainFrame.class);
                    ex.setVisible(true);
        });
    }

}