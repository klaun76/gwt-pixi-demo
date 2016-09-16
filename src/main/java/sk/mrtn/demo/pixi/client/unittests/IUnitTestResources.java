package sk.mrtn.demo.pixi.client.unittests;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;
import sk.mrtn.pixi.client.resources.textureatlas.TextureAtlasResource;

/**
 * Created by martinliptak on 21/08/16.
 */
public interface IUnitTestResources extends ClientBundle {

    IUnitTestResources impl =  GWT.create(IUnitTestResources.class);

    @Source("UV_Grid_Sm.jpg")
    ImageResource testTextureSquare1024();

    @Source("uvmap.png")
    ImageResource testTextureSquare256();

    @Source("unitTest.json")
    TextureAtlasResource unitTestAtlas();

}
