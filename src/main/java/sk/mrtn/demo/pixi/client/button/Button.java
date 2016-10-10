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
 * Created by Patrik on 28. 9. 2016.
 */
public class Button extends AButton implements IButton {

    private List<IOnEventHandler> onClickEventHandlersList;
    private boolean dragging;

    @Override
    public IButton create(DisplayObject normalStateDisplayObject) {

        this.onClickEventHandlersList = new ArrayList<>();
        return super.create(normalStateDisplayObject);
    }

    @Override
    protected void addInteraction() {
        this.container.on(Event.MOUSEDOWN, (EventListener) event -> onMouseDown());
        this.container.on(Event.MOUSEUP, (EventListener) event -> onMouseUp());
        this.container.on(Event.MOUSEOVER, (EventListener) event -> onMouseOver());
        this.container.on(Event.MOUSEOUT, (EventListener) event -> onMouseOut());
        this.container.on(Event.MOUSEMOVE, (EventListener) event -> onMouseMove());
    }


    private void onMouseOut() {
        GWT.log("@hover end");

    }

    private void onMouseMove() {
        if (draggable && dragging){
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
            container.removeChild(clickedStateTexture);
            container.addChildAt(normalStateTexture,0);
//            this.displayObject = normalStateTexture;
            onClickEventHandlersList.forEach(IOnEventHandler::onClick);
        }
        this.dragging = false;
    }

    private void onMouseDown() {
        if (enabled){
            GWT.log("@mouse down");
            if (clickedStateTexture != null){
                container.removeChild(normalStateTexture);
                container.addChildAt(clickedStateTexture,0);
//                this.displayObject = clickedStateTexture;
            }
            this.dragging = true;
        }
    }

    @Override
    public HandlerRegistration addClickHandler(IOnEventHandler handler) {
        onClickEventHandlersList.add(handler);
        HandlerRegistration handlerRegistration = () -> onClickEventHandlersList.remove(handler);
        return handlerRegistration;
    }
}
