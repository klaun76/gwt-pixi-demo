package sk.mrtn.demo.pixi.client;

import com.google.gwt.logging.client.LogConfiguration;
import sk.mrtn.demo.pixi.client.buttons.IShapeButton;
import sk.mrtn.demo.pixi.client.common.IStage;
import sk.mrtn.demo.pixi.client.defaultdemo.DefaultDemo;
import sk.mrtn.pixi.client.Container;
import sk.mrtn.pixi.client.PIXI;

import javax.inject.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by martinliptak on 16/09/16.
 */
public abstract class ADemo {

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
            final IStage stage,
            Provider<IShapeButton> buttonProvider
    ){
        this.stage = stage;
        this.buttonProvider = buttonProvider;
        this.mainContainer = new Container();
        button = this.buttonProvider.get().create(
                50,50,20, IShapeButton.Color.RED,"X"
        );
        button.asDisplayObject().position.set(
                this.stage.getWidth() - button.asDisplayObject().getBounds().width,
                0
        );
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
        this.stage.setStage(this.mainContainer);
        this.stage.render();
        if (this.onOpenedListener != null) {
            this.onOpenedListener.onOpened();
        }
    }
}
