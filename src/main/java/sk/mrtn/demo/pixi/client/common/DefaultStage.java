package sk.mrtn.demo.pixi.client.common;

import elemental.client.Browser;
import elemental.css.CSSStyleDeclaration;
import sk.mrtn.pixi.client.Container;
import sk.mrtn.pixi.client.PIXI;
import sk.mrtn.pixi.client.PixiEntryPoint;
import sk.mrtn.pixi.client.Renderer;

import javax.inject.Inject;

/**
 * Created by martinliptak on 22/09/16.
 */
public class DefaultStage implements IStage {

    protected PIXI pixi;
    protected Renderer renderer;
    protected Container stage;
    private int width;
    private int height;

    @Inject
    DefaultStage(){
    }

    @Override
    public PIXI getPixi() {
        return pixi;
    }

    @Override
    public Renderer getRenderer() {
        return renderer;
    }

    @Override
    public Container getStage() {
        return stage;
    }

    @Override
    public void setStage(Container stage) {
        this.stage = stage;
    }

    @Override
    public void render() {
        if (this.stage != null) {
            this.renderer.render(this.stage);
        }
    }

    @Override
    public void initialize(int width, int height) {
        this.width = width;
        this.height = height;
        this.stage = new Container();
        this.pixi = PixiEntryPoint.getPixi();
        this.renderer = this.pixi.autoDetectRenderer(width, height);
        Browser.getDocument().getBody().appendChild(renderer.view);
        // TODO: figure out why next line of code fails when built
//        this.renderer.view.getStyle().setPosition(CSSStyleDeclaration.Position.ABSOLUTE);
    }

    public static native void log(Object object) /*-{
        $wnd.test = object;
        $wnd.console.log(object);
    }-*/;

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
