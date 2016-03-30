package terrains;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import gameEngine.Loader;
import textures.*;
import toolbox.Maths;
import models.RawModel;

public class terrainspare {
	
	private static final float SIZE = 800; // size of terrain
	private static final float MAX_HEIGHT = 40; // MAX HEIGHT 
	private static final float MAX_PIXEL_COLOR = 256 * 256 * 256; // max color
	
	private float x; // x position of terrain in the world
	private float z; // z postion of terrain in the world
	private RawModel model; // raw model ie the terrain mesh
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	
	private float[] [] heights; // heights of the terrain
	
	
	public terrainspare(int gridX, int gridZ, 
			Loader loader,TerrainTexturePack texturePack, TerrainTexture blendMap,String heightMap){ // every terrain must have a grid x and z position in the world, loader, and a texture
		this.x = gridX * SIZE; // this way postition is proportional to the size of our world 
		this.z = gridZ * SIZE;
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.model = generateTerrain(loader,heightMap);
	}
	
	// generate flat terrain
	private RawModel generateTerrain(Loader loader, String heightMap){
		BufferedImage image = null;
		try{
		image = ImageIO.read(new File("res/"+ heightMap + ".png")); 
		}
		catch(IOException e){
			e.printStackTrace();
		}
		int VERTEX_COUNT = image.getHeight(); 
		heights = new float[VERTEX_COUNT][VERTEX_COUNT]; /// the height is the vertex count
		
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){

				float height = getHeight(j,i,image);
				heights[j][i] = height; // store all heights in the array
				
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer*3+1] = getHeight(j,i,image);
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				//Vector3f normal = calculateNormal(j,i,image);
				normals[vertexPointer*3] = 0; // normal.x
				normals[vertexPointer*3+1] = 1; // normal .y
				normals[vertexPointer*3+2] = 0; // normal.z
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, indices, normals, textureCoords);
	}
	
	public float getHeightOfTerrain(float worldX, float worldZ){
		float terrainX  = worldX -this.x; 
		float terrainZ = worldZ -this.z; 
		float gridSquareSize = SIZE / ((float) heights.length -1); 
		int gridX = (int) Math.floor(terrainX/ gridSquareSize); 
		int gridZ = (int) Math.floor(terrainZ/ gridSquareSize); 
		if(gridX >= heights.length -1 || gridZ >= heights.length -1 || gridX < 0 || gridZ < 0){
			return 0; 
		}
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize; 
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float answer;
		if (xCoord <= (1-zCoord)) {
			answer = Maths
					.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			answer = Maths
					.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		return answer;
		
	}
	
	private float getHeight(int x, int z, BufferedImage image){
		if(x<0 || x>image.getHeight() || z<0 || z>=image.getHeight()){ // if  it is less then 0 or the value is greater then the max height 
			return 0; 
		}
		float height = image.getRGB(x,z); // returns a value from the colors like the darkness and stuff
		height += MAX_PIXEL_COLOR/2f; // adds more to range
		height /= MAX_PIXEL_COLOR/2f;
		height *= MAX_HEIGHT; 
		return height; 
		
	}
	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getModel() {
		return model;
	}

	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}

	public TerrainTexture getBlendMap() {
		return blendMap;
	}
	private Vector3f calculateNormal(int x, int z, BufferedImage image){
		float heightL = getHeight(x-1,z,image);
		float heightR = getHeight(x+1,z,image);
		float heightD = getHeight(x,z-1,image);
		float heightU = getHeight(x, z+1, image); 
		Vector3f normal = new Vector3f(heightL-heightR,2f,heightD - heightU);
		normal.normalise();
		return normal; 
		
				
		 
	}
	
}







/////////////////////////

/*
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import gameEngine.Loader;
import models.RawModel;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.Maths;

public class Terrain {
	private static final float SIZE = 5000;
	private static final int MAX_HEIGHT = 40;
	private static final int MAX_PIXEL_COLOUR = 256*256*256;
	
	private float x;
	private float z;
	private RawModel model;
	private TerrainTexturePack texturePack;
	private TerrainTexture blendMap;
	
	private float[][] heights;
	
	public Terrain(int gridX, int gridZ, Loader loader, TerrainTexturePack texturePack
			, TerrainTexture blendMap, String heightMap){
		this.texturePack = texturePack;
		this.blendMap = blendMap;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.model = generateTerrain(loader, heightMap);
	}
	
	
	
	public float getX() {
		return x;
	}



	public float getZ() {
		return z;
	}


	public RawModel getModel() {
		return model;
	}


	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}



	public TerrainTexture getBlendMap() {
		return blendMap;
	}
	
	public float getHeightOfTerrain(float worldX, float worldZ){
		float terrainX  = worldX -this.x; 
		float terrainZ = worldZ -this.z; 
		float gridSquareSize = SIZE / ((float) heights.length -1); 
		int gridX = (int) Math.floor(terrainX/ gridSquareSize); 
		int gridZ = (int) Math.floor(terrainZ/ gridSquareSize); 
		if(gridX >= heights.length -1 || gridZ >= heights.length -1 || gridX < 0 || gridZ < 0){
			return 0; 
		}
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize; 
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float answer;
		if (xCoord <= (1-zCoord)) {
			answer = Maths
					.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			answer = Maths
					.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		return answer;
	}

	private RawModel generateTerrain(Loader loader, String heightMap){
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("res/"+heightMap+".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int VERTEX_COUNT = image.getHeight();
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				float height = getHeight(j,i,image);
				heights[j][i] = height;
				vertices[vertexPointer*3+1] = height;
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calculateNormal(j, i, image);
				normals[vertexPointer*3] = normal.x;
				normals[vertexPointer*3+1] = normal.y;
				normals[vertexPointer*3+2] = normal.x;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		
		return loader.loadToVAO(vertices, indices, textureCoords, normals);
	}
	
	private Vector3f calculateNormal(int x, int z, BufferedImage image){
		float heightL = getHeight(x-1,z,image);
		float heightR = getHeight(x+1,z,image);
		float heightD = getHeight(x,z-1,image);
		float heightU = getHeight(x, z+1, image); 
		Vector3f normal = new Vector3f(heightL-heightR,2f,heightD - heightU);
		normal.normalise();
		return normal; 

	}
	
	private float getHeight(int x, int z, BufferedImage image){
		if(x<0 || x>=image.getHeight() || z<0 || z>=image.getHeight()){
			return 0;
		}
		float height = image.getRGB(x, z);
		height += MAX_PIXEL_COLOUR/2f;
		height /= MAX_PIXEL_COLOUR/2f;
		height *= MAX_HEIGHT;
		return height;
	}
}
/*
