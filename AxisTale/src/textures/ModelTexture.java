package textures;

public class ModelTexture {
	
	private int textureID;
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private boolean hasTransperancy = false;
	private boolean fakeLighting = false;
	
	public ModelTexture(int textureID){
		this.textureID = textureID;
	}
	
	
	
	public boolean isFakeLighting() {
		return fakeLighting;
	}



	public void setFakeLighting(boolean fakeLighting) {
		this.fakeLighting = fakeLighting;
	}



	public boolean isHasTransperancy() {
		return hasTransperancy;
	}

	public void setHasTransperancy(boolean hasTransperancy) {
		this.hasTransperancy = hasTransperancy;
	}

	public int getID(){
		return this.textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}
	
	
	
}
