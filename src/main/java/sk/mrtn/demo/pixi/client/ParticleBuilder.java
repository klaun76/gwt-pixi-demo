package sk.mrtn.demo.pixi.client;

import com.google.gwt.logging.client.LogConfiguration;
import sk.mrtn.pixi.client.*;
import sk.mrtn.pixi.client.particles.*;
import sk.mrtn.pixi.client.particles.config.EmitterConfig;
import sk.mrtn.pixi.client.resources.textureatlas.TextureAtlasResource;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by martinliptak on 23/08/16.
 * Object should serve as automator for building
 * and bringing to life standard/simple particle systems
 */
public class ParticleBuilder {

    private static Logger LOG;
    static {
        if (LogConfiguration.loggingIsEnabled()) {
            LOG = Logger.getLogger(ParticleBuilder.class.getSimpleName());
            LOG.setLevel(Level.ALL);
        }
    }

    private final EmitterFactory emitterFactory;
    private final Provider<ParticleContainer> particleContainerProvider;
    private final Provider<Container> containerProvider;
    private final Provider<ParticleContainerProperties> particleContainerPropertiesProvider;
    private Emitter emitter;

    public Container getContainer() {
        return container;
    }

    private Container container;

    @Inject
    ParticleBuilder(
            final EmitterFactory emitterFactory,
            final Provider<ParticleContainer> particleContainerProvider,
            final Provider<Container> containerProvider,
            final Provider<ParticleContainerProperties> particleContainerPropertiesProvider
            ){
        this.emitterFactory = emitterFactory;
        this.particleContainerProvider = particleContainerProvider;
        this.containerProvider = containerProvider;
        this.particleContainerPropertiesProvider = particleContainerPropertiesProvider;
    }

    // TODO: add customisation for container type, and particle type
    public void initialize(String emitterConfig, TextureAtlasResource textureAtlasResource) {
        LOG.fine("initialize");
        EmitterConfig config = EmitterConfig.parse(emitterConfig);
        AnimatedParticleArtTextureNames[] arts = new AnimatedParticleArtTextureNames[1];
        AnimatedParticleArtTextureNames art = new AnimatedParticleArtTextureNames();
        art.setFramerateToMatchLife();
        art.loop = true;
        List<String> frames = textureAtlasResource.getFrames();
        String[] textures = new String[frames.size()];
        for (int i = 0; i < frames.size(); i++) {
            textures[i] = frames.get(i);
        }
        art.setTextures(textures);
        art.addTexture(Texture.fromFrame("gold_6.png"),20);
        arts[0]=art;
        this.container = getContainer(true);
        this.emitter = buildEmitter(this.container, config, arts, ParticleType.ANIMATED_PARTICLE);
    }

    public static native void logg(Object object) /*-{
        $wnd.console.log(object);
    }-*/;

    private Emitter buildEmitter(Container container, EmitterConfig config, AnimatedParticleArtTextureNames[] art, ParticleType animatedParticle) {
        Emitter emitter = this.emitterFactory.create(container, art, config);
        switch (animatedParticle) {
            case PARTICLE:
                break;
            case ANIMATED_PARTICLE:
                setAnimatedParticleConstructor(emitter);
                break;
            case PATH_PARTICLE:
                setPathParticleConstructor(emitter);
                break;
        }
        return emitter;
    }

    // at time i did not know any better solution for this setting :-(
    public static native void setAnimatedParticleConstructor(Emitter emitter) /*-{
        emitter.particleConstructor = $wnd.PIXI.particles.AnimatedParticle;
    }-*/;

    // at time i did not know any better solution for this setting :-(
    public static native void setPathParticleConstructor(Emitter emitter) /*-{
        emitter.particleConstructor = $wnd.PIXI.particles.PathParticle;
    }-*/;

    private Container getContainer(boolean useParticleContainer) {
        if (useParticleContainer) {
            ParticleContainerProperties particleContainerProperties = this.particleContainerPropertiesProvider.get();
            ParticleContainer particleContainer = this.particleContainerProvider.get();
            particleContainer.setProperties(particleContainerProperties.set(true,true,true,true,true));
            return particleContainer;
        } else {
            return containerProvider.get();
        }
    }

    public Emitter getEmitter() {
        return emitter;
    }
}
