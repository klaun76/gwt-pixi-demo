package sk.mrtn.demo.pixi.client.button;

import sk.mrtn.demo.pixi.client.button.handlers.IOnEventHandler;
import sk.mrtn.pixi.client.DisplayObject;

/**
 * Created by Patrik on 28. 9. 2016.
 */
public interface IButton {

    IButton setEnabled(boolean enabled);
    IButton setDraggable(boolean draggable);

    DisplayObject asDisplayObject();

    /**
     * removes handler, if it's already registered.
     * @param handler
     */
    void removeHandler(IOnEventHandler handler);
}
