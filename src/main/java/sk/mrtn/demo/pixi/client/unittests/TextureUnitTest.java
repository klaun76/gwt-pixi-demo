package sk.mrtn.demo.pixi.client.unittests;

import sk.mrtn.pixi.client.Texture;

import javax.inject.Inject;

/**
 * Created by martinliptak on 16/09/16.
 */
public class TextureUnitTest extends AUnitTest {

    private Texture texture;
    private int width;
    private int height;

    @Inject
    TextureUnitTest(){}

    public TextureUnitTest initialize(String label, Texture texture, int width, int height){
        initialize(label);
        this.texture = texture;
        this.width = width;
        this.height = height;
        log(texture);
        testFieldsValidity();
        testSize();
        return this;
    }

    private void testSize() {
        boolean widthIsValid = this.texture.frame.width == this.width;
        boolean heigthIsValid = this.texture.frame.height == this.height;
        if (!widthIsValid) LOG.severe("width is not correct");
        if (!heigthIsValid) LOG.severe("height is not correct");
    }

    private void testFieldsValidity() {
        if (this.texture._frame == null) {
            LOG.severe("INVALID this.texture._frame");
        } else {
            LOG.fine("_frame: "+this.texture._frame.toString());
        }

        if (this.texture.frame == null) {
            LOG.severe("INVALID this.texture.frame");
        } else {
            LOG.fine("frame: "+this.texture.frame.toString());
        }

        if (this.texture.orig == null) {
            LOG.severe("INVALID this.texture.orig");
        } else {
            LOG.fine("orig: "+this.texture.orig.toString());
        }

        if (this.texture.trim == null) {
            LOG.fine("undefined this.texture.trim");
        } else {
            LOG.fine("trim: "+this.texture.trim.toString());
        }

        if (this.texture.baseTexture == null) LOG.severe("INVALID this.texture.baseTexture");

        LOG.fine("this.texture.noFrame value = "+this.texture.noFrame);
        LOG.fine("this.texture.requiresUpdate value = "+this.texture.requiresUpdate);
        LOG.fine("this.texture.valid value = "+this.texture.valid);
    }
}
