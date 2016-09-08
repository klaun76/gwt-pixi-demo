package sk.mrtn.demo.pixi.client;

import com.google.gwt.logging.client.LogConfiguration;
import sk.mrtn.pixi.client.Container;
import sk.mrtn.pixi.client.particles.AnimatedParticleArtTextureNames;
import sk.mrtn.pixi.client.particles.Emitter;
import sk.mrtn.pixi.client.particles.EmitterFactory;
import sk.mrtn.pixi.client.particles.ParticleContainer;
import sk.mrtn.pixi.client.particles.ParticleContainerProperties;
import sk.mrtn.pixi.client.particles.ParticleType;
import sk.mrtn.pixi.client.particles.config.EmitterConfig;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Patrik on 07/09/16.
 * Object should serve as automator for building
 * and bringing to life particle systems
 * with multiple differently configured emitters
 * contained within one single ParticleContainer
 */
public class MultiParticleBuilder {

    private static Logger LOG;
    static {
        if (LogConfiguration.loggingIsEnabled()) {
            LOG = Logger.getLogger(MultiParticleBuilder.class.getSimpleName());
            LOG.setLevel(Level.ALL);
        }
    }

    private final EmitterFactory emitterFactory;
    private final Provider<ParticleContainer> particleContainerProvider;
    private final Provider<Container> containerProvider;
    private final Provider<ParticleContainerProperties> particleContainerPropertiesProvider;
    private List<Emitter> emittersList;

    public Container getContainer() {
        return container;
    }

    private Container container;

    @Inject
    MultiParticleBuilder(
            final EmitterFactory emitterFactory,
            final Provider<ParticleContainer> particleContainerProvider,
            final Provider<Container> containerProvider,
            final Provider<ParticleContainerProperties> particleContainerPropertiesProvider
            ){
        this.emitterFactory = emitterFactory;
        this.particleContainerProvider = particleContainerProvider;
        this.containerProvider = containerProvider;
        this.particleContainerPropertiesProvider = particleContainerPropertiesProvider;
        this.emittersList = new ArrayList<>();
    }

    // TODO: add customisation for container type, and particle type

    /**
     * Method that creates multiple emitters according to given configuration and art
     * and puts them all in the same ParticleContainer
     * @param configToArtsMap is data structure representing specification for one emitter.
     *                        Key of the map is String - config of given emitter
     *                        Value of the map is list of arts, which specifies which
     *                        animations are used for given emitter
     *
     */
    public void initialize(Map<String,List<AnimatedParticleArtTextureNames>> configToArtsMap) {
        LOG.fine("initialize");
        this.container = getContainer(true);

        for (Map.Entry<String, List<AnimatedParticleArtTextureNames>> entry : configToArtsMap.entrySet()) {

            EmitterConfig config = EmitterConfig.parse(entry.getKey());
            AnimatedParticleArtTextureNames[] arts = new AnimatedParticleArtTextureNames[entry.getValue().size()];
            arts = entry.getValue().toArray(arts);
            Emitter emitter = buildEmitter(this.container, config, arts, ParticleType.ANIMATED_PARTICLE);
            emittersList.add(emitter);
        }
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

    public void update(double delta){
        for (Emitter emitter : emittersList) {
            emitter.update(delta);
        }
    }
}
