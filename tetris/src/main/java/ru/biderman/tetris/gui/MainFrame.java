package ru.biderman.tetris.gui;

import org.springframework.stereotype.Component;
import ru.biderman.tetris.model.Field;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

@Component
public class MainFrame extends JFrame {
    private GameFieldPanel gameFieldPanel;
    private JLabel endGameLabel;
    private InterfaceEvents events;
    private InterfaceCommands commands = null;

    public MainFrame() throws HeadlessException {
        initUI();
    }

    private void initUI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Тетрис");
        this.setSize(600,600);
        gameFieldPanel = new GameFieldPanel(new FlowLayout());
        this.add(gameFieldPanel);
        setActions(gameFieldPanel);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));
        controlPanel.setBorder(new BevelBorder(BevelBorder.RAISED));

        this.add(controlPanel, BorderLayout.EAST);
        endGameLabel = new JLabel("Конец игры");
        endGameLabel.setForeground(Color.RED);
        endGameLabel.setVisible(false);
        controlPanel.add(endGameLabel);
        controlPanel.add(new JLabel(" Движение вправо/влево/вниз - стрелки "));
        controlPanel.add(new JLabel(" Поворот - стрелка вверх"));
        controlPanel.add(new JLabel(" s - запуск, p - пауза"));
        controlPanel.add(new JLabel(" n - начать заново"));

        events = new Events();
    }

    private void setActions(JPanel jPanel) {
        InputMap inputMap = jPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = jPanel.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "left");
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "right");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "down");
        inputMap.put(KeyStroke.getKeyStroke("UP"), "rotate");
        inputMap.put(KeyStroke.getKeyStroke('s'), "start");
        inputMap.put(KeyStroke.getKeyStroke('p'), "pause");
        inputMap.put(KeyStroke.getKeyStroke('n'), "new");

        actionMap.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (commands != null)
                    commands.left();
            }
        });

        actionMap.put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (commands != null)
                    commands.right();
            }
        });

        actionMap.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (commands != null)
                    commands.down();
            }
        });

        actionMap.put("rotate", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (commands != null)
                    commands.rotate();
            }
        });

        actionMap.put("start", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (commands != null)
                    commands.start();
            }
        });
        actionMap.put("pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (commands != null)
                    commands.pause();
            }
        });

        actionMap.put("new", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (commands != null)
                    commands.newGame();

                endGameLabel.setVisible(false);
            }
        });
    }

    public InterfaceEvents getEvents() {
        return events;
    }

    public void setCommands(InterfaceCommands commands) {
        this.commands = commands;
    }

    private class Events implements InterfaceEvents {
        @Override
        public void onUpdateField(Field field) {
            EventQueue.invokeLater(() -> gameFieldPanel.setField(field));
        }

        @Override
        public void onEndGame() {
            EventQueue.invokeLater(() -> endGameLabel.setVisible(true));
        }
    }
}
