package sk.mrtn.demo.pixi.client.common;

import elemental.dom.Node;
import sk.mrtn.library.client.ui.mainpanel.IRootResponsivePanel;
import sk.mrtn.pixi.client.Container;
import sk.mrtn.pixi.client.PIXI;
import sk.mrtn.pixi.client.PixiEntryPoint;
import sk.mrtn.pixi.client.Renderer;

import javax.inject.Inject;

/**
 * Created by martinliptak on 22/09/16.
 */
public class DefaultStage implements IStage {

    private final IRootResponsivePanel mainResponsivePanel;
    protected PIXI pixi;
    protected Renderer renderer;
    protected Container stage;
    private int width;
    private int height;
    private IResponsiveController responsiveStage;

    @Inject
    DefaultStage(
            final IRootResponsivePanel mainResponsivePanel
    ){
        this.mainResponsivePanel = mainResponsivePanel;
    }

    @Override
    public void onResized(double width, double height) {
        this.width = (int) width;
        this.height = (int) height;
        this.renderer.resize(width,height);
        if (this.responsiveStage != null) {
            this.responsiveStage.onResized(width, height);
        }
        this.render();
    }

    @Override
    public Node asNode() {
        return this.renderer.view;
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
        onDetached();
        this.responsiveStage = null;
        this.stage = stage;
    }

    private void onDetached() {
        if (this.responsiveStage != null) {
            this.responsiveStage.onDetached();
        }
    }

    @Override
    public void setResponsiveStage(IResponsiveController responsiveStage) {
        onDetached();
        this.responsiveStage = responsiveStage;
        this.stage = responsiveStage.getContainer();
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
        this.mainResponsivePanel.insert(this);
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
