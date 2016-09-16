package sk.mrtn.demo.pixi.client;

import com.google.gwt.logging.client.LogConfiguration;
import sk.mrtn.demo.pixi.client.defaultdemo.DefaultDemo;
import sk.mrtn.demo.pixi.client.lastguardiandemo.LastGuardianDemo;
import sk.mrtn.demo.pixi.client.tokitori.TokiToriDemo;
import sk.mrtn.demo.pixi.client.unittests.UnitTests;
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

    public static IResources RES = IResources.impl;
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
    private final Provider<UnitTests> unitTestsProvider;
    private final Provider<TokiToriDemo> tokiToriDemoProvider;

    @Inject
    DemoPixi(
            final IUrlParametersManager urlParametersManager,
            final Provider<DefaultDemo> defaultDemoProvider,
            final Provider<LastGuardianDemo> lastGuardianDemoProvider,
            final Provider<UnitTests> unitTestsProvider,
            final Provider<TokiToriDemo> tokiToriDemoProvider
            ){
        this.urlParametersManager = urlParametersManager;
        this.defaultDemoProvider = defaultDemoProvider;
        this.lastGuardianDemoProvider = lastGuardianDemoProvider;
        this.unitTestsProvider = unitTestsProvider;
        this.tokiToriDemoProvider = tokiToriDemoProvider;
    }

    public void initialize() {
        LOG.info("INJECTION STARTED");
        setStage();
        String type = urlParametersManager.getParameter("autorun");
        switch (type) {
            case "tokitori":
                this.tokiToriDemoProvider.get().initialize();
                break;
            case "unittests":
                this.unitTestsProvider.get().initialize();
                break;
            case "lastguardian":
               lastGuardianDemoProvider.get().initialize();
               break;
            default:
                this.defaultDemoProvider.get().initialize();
        }


    }

    private void setStage() {
        RES.main().ensureInjected();
    }

}
