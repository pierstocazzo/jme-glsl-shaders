package com.filter.colorscale;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.asset.plugins.HttpZipLocator;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;


public class TestColorScale extends SimpleApplication {

    private FilterPostProcessor fpp;
    private boolean enabled = true;
    private ColorScaleFilter colorScale;

    public static void main(final String[] args) {
        final TestColorScale app = new TestColorScale();
        app.start();
    }

    @Override
    public void simpleInitApp() {

        this.assetManager.registerLocator("assets", FileLocator.class);

        this.flyCam.setMoveSpeed(10);

        final Node mainScene = new Node();
        mainScene.attachChild(SkyFactory.createSky(this.assetManager, "Textures/Sky/Bright/BrightSky.dds", false));
        this.assetManager.registerLocator("http://jmonkeyengine.googlecode.com/files/wildhouse.zip",
                HttpZipLocator.class);
        // this.assetManager.registerLocator("wildhouse.zip", ZipLocator.class);
        final Spatial scene = this.assetManager.loadModel("main.scene");
        mainScene.attachChild(scene);
        this.rootNode.attachChild(mainScene);

        final DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        dl.setColor(new ColorRGBA(1, 1, 1, 1));
        this.rootNode.addLight(dl);

        final AmbientLight al = new AmbientLight();
        al.setColor(new ColorRGBA(1.5f, 1.5f, 1.5f, 1.0f));
        this.rootNode.addLight(al);

        this.flyCam.setMoveSpeed(15);

        this.fpp = new FilterPostProcessor(this.assetManager);
        this.fpp.setNumSamples(4);
        this.colorScale = new ColorScaleFilter(new ColorRGBA(130f / 255f, 26f / 255f, 90f / 255f, 1.0f), 0.7f);
        this.fpp.addFilter(this.colorScale);
        this.viewPort.addProcessor(this.fpp);
        this.initInputs();


        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize());
        ch.setText("Space,Y,H,1,2,3"); // crosshairs
        ch.setColor(new ColorRGBA(1f, 0.8f, 0.1f, 1f));
        ch.setLocalTranslation(settings.getWidth() * 0.3f, settings.getHeight() * 0.1f, 0);
        guiNode.attachChild(ch);

    }

    private void initInputs() {
        this.inputManager.addMapping("toggle", new KeyTrigger(KeyInput.KEY_SPACE));
        this.inputManager.addMapping("Overlay", new KeyTrigger(KeyInput.KEY_1));
        this.inputManager.addMapping("Multiply", new KeyTrigger(KeyInput.KEY_2));
        this.inputManager.addMapping("Normal", new KeyTrigger(KeyInput.KEY_3));
        this.inputManager.addMapping("DensityUp", new KeyTrigger(KeyInput.KEY_Y));
        this.inputManager.addMapping("DensityDown", new KeyTrigger(KeyInput.KEY_H));


        final ActionListener acl = new ActionListener() {
            @Override
            public void onAction(final String name, final boolean keyPressed, final float tpf) {
                if (name.equals("Overlay") && keyPressed) {
                    if (TestColorScale.this.enabled) {
                        TestColorScale.this.colorScale.setMultiply(false);
                        TestColorScale.this.colorScale.setOverlay(true);
                    }
                }

                if (name.equals("Multiply") && keyPressed) {
                    if (TestColorScale.this.enabled) {
                        TestColorScale.this.colorScale.setMultiply(true);
                        TestColorScale.this.colorScale.setOverlay(false);
                    }
                }

                if (name.equals("Normal") && keyPressed) {
                    if (TestColorScale.this.enabled) {
                        TestColorScale.this.colorScale.setMultiply(false);
                        TestColorScale.this.colorScale.setOverlay(false);
                    }
                }

                if (name.equals("toggle") && keyPressed) {
                    if (TestColorScale.this.enabled) {
                        TestColorScale.this.enabled = false;
                        TestColorScale.this.viewPort.removeProcessor(TestColorScale.this.fpp);
                    } else {
                        TestColorScale.this.enabled = true;
                        TestColorScale.this.viewPort.addProcessor(TestColorScale.this.fpp);
                    }
                }

            }
        };

        final AnalogListener anl = new AnalogListener() {
            @Override
            public void onAnalog(final String name, final float isPressed, final float tpf) {
                if (name.equals("DensityUp")) {
                    TestColorScale.this.colorScale
                            .setColorDensity(TestColorScale.this.colorScale.getColorDensity() + 0.001f);
                    System.out.println("ColorScale density : " + TestColorScale.this.colorScale.getColorDensity());
                }
                if (name.equals("DensityDown")) {
                    TestColorScale.this.colorScale
                            .setColorDensity(TestColorScale.this.colorScale.getColorDensity() - 0.001f);
                    System.out.println("ColorScale density : " + TestColorScale.this.colorScale.getColorDensity());
                }
            }
        };

        this.inputManager.addListener(acl, "toggle");
        this.inputManager.addListener(acl, "Overlay");
        this.inputManager.addListener(acl, "Multiply");
        this.inputManager.addListener(acl, "Normal");
        this.inputManager.addListener(anl, "DensityUp", "DensityDown");

    }
}