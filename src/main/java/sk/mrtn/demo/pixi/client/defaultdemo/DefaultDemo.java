package sk.mrtn.demo.pixi.client.defaultdemo;

import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Timer;
import jsinterop.annotations.JsMethod;
import sk.mrtn.demo.pixi.client.ADemo;
import sk.mrtn.demo.pixi.client.DemoPixi;
import sk.mrtn.demo.pixi.client.MultiParticleBuilder;
import sk.mrtn.demo.pixi.client.ParticleBuilder;
import sk.mrtn.demo.pixi.client.common.IStage;
import sk.mrtn.library.client.ticker.ITickable;
import sk.mrtn.library.client.ticker.ITicker;
import sk.mrtn.pixi.client.Container;
import sk.mrtn.pixi.client.Point;
import sk.mrtn.pixi.client.Sprite;
import sk.mrtn.pixi.client.Texture;
import sk.mrtn.pixi.client.filters.ColorMatrixFilter;
import sk.mrtn.pixi.client.loaders.Loader;
import sk.mrtn.pixi.client.particles.AnimatedParticleArtTextureNames;
import sk.mrtn.pixi.client.particles.Emitter;
import sk.mrtn.pixi.client.particles.RepetitiveTexture;
import sk.mrtn.pixi.client.particles.config.EmitterConfig;
import sk.mrtn.pixi.client.resources.textureatlas.TextureAtlasResource;

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
    private final ITicker ticker;

    private Sprite bunnySprite;

    @Inject
    DefaultDemo(
            final IStage stage,
            final ParticleBuilder particleBuilder,
            final MultiParticleBuilder multiParticleBuilder,
            final ITicker ticker
    ){
        super(stage);
        this.particleBuilder = particleBuilder;
        this.multiParticleBuilder = multiParticleBuilder;
        this.ticker = ticker;
        loadResources();

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

        Sprite background = createBackground();

        createBunnies();

        this.stage.render();

        testFilters(background);

        testHandlers();

        testEmitters();

        testParticleBuilder();

        testMultiEmitters();

        this.ticker.addTickable(new ITickable() {
            @Override
            public void update(ITicker ticker) {
                stage.render();
            }

            @Override
            public boolean shouldTick() {
                return false;
            }
        });
        this.ticker.start();
    }

    private Sprite createBackground() {
        Sprite background = createSprite(DemoPixi.RES.spices3().getSafeUri().asString());
        stage.getStage().addChild(background);
        return background;
    }

    private void createBunnies() {
        bunnySprite = createSprite(DemoPixi.RES.bunny().getSafeUri().asString());
        bunnySprite.name = "bunny1";
        bunnySprite.position.set(100,100);
        bunnySprite.anchor.set(0.5,0.5);
        stage.getStage().addChild(bunnySprite);

        Sprite bunnySprite2 = createSprite(DemoPixi.RES.bunny().getSafeUri().asString());
        bunnySprite2.position.set(200,100);
        bunnySprite2.anchor.set(0.5,0.5);
        bunnySprite2.name = "bunny2";
        stage.getStage().addChild(bunnySprite2);
    }

    private void testParticleBuilder() {
        String emitterConfig = DemoPixi.RES.goldEmitter().getText();
        TextureAtlasResource textureAtlasResource = DemoPixi.RES.goldAnim();
        particleBuilder.initialize(emitterConfig,textureAtlasResource);
        stage.getStage().addChild(particleBuilder.getContainer());
        particleBuilder.getContainer().position.set(300,300);

        this.ticker.addTickable(new ITickable() {
            @Override
            public void update(ITicker ticker) {
                particleBuilder.getEmitter().update(ticker.getDeltaTick() * 0.001);
            }

            @Override
            public boolean shouldTick() {
                return true;
            }
        });
    }

    private void testEmitters() {
        EmitterConfig config = EmitterConfig.parse(DemoPixi.RES.goldEmitter().getText());
        LOG.fine("EmitterConfig test: "+config.color.start+":"+config.color.end);
        Texture texture = Texture.fromImage(DemoPixi.RES.bunny().getSafeUri().asString());
        Container particleContainer = new Container();
        stage.getStage().addChild(particleContainer);
        Emitter emitter = new Emitter(particleContainer, new Texture[]{texture}, config);
        particleContainer.position.set(300,300);
        this.ticker.addTickable(new ITickable() {
            @Override
            public void update(ITicker ticker) {
                emitter.update(ticker.getDeltaTick() * 0.001);
            }

            @Override
            public boolean shouldTick() {
                return true;
            }
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
        stage.getStage().addChild(multiParticleBuilder.getContainer());
        multiParticleBuilder.getContainer().position.set(stage.getStage().getBounds().width/2, stage.getStage().getBounds().height/2);
//        this.ticker.add(difference -> multiParticleBuilder.update(DefaultDemo.this.ticker.elapsedMS * 0.001));
        this.ticker.addTickable(new ITickable() {
            @Override
            public void update(ITicker ticker) {
                multiParticleBuilder.update(ticker.getDeltaTick() * 0.001);
            }

            @Override
            public boolean shouldTick() {
                return true;
            }
        });
    }

    public static native void logg(Object object) /*-{
        $wnd.console.log(object);
    }-*/;

    private void testHandlers() {
        LOG.warning("interactivity logic changed, must check and rebuild test");
//        bunnySprite.interactive = true;
//        bunnySprite.mousedown = (eventData) -> LOG.fine("mousedown "+eventData.target.name);
//        bunnySprite.click = (eventData) -> LOG.fine("click "+eventData.target.name);
    }

    private void testFilters(Sprite sprite) {
        ColorMatrixFilter filter = new ColorMatrixFilter();
        filter.predator(0.5,true);
        sprite.addFilter(filter);
        this.stage.render();

        new Timer() {
            @Override
            public void run() {
                ColorMatrixFilter colorMatrixFilter = new ColorMatrixFilter();
                colorMatrixFilter.lsd(false);
                bunnySprite.addFilter(colorMatrixFilter);
                stage.render();
            }
        }.schedule(1000);

        new Timer() {
            @Override
            public void run() {
                filter.reset();
                filter.kodachrome(true);
                stage.render();
            }
        }.schedule(2000);

        new Timer() {
            @Override
            public void run() {
                bunnySprite.scale = new Point(2,2);
                stage.render();
            }
        }.schedule(3000);
    }

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
