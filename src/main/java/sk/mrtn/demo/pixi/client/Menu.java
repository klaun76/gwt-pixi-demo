package sk.mrtn.demo.pixi.client;

import com.google.gwt.aria.client.SearchRole;
import com.google.gwt.logging.client.LogConfiguration;
import sk.mrtn.demo.pixi.client.buttons.IShapeButton;
import sk.mrtn.demo.pixi.client.common.IStage;
import sk.mrtn.pixi.client.*;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by martinliptak on 12/09/16.
 */
@Singleton
public class Menu {

    private double buttonWidth;
    private double buttonHeight;
    private Point startingPoint;
    private final Container mainContainer;

    @FunctionalInterface
    public interface IButtonCommand{
        void run();
    }

    private static Logger LOG;
    static {
        if (LogConfiguration.loggingIsEnabled()) {
            LOG = Logger.getLogger(Menu.class.getSimpleName());
            LOG.setLevel(Level.ALL);
        }
    }

    private final IStage stage;
    private final Provider<IShapeButton> shapeButtonProvider;
    private IOnInitializedListener onInitializedListener;

    @Inject
    Menu(
            final IStage stage,
            final Provider<IShapeButton> shapeButtonProvider
    ){
        this.stage = stage;
        this.shapeButtonProvider = shapeButtonProvider;
        this.mainContainer = new Container();
    }

    public void initialize(IOnInitializedListener onInitializedListener) {
        this.onInitializedListener = onInitializedListener;
        this.buttonWidth = 256;
        this.buttonHeight = 64;
        double x = this.stage.getWidth() / 2 - this.buttonWidth/2;
        double y = this.stage.getHeight()/2 - (this.buttonHeight*3);
        this.startingPoint = new Point(x, y);

        loadResources();
    }

    private void loadResources() {
//        Loader aLoader = new Loader();
//        for (SafeUri safeUri : RES.unitTestAtlas().getSafeUris()) {
//            aLoader.add(safeUri.asString());
//        }
//        aLoader.load((loader, resources) -> {
//            buildStage();
//        });
        buildStage();
    }

    protected void buildStage() {
        this.onInitializedListener.onInitialized();
    }

    protected void show() {
        this.stage.setStage(this.mainContainer);
        this.stage.render();
    }

    public void addButton(String name, IButtonCommand callback) {
        LOG.info("addButton: "+name+", "+startingPoint.toString());
        IShapeButton button = this.shapeButtonProvider.get().create(buttonWidth, buttonHeight, 10, IShapeButton.Color.GREEN, name);
        button.asDisplayObject().position = startingPoint.clone();
        startingPoint.set(startingPoint.x , startingPoint.y+ buttonHeight *1.2);
        this.mainContainer.addChild(button.asDisplayObject());
        button.addClickHandler(button1 -> callback.run());
        this.stage.render();

    }
}
