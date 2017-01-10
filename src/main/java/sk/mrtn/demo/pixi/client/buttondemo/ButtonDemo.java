package sk.mrtn.demo.pixi.client.buttondemo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import sk.mrtn.demo.pixi.client.ADemo;
import sk.mrtn.demo.pixi.client.button.ButtonBuilder;
import sk.mrtn.demo.pixi.client.button.IButton;
import sk.mrtn.demo.pixi.client.buttons.IShapeButton;
import sk.mrtn.library.client.ticker.ITickable;
import sk.mrtn.library.client.ticker.ITicker;
import sk.mrtn.pixi.client.Graphics;
import sk.mrtn.pixi.client.Text;
import sk.mrtn.pixi.client.TextOptions;
import sk.mrtn.pixi.client.TextStyle;
import sk.mrtn.pixi.client.stage.IStage;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

/**
 * Created by Patrik on 28. 9. 2016.
 */
@Singleton
public class ButtonDemo extends ADemo {

    private final Provider<ButtonBuilder> buttonBuilderProvider;
    private final ITicker ticker;
    private IButton button;

    @Inject
    protected ButtonDemo(
            final @Named("Common") EventBus eventBus,
            IStage stage,
            Provider<IShapeButton> shapeButtonProvider,
            Provider<ButtonBuilder> buttonBuilderProvider,
            final ITicker ticker
            ) {
        super(eventBus,stage, shapeButtonProvider);
        this.buttonBuilderProvider = buttonBuilderProvider;
        this.ticker = ticker;
    }

    @Override
    protected void buildStage() {
        addButtonTest();
        super.buildStage();

        ticker.addTickable(new ITickable() {
            @Override
            public void update(ITicker ticker) {
                stage.render();
            }

            @Override
            public boolean shouldTick() {
                return true;
            }
        });

        ticker.start();
    }

    private void addButtonTest() {

        Text test = new Text("TEST");
        TextOptions textOptions = new TextOptions();
        test.style = new TextStyle(textOptions);

        this.button = buttonBuilderProvider.get()
                .setNormalStateDisplayObject(createTestBackground(IShapeButton.Color.BLUE))
                .setClickedStateDisplayObject(createTestBackground(IShapeButton.Color.RED))
                .setHoverStateDisplayObject(createTestBackground(IShapeButton.Color.DARK_BLUE))
                .setNormalStateText(test)
                .addClickHandler(this::onButtonClick)
                .addTouchHandler(this::onButtonClick)
                .build()
        ;

        this.button.setDraggable(true);
        this.mainContainer.addChild(button.asDisplayObject());
    }

    private void onButtonClick() {
        GWT.log("@click");
    }

    private Graphics createTestBackground(IShapeButton.Color color) {
        Graphics graphics = new Graphics();

        // draw a rounded rectangle
        int lineWidth = 6;

        graphics.lineStyle(lineWidth, color.getColors()[0], 1);
        graphics.beginFill(color.getColors()[1], 1);
        int width = 200;
        int height = 100;
        double radius = 20;


        graphics.drawRoundedRect(0, 0, width-lineWidth, height-lineWidth, radius);
        graphics.endFill();
        graphics.position.set(lineWidth/2,lineWidth/2);
        return graphics;
    }
}
