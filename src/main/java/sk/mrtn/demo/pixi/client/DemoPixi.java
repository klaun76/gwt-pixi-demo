package sk.mrtn.demo.pixi.client;

import com.google.gwt.logging.client.LogConfiguration;
import sk.mrtn.demo.pixi.client.defaultdemo.DefaultDemo;
import sk.mrtn.demo.pixi.client.lastguardiandemo.LastGuardianDemo;
import sk.mrtn.library.client.utils.IUrlParametersManager;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by klaun on 25/04/16.
 * This class is supposed to be main entry point for whole project
 */
public class DemoPixi {

    private static IResources RES = IResources.impl;
    private static Logger LOG;
    static {
        if (LogConfiguration.loggingIsEnabled()) {
            LOG = Logger.getLogger(DemoPixi.class.getSimpleName());
            LOG.setLevel(Level.ALL);
        }
    }

    private final IUrlParametersManager urlParametersManager;
    private final Provider<DefaultDemo> defaultDemoProvider;
    private final Provider<LastGuardianDemo> lastGuardianDemoProvider;

    @Inject
    DemoPixi(
            final IUrlParametersManager urlParametersManager,
            final Provider<DefaultDemo> defaultDemoProvider,
            final Provider<LastGuardianDemo> lastGuardianDemoProvider
            ){
        this.urlParametersManager = urlParametersManager;
        this.defaultDemoProvider = defaultDemoProvider;
        this.lastGuardianDemoProvider = lastGuardianDemoProvider;
    }



    public void initialize() {
        LOG.info("INJECTION STARTED");
        String type = urlParametersManager.getParameter("autorun");
        switch (type) {
           case "lastguardian":
               lastGuardianDemoProvider.get().initialize();
               break;
            default:
                this.defaultDemoProvider.get().initialize();
        }


    }

}
