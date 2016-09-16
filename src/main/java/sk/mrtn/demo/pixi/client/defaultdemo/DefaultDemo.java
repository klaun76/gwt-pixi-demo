package sk.mrtn.demo.pixi.client.defaultdemo;

import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Timer;
import elemental.client.Browser;
import elemental.css.CSSStyleDeclaration;
import elemental.html.DivElement;
import jsinterop.annotations.JsMethod;
import sk.mrtn.demo.pixi.client.*;
import sk.mrtn.pixi.client.*;
import sk.mrtn.pixi.client.filters.ColorMatrixFilter;
import sk.mrtn.pixi.client.loaders.Loader;
import sk.mrtn.pixi.client.particles.AnimatedParticleArtTextureNames;
import sk.mrtn.pixi.client.particles.Emitter;
import sk.mrtn.pixi.client.particles.RepetitiveTexture;
import sk.mrtn.pixi.client.particles.config.EmitterConfig;
import sk.mrtn.pixi.client.resources.textureatlas.TextureAtlasResource;
import sk.mrtn.pixi.client.ticker.Ticker;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by martinliptak on 12/09/16.
 */
public class DefaultDemo extends ADemo {


    private static Logger LOG;
    static {
        if (LogConfiguration.loggingIsEnabled()) {
            LOG = Logger.getLogger(DefaultDemo.class.getSimpleName());
            LOG.setLevel(Level.ALL);
        }
    }

    private final ParticleBuilder particleBuilder;
    private final MultiParticleBuilder multiParticleBuilder;
    private final Ticker ticker;

    private Sprite bunnySprite;
    private final DivElement fpsNode;

    @FunctionalInterface
    private interface IButtonCommand{
        void run();
    }
    @Inject
    DefaultDemo(
            final ParticleBuilder particleBuilder,
            final MultiParticleBuilder multiParticleBuilder,
            final Ticker ticker
    ){
        this.particleBuilder = particleBuilder;
        this.multiParticleBuilder = multiParticleBuilder;
        this.ticker = ticker;
//        InterfaceReader.parseObjectAndOutputToConsole( new Stats(), "Stats");
        loadResources();

        fpsNode = Browser.getDocument().createDivElement();
        fpsNode.getStyle().setPosition(CSSStyleDeclaration.Position.ABSOLUTE);
        Browser.getDocument().getBody().appendChild(fpsNode);
    }



    private void loadResources() {
        Loader aLoader = new Loader();
        aLoader.add("bunny",DemoPixi.RES.bunny().getSafeUri().asString());
        aLoader.add("spices3",DemoPixi.RES.spices3().getSafeUri().asString());
        for (SafeUri safeUri : DemoPixi.RES.goldAnim().getSafeUris()) {
            aLoader.add(safeUri.asString());
        }
        for (SafeUri safeUri : DemoPixi.RES.bubblesAndCoinAnims().getSafeUris()) {
            aLoader.add(safeUri.asString());
        }
        aLoader.load((loader, resources) -> {
//            log(loader,resources);
            buildStage();
        });
    }

    private void buildStage() {
        createAndAppendStage(800,600);

        Sprite background = createBackground();

        createBunnies();

        renderer.render(stage);

        testFilters(background);
        testHandlers();

        testEmitters();
//        testParticleBuilder();
        testMultiEmitters();

        ticker.add(difference -> {
            double fps = Math.round((1000 / ticker.elapsedMS) * 100.0) / 100.0;
            fpsNode.setTextContent("fps: "+fps);
            renderer.render(stage);
        });
        ticker.start();
    }

    private Sprite createBackground() {
        Sprite background = createSprite(DemoPixi.RES.spices3().getSafeUri().asString());
        stage.addChild(background);
        return background;
    }

    private void createBunnies() {
        bunnySprite = createSprite(DemoPixi.RES.bunny().getSafeUri().asString());
        bunnySprite.name = "bunny1";
        bunnySprite.position.set(100,100);
        bunnySprite.anchor.set(0.5,0.5);
        stage.addChild(bunnySprite);

        Sprite bunnySprite2 = createSprite(DemoPixi.RES.bunny().getSafeUri().asString());
        bunnySprite2.position.set(200,100);
        bunnySprite2.anchor.set(0.5,0.5);
        bunnySprite2.name = "bunny2";
        stage.addChild(bunnySprite2);
    }

    private void testParticleBuilder() {
        String emitterConfig = DemoPixi.RES.goldEmitter().getText();
        TextureAtlasResource textureAtlasResource = DemoPixi.RES.goldAnim();
        particleBuilder.initialize(emitterConfig,textureAtlasResource);
        stage.addChild(particleBuilder.getContainer());
        particleBuilder.getContainer().position.set(300,300);
        this.ticker.add(difference -> particleBuilder.getEmitter().update(DefaultDemo.this.ticker.elapsedMS * 0.001));
    }

    private void testEmitters() {
        EmitterConfig config = EmitterConfig.parse(DemoPixi.RES.goldEmitter().getText());
        LOG.fine("EmitterConfig test: "+config.color.start+":"+config.color.end);
        Texture texture = Texture.fromImage(DemoPixi.RES.bunny().getSafeUri().asString());
        Container particleContainer = new Container();
        stage.addChild(particleContainer);
        Emitter emitter = new Emitter(particleContainer, new Texture[]{texture}, config);
        particleContainer.position.set(300,300);
        this.ticker.add(difference -> {
            emitter.update(DefaultDemo.this.ticker.elapsedMS * 0.001);
        });
    }

    /**
     * showcase method for using two emitters inthe same ParticleContainer
     */
    private void testMultiEmitters() {

        Map<String,List<AnimatedParticleArtTextureNames>> configToArtsMap = new HashMap<>();

        String emitterConfig1 = DemoPixi.RES.goldEmitter().getText();
        String emitterConfig2 = DemoPixi.RES.bubblesEmitter().getText();
        List<AnimatedParticleArtTextureNames> arts1 = new ArrayList<>();
        List<AnimatedParticleArtTextureNames> arts2 = new ArrayList<>();
//        List<String> frames = DemoPixi.RES.bubblesAndCoinAnims().getFrames();

        //coins anim
        AnimatedParticleArtTextureNames art1 = new AnimatedParticleArtTextureNames();
        art1.setFramerateToMatchLife();
        art1.loop = true;

        //bubble anim
        AnimatedParticleArtTextureNames art2 = new AnimatedParticleArtTextureNames();
        art2.setFramerateToMatchLife();
        art2.loop = false;

        art1.addTexture("coin_anim_00.png");
        art1.addTexture("coin_anim_01.png");
        art1.addTexture("coin_anim_02.png");
        art1.addTexture("coin_anim_03.png");
        art1.addTexture("coin_anim_04.png");
        art1.addTexture("coin_anim_05.png");

        art2.addTexture(new RepetitiveTexture("buble_anim_00.png",40));
        art2.addTexture("buble_anim_01.png");
        art2.addTexture("buble_anim_02.png");
        art2.addTexture("buble_anim_03.png");

        arts1.add(art1);
        arts2.add(art2);
        configToArtsMap.put(emitterConfig1,arts1);
        configToArtsMap.put(emitterConfig2,arts2);

        multiParticleBuilder.initialize(configToArtsMap);
        stage.addChild(multiParticleBuilder.getContainer());
        multiParticleBuilder.getContainer().position.set(stage.getBounds().width/2, stage.getBounds().height/2);
        this.ticker.add(difference -> multiParticleBuilder.update(DefaultDemo.this.ticker.elapsedMS * 0.001));
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
