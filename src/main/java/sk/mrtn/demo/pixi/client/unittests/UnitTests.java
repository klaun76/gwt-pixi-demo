package sk.mrtn.demo.pixi.client.unittests;

import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.safehtml.shared.SafeUri;
import elemental.client.Browser;
import sk.mrtn.demo.pixi.client.ADemo;
import sk.mrtn.pixi.client.*;
import sk.mrtn.pixi.client.loaders.Loader;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by martinliptak on 12/09/16.
 */
public class UnitTests extends ADemo {


    private static IUnitTestResources RES = IUnitTestResources.impl;
    private static Logger LOG;
    static {
        if (LogConfiguration.loggingIsEnabled()) {
            LOG = Logger.getLogger(UnitTests.class.getSimpleName());
            LOG.setLevel(Level.ALL);
        }
    }

    private final SpriteFactory spriteFactory;
    private final Provider<TextureUnitTest> textureUnitTestProvider;



    @Inject
    UnitTests(
            final SpriteFactory spriteFactory,
            final Provider<TextureUnitTest> textureUnitTestProvider
            ){
        this.spriteFactory = spriteFactory;
        this.textureUnitTestProvider = textureUnitTestProvider;
    }

    public void initialize() {
        LOG.info("INJECTION STARTED");
        loadResources();
    }


    private void loadResources() {
        Loader aLoader = new Loader();
        aLoader.add("test256",RES.testTextureSquare256().getSafeUri().asString());
        aLoader.add("test1024",RES.testTextureSquare1024().getSafeUri().asString());
        for (SafeUri safeUri : RES.unitTestAtlas().getSafeUris()) {
            aLoader.add(safeUri.asString());
        }
        aLoader.load((loader, resources) -> {
            buildStage();
        });
    }

    protected void buildStage() {
        createAndAppendStage(1024,1024);
        createTexturesFromImage();
        renderer.render(stage);
        addAlignmentTest();
        renderer.render(stage);
    }

    private void createTexturesFromImage() {
        // unit test for texture of size 1024x1024 from image
        Texture texture1024 = Texture.fromImage(RES.testTextureSquare1024().getSafeUri().asString());
        Sprite sprite1024 = this.spriteFactory.create(texture1024);
        this.stage.addChild(sprite1024);
        this.textureUnitTestProvider.get().initialize("1024", texture1024,1024,1024);

        // unit test for texture of size 1024x1024 from image
        Texture texture256 = Texture.fromImage(RES.testTextureSquare256().getSafeUri().asString());
        Sprite sprite256 = this.spriteFactory.create(texture256);
        this.textureUnitTestProvider.get().initialize("256", texture256,256,256);
        this.stage.addChild(sprite256);

        Texture texture128 = Texture.fromFrame("test128x128");
        Sprite sprite128 = this.spriteFactory.create(texture128);
        this.textureUnitTestProvider.get().initialize("128",texture128,128,128);
        this.stage.addChild(sprite128);

        Texture textureSmile = Texture.fromFrame("smile");
        Sprite spriteSmile = this.spriteFactory.create(textureSmile);
        this.textureUnitTestProvider.get().initialize("smile",textureSmile,128,106);
        this.stage.addChild(spriteSmile);
        Point point = new Point(spriteSmile.parent.width / 2, spriteSmile.parent.height / 2);
        spriteSmile.anchor.set(0.5,0.5);
        spriteSmile.position = point;
    }

    private void addAlignmentTest() {
        Texture tArrowLeft = Texture.fromFrame("arrowLeft");
        Sprite sArrowLeft = this.spriteFactory.create(tArrowLeft);
        sArrowLeft.anchor.set(1,0.5);
        sArrowLeft.position = new Point(this.stage.width , this.stage.height / 2);
        this.stage.addChild(sArrowLeft);

        Texture tArrowRight = Texture.fromFrame("arrowRight");
        Sprite sArrowRight = this.spriteFactory.create(tArrowRight);
        sArrowRight.anchor.set(0,0.5);
        sArrowRight.position = new Point(0, this.stage.height / 2);
        this.stage.addChild(sArrowRight);

        Texture tArrowDown = Texture.fromFrame("arrowDown");
        Sprite sArrowDown = this.spriteFactory.create(tArrowDown);
        sArrowDown.anchor.set(0.5,1);
        sArrowDown.position = new Point(this.stage.width / 2, this.stage.height );
        this.stage.addChild(sArrowDown);

        Texture tArrowUp = Texture.fromFrame("arrowUp");
        Sprite sArrowUp = this.spriteFactory.create(tArrowUp);
        sArrowUp.anchor.set(0.5,0);
        sArrowUp.position = new Point(this.stage.width / 2, 0);
        this.stage.addChild(sArrowUp);

    }





}
