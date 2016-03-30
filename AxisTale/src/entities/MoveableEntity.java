package entities;

import org.lwjgl.util.vector.Vector3f;

import models.TexturedModel;

public class MoveableEntity extends Entity{
	
	private static final int mvSpeed = 10;

	public MoveableEntity(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale);
		// TODO Auto-generated constructor stub
	}
	
	public void moveLeft(){
		increasePosition(mvSpeed, 0, 0);
	}
	
	public void moveRight(){
		increasePosition(-mvSpeed, 0, 0);
	}
	
	public void moveFront(){
		increasePosition(0, 0, mvSpeed);
	}
	
	public void moveBack(){
		this.increasePosition(0, 0, -mvSpeed);
	}
	

}
