package sk.mrtn.demo.pixi.client.common;

import sk.mrtn.library.client.ui.mainpanel.IResponsivePanel;
import sk.mrtn.pixi.client.Container;
import sk.mrtn.pixi.client.PIXI;
import sk.mrtn.pixi.client.Renderer;

/**
 * Created by martinliptak on 22/09/16.
 */
public interface IStage extends IResponsivePanel {

    void initialize(int width, int height);

    void render();

    void setResponsiveStage(IResponsiveController responsiveStage);

    int getWidth();

    int getHeight();

}
