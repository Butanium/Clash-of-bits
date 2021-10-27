package view.UI;

import com.codingame.gameengine.module.entities.Entity;
import com.codingame.gameengine.module.entities.GraphicEntityModule;
import com.codingame.gameengine.module.entities.Group;

import static com.codingame.game.Constants.HEALTH_BAR_HEIGHT;
import static com.codingame.game.Constants.HEALTH_BAR_WIDTH;

public class ProgressBar {
    private final Group barGroup;
    private final Entity<?> bar;

    public ProgressBar(int color, GraphicEntityModule graphicEntityModule) {
        bar = graphicEntityModule.createRectangle().setFillColor(color).setLineWidth(0).setAlpha(0.8)
                .setWidth(HEALTH_BAR_WIDTH).setHeight(HEALTH_BAR_HEIGHT).setZIndex(7);
        Group group = graphicEntityModule.createGroup(graphicEntityModule.createRectangle().setFillColor(0x777777).setAlpha(.5)
                .setWidth(HEALTH_BAR_WIDTH).setHeight(HEALTH_BAR_HEIGHT), bar);
        for (int i = 0; i < 5; i++) {
            group.add(graphicEntityModule.createRectangle().setHeight(HEALTH_BAR_HEIGHT).setWidth(2)
                    .setX(group.getX() + i * HEALTH_BAR_WIDTH / 4).setFillColor(0).setAlpha(0.5).setZIndex(8));
        }
        barGroup = group;
    }

    public Group getBarGroup() {
        return barGroup;
    }

    public void setBar(double v) {
        bar.setScaleX(v);
    }


}