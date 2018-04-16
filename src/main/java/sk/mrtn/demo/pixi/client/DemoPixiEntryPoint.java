package sk.mrtn.demo.pixi.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.logging.client.LogConfiguration;
import dagger.Component;
import elemental.client.Browser;
import sk.mrtn.library.client.utils.Spinner;
import sk.mrtn.pixi.client.PIXI;
import sk.mrtn.pixi.client.PixiLoader;
import sk.mrtn.pixi.client.parsers.InterfaceReader;
import sk.mrtn.pixi.client.spine.SpineCrossVersionFix;

import javax.inject.Singleton;
import java.util.logging.Logger;


/**
 * Created by klaun on 21/04/16.
 * Standard gwt entry point. Because of dagger injection i decided
 * to leave everything within injection. Thats why there is only injection
 * of demo pixi component. rest of code will be fully injectable.
 */
public class DemoPixiEntryPoint implements EntryPoint {

    private Logger LOG;

    @Component(modules = DemoPixiModule.class)
    @Singleton
    public interface IDemoPixiComponent {
        DemoPixi get();
    }

    @Override
    public void onModuleLoad() {
        if (LogConfiguration.loggingIsEnabled()) {
            LOG = Logger.getLogger("common");
            LOG.info("onModuleLoad --> entrypoint initiated!");
        }
        Spinner spinner = new Spinner();
        Browser.getDocument().getBody().appendChild(spinner.asNode());
        spinner.setColor("#fff");
        spinner.onLoad();
        GWT.runAsync(new RunAsyncCallback() {

            @Override
            public void onFailure(Throwable reason) {
                LOG.severe("could not load");
            }

            @Override
            public void onSuccess() {
                LOG.severe("onSuccess");
                PixiLoader.Statics.ensureInjected();
                SpineCrossVersionFix.fix();
                IDemoPixiComponent root = DaggerDemoPixiEntryPoint_IDemoPixiComponent
                        .create();
                root.get().initialize();
                spinner.onUnload();
            }

        });

    }
}
