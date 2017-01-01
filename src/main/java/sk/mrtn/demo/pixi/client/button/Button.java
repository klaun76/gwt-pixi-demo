package sk.mrtn.demo.pixi.client.button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import elemental.events.Event;
import sk.mrtn.demo.pixi.client.button.handlers.IOnEventHandler;
import sk.mrtn.pixi.client.interaction.EventListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Patrik on 28. 9. 2016.
 */
public class Button extends AButton implements IButton {

    private Map<IOnEventHandler,HandlerRegistration> onClickEventHandlersMap;

    protected Button(ButtonBuilder builder) {
        super(builder);
        if (this.onClickEventHandlersMap == null){
            this.onClickEventHandlersMap = new HashMap<>();
        }
    }

    @Override
    protected void addClickInteraction(Map<IOnEventHandler,HandlerRegistration> onClickEventHandlersMap) {
        this.onClickEventHandlersMap = onClickEventHandlersMap;
        this.container.on(Event.MOUSEDOWN, (EventListener) event -> onMouseDown());
        this.container.on(Event.MOUSEUP, (EventListener) event -> onMouseUp());
        this.container.on(Event.MOUSEOVER, (EventListener) event -> onMouseOver());
        this.container.on(Event.MOUSEOUT, (EventListener) event -> onMouseOut());
        this.container.on(Event.MOUSEMOVE, (EventListener) event -> onMouseMove());
    }

    @Override
    public void removeHandler(IOnEventHandler handler) {
        this.onClickEventHandlersMap.remove(handler);
    }

    private void onMouseOut() {
        GWT.log("@hover end");

    }

    private void onMouseMove() {
        if (draggable && isBeingDragged){
//            container.position.set(++container.position.x,++container.position.y);
            GWT.log("@btn move  " + container.position.x);
        }
    }

    private void onMouseOver() {

        GWT.log("@hover");
    }

    private void onMouseUp() {
        if (enabled){
            GWT.log("@mouse up");
            onMouseOrTouchUp();
            onClickEventHandlersMap.keySet().forEach(IOnEventHandler::onClick);
        }
    }

    private void onMouseDown() {
        if (enabled){
            GWT.log("@mouse down");
            onMouseOrTouchDown();
        }
    }
}
