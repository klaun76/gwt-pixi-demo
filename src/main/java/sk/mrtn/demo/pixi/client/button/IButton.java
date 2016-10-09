package sk.mrtn.demo.pixi.client.button;

import com.google.gwt.event.shared.HandlerRegistration;
import sk.mrtn.demo.pixi.client.button.handlers.IOnEventHandler;
import sk.mrtn.pixi.client.DisplayObject;
import sk.mrtn.pixi.client.Text;

/**
 * Created by Patrik on 28. 9. 2016.
 */
public interface IButton {

    IButton create(DisplayObject normalStateDisplayObject);
    IButton setClickedStateTexture(DisplayObject displayObject);
    IButton setHoverStateTexture(DisplayObject displayObject);
    IButton setInactiveStateTexture(DisplayObject displayObject);
    IButton setText(Text text);


    void setEnabled(boolean enabled);
    IButton setDraggable(boolean draggable);

    HandlerRegistration addClickHandler(IOnEventHandler handler);
    HandlerRegistration addTouchHandler(IOnEventHandler handler);

    DisplayObject asDisplayObject();
}
