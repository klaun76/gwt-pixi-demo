package sk.mrtn.demo.pixi.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Timer;
import elemental.client.Browser;
import jsinterop.annotations.JsMethod;
import sk.mrtn.pixi.client.*;
import sk.mrtn.pixi.client.filters.*;
import sk.mrtn.pixi.client.loaders.Loader;
import sk.mrtn.pixi.client.particles.Emitter;
import sk.mrtn.pixi.client.particles.ParticleContainer;
import sk.mrtn.pixi.client.particles.config.EmitterConfig;
import sk.mrtn.pixi.client.resources.textureatlas.TextureAtlasResource;

import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by klaun on 25/04/16.
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

    private PIXI pixi;
    private Renderer renderer;
    private Container stage;
    private Sprite bunnySprite;

    @FunctionalInterface
    private interface IButtonCommand{
        void run();
    }

    @Inject
    DemoPixi(
            final ParticleBuilder particleBuilder
            ){
        this.particleBuilder = particleBuilder;
//        InterfaceReader.parseObjectAndOutputToConsole( new BlurFilter(), "PIXI.filters.BlurFilter");
        loadResources();
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

        Sprite spices3 = createSprite(RES.spices3().getSafeUri().asString());
        stage.addChild(spices3);

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

        renderer.render(stage);
        Emitter object = new Emitter(bunnySprite2);
//        InterfaceReader.parseObjectAndOutputToConsole(new PathParticle(object), "PIXI.particles.AnimatedParticle");
        testFilters(spices3);
        testHandlers();
        testEmitters();
        testParticleBuilder();
    }

    private void testParticleBuilder() {
        String emitterConfig = RES.emitter().getText();
        TextureAtlasResource textureAtlasResource = RES.goldAnim();
        particleBuilder.initialize(emitterConfig,textureAtlasResource);
        stage.addChild(particleBuilder.getContainer());
        startEmit(particleBuilder.getEmitter(),renderer,stage);
        particleBuilder.getContainer().position.set(300,300);
    }
    private void testEmitters() {
        JavaScriptObject jConfig = JSONParser.parseStrict(RES.emitter().getText()).isObject().getJavaScriptObject();
        EmitterConfig config = PixiUtils.castToEmitter(jConfig);
        LOG.fine("EmitterConfig test: "+config.alpha.start+":"+config.alpha.end);
        Texture texture = Texture.fromImage(RES.bunny().getSafeUri().asString());
        ParticleContainer particleContainer = new ParticleContainer();
        stage.addChild(particleContainer);
        Emitter emitter = new Emitter(particleContainer, new Texture[]{texture}, config);
        particleContainer.position.set(300,300);
        startEmit(emitter,renderer,stage);
    }

    public static native void logg(Object object) /*-{
        $wnd.console.log(object);
    }-*/;

    public static native void startEmit(Emitter emitter, Renderer renderer, Container stage) /*-{
        // Calculate the current time
        var elapsed = Date.now();

        // Update function every frame
        var update = function(){

            // Update the next frame
            $wnd.requestAnimationFrame(update);

            var now = $wnd.Date.now();

            // The emitter requires the elapsed
            // number of seconds since the last update
            emitter.update((now - elapsed) * 0.001);
            elapsed = now;

            // Should re-render the PIXI Stage
             renderer.render(stage);
        };

        // Start emitting
        emitter.emit = true;

        // Start the update
        update();
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
