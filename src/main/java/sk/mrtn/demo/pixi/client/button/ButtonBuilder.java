package sk.mrtn.demo.pixi.client.button;

import com.google.gwt.event.shared.HandlerRegistration;
import sk.mrtn.demo.pixi.client.button.handlers.IOnEventHandler;
import sk.mrtn.library.client.utils.mobiledetect.MobileDetect;
import sk.mrtn.pixi.client.DisplayObject;
import sk.mrtn.pixi.client.Text;

import javax.inject.Inject;

/**
 * Created by Patrik on 21. 10. 2016.
 */
public class ButtonBuilder {

    private final MobileDetect mobileDetect;

    protected DisplayObject normalStateDisplayObject;
    protected DisplayObject hoverStateDisplayObject;
    protected DisplayObject clickedStateDisplayObject;
    protected DisplayObject disabledStateDisplayObject;

    protected Text normalStateText;
    protected Text hoveredStateText;
    protected Text clickedStateText;
    protected Text disabledStateText;

    @Inject
    public ButtonBuilder(
            MobileDetect mobileDetect
            ){
        this.mobileDetect = mobileDetect;
    }

    public IButton build(){
        if (mobileDetect.mobile() == null) {
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

//    public ButtonBuilder addClickHandler(IOnEventHandler handler){
//
//        return this;
//    }
//
//    public ButtonBuilder addTouchHandler(IOnEventHandler handler){
//
//        return this;
//    }
}
