package sk.mrtn.demo.pixi.client.soccerball;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safehtml.shared.SafeUri;
import jsinterop.annotations.JsMethod;
import sk.mrtn.demo.pixi.client.ADemo;
import sk.mrtn.demo.pixi.client.DemoPixi;
import sk.mrtn.demo.pixi.client.buttons.IShapeButton;
import sk.mrtn.library.client.ticker.ITickable;
import sk.mrtn.library.client.ticker.ITicker;
import sk.mrtn.library.client.tweenengine.TickableTweenManager;
import sk.mrtn.library.client.tweenengine.Timeline;
import sk.mrtn.pixi.client.loaders.Loader;
import sk.mrtn.pixi.client.parsers.InterfaceReader;
import sk.mrtn.pixi.client.spine.SkeletonData;
import sk.mrtn.pixi.client.spine.Spine;
import sk.mrtn.pixi.client.spine.events.impl.SpineEventHandling;
import sk.mrtn.pixi.client.stage.IStage;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * @author Tomas Ecker
 */
@Singleton
public class SoccerBallDemo extends ADemo {

    private ITicker ticker;
    private TickableTweenManager tweenManager;
    private Spine spine;
    private Spine spineForTicker;

    @Inject
    SoccerBallDemo(
            final @Named("Common") EventBus eventBus,
            final IStage stage,
            final ITicker ticker,
            final Provider<IShapeButton> buttonProvider,
            final @Named("Common") TickableTweenManager tweenManager
    ) {
        super(eventBus, stage, buttonProvider);
        this.ticker = ticker;
        this.tweenManager = tweenManager;
    }

    protected void loadResources() {
        Loader aLoader = new Loader();
        for (SafeUri safeUri : DemoPixi.RES.soccerBall().getUrisOfAtlasImages()) {
            aLoader.add(safeUri.asString());
        }
        aLoader.load((loader, resources) -> {
//            log(loader,resources);
            buildStage();
        });
    }

    protected void buildStage() {

        createSoccerBall();

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
        super.buildStage();
    }

    @Override
    protected void doOpen() {
        super.doOpen();
        playSoccerBall();
        play();
        this.ticker.start();
        this.ticker.requestTick();
    }

    private void playSoccerBall() {
        SpineAnimation spineAnimation = new SpineAnimation(this.spine, "animation");
        spineAnimation.getSpine().position.set(stage.getWidth() / 2, stage.getHeight());

        Timeline timeline = Timeline.createSequence();
        timeline.push(spineAnimation.asTimeline());
        timeline.start(this.tweenManager);
        this.tweenManager.start();
    }

    private void play() {
        this.spineForTicker.autoUpdate = true;
        this.spineForTicker.state.setAnimation(0, "animation", true);
        this.ticker.addTickable(new ITickable() {
            @Override
            public void update(ITicker ticker) {
                spineForTicker.update(ticker.getDeltaTick() * 0.0001);
            }

            @Override
            public boolean shouldTick() {
                return pageActive;
            }
        });
    }

    private void createSoccerBall() {
        this.spine = new Spine(DemoPixi.RES.soccerBall().getSkeletonData());
        this.spine.position.set(stage.getWidth() / 2, stage.getHeight());

        this.spineForTicker = new Spine(DemoPixi.RES.soccerBall().getSkeletonData());
        this.spineForTicker.position.set(stage.getWidth() - 100, stage.getHeight());

        addSpineListeners("timeline Spine", this.spine);
        addSpineListeners("ticker Spine", this.spineForTicker);

        this.mainContainer.addChild(this.spine);
        this.mainContainer.addChild(this.spineForTicker);
    }

    private void addSpineListeners(String spineName, Spine spine) {
        SpineEventHandling spineEventHandling = SpineEventHandling.get(spine);

        spineEventHandling.addStartEventListener((spine1, trackIndex) -> {
            LOG.info(spineName + ": START");
        });

        spineEventHandling.addUserEventListener((spine1, trackIndex, event) -> {
            LOG.info(spineName + ": USER EVENT");
        });

        spineEventHandling.addCompleteEventListener((spine1, trackIndex, count) -> {
            LOG.info(spineName + ": COMPLETE");
        });

        spineEventHandling.addEndEventListener((spine1, trackIndex) -> {
            LOG.info(spineName + ": END");
        });

        spineEventHandling.addDisposeEventListener((spine1, trackIndex) -> {
            LOG.info(spineName + ": DISPOSE");
        });

        spineEventHandling.addInterruptedEventListener((spine1, trackIndex) -> {
            LOG.info(spineName + ": INTERRUPTED");
        });
    }

    //region TESTING
    @JsMethod(namespace = "Math")
    private static native double max(double x, double y);

    private void spineTest() {
        SkeletonData skeletonData = DemoPixi.RES.soccerBall().getSkeletonData();
        InterfaceReader.parseObjectAndOutputToConsole(skeletonData, "SkeletonData");
        InterfaceReader.parseObjectAndOutputToConsole("PIXI.spine.core.SkeletonData");
    }
    //endregion
}
