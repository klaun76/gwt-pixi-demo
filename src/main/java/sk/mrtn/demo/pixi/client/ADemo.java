package sk.mrtn.demo.pixi.client;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.logging.client.LogConfiguration;
import sk.mrtn.demo.pixi.client.buttons.IShapeButton;
import sk.mrtn.pixi.client.stage.IResponsiveController;
import sk.mrtn.pixi.client.stage.IStage;
import sk.mrtn.demo.pixi.client.defaultdemo.DefaultDemo;
import sk.mrtn.pixi.client.Container;
import sk.mrtn.pixi.client.PIXI;

import javax.inject.Named;
import javax.inject.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by martinliptak on 16/09/16.
 */
public abstract class ADemo implements IResponsiveController {

    protected boolean pageActive;
    protected static Logger LOG;
    static {
        if (LogConfiguration.loggingIsEnabled()) {
            LOG = Logger.getLogger(DefaultDemo.class.getSimpleName());
            LOG.setLevel(Level.ALL);
        }
    }

    protected final Container mainContainer;
    protected PIXI pixi;
    protected IStage stage;
    private final Provider<IShapeButton> buttonProvider;
    protected IOnOpenedListener onOpenedListener;

    public boolean isInitialized() {
        return initialized;
    }

    protected boolean initialized;

    public IShapeButton getButton() {
        return button;
    }

    private IShapeButton button;

    protected ADemo(
            final @Named("Common") EventBus eventBus,
            final IStage stage,
            Provider<IShapeButton> buttonProvider
    ){
        this.stage = stage;
        this.buttonProvider = buttonProvider;
        this.mainContainer = new Container();
        button = this.buttonProvider.get().create(
                50,50,20, IShapeButton.Color.RED,"X"
        );
        onResized(this.stage.getWidth(),this.stage.getHeight());
    }

    @Override
    public void onResized(double width, double height) {
        LOG.warning("onResized - this method should be overriden");
        button.asDisplayObject().position.set(
                width - button.asDisplayObject().getBounds().width,
                0
        );
    }

    @Override
    public Container getContainer() {
        return this.mainContainer;
    }

    @Override
    public void onDetached() {
        this.pageActive = false;
    }

    public static native void log(Object object) /*-{
        $wnd.console.log(object);
    }-*/;

    public void open(IOnOpenedListener onOpenedListener) {
        this.onOpenedListener = onOpenedListener;
        if (this.initialized) {
            doOpen();
        } else {
            loadResources();
        }
    }

    protected void loadResources() {
        buildStage();
    }

    protected void buildStage() {
        this.initialized = true;
        this.mainContainer.addChild(button.asDisplayObject());
        doOpen();
    }

    protected void doOpen() {
        this.pageActive = true;
        onResized(this.stage.getWidth(),this.stage.getHeight());
        this.stage.setResponsiveStage(this);
        this.stage.render();
        if (this.onOpenedListener != null) {
            this.onOpenedListener.onOpened();
        }
    }

    @Override
    public void addResponsiveController(IResponsiveController responsiveController) {
        throw new UnsupportedOperationException();
    }
}
