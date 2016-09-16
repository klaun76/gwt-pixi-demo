package sk.mrtn.demo.pixi.client.lastguardiandemo;

import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.safehtml.shared.SafeUri;
import elemental.client.Browser;
import sk.mrtn.demo.pixi.client.ADemo;
import sk.mrtn.demo.pixi.client.DemoPixi;
import sk.mrtn.demo.pixi.client.IResources;
import sk.mrtn.pixi.client.*;
import sk.mrtn.pixi.client.extras.MovieClip;
import sk.mrtn.pixi.client.loaders.Loader;
import sk.mrtn.pixi.client.ticker.Ticker;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by martinliptak on 12/09/16.
 */
public class LastGuardianDemo extends ADemo {


    private static Logger LOG;
    static {
        if (LogConfiguration.loggingIsEnabled()) {
            LOG = Logger.getLogger(LastGuardianDemo.class.getSimpleName());
            LOG.setLevel(Level.ALL);
        }
    }

    private final Ticker ticker;
    private final Provider<Avatar> avatarProvider;

    @Inject
    LastGuardianDemo(
            final Ticker ticker,
            final Provider<Avatar> avatarProvider
            ){
        this.ticker = ticker;

        this.avatarProvider = avatarProvider;
    }

    public void initialize() {
        LOG.info("INJECTION STARTED");
        loadResources();
    }

    private void loadResources() {
        Loader aLoader = new Loader();
        for (SafeUri safeUri : DemoPixi.RES.lastGuardianAvatars().getSafeUris()) {
            aLoader.add(safeUri.asString());
        }
        aLoader.load((loader, resources) -> {
            buildStage();
        });
    }

    private void buildStage() {
        createAndAppendStage(800,600);
        List<Avatar> avatars = new ArrayList<>();

        for (String avatarFrame : DemoPixi.RES.lastGuardianAvatars().getFrames()) {
            String[] split = avatarFrame.split("_");
            String name = split[0];
            boolean exists = false;
            for (Avatar avatar : avatars) {
                if (avatar.getName().equals(name)) {
                    exists = true;
                }
            }
            if (exists) {
                continue;
            }
            Avatar avatar = avatarProvider.get().create(name);
            avatars.add(avatar);
        }
        int counter = 0;

        for (Avatar avatar : avatars) {
//            Sprite sprite = createSprite(avatar.getRight()[0], counter);
            MovieClip sprite = createMovieClip(avatar.getFront(), counter);
            this.stage.addChild(sprite);
            counter++;
        }

        for (Avatar avatar : avatars) {
//            Sprite sprite = createSprite(avatar.getRight()[0], counter);
            MovieClip sprite = createMovieClip(avatar.getRight(), counter);
            this.stage.addChild(sprite);
            counter++;
        }
        for (Avatar avatar : avatars) {
//            Sprite sprite = createSprite(avatar.getRight()[0], counter);
            MovieClip sprite = createMovieClip(avatar.getLeft(), counter);
            this.stage.addChild(sprite);
            counter++;
        }
        for (Avatar avatar : avatars) {
//            Sprite sprite = createSprite(avatar.getRight()[0], counter);
            MovieClip sprite = createMovieClip(avatar.getBack(), counter);
            this.stage.addChild(sprite);
            counter++;
        }

        renderer.render(stage);

        ticker.add(difference -> {
            renderer.render(stage);
        });
        ticker.start();
    }

    public static native void log(Object object) /*-{
        $wnd.console.log(object);
    }-*/;

    private MovieClip createMovieClip(Texture[] textures, int counter) {
        int columns = 25;
        int row = counter % columns;
        int column = (counter - row) / columns;

        int delta = 32;
        MovieClip movieClip = new MovieClip(textures);
        movieClip.anchor.x = 0;
        movieClip.anchor.y = 0;
        movieClip.position.x = row * delta;
        movieClip.position.y = column * delta;
        movieClip.animationSpeed = 0.1;
        movieClip.play();
        return movieClip;
    }

    private Sprite createSprite(Texture texture, int counter) {
        int columns = 20;
        int row = counter % columns;
        int column = (counter - row) / columns;

        int delta = 32;
        Sprite sprite = new Sprite(texture);
        sprite.anchor.x = 0;
        sprite.anchor.y = 0;
        sprite.position.x = row * delta;
        sprite.position.y = column * delta;
        return sprite;
    }



}
