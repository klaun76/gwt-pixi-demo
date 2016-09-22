package sk.mrtn.demo.pixi.client;

import sk.mrtn.demo.pixi.client.common.IStage;
import sk.mrtn.pixi.client.Container;
import sk.mrtn.pixi.client.PIXI;
import sk.mrtn.pixi.client.Renderer;

/**
 * Created by martinliptak on 16/09/16.
 */
public class ADemo {

    protected PIXI pixi;
    protected IStage stage;

    protected ADemo(
            final IStage stage
    ){
        this.stage = stage;
    }
    public static native void log(Object object) /*-{
        $wnd.console.log(object);
    }-*/;
}
