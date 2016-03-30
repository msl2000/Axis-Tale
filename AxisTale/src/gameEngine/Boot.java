package gameEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Character;
import entities.Light;
import gameEngine.DisplayManager;
import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import terrains.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class Boot {

	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		Loader loader = new Loader();
		
		//***************TERRAIN TEXTURE STUFF*************//

		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("path"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("sand"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture,rTexture, gTexture,bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendmap"));
		
		//***************************************************
		
		RawModel stallModel = ObjLoader.loadObjModel("tree", loader);
		TexturedModel tStallModel = new TexturedModel(stallModel,new ModelTexture(loader.loadTexture("tree")));
		ModelTexture stallTexture = tStallModel.getTexture();
		stallTexture.setShineDamper(10);
		stallTexture.setReflectivity(1);
		
		RawModel goblinModel = ObjLoader.loadObjModel("chicken", loader);
		TexturedModel tGoblinModel = new TexturedModel(goblinModel,new ModelTexture(loader.loadTexture("chicken")));
		ModelTexture goblinTexture = tGoblinModel.getTexture();
		goblinTexture.setShineDamper(15);
		goblinTexture.setReflectivity(1);
		
		RawModel gary = ObjLoader.loadObjModel("gary", loader);
		TexturedModel tGary = new TexturedModel(gary,new ModelTexture(loader.loadTexture("gary")));
		ModelTexture garyTexture = tGary.getTexture();
		garyTexture.setShineDamper(15);
		garyTexture.setReflectivity(1);
		
		RawModel lamp = ObjLoader.loadObjModel("lamp1", loader);
		TexturedModel tLamp = new TexturedModel(lamp,new ModelTexture(loader.loadTexture("lamp1")));
		ModelTexture lampTexture = tGary.getTexture();
		lampTexture.setShineDamper(10);
		lampTexture.setReflectivity(1);
		
		RawModel fernModel = ObjLoader.loadObjModel("fern", loader);
		TexturedModel tFernModel = new TexturedModel(fernModel,new ModelTexture(loader.loadTexture("fern")));
		ModelTexture fernTexture = tFernModel.getTexture();
		fernTexture.setShineDamper(10);
		fernTexture.setReflectivity(1);
		//fernTexture.isHasTransperancy();
		
		Light light = new Light(new Vector3f(0,10000,-600), new Vector3f(0.4f,0.4f,0.4f)); // The sun
		List<Light> lights = new ArrayList<Light>();
		lights.add(light);
		lights.add(new Light(new Vector3f(100,0,-200), new Vector3f(10,0,0),new Vector3f(1,0.01f,0.002f)));
		lights.add(new Light(new Vector3f(200,0,-100), new Vector3f(0,0,10), new Vector3f(1,0.01f,0.002f)));
		
		Terrain terrain =  new Terrain(0,-1,loader,texturePack, blendMap, "heightmap");
		
		ArrayList<Entity> bunchOfStalls = new ArrayList<Entity>();
		ArrayList<Entity> bunchOfRocks = new ArrayList<Entity>();
		ArrayList<Entity> ferns = new ArrayList<Entity>();
		ArrayList<Entity> lamps = new ArrayList<Entity>();
		
		Random random = new Random();
		
		for(int i=0; i<100; i++){
			float x = random.nextFloat() *300;
			float y = random.nextFloat();
			float z = random.nextFloat() *-5000;
			bunchOfStalls.add(new Entity(tStallModel, new Vector3f(x,terrain.getHeightOfTerrain(x, z),z),0,y*100,0,8));
		}
		
		for(int i=0; i<200; i++){
			float x = random.nextFloat() *2000-50;
			float y = random.nextFloat();
			float z = random.nextFloat() * -5000;
			ferns.add(new Entity(tFernModel, new Vector3f(x,terrain.getHeightOfTerrain(x, z),z),0,y*100,0,1));
		}
		
		for(int i=0; i<50; i++){
			float x = random.nextFloat() *2000-50;
			float y = random.nextFloat();
			float z = random.nextFloat() * -5000;
			lamps.add(new Entity(tLamp, new Vector3f(x,terrain.getHeightOfTerrain(x, z),z),0,y*100,0,1.3f));
		}
		
		Character player = new Character(tGary, new Vector3f(0,10,-50),0,0,0,0.5f);
		
		Camera camera = new Camera(player);
		
		MasterRenderer renderer = new MasterRenderer();
		
		while(!Display.isCloseRequested()){
			player.move(terrain);
			camera.move();
			renderer.processTerrain(terrain);

			for(Entity entity:bunchOfStalls){
				renderer.processEntity(entity);
			}
			
			for(Entity fern:ferns){
				renderer.processEntity(fern);
			}
			
			for(Entity lamp1:lamps){
				renderer.processEntity(lamp1);
			}
			
			renderer.processEntity(player);
			
			renderer.render(lights, camera);
			DisplayManager.updateDisplay();
		}
		
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.destroyDisplay();
		
		
	

	}

}
