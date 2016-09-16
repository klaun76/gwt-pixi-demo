package sk.mrtn.demo.pixi.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import sk.mrtn.pixi.client.resources.textureatlas.TextureAtlasResource;

/**
 * Created by martinliptak on 21/08/16.
 */
public interface IResources extends ClientBundle {

    IResources impl =  GWT.create(IResources.class);

    @Source("main.css")
    CssResource main();

    @Source("gold_anim.json")
    TextureAtlasResource goldAnim();

    @Source("buble_coin_atlas_d-1080_0.json")
    TextureAtlasResource bubblesAndCoinAnims();

    @Source("lastGuardianAvatars.json")
    TextureAtlasResource lastGuardianAvatars();

    @Source("bunny.png")
    ImageResource bunny();

    @Source("spices3.jpg")
    ImageResource spices3();

    @Source("gold_anim_emitter.json")
    TextResource goldEmitter();

    @Source("bubbles_emitter.json")
    TextResource bubblesEmitter();
}
