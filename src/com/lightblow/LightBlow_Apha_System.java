
package com.lightblow;


import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.util.TangentBinormalGenerator;


public class LightBlow_Apha_System extends SimpleApplication {


    
    
  public static void main(String[] args) {
    LightBlow_Apha_System app = new LightBlow_Apha_System();
    app.start();
  }
    

    
    
  @Override
  public void simpleInitApp() {

      TextureKey skyhi = new TextureKey("ShaderBlow/Textures/Water256.dds", true);
        skyhi.setGenerateMips(true);
        skyhi.setAsCube(true);
      final  Texture texhi = assetManager.loadTexture(skyhi);

      
//      TextureKey skylow = new TextureKey("Textures/Water32.dds", true);
//        skylow.setGenerateMips(true);
//        skylow.setAsCube(true);
      final  Texture texlow = assetManager.loadTexture(skyhi);
         rootNode.attachChild(SkyFactory.createSky(assetManager, texlow, false));

         
         
    Spatial char_boy = assetManager.loadModel("ShaderBlow/Models/LightBlow/jme_lightblow.mesh.xml");
    Material mat = assetManager.loadMaterial("ShaderBlow/Materials/LightBlow/Alpha_System/LightBlow_AlphaDiffuseMap.j3m");
    char_boy.setMaterial(mat);
    TangentBinormalGenerator.generate(char_boy);
    char_boy.setQueueBucket(Bucket.Transparent); 
    rootNode.attachChild(char_boy);

    Spatial char_boy2 = assetManager.loadModel("ShaderBlow/Models/LightBlow/jme_lightblow.mesh.xml");
    Material mat2 = assetManager.loadMaterial("ShaderBlow/Materials/LightBlow/Alpha_System/LightBlow_AlphaDiffuseMap_Threshould.j3m");
    char_boy2.setMaterial(mat2);
    char_boy2.setLocalTranslation(-2f, 0, 0);
    TangentBinormalGenerator.generate(char_boy2);
    char_boy2.setQueueBucket(Bucket.Transparent);
    rootNode.attachChild(char_boy2);
    
    
    Spatial char_boy3 = assetManager.loadModel("ShaderBlow/Models/LightBlow/jme_lightblow.mesh.xml");
    Material mat3 = assetManager.loadMaterial("ShaderBlow/Materials/LightBlow/Alpha_System/LightBlow_AlphaNormalMap.j3m");
    char_boy3.setMaterial(mat3);
    char_boy3.setLocalTranslation(-4f, 0, 0);
    TangentBinormalGenerator.generate(char_boy3);
    char_boy3.setQueueBucket(Bucket.Transparent);
    rootNode.attachChild(char_boy3);
    
    Spatial char_boy4 = assetManager.loadModel("ShaderBlow/Models/LightBlow/jme_lightblow.mesh.xml");
    Material mat4 = assetManager.loadMaterial("ShaderBlow/Materials/LightBlow/Alpha_System/LightBlow_AlphaNormalMap_Threshould.j3m");
    char_boy4.setMaterial(mat4);
    char_boy4.setLocalTranslation(-6f, 0, 0);
    TangentBinormalGenerator.generate(char_boy4);
    char_boy4.setQueueBucket(Bucket.Transparent);
    rootNode.attachChild(char_boy4);
    


        flyCam.setMoveSpeed(5);   
     

        DirectionalLight dl = new DirectionalLight();
        dl.setDirection(new Vector3f(-0.8f, -0.6f, -0.08f).normalizeLocal());
        dl.setColor(new ColorRGBA(1,1,1,1));
        rootNode.addLight(dl);
        
    
        
        
  }
  

@Override
    public void simpleUpdate(float tpf){
 

    
    
  
    }
    
    
    }
  

