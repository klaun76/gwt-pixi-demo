package sk.mrtn.demo.pixi.client;

import elemental.client.Browser;
import elemental.css.CSSStyleDeclaration;
import sk.mrtn.pixi.client.Container;
import sk.mrtn.pixi.client.PIXI;
import sk.mrtn.pixi.client.PixiEntryPoint;
import sk.mrtn.pixi.client.Renderer;

/**
 * Created by martinliptak on 16/09/16.
 */
public class ADemo {

    protected PIXI pixi;
    protected Renderer renderer;
    protected Container stage;

    protected void createAndAppendStage(int width, int height) {
        this.stage = new Container();
        this.pixi = PixiEntryPoint.getPixi();
        renderer = this.pixi.autoDetectRenderer(width, height);
        Browser.getDocument().getBody().appendChild(renderer.view);
        renderer.view.getStyle().setPosition(CSSStyleDeclaration.Position.ABSOLUTE);
    }

    public static native void log(Object object) /*-{
        $wnd.console.log(object);
    }-*/;
}
