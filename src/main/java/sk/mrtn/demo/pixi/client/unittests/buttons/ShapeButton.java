package sk.mrtn.demo.pixi.client.unittests.buttons;

import com.google.gwt.event.shared.HandlerRegistration;
import elemental.events.Event;
import sk.mrtn.library.client.utils.mobiledetect.MobileDetect;
import sk.mrtn.pixi.client.*;
import sk.mrtn.pixi.client.interaction.EventListener;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by martinliptak on 19/09/16.
 */
public class ShapeButton implements IShapeButton {

    List<IOnClickEventHandler> onClickEventHandlerList;
    private final MobileDetect mobileDetect;
    private Container container;
    private Graphics background;

    @Inject
    protected ShapeButton(
            final MobileDetect mobileDetect
    ){
        this.mobileDetect = mobileDetect;
        this.onClickEventHandlerList = new ArrayList<>();
    }

    @Override
    public IShapeButton create(double width, double height, double radius) {
        return create(width, height, radius, Color.GREEN);
    }

    @Override
    public IShapeButton create(double width, double height, double radius, Color color) {
        return create(width, height, radius, color, null);
    }

    @Override
    public IShapeButton create(
            double width,
            double height,
            double radius,
            Color color,
            String label
    ) {
        this.container = createContainer();
        this.background = createBackground(width, height, radius, color);
        if (label != null) {
            Text label1 = createLabel(label);
            label1.position = new Point(this.background.width/2, this.background.height/2);
            this.container.addChild(label1);
        }
        addInteraction();
        return this;
    }

    @Override
    public HandlerRegistration addClickHandler(IOnClickEventHandler onClickEventHandler) {
        this.onClickEventHandlerList.add(onClickEventHandler);
        HandlerRegistration handlerRegistration = () -> onClickEventHandlerList.remove(onClickEventHandler);
        return handlerRegistration;
    }

    private void addInteraction() {
        if (this.mobileDetect.mobile() == null) {
            this.container.on(Event.MOUSEDOWN, (EventListener) event -> onInteracted());
        } else {
            this.container.on(Event.TOUCHSTART, (EventListener) event -> onInteracted());
        }
    }

    private void onInteracted() {
        for (IOnClickEventHandler handler : this.onClickEventHandlerList) {
            handler.onClick(this);
        }
    }

    public static native void log(Object object) /*-{
        $wnd.console.log(object);
    }-*/;

    private Graphics createBackground(double width, double height, double radius, Color color) {
        Graphics graphics = new Graphics();

        // draw a rounded rectangle
        graphics.lineStyle(4, color.getColors()[0], 1);
        graphics.beginFill(color.getColors()[1], 1);
        graphics.drawRoundedRect(0, 0, width, height, radius);
        graphics.endFill();

        this.container.addChild(graphics);
        return graphics;
    }

    private Container createContainer() {
        Container container = new Container();
        container.buttonMode = true;
        container.interactive = true;
        return container;
    }

    private Text createLabel(String label) {
        String[] lines = label.split("\\n");

        TextOptions textOptions = new TextOptions();
        if (lines.length == 1) {
            textOptions.fontSize = 24;
        } else {
            textOptions.fontSize = 20;
            textOptions.align = "center";
        }
//        textOptions.lineHeight = textOptions.fontSize;
        Text text = new Text(label,new TextStyle(textOptions));
        text.anchor.set(0.5,0.5);
        log(text.style);
        return text;
    }

    @Override
    public DisplayObject asDisplayObject() {
        return this.container;
    }

}
