package sk.mrtn.demo.pixi.client.unittests;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.logging.client.LogConfiguration;
import com.google.gwt.safehtml.shared.SafeUri;
import sk.mrtn.demo.pixi.client.ADemo;
import sk.mrtn.demo.pixi.client.unittests.buttons.IShapeButton;
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
    private final Provider<IShapeButton> shapeButtonProvider;
    private final Provider<TextureUnitTest> textureUnitTestProvider;

    @Inject
    UnitTests(
            final SpriteFactory spriteFactory,
            final Provider<IShapeButton> shapeButtonProvider,
            final Provider<TextureUnitTest> textureUnitTestProvider
            ){
        this.spriteFactory = spriteFactory;
        this.shapeButtonProvider = shapeButtonProvider;
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
        addAlignmentTest();
        drawShapes();
        addButton();
        renderer.render(stage);

        Container container = new Container();
        log(container);
    }

    private void addButton() {
        double width = 256;
        double height = 64;
        Point point = new Point(512, 512);
        IShapeButton green = this.shapeButtonProvider.get().create(width, height, 10, IShapeButton.Color.GREEN, "green\nbutton");
        green.asDisplayObject().position = point.clone();
        this.stage.addChild(green.asDisplayObject());

        IShapeButton red = this.shapeButtonProvider.get().create(width, height, 10, IShapeButton.Color.RED, "q red");
        point.set(point.x,point.y+height*1.2);
        red.asDisplayObject().position = point.clone();
        this.stage.addChild(red.asDisplayObject());

        IShapeButton blue = this.shapeButtonProvider.get().create(width, height, 10, IShapeButton.Color.BLUE, "blue Q");
        point.set(point.x,point.y+height*1.2);
        blue.asDisplayObject().position = point.clone();
        this.stage.addChild(blue.asDisplayObject());

        IShapeButton violet = this.shapeButtonProvider.get().create(width, height, 10, IShapeButton.Color.VIOLET, "VIOLET");
        point.set(point.x,point.y+height*1.2);
        violet.asDisplayObject().position = point.clone();
        this.stage.addChild(violet.asDisplayObject());
    }

    /**
     * @see <a href="https://pixijs.github.io/examples/#/basics/graphics.js">Graphic demo</a>
     */
    private void drawShapes() {
        Graphics graphics = new Graphics();
        graphics.beginFill(0xFF3300,0.5);
        graphics.lineStyle(4, 0xffd900, 1);

        // draw a shape
        graphics.moveTo(50,50);
        graphics.lineTo(250, 50);
        graphics.lineTo(100, 100);
        graphics.lineTo(50, 50);
        graphics.endFill();

        // set a fill and a line style again and draw a rectangle
        graphics.lineStyle(2, 0x0000FF, 1);
        graphics.beginFill(0xFF700B, 1);
        graphics.drawRect(50, 250, 120, 120);

        // draw a rounded rectangle
        graphics.lineStyle(2, 0xFF00FF, 1);
        graphics.beginFill(0xFF00BB, 0.25);
        graphics.drawRoundedRect(150, 450, 300, 100, 15);
        graphics.endFill();

        // draw a circle, set the lineStyle to zero so the circle doesn't have an outline
        graphics.lineStyle(0);
        graphics.beginFill(0xFFFF0B, 0.5);
        graphics.drawCircle(470, 90,60);
        graphics.endFill();

        this.stage.addChild(graphics);
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
