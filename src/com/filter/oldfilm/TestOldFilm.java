package com.filter.oldfilm;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.asset.plugins.HttpZipLocator;
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


public class TestOldFilm extends SimpleApplication {

    private FilterPostProcessor fpp;
    private boolean enabled = true;
    private OldFilmFilter colorScale;

    public static void main(final String[] args) {
        final TestOldFilm app = new TestOldFilm();
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
        this.colorScale = new OldFilmFilter(new ColorRGBA(112f / 255f, 66f / 255f, 20f / 255f, 1.0f), 0.7f, 0.4f, 0.3f);
        this.fpp.addFilter(this.colorScale);
        this.viewPort.addProcessor(this.fpp);
        this.initInputs();
    }

    private void initInputs() {
        this.inputManager.addMapping("toggle", new KeyTrigger(KeyInput.KEY_SPACE));
        this.inputManager.addMapping("ColorDensityUp", new KeyTrigger(KeyInput.KEY_Y));
        this.inputManager.addMapping("ColorDensityDown", new KeyTrigger(KeyInput.KEY_H));
        this.inputManager.addMapping("NoiseDensityUp", new KeyTrigger(KeyInput.KEY_U));
        this.inputManager.addMapping("NoiseDensityDown", new KeyTrigger(KeyInput.KEY_J));
        this.inputManager.addMapping("ScratchDensityUp", new KeyTrigger(KeyInput.KEY_I));
        this.inputManager.addMapping("ScratchDensityDown", new KeyTrigger(KeyInput.KEY_K));

        final ActionListener acl = new ActionListener() {

            @Override
            public void onAction(final String name, final boolean keyPressed, final float tpf) {
                if (name.equals("toggle") && keyPressed) {
                    if (TestOldFilm.this.enabled) {
                        TestOldFilm.this.enabled = false;
                        TestOldFilm.this.viewPort.removeProcessor(TestOldFilm.this.fpp);
                    } else {
                        TestOldFilm.this.enabled = true;
                        TestOldFilm.this.viewPort.addProcessor(TestOldFilm.this.fpp);
                    }
                }

            }
        };

        final AnalogListener anl = new AnalogListener() {

            @Override
            public void onAnalog(final String name, final float isPressed, final float tpf) {
                if (name.equals("ColorDensityUp")) {
                    TestOldFilm.this.colorScale.setColorDensity(TestOldFilm.this.colorScale.getColorDensity() + 0.01f);
                    System.out.println("ColorScale color density : " + TestOldFilm.this.colorScale.getColorDensity());
                }
                if (name.equals("ColorDensityDown")) {
                    TestOldFilm.this.colorScale.setColorDensity(TestOldFilm.this.colorScale.getColorDensity() - 0.01f);
                    System.out.println("ColorScale color density : " + TestOldFilm.this.colorScale.getColorDensity());
                }
                if (name.equals("NoiseDensityUp")) {
                    TestOldFilm.this.colorScale.setNoiseDensity(TestOldFilm.this.colorScale.getNoiseDensity() + 0.01f);
                    System.out.println("ColorScale noise density : " + TestOldFilm.this.colorScale.getNoiseDensity());
                }
                if (name.equals("NoiseDensityDown")) {
                    TestOldFilm.this.colorScale.setNoiseDensity(TestOldFilm.this.colorScale.getNoiseDensity() - 0.01f);
                    System.out.println("ColorScale noise density : " + TestOldFilm.this.colorScale.getNoiseDensity());
                }
                if (name.equals("ScratchDensityUp")) {
                    TestOldFilm.this.colorScale
                            .setScratchDensity(TestOldFilm.this.colorScale.getScratchDensity() + 0.01f);
                    System.out.println("ColorScale scratch density : "
                            + TestOldFilm.this.colorScale.getScratchDensity());
                }
                if (name.equals("ScratchDensityDown")) {
                    TestOldFilm.this.colorScale
                            .setScratchDensity(TestOldFilm.this.colorScale.getScratchDensity() - 0.01f);
                    System.out.println("ColorScale scratch density : "
                            + TestOldFilm.this.colorScale.getScratchDensity());
                }
            }
        };

        this.inputManager.addListener(acl, "toggle");
        this.inputManager.addListener(anl, "ColorDensityUp", "ColorDensityDown", "NoiseDensityUp", "NoiseDensityDown",
                "ScratchDensityUp", "ScratchDensityDown");

    }
}