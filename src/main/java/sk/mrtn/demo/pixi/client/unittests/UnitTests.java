package sk.mrtn.demo.pixi.client.unittests;

import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.safehtml.shared.SafeUri;
import elemental.client.Browser;
import sk.mrtn.pixi.client.*;
import sk.mrtn.pixi.client.loaders.Loader;

import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by martinliptak on 12/09/16.
 */
public class UnitTests {


    private static IUnitTestResources RES = IUnitTestResources.impl;
    private static Logger LOG;
    static {
        if (LogConfiguration.loggingIsEnabled()) {
            LOG = Logger.getLogger(UnitTests.class.getSimpleName());
            LOG.setLevel(Level.ALL);
        }
    }

    private final SpriteFactory spriteFactory;

    private PIXI pixi;
    private Renderer renderer;
    private Container stage;

    @Inject
    UnitTests(
            final SpriteFactory spriteFactory
            ){
        this.spriteFactory = spriteFactory;
    }

    public void initialize() {
        LOG.info("INJECTION STARTED");
        loadResources();
    }


    private void loadResources() {
        Loader aLoader = new Loader();
        aLoader.add("test256",RES.testTextureSquare256().getSafeUri().asString());
        aLoader.add("test1024",RES.testTextureSquare1024().getSafeUri().asString());
        for (SafeUri safeUri : RES.unitTestAtlas().getSafeUris()) {
            aLoader.add(safeUri.asString());
        }
        aLoader.load((loader, resources) -> {
            buildStage();
        });
    }

    private void buildStage() {
        this.stage = new Container();
        this.pixi = PixiEntryPoint.getPixi();
        renderer = this.pixi.autoDetectRenderer(1024, 768);
        Browser.getDocument().getBody().appendChild(renderer.view);
        createTexturesFromImage();
        renderer.render(stage);
    }

    private void createTexturesFromImage() {
        Texture texture1024 = Texture.fromImage(RES.testTextureSquare1024().getSafeUri().asString());
        Sprite sprite1024 = this.spriteFactory.create(texture1024);
        this.stage.addChild(sprite1024);

        Texture texture254 = Texture.fromImage(RES.testTextureSquare256().getSafeUri().asString());
        Sprite sprite254 = this.spriteFactory.create(texture254);
        this.stage.addChild(sprite254);
    }


    public static native void log(Object object) /*-{
        $wnd.console.log(object);
    }-*/;



}
