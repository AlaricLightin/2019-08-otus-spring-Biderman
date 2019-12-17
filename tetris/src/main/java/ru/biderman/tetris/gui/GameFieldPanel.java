package ru.biderman.tetris.gui;

import ru.biderman.tetris.model.Field;

import javax.swing.*;
import java.awt.*;

class GameFieldPanel extends JPanel {
    private Field field;

    GameFieldPanel(LayoutManager layout) {
        super(layout);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGameField(g);
    }

    private void drawGameField(Graphics g) {
        if (field == null)
            return;

        GraphicParams gp = new GraphicParams(this.getVisibleRect().width, this.getVisibleRect().height,
                field.getColumnCount(), field.getRowCount());

        g.drawLine(gp.x0 - 1, gp.y0 - 1, gp.x0 - 1, gp.y1 + 1);
        g.drawLine(gp.x0 - 1, gp.y1 + 1, gp.x1 + 1, gp.y1 + 1);
        g.drawLine(gp.x1 + 1, gp.y1 + 1, gp.x1 + 1, gp.y0 - 1);

        for (int i = 0; i < field.getColumnCount(); i++) {
            for (int j = 0; j < field.getRowCount(); j++) {
                if (field.isCellOccupied(i, j))
                    g.drawRect(gp.x0 + i * gp.squareSide + 1, gp.y0 + j * gp.squareSide + 1,
                            gp.squareSide - 2, gp.squareSide - 2);
            }
        }
    }

    void setField(Field field) {
        this.field = field;
        repaint();
    }

    private static class GraphicParams {
        private final int x0;
        private final int y0;
        private final int x1;
        private final int y1;
        private final int squareSide;

        GraphicParams(int width, int height, int columnCount, int rowCount) {
            squareSide = Math.min(width / columnCount, height / rowCount);
            x0 = (width - columnCount * squareSide) / 2;
            y0 = (height - rowCount * squareSide) / 2;

            x1 = x0 + columnCount * squareSide;
            y1 = y0 + rowCount * squareSide;
        }
    }
}
