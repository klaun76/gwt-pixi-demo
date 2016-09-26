package sk.mrtn.demo.pixi.client;

import com.google.gwt.logging.client.LogConfiguration;
import sk.mrtn.demo.pixi.client.common.IStage;
import sk.mrtn.demo.pixi.client.defaultdemo.DefaultDemo;
import sk.mrtn.demo.pixi.client.lastguardiandemo.LastGuardianDemo;
import sk.mrtn.demo.pixi.client.tokitori.TokiToriDemo;
import sk.mrtn.demo.pixi.client.unittests.UnitTests;
import sk.mrtn.library.client.ticker.ITicker;
import sk.mrtn.library.client.utils.IUrlParametersManager;
import sk.mrtn.library.client.utils.stats.StatsLoader;

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
    private final IStage stage;
    private final Menu menu;
    private final ITicker ticker;

    enum Type {
        PARTICLES("particles"),
        TOKI_TORI("tokitori"),
        LAST_GUARDIAN("lastguardian"),
        UNIT_TESTS("unittests"),
        DEFAULT("default");

        private final String name;

        Type(String name) {
            this.name = name;
        }

        public static Type parse(String name) {
            for (Type type : values()) {
                if (type.name.equals(name)) return type;
            }
            return DEFAULT;
        }
        public String toString() {
            return this.name;
        }
    }

    @Inject
    DemoPixi(
            final ITicker ticker,
            final IStage stage,
            final IUrlParametersManager urlParametersManager,
            final Menu menu,
            final Provider<DefaultDemo> defaultDemoProvider,
            final Provider<LastGuardianDemo> lastGuardianDemoProvider,
            final Provider<UnitTests> unitTestsProvider,
            final Provider<TokiToriDemo> tokiToriDemoProvider
    ){
        this.ticker = ticker;
        this.stage = stage;
        this.urlParametersManager = urlParametersManager;
        this.menu = menu;
        this.defaultDemoProvider = defaultDemoProvider;
        this.lastGuardianDemoProvider = lastGuardianDemoProvider;
        this.unitTestsProvider = unitTestsProvider;
        this.tokiToriDemoProvider = tokiToriDemoProvider;
    }

    public void initialize() {
        LOG.info("INJECTION STARTED");
        this.stage.initialize(1024,1024);
        RES.main().ensureInjected();
        StatsLoader.Statics.initialize(this.ticker,this.urlParametersManager);
        this.menu.initialize(() -> onMenuInitialized());
    }

    private void onMenuInitialized() {
        menu.addButton(Type.PARTICLES.name(),DemoPixi.this::startParticles);
        menu.addButton(Type.TOKI_TORI.name(),DemoPixi.this::startTokiTori);
        menu.addButton(Type.LAST_GUARDIAN.name(),DemoPixi.this::startLastGuardian);
        menu.addButton(Type.UNIT_TESTS.name(),DemoPixi.this::startUnitTests);

        Type type = Type.parse(urlParametersManager.getParameter("autorun"));
        switch (type) {
            case PARTICLES:
                startParticles();
                break;
            case TOKI_TORI:
                startTokiTori();
                break;
            case LAST_GUARDIAN:
                startLastGuardian();
                break;
            case UNIT_TESTS:
                startUnitTests();
                break;
            case DEFAULT:
                menu.show();
        }
    }

    private void startLastGuardian() {
        lastGuardianDemoProvider.get().open(null);
        if (!this.lastGuardianDemoProvider.get().isInitialized()) {
            lastGuardianDemoProvider.get().getButton().addClickHandler(button -> menu.show());
        }
    }

    private void startUnitTests() {
        this.unitTestsProvider.get().open(null);
        if (!this.unitTestsProvider.get().isInitialized()) {
            unitTestsProvider.get().getButton().addClickHandler(button -> menu.show());
        }
    }

    private void startTokiTori() {
        this.tokiToriDemoProvider.get().open(null);
        if (!this.tokiToriDemoProvider.get().isInitialized()) {
            tokiToriDemoProvider.get().getButton().addClickHandler(button -> menu.show());
        }
    }

    private void startParticles() {
        this.defaultDemoProvider.get().open(null);
        if (!this.defaultDemoProvider.get().isInitialized()) {
            defaultDemoProvider.get().getButton().addClickHandler(button -> menu.show());
        }
    }

    public static native void log(Object object) /*-{
        $wnd.console.log(object);
    }-*/;
}
