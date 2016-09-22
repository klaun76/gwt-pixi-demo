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
    public void render() {
        this.renderer.render(this.stage);
    }

    @Override
    public void initialize(int width, int height) {
        this.stage = new Container();
        this.pixi = PixiEntryPoint.getPixi();
        this.renderer = this.pixi.autoDetectRenderer(width, height);
        Browser.getDocument().getBody().appendChild(renderer.view);
        this.renderer.view.getStyle().setPosition(CSSStyleDeclaration.Position.ABSOLUTE);
    }

}
