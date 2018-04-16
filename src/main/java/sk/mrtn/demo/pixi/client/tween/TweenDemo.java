package sk.mrtn.demo.pixi.client.tween;

import sk.mrtn.library.client.tweenengine.*;
import com.google.gwt.event.shared.EventBus;
import sk.mrtn.demo.pixi.client.ADemo;
import sk.mrtn.demo.pixi.client.buttons.IShapeButton;
import sk.mrtn.pixi.client.Container;
import sk.mrtn.pixi.client.DisplayObject;
import sk.mrtn.pixi.client.stage.IStage;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class TweenDemo extends ADemo {

    private TickableTweenManager tweenManager;

    @Inject
    protected TweenDemo(
            final @Named("Common") EventBus eventBus,
            final @Named("Common") TickableTweenManager tweenManager,
            IStage stage,
            Provider<IShapeButton> buttonProvider) {
        super(eventBus, stage, buttonProvider);
        this.tweenManager = tweenManager;
    }

    public void test() {

        Tween.registerAccessor(DisplayObject.class, new DisplayObjectAccessor());
        Container container = new Container();
        container.position.set(0,0);
        LOG.severe("start");
        Timeline sequence = Timeline.createSequence();
        sequence.pushPause(2);
        sequence.push(Tween.call((type, source) -> LOG.info("test event on after pause")));

        sequence.push(Tween.to(container,DisplayObjectAccessor.X,2).target(100).ease(TweenEquations.easeNone));
        sequence.push(Tween.call((type, source) -> LOG.info("test event on after container")));
        sequence.setCallbackTriggers(TweenCallback.BEGIN|TweenCallback.COMPLETE);
        sequence.setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                if (type == TweenCallback.BEGIN) {
                    LOG.info("test event on tween callback BEGIN");
                }
                if (type == TweenCallback.COMPLETE) {
                    LOG.info("test event on tween callback COMPLETE");
                }
            }
        });
        sequence.start(this.tweenManager);
        this.tweenManager.start();
    }

}
