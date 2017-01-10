package sk.mrtn.demo.pixi.client.button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import sk.mrtn.demo.pixi.client.button.handlers.IOnEventHandler;
import sk.mrtn.pixi.client.Container;
import sk.mrtn.pixi.client.DisplayObject;
import sk.mrtn.pixi.client.Text;

import java.util.Map;

/**
 * Created by Patrik on 5. 10. 2016.
 */
public abstract class AButton implements IButton{

    private static final int LONG_CLICK_TIMEOUT = 1500;

    private final Timer longClickTimer;
    protected Container container;
    protected boolean enabled;
    protected boolean draggable;
    private DisplayObject currentDisplayObject;
    protected boolean isBeingDragged;

    protected DisplayObject normalStateDisplayObject;
    protected DisplayObject clickedStateDisplayObject;
    protected DisplayObject hoverStateDisplayObject;
    protected DisplayObject disabledStateDisplayObject;

    private Text normalStateText;
    private final Text disabledStateText;
    private final Text hoveredStateText;
    private final Text clickedStateText;

    protected AButton (ButtonBuilder builder){

        this.container = new Container();
        this.container.buttonMode = true;
        this.container.interactive = true;

        this.normalStateDisplayObject = builder.normalStateDisplayObject;
        this.hoverStateDisplayObject = builder.hoverStateDisplayObject;
        this.clickedStateDisplayObject = builder.clickedStateDisplayObject;
        this.disabledStateDisplayObject = builder.disabledStateDisplayObject;

        this.normalStateText = builder.normalStateText;
        this.hoveredStateText = builder.hoveredStateText;
        this.clickedStateText = builder.clickedStateText;
        this.disabledStateText = builder.disabledStateText;

        requestRedrawBackground(normalStateDisplayObject);

        if (this.normalStateText != null){
            normalStateText.position.set(container.width/2,container.height/2);
            normalStateText.anchor.set(0.5,0.5);
            this.container.addChild(normalStateText);
        }

        this.longClickTimer = new Timer() {
            @Override
            public void run() {
                onLongCLickOrTouch();
            }
        };

        addClickInteraction(builder.onClickEventHandlersMap);
        addTouchInteraction(builder.onTouchEventHandlersMap);
        this.enabled = true;
    }

    protected void requestRedrawBackground(DisplayObject newBackground) {
        if (newBackground != null){
            container.removeChild(currentDisplayObject);
            this.currentDisplayObject = newBackground;
            container.addChildAt(currentDisplayObject,0);
        }
    }

    protected void addClickInteraction(Map<IOnEventHandler,HandlerRegistration> onClickEventHandlersMap) {

    }

    protected void addTouchInteraction(Map<IOnEventHandler,HandlerRegistration> onTouchEventHandlersMap) {

    }

    @Override
    public DisplayObject asDisplayObject() {
        return this.container;
    }

    @Override
    public IButton setEnabled(boolean enabled) {
        if (enabled){
            this.enabled = true;
            this.container.interactive = true;
            this.container.buttonMode = true;
        } else {
            this.enabled = false;
            this.container.interactive = false;
            this.container.buttonMode = false;
        }

        return this;
    }

    protected void onMouseOrTouchDown(){
        requestRedrawBackground(clickedStateDisplayObject);
        this.longClickTimer.schedule(LONG_CLICK_TIMEOUT);
        this.isBeingDragged = true;
    }

    protected void onMouseOrTouchUp(){
        this.longClickTimer.cancel();
        this.isBeingDragged = false;
    }

    protected void onLongCLickOrTouch(){
        GWT.log("@long click");
        onMouseOrTouchUp();
    }

    @Override
    public IButton setDraggable(boolean draggable) {
        this.draggable = draggable;
        return this;
    }
}
