package sk.mrtn.demo.pixi.client.button;

import com.google.gwt.event.shared.HandlerRegistration;
import sk.mrtn.demo.pixi.client.button.handlers.IOnEventHandler;
import sk.mrtn.library.client.device.DeviceType;
import sk.mrtn.pixi.client.DisplayObject;
import sk.mrtn.pixi.client.Text;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Patrik on 21. 10. 2016.
 */
public class ButtonBuilder {


    protected DisplayObject normalStateDisplayObject;
    protected DisplayObject hoverStateDisplayObject;
    protected DisplayObject clickedStateDisplayObject;
    protected DisplayObject disabledStateDisplayObject;

    protected Text normalStateText;
    protected Text hoveredStateText;
    protected Text clickedStateText;
    protected Text disabledStateText;

    /**
     * these Maps keep pairs of IOnEventHandlers and their registrations.
     * Registrations can be used to remove these handlers, if needed.
     */
    protected Map<IOnEventHandler,HandlerRegistration> onClickEventHandlersMap;
    protected Map<IOnEventHandler,HandlerRegistration> onTouchEventHandlersMap;

    @Inject
    public ButtonBuilder(
            ){
        this.onClickEventHandlersMap = new HashMap<>();
        this.onTouchEventHandlersMap = new HashMap<>();
    }

    public IButton build(){
        if (DeviceType.isDesktop()) {
            return new Button(this);
        } else {
            return new ButtonTouch(this);
        }
    }

    public ButtonBuilder setNormalStateDisplayObject(DisplayObject normalStateDisplayObject){
        this.normalStateDisplayObject = normalStateDisplayObject;
        return this;
    }

    public ButtonBuilder setHoverStateDisplayObject(DisplayObject hoverStateDisplayObject){
        this.hoverStateDisplayObject = hoverStateDisplayObject;
        return this;
    }

    public ButtonBuilder setClickedStateDisplayObject(DisplayObject clickedStateDisplayObject){
        this.clickedStateDisplayObject = clickedStateDisplayObject;
        return this;
    }

    public ButtonBuilder setDisabledStateDisplayObject(DisplayObject disabledStateDisplayObject){
        this.disabledStateDisplayObject = disabledStateDisplayObject;
        return this;
    }

    public ButtonBuilder setNormalStateText(Text normalStateText){
        this.normalStateText = normalStateText;
        return this;
    }

    public ButtonBuilder setHoverStateText(Text hoverStateText){
        this.hoveredStateText = hoverStateText;
        return this;
    }

    public ButtonBuilder setClickedStateText(Text clickedStateText){
        this.clickedStateText = clickedStateText;
        return this;
    }

    public ButtonBuilder setDisabledStateText(Text disabledStateText){
        this.disabledStateText = disabledStateText;
        return this;
    }

    public ButtonBuilder addClickHandler(final IOnEventHandler handler){
        HandlerRegistration handlerRegistration = () -> onClickEventHandlersMap.remove(handler);
        this.onClickEventHandlersMap.put(handler,handlerRegistration);
        return this;
    }

    public ButtonBuilder addTouchHandler(final IOnEventHandler handler){
        HandlerRegistration handlerRegistration = () -> onTouchEventHandlersMap.remove(handler);
        this.onTouchEventHandlersMap.put(handler,handlerRegistration);
        return this;
    }
}
