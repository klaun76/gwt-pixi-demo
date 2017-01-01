package sk.mrtn.demo.pixi.client.button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import elemental.events.Event;
import sk.mrtn.demo.pixi.client.button.handlers.IOnEventHandler;
import sk.mrtn.pixi.client.interaction.EventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Patrik on 4. 10. 2016.
 */
public class ButtonTouch extends AButton implements IButton {


    private Map<IOnEventHandler,HandlerRegistration> onTouchEventHandlersMap;

    protected ButtonTouch(ButtonBuilder builder) {
        super(builder);
        if (this.onTouchEventHandlersMap == null){
            this.onTouchEventHandlersMap = new HashMap<>();
        }
    }

    @Override
    protected void addTouchInteraction(Map<IOnEventHandler,HandlerRegistration> onTouchEventHandlersMap) {
        this.onTouchEventHandlersMap = onTouchEventHandlersMap;

        this.container.on(Event.TOUCHSTART, (EventListener) event -> onTouchStart());
        this.container.on(Event.TOUCHEND, (EventListener) event -> onTouchEnd());
        this.container.on(Event.TOUCHMOVE, (EventListener) event -> onTouchMove());
    }

    @Override
    public void removeHandler(IOnEventHandler handler) {
        this.onTouchEventHandlersMap.remove(handler);
    }

    private void onTouchMove() {
        GWT.log("@touchMove");
    }

    private void onTouchEnd() {
        GWT.log("@touchEnd");
        onMouseOrTouchUp();
        onTouchEventHandlersMap.keySet().forEach(IOnEventHandler::onClick);
    }

    private void onTouchStart() {
        GWT.log("@touchStart");
        onMouseOrTouchDown();
    }
}
