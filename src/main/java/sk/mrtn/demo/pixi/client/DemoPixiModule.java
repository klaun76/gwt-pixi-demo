package sk.mrtn.demo.pixi.client;

import dagger.Module;
import dagger.Provides;
import sk.mrtn.demo.pixi.client.common.DefaultStage;
import sk.mrtn.demo.pixi.client.common.IStage;
import sk.mrtn.demo.pixi.client.unittests.buttons.IShapeButton;
import sk.mrtn.demo.pixi.client.unittests.buttons.ShapeButton;
import sk.mrtn.library.client.UtilsModule;
import sk.mrtn.library.client.ticker.ITicker;
import sk.mrtn.library.client.ticker.Ticker;

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

}
