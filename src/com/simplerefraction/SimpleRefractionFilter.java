/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.simplerefraction;

/*
 * Copyright (c) 2009-2010 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
import com.jme3.asset.AssetManager;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.post.Filter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.texture.Image.Format;
import com.jme3.texture.Texture.MagFilter;
import com.jme3.texture.Texture.MinFilter;
import com.jme3.texture.Texture.WrapMode;
import com.jme3.texture.Texture2D;
import java.io.IOException;
import java.util.ArrayList;

/**
 * BloomFilter is used to make objects in the scene have a glow effect.<br>
 * There are 2 mode : Scene and Objects.<br>
 * Scene mode extracts the bright parts of the scene to make them glow<br>
 * Object mode make objects glow according to their material's glowMap or their GlowColor<br>
 * @see <a href="http://jmonkeyengine.org/wiki/doku.php/jme3:advanced:bloom_and_glow">advanced:bloom_and_glow</a> for more details
 * 
 * @author Rémy Bouquet aka Nehon
 */
public class SimpleRefractionFilter extends Filter {

    //Bloom parameters
    private float blurScale = 1.5f;
    private float exposurePower = 5.0f;
    private float exposureCutOff = 0.0f;
    private float bloomIntensity = 2.0f;
    private float downSamplingFactor = 1;
    private Pass verticalalBlur = new Pass();
    private Material vBlurMat;
    private int screenWidth;
    private int screenHeight;
    protected Texture2D refractionTexture;
    protected Texture2D depthTexture;
    protected Texture2D normalTexture;
    protected Texture2D dudvTexture;
    private AssetManager manager;
    private Material material2;
    /**
     * Creates a Bloom filter
     */
    public SimpleRefractionFilter() {
        super("RefractionFilter");
    }

    @Override
    protected void initFilter(AssetManager manager, RenderManager renderManager, ViewPort vp, int w, int h) {
        screenWidth = (int) Math.max(1, (w / downSamplingFactor));
        screenHeight = (int) Math.max(1, (h / downSamplingFactor));
        //    System.out.println(screenWidth + " " + screenHeight);

        postRenderPasses = new ArrayList<Pass>();


//        material.setTexture("BloomTex", verticalalBlur.getRenderedTexture());


        
        //configuring vertical blur pass
        vBlurMat = new Material(manager, "Common/MatDefs/Misc/Unshaded.j3md");
        verticalalBlur = new Pass() {

            @Override
            public void beforeRender() {

            }
        };

        verticalalBlur.init(renderManager.getRenderer(), screenWidth, screenHeight, Format.RGBA8, Format.Depth, 1, vBlurMat);
        postRenderPasses.add(verticalalBlur);

        this.manager = manager;
        
        //final material
        material2 = new Material(manager, "MatDefs/SimpleRefraction/SimpleRefractionFilter.j3md");
        
        
        normalTexture = (Texture2D) manager.loadTexture("Common/MatDefs/Water/Textures/water_normalmap.dds");
        normalTexture.setWrap(WrapMode.Repeat);
        normalTexture.setMagFilter(MagFilter.Bilinear);
        normalTexture.setMinFilter(MinFilter.BilinearNearestMipMap);

        dudvTexture = (Texture2D) manager.loadTexture("Common/MatDefs/Water/Textures/dudv_map.jpg");
        dudvTexture.setMagFilter(MagFilter.Bilinear);
        dudvTexture.setMinFilter(MinFilter.BilinearNearestMipMap);
        dudvTexture.setWrap(WrapMode.Repeat);
        
        
        material2.setFloat("waterTransparency", 0.5f / 10);
//        material.setColor("waterColor", ColorRGBA.White);
//        material.setVector3("lightPos", new Vector3f(1, -1, 1));

        material2.setColor("distortionScale", new ColorRGBA(0.2f, 0.2f, 0.2f, 0.2f));
        material2.setColor("distortionMix", new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f));
        material2.setColor("texScale", new ColorRGBA(1.0f, 1.0f, 1.0f, 1.0f));
        material2.setVector2("FrustumNearFar", new Vector2f(vp.getCamera().getFrustumNear(), vp.getCamera().getFrustumFar()));        
 
//        mat.setTexture("water_reflection", reflectionTexture);
        material2.setTexture("Texture", defaultPass.getRenderedTexture());
        //   mat.setTexture("water_depthmap", depthTexture);
        material2.setTexture("water_normalmap", normalTexture);
        material2.setTexture("water_dudvmap", dudvTexture);
        material2.setFloat("time", 1f);
        
    }



//    protected void createTextures() {
//        refractionTexture = new Texture2D(screenWidth, screenHeight, Format.RGBA8);
//        depthTexture = new Texture2D(screenWidth, screenHeight, Format.Depth);
//    }


    @Override
    protected Material getMaterial() {

        return material2;
    }

    @Override
    protected void postQueue(RenderManager renderManager, ViewPort viewPort) {
//        if (glowMode != GlowMode.Scene) {           
//            renderManager.getRenderer().setBackgroundColor(ColorRGBA.BlackNoAlpha);            
//            renderManager.getRenderer().setFrameBuffer(preGlowPass.getRenderFrameBuffer());
//            renderManager.getRenderer().clearBuffers(true, true, true);
//            renderManager.setForcedTechnique("Glow");
//            renderManager.renderViewPortQueues(viewPort, false);         
//            renderManager.setForcedTechnique(null);
//            renderManager.getRenderer().setFrameBuffer(viewPort.getOutputFrameBuffer());
//        }
    }

//    @Override
//    public void preFrame(float tpf) {
//        time = time + (tpf * speed);
//        if (time > 1f) {
//            time = 0;
//        }
//        material.setFloat("time", time);
//        savedTpf = tpf;
//    }    
    
    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(blurScale, "blurScale", 1.5f);
        oc.write(exposurePower, "exposurePower", 5.0f);
        oc.write(exposureCutOff, "exposureCutOff", 0.0f);
        oc.write(bloomIntensity, "bloomIntensity", 2.0f);
        oc.write(downSamplingFactor, "downSamplingFactor", 1);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        blurScale = ic.readFloat("blurScale", 1.5f);
        exposurePower = ic.readFloat("exposurePower", 5.0f);
        exposureCutOff = ic.readFloat("exposureCutOff", 0.0f);
        bloomIntensity = ic.readFloat("bloomIntensity", 2.0f);
        downSamplingFactor = ic.readFloat("downSamplingFactor", 1);
    }
}
