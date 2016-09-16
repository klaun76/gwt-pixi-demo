package sk.mrtn.demo.pixi.client.tokitori;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import sk.mrtn.pixi.client.resources.textureatlas.TextureAtlasResource;

/**
 * Created by klaun on 21/08/16.
 *
 * Demo images downloaded from http://www.spriters-resource.com/wii/tokitori/
 * and used with courtesy of
 * Collin van Ginkel <collin@twotribes.com>, Co-Founder @ www.twotribes.com
 * I asked permision by mail on Sept 14 2016:
 * I'm working on technical demo for html5 based engine (pixi.js
 * , gwt, i do not want to bore you with details). And for this i
 * need some nice animations. I was ready to purchase some but i
 * did not find anything nice. but while browsing for something i
 * found out this site: http://www.spriters-resource.com/wii/tokitori/.
 * On this site there are your sprites of animations. I would like to
 * know if i could use theese sprites for my technical demo.
 *
 * The answer:
 *
 * go ahead!
 * Show us the results?
 * Thanks!
 */
public interface ITokiToriResources extends ClientBundle {

    ITokiToriResources impl =  GWT.create(ITokiToriResources.class);

    @Source("tokitori_0.json")
    TextureAtlasResource toki();

}
