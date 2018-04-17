package sk.mrtn.demo.pixi.client.soccerball;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.safehtml.shared.SafeUri;
import jsinterop.annotations.JsMethod;
import sk.mrtn.demo.pixi.client.ADemo;
import sk.mrtn.demo.pixi.client.DemoPixi;
import sk.mrtn.demo.pixi.client.buttons.IShapeButton;
import sk.mrtn.library.client.ticker.ITickable;
import sk.mrtn.library.client.ticker.ITicker;
import sk.mrtn.pixi.client.loaders.Loader;
import sk.mrtn.pixi.client.parsers.InterfaceReader;
import sk.mrtn.pixi.client.spine.SkeletonData;
import sk.mrtn.pixi.client.spine.Spine;
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

    @Inject
    SoccerBallDemo(
            final @Named("Common") EventBus eventBus,
            final IStage stage,
            final ITicker ticker,
            final Provider<IShapeButton> buttonProvider
    ) {
        super(eventBus, stage, buttonProvider);
        this.ticker = ticker;
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
        this.ticker.start();
        this.ticker.requestTick();
    }

    private void createSoccerBall() {
        final Spine spine = new Spine(DemoPixi.RES.soccerBall().getSkeletonData());
        spine.position.set(stage.getWidth() / 2, stage.getHeight());
        spine.state.setAnimation(0, "animation",true);
        this.mainContainer.addChild(spine);
        this.ticker.addTickable(new ITickable() {
            @Override
            public void update(ITicker ticker) {
                spine.update(ticker.getDeltaTick());
            }

            @Override
            public boolean shouldTick() {
                return pageActive;
            }
        });
        this.ticker.start();
    }

    //region TESTING
    @JsMethod(namespace="Math")
    private static native double max(double x, double y);

    private void spineTest() {
        SkeletonData skeletonData = DemoPixi.RES.soccerBall().getSkeletonData();
        InterfaceReader.parseObjectAndOutputToConsole(skeletonData, "SkeletonData");
        InterfaceReader.parseObjectAndOutputToConsole("PIXI.spine.core.SkeletonData");
    }
    //endregion
}
