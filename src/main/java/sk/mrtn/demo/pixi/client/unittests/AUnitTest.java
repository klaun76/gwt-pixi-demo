package sk.mrtn.demo.pixi.client.unittests;

import com.google.gwt.logging.client.LogConfiguration;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by martinliptak on 16/09/16.
 */
public abstract class AUnitTest {
    protected Logger LOG;

    protected AUnitTest() {
    }

    protected void initialize(String label) {
        if (!LogConfiguration.loggingIsEnabled()) {
            throw new NullPointerException("logging disabled, unit tests not relevant");
        }
        LOG = Logger.getLogger(getClass().getSimpleName()+"."+label);
        LOG.setLevel(Level.ALL);
    }

    public static native void log(Object object) /*-{
        $wnd.console.log(object);
    }-*/;
}
