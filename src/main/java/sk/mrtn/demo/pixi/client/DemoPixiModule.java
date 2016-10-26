package sk.mrtn.demo.pixi.client;

import dagger.Module;
import dagger.Provides;
import sk.mrtn.demo.pixi.client.button.Button;
import sk.mrtn.demo.pixi.client.button.ButtonBuilder;
import sk.mrtn.demo.pixi.client.button.ButtonTouch;
import sk.mrtn.demo.pixi.client.button.IButton;
import sk.mrtn.demo.pixi.client.buttons.IShapeButton;
import sk.mrtn.demo.pixi.client.buttons.ShapeButton;
import sk.mrtn.pixi.client.stage.DefaultStage;
import sk.mrtn.pixi.client.stage.IStage;
import sk.mrtn.library.client.UtilsModule;
import sk.mrtn.library.client.ticker.ITicker;
import sk.mrtn.library.client.ticker.Ticker;
import sk.mrtn.library.client.ui.mainpanel.IRootResponsivePanel;
import sk.mrtn.library.client.ui.mainpanel.RootResponsivePanel;
import sk.mrtn.library.client.utils.mobiledetect.MobileDetect;

import javax.inject.Singleton;

/**
 * Created by klaun on 25/04/16.
 * For now module is empty, class is ready to be filled when there
 * is need for more complex injection
 */
@Module(includes = {UtilsModule.class})
public class DemoPixiModule {

    @Provides
    @Singleton
    ITicker providesTicker(Ticker ticker) {
        return ticker;
    }

    @Provides
    IShapeButton providesIShapeButton(ShapeButton shapeButton) {
        return shapeButton;
    }

    @Provides
    @Singleton
    IStage providesIstage(final DefaultStage stage){
        return stage;
    }

    @Provides
    @Singleton
    IRootResponsivePanel provideIMainResponsivePanel(RootResponsivePanel panel){
        return panel;
    }

}
