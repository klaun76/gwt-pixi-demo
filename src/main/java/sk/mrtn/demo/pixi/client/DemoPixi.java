package sk.mrtn.demo.pixi.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Timer;
import elemental.client.Browser;
import elemental.css.CSSStyleDeclaration;
import elemental.html.DivElement;
import jsinterop.annotations.JsMethod;
import sk.mrtn.pixi.client.*;
import sk.mrtn.pixi.client.filters.ColorMatrixFilter;
import sk.mrtn.pixi.client.loaders.Loader;
import sk.mrtn.pixi.client.particles.Emitter;
import sk.mrtn.pixi.client.particles.ParticleContainer;
import sk.mrtn.pixi.client.particles.config.EmitterConfig;
import sk.mrtn.pixi.client.resources.textureatlas.TextureAtlasResource;
import sk.mrtn.pixi.client.ticker.ITickable;
import sk.mrtn.pixi.client.ticker.Ticker;

import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by klaun on 25/04/16.
 * This class is supposed to be main entry point for whole project
 */
public class DemoPixi {

    private static IResources RES = IResources.impl;
    private static Logger LOG;
    static {
        if (LogConfiguration.loggingIsEnabled()) {
            LOG = Logger.getLogger(DemoPixi.class.getSimpleName());
            LOG.setLevel(Level.ALL);
        }
    }

    private final ParticleBuilder particleBuilder;
    private final Ticker ticker;

    private PIXI pixi;
    private Renderer renderer;
    private Container stage;
    private Sprite bunnySprite;
    private final DivElement fpsNode;

    @FunctionalInterface
    private interface IButtonCommand{
        void run();
    }

    @Inject
    DemoPixi(
            final ParticleBuilder particleBuilder,
            final Ticker ticker
            ){
        this.particleBuilder = particleBuilder;
        this.ticker = ticker;
//        InterfaceReader.parseObjectAndOutputToConsole( new Stats(), "Stats");
        loadResources();

        fpsNode = Browser.getDocument().createDivElement();
        fpsNode.getStyle().setPosition(CSSStyleDeclaration.Position.ABSOLUTE);
        Browser.getDocument().getBody().appendChild(fpsNode);

    }


    private void loadResources() {
        Loader aLoader = new Loader();
        aLoader.add("bunny",RES.bunny().getSafeUri().asString());
        aLoader.add("spices3",RES.spices3().getSafeUri().asString());
        for (SafeUri safeUri : RES.goldAnim().getSafeUris()) {
            aLoader.add(safeUri.asString());
        }
        aLoader.load((loader, resources) -> {
            log(loader,resources);
            buildStage();
        });
    }

    private void buildStage() {
        stage = new Container();
        this.pixi = PixiEntryPoint.getPixi();
        renderer = this.pixi.autoDetectRenderer(800, 600);
        Browser.getDocument().getBody().appendChild(renderer.view);

        Sprite background = createBackground();

        createBunnies();

        renderer.render(stage);

        testFilters(background);
        testHandlers();
        testEmitters();
        testParticleBuilder();
        ticker.add(difference -> {
            double fps = Math.round((1000 / ticker.elapsedMS) * 100.0) / 100.0;
            fpsNode.setTextContent("fps: "+fps);
            renderer.render(stage);
        });
        ticker.start();
    }

    private Sprite createBackground() {
        Sprite background = createSprite(RES.spices3().getSafeUri().asString());
        stage.addChild(background);
        return background;
    }

    private void createBunnies() {
        bunnySprite = createSprite(RES.bunny().getSafeUri().asString());
        bunnySprite.name = "bunny1";
        bunnySprite.position.set(100,100);
        bunnySprite.anchor.set(0.5,0.5);
        stage.addChild(bunnySprite);

        Sprite bunnySprite2 = createSprite(RES.bunny().getSafeUri().asString());
        bunnySprite2.position.set(200,100);
        bunnySprite2.anchor.set(0.5,0.5);
        bunnySprite2.name = "bunny2";
        stage.addChild(bunnySprite2);
    }

    private void testParticleBuilder() {
        String emitterConfig = RES.emitter().getText();
        TextureAtlasResource textureAtlasResource = RES.goldAnim();
        particleBuilder.initialize(emitterConfig,textureAtlasResource);
        stage.addChild(particleBuilder.getContainer());
        particleBuilder.getContainer().position.set(300,300);
        this.ticker.add(difference -> particleBuilder.getEmitter().update(DemoPixi.this.ticker.elapsedMS * 0.001));
    }

    private void testEmitters() {
        EmitterConfig config = EmitterConfig.parse(RES.emitter().getText());
        LOG.fine("EmitterConfig test: "+config.alpha.start+":"+config.alpha.end);
        Texture texture = Texture.fromImage(RES.bunny().getSafeUri().asString());
        ParticleContainer particleContainer = new ParticleContainer();
        stage.addChild(particleContainer);
        Emitter emitter = new Emitter(particleContainer, new Texture[]{texture}, config);
        particleContainer.position.set(300,300);
        this.ticker.add(difference -> {
            emitter.update(DemoPixi.this.ticker.elapsedMS * 0.001);
        });
    }

    public static native void logg(Object object) /*-{
        $wnd.console.log(object);
    }-*/;

    private void testHandlers() {
        bunnySprite.interactive = true;
        bunnySprite.mousedown = (eventData) -> LOG.fine("mousedown "+eventData.target.name);
        bunnySprite.click = eventData -> LOG.fine("click "+eventData.target.name);
    }

    private void testFilters(Sprite sprite) {
        ColorMatrixFilter filter = new ColorMatrixFilter();
        filter.predator(0.5,true);
        sprite.addFilter(filter);
        renderer.render(stage);

        new Timer() {
            @Override
            public void run() {
                ColorMatrixFilter colorMatrixFilter = new ColorMatrixFilter();
                colorMatrixFilter.lsd(false);
                bunnySprite.addFilter(colorMatrixFilter);
                renderer.render(stage);
            }
        }.schedule(1000);

        new Timer() {
            @Override
            public void run() {
                filter.reset();
                filter.kodachrome(true);
                renderer.render(stage);
            }
        }.schedule(2000);

        new Timer() {
            @Override
            public void run() {
                bunnySprite.scale = new Point(2,2);
                renderer.render(stage);
            }
        }.schedule(3000);
    }

    private void testFilters() {

    }
    @JsMethod(namespace = "console")
    public static native void log(Object... object);

    private Sprite createSprite(String textureName) {
        Texture texture = Texture.fromImage(textureName);
        Sprite sprite = new Sprite(texture);
        sprite.anchor.x = 0;
        sprite.anchor.y = 0;
        sprite.position.x = 0;
        sprite.position.y = 0;
        return sprite;
    }

    public void initialize() {
        LOG.info("INJECTION STARTED");
    }

}
