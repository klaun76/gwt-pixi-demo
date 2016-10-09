package sk.mrtn.demo.pixi.client.button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import elemental.events.Event;
import sk.mrtn.demo.pixi.client.button.handlers.IOnEventHandler;
import sk.mrtn.pixi.client.DisplayObject;
import sk.mrtn.pixi.client.interaction.EventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patrik on 4. 10. 2016.
 */
public class ButtonTouch extends AButton implements IButton {


    private List<IOnEventHandler> onTouchEventHandlersList;

    @Override
    public IButton create(DisplayObject normalStateDisplayObject) {

        this.onTouchEventHandlersList = new ArrayList<>();
        return super.create(normalStateDisplayObject);
    }

    @Override
    protected void addInteraction() {
        this.container.on(Event.TOUCHSTART, (EventListener) event -> onTouchStart());
        this.container.on(Event.TOUCHEND, (EventListener) event -> onTouchEnd());
        this.container.on(Event.TOUCHMOVE, (EventListener) event -> onTouchMove());
    }

    private void onTouchMove() {
        GWT.log("@touchMove");
    }

    private void onTouchEnd() {
        GWT.log("@touchEnd");
    }

    private void onTouchStart() {
        GWT.log("@touchStart");
    }

    @Override
    public HandlerRegistration addTouchHandler(IOnEventHandler handler) {
        onTouchEventHandlersList.add(handler);
        HandlerRegistration handlerRegistration = () -> onTouchEventHandlersList.remove(handler);
        return handlerRegistration;
    }
}
