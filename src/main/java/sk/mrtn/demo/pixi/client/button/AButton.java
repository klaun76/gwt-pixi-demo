package sk.mrtn.demo.pixi.client.button;

import com.google.gwt.event.shared.HandlerRegistration;
import sk.mrtn.demo.pixi.client.button.handlers.IOnEventHandler;
import sk.mrtn.pixi.client.Container;
import sk.mrtn.pixi.client.DisplayObject;
import sk.mrtn.pixi.client.Text;

/**
 * Created by Patrik on 5. 10. 2016.
 */
public abstract class AButton implements IButton{

    protected Container container;
    protected boolean enabled;
    protected boolean draggable;
    protected DisplayObject clickedStateTexture;
    private DisplayObject hoverStateTexture;
    private DisplayObject inactiveStateTexture;
    private DisplayObject displayObject;
    protected DisplayObject normalStateTexture;
    private Text text;

    public IButton create(DisplayObject normalStateDisplayObject) {

        this.container = new Container();
        this.container.buttonMode = true;
        this.container.interactive = true;
        this.normalStateTexture = normalStateDisplayObject;
        this.displayObject = normalStateDisplayObject;

        this.container.addChild(displayObject);

        addInteraction();
        this.enabled = true;

        return this;
    }

    protected abstract void addInteraction();

    @Override
    public DisplayObject asDisplayObject() {

        if (this.text != null){
            text.position.set(container.width/2,container.height/2);
            text.anchor.set(0.5,0.5);
            this.container.addChild(text);
        }

        return this.container;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled){
            this.enabled = true;
            this.container.interactive = true;
            this.container.buttonMode = true;
        } else {
            this.enabled = false;
            this.container.interactive = false;
            this.container.buttonMode = false;
        }
    }

    @Override
    public IButton setDraggable(boolean draggable) {
        this.draggable = draggable;
        return this;
    }

    @Override
    public IButton setText(Text text) {
        this.text = text;
        return this;
    }


    @Override
    public IButton setClickedStateTexture(DisplayObject displayObject) {
        this.clickedStateTexture = displayObject;
        return this;
    }

    @Override
    public IButton setHoverStateTexture(DisplayObject displayObject) {
        this.hoverStateTexture = displayObject;
        return this;
    }

    @Override
    public IButton setInactiveStateTexture(DisplayObject displayObject) {
        this.inactiveStateTexture = displayObject;
        return this;
    }

    @Override
    public HandlerRegistration addClickHandler(IOnEventHandler handler) {
        return null;
    }

    @Override
    public HandlerRegistration addTouchHandler(IOnEventHandler handler) {
        return null;
    }
}
