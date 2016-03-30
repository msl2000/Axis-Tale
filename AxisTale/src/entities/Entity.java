package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;
import terrains.Terrain;

public class Entity {

	private Vector3f position;
	private float rotX, rotY, rotZ;
	private float scale;

	private TexturedModel model;
	
	public Entity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	public void increasePosition(float dx, float dy, float dz){
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	
	public void increaseRotation(float dx, float dy, float dz){
		this.rotX += dx;
		this.rotY+= dy;
		this.rotZ += dz;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	public float getRotX() {
		return rotX;
	}
	public float getRotY() {
		return rotY;
	}
	public float getRotZ() {
		return rotZ;
	}
	public float getScale() {
		return scale;
	}
	public TexturedModel getModel() {
		return model;
	}
	
}
