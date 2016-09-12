package sk.mrtn.demo.pixi.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Timer;
import elemental.client.Browser;
import elemental.css.CSSStyleDeclaration;
import elemental.html.DivElement;
import jsinterop.annotations.JsMethod;
import sk.mrtn.library.client.utils.IUrlParametersManager;
import sk.mrtn.pixi.client.Container;
import sk.mrtn.pixi.client.PIXI;
import sk.mrtn.pixi.client.PixiEntryPoint;
import sk.mrtn.pixi.client.Point;
import sk.mrtn.pixi.client.Renderer;
import sk.mrtn.pixi.client.Sprite;
import sk.mrtn.pixi.client.Texture;
import sk.mrtn.pixi.client.filters.ColorMatrixFilter;
import sk.mrtn.pixi.client.loaders.Loader;
import sk.mrtn.pixi.client.particles.AnimatedParticleArtTextureNames;
import sk.mrtn.pixi.client.particles.Emitter;
import sk.mrtn.pixi.client.particles.RepetitiveTexture;
import sk.mrtn.pixi.client.particles.config.EmitterConfig;
import sk.mrtn.pixi.client.resources.textureatlas.TextureAtlasResource;
import sk.mrtn.pixi.client.ticker.Ticker;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @FunctionalInterface
    private interface IButtonCommand{
        void run();
    }

    @Inject
    DemoPixi(
            final IUrlParametersManager urlParametersManager,
            final Provider<DefaultDemo> defaultDemoProvider
            ){
        this.urlParametersManager = urlParametersManager;
        this.defaultDemoProvider = defaultDemoProvider;
    }



    public void initialize() {
        LOG.info("INJECTION STARTED");
        String type = urlParametersManager.getParameter("autorun");
        switch (type) {
           case "mrtn":
               LOG.severe("dorob si uvod!");
               break;
            default:
                this.defaultDemoProvider.get().initialize();
        }


    }

}
