package sk.mrtn.demo.pixi.client;

import com.google.gwt.logging.client.LogConfiguration;
import sk.mrtn.demo.pixi.client.buttons.IShapeButton;
import sk.mrtn.pixi.client.stage.IResponsiveController;
import sk.mrtn.pixi.client.stage.IStage;
import sk.mrtn.pixi.client.Container;
import sk.mrtn.pixi.client.DisplayObject;
import sk.mrtn.pixi.client.Point;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by martinliptak on 12/09/16.
 * TODO: skraslit usporaduvanie buttonov
 */
@Singleton
public class Menu implements IResponsiveController {

    private List<IShapeButton> shapeButtons;
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
        this.shapeButtons = new ArrayList<>();
    }

    @Override
    public void onResized(double width, double height) {
        updateButtons(width,height);
    }

    @Override
    public Container getContainer() {
        return this.mainContainer;
    }

    @Override
    public void onDetached() {
        LOG.fine("onDetached");
    }

    public void initialize(IOnInitializedListener onInitializedListener) {
        this.onInitializedListener = onInitializedListener;
        this.buttonWidth = 256;
        this.buttonHeight = 64;
        double x = this.stage.getWidth() / 2 - this.buttonWidth / 2;
        double y = this.stage.getHeight() / 2 - (this.buttonHeight * 3);
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
        if (this.onInitializedListener != null) {
            this.onInitializedListener.onInitialized();
        } else {
            LOG.severe("onInitializedListener not set");
        }
    }

    protected void show() {
        onResized(this.stage.getWidth(),this.stage.getHeight());
        this.stage.setResponsiveStage(this);
        this.stage.render();
    }

    public void addButton(String name, IButtonCommand callback) {
        LOG.info("addButton: "+name+", "+startingPoint.toString());

        IShapeButton button = this.shapeButtonProvider.get().create(buttonWidth, buttonHeight, 10, IShapeButton.Color.GREEN, name);
        this.shapeButtons.add(button);

        button.asDisplayObject().position = startingPoint.clone();
        startingPoint.set(startingPoint.x , startingPoint.y+ buttonHeight * 1.2);
        this.mainContainer.addChild(button.asDisplayObject());

        button.addClickHandler(button1 -> callback.run());

        this.stage.render();

    }

    private void updateButtons(double width, double height) {
        double y = height / 2 - (this.buttonHeight * 1.2 * (this.shapeButtons.size() / 4));
        double yDelta = 0;
        boolean isRightSide = false;
        for (int i = 0; i < this.shapeButtons.size(); i++) {
            DisplayObject button = this.shapeButtons.get(i).asDisplayObject();
            if (i == this.shapeButtons.size() / 2 && !isRightSide) {
                isRightSide = true;
                yDelta = 0;
            }
            double x = (isRightSide ? (width - width / 4) : (width / 4)) - button.getBounds().width / 2;
            button.position.set(x, y + yDelta);
            yDelta += buttonHeight * 1.2;
        }
    }

    @Override
    public void addResponsiveController(IResponsiveController responsiveController) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeResponsiveController(IResponsiveController responsiveController) {
        throw new UnsupportedOperationException();
    }

}
