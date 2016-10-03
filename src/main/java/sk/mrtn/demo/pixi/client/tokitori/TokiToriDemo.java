package sk.mrtn.demo.pixi.client.tokitori;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safehtml.shared.SafeUri;
import sk.mrtn.demo.pixi.client.ADemo;
import sk.mrtn.demo.pixi.client.buttons.IShapeButton;
import sk.mrtn.demo.pixi.client.common.IStage;
import sk.mrtn.library.client.ticker.ITickable;
import sk.mrtn.library.client.ticker.ITickableRegistration;
import sk.mrtn.library.client.ticker.ITicker;
import sk.mrtn.pixi.client.Texture;
import sk.mrtn.pixi.client.extras.MovieClip;
import sk.mrtn.pixi.client.loaders.Loader;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by martinliptak on 12/09/16.
 */
@Singleton
public class TokiToriDemo extends ADemo {

    private static ITokiToriResources RES = ITokiToriResources.impl;
    private final ITicker ticker;
    private ITickableRegistration tickableRegistration;


    @Inject
    TokiToriDemo(
            final @Named("Common") EventBus eventBus,
            final IStage stage,
            final ITicker ticker,
            Provider<IShapeButton> buttonProvider){
        super(eventBus, stage, buttonProvider);
        this.ticker = ticker;
    }

    public void initialize() {
        LOG.info("INJECTION STARTED");
        loadResources();
    }

    protected void loadResources() {
        Loader aLoader = new Loader();
        for (SafeUri safeUri : RES.toki().getSafeUris()) {
            aLoader.add(safeUri.asString());
        }
        aLoader.load((loader, resources) -> {
            buildStage();
        });
    }

    class Animation {
        public String name;
        public List<Integer> frameNames = new ArrayList<>();
        public Texture[] textures;

        public Animation(String name, String frame) {
            LOG.fine("adding: "+name);
            this.name = name;
            this.frameNames.add(Integer.parseInt(frame));
        }

        public Animation build() {
            frameNames = frameNames.stream().sorted().collect(Collectors.toList());
            this.textures = new Texture[this.frameNames.size()];
            for (int counter = 0; counter < this.frameNames.size(); counter++) {
                String frameId = name + "_" + frameNames.get(counter);
                textures[counter] = Texture.fromFrame(frameId);
            }
            return this;
        }
    }

    protected void buildStage() {

        List<Animation> animations = new ArrayList<>();

        for (String textureFrame : RES.toki().getFrames()) {
            String[] nameAndFrameNumber = textureFrame.split("_");
            if (nameAndFrameNumber.length == 2) {
                String name = nameAndFrameNumber[0];
                String frame = nameAndFrameNumber[1];
                updateAnimations(animations, name, frame);
            }
        }
        int counter = 0;
        for (Animation animation : animations) {
            animation.build();
            MovieClip sprite = createMovieClip(animation.textures, counter);
            this.mainContainer.addChild(sprite);
            counter ++;
        }

        this.tickableRegistration = this.ticker.addTickable(new ITickable() {
            @Override
            public void update(ITicker ticker) {
                stage.render();
            }

            @Override
            public boolean shouldTick() {
                return pageActive;
            }
        });
        this.ticker.start();
        super.buildStage();
    }

    @Override
    protected void doOpen() {
        super.doOpen();
        this.ticker.start();
        this.ticker.requestTick();
    }

    private void updateAnimations(List<Animation> animations, String name, String frame) {
        for (Animation animation : animations) {
            if (animation.name.equals(name)) {
                animation.frameNames.add(Integer.parseInt(frame));
                return;
            }
        }
        Animation animation = new Animation(name, frame);
        animations.add(animation);
    }

    private MovieClip createMovieClip(Texture[] textures, int counter) {
        int columns = 4;
        int row = counter % columns;
        int column = (counter - row) / columns;

        int delta = 100;
        MovieClip movieClip = new MovieClip(textures);
        movieClip.anchor.x = 0;
        movieClip.anchor.y = 0;
        movieClip.position.x = row * delta;
        movieClip.position.y = column * delta;
        movieClip.animationSpeed = 0.5;
        movieClip.play();
        return movieClip;
    }

}
