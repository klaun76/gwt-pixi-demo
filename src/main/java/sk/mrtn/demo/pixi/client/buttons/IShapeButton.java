package sk.mrtn.demo.pixi.client.buttons;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import sk.mrtn.pixi.client.DisplayObject;

/**
 * Created by martinliptak on 20/09/16.
 * Simple button for demo presentations and menus
 * Serves as prototype to test basic functionality
 * This button will be changed often until it'll
 * have all features.
 * TODO: add visual button states, disabled enabled presed etc.
 * TODO: solve problem with label, text size font type, multiline label etc.
 * TODO: allow draggable button
 * TODO: create long click
 * TODO: solve alignment problem
 */
public interface IShapeButton  {

    /**
     * may be not cleverest idea of using enum
     * but i just wanted to test something new
     */
    enum Color {
        RED(new int[]{0x650000,0xCE0000}),
        GREEN(new int[]{0x006504,0x00CE0E}),
        BLUE(new int[]{0x002A64,0X0082CD}),
        VIOLET(new int[]{0x65005D,0xAD00CE});

        private final int[] orientation;

        Color(int[] colors) {
            this.orientation = colors;
        }

        public int[] getColors() {
            return orientation;
        }
    }

    DisplayObject asDisplayObject();

    interface IOnClickEventHandler extends EventHandler {
        void onClick(IShapeButton button);
    }

    IShapeButton create(double width, double height, double radius);

    IShapeButton create(double width, double height, double radius, Color color);

    IShapeButton create(
            double width,
            double height,
            double radius,
            Color color,
            String label
    );

    HandlerRegistration addClickHandler(IOnClickEventHandler onClickEventHandler);

}
