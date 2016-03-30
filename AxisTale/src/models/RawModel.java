package models;

public class RawModel {

	private int vaoID;
	private int vertexCount;
	
	//the vao will hold the details of the model, such as vertices positions
	public RawModel(int vaoID, int vertexCount){
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
}
