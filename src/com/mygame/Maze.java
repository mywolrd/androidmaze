package com.mygame;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Maze {
	private FloatBuffer vertexBuffer;   // buffer holding the vertices
	private FloatBuffer textureBuffer;
	private FloatBuffer squareBuffer;
	
	private RecursiveBT _maze;
	private float x_range = 2.0f;
	private float y_range = 3.0f;
	
	private float x_start = -1.0f;
	private float y_start = 1.5f;
	
	private float x_inc;
	private float y_inc;
	private byte num = 15;
	
	private int[] images = new int[1];

	
	private float texture[] = {
			// Mapping coordinates for the vertices
			0.0f, 1.0f,     // top left     (V2)
			0.0f, 0.0f,     // bottom left  (V1)
			1.0f, 1.0f,     // top right    (V4)
			1.0f, 0.0f      // bottom right (V3)
		};
	
	public Maze() {

		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(6 * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        // allocates the memory from the byte buffer
        vertexBuffer = byteBuffer.asFloatBuffer();
 		
        byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuffer.asFloatBuffer();
        textureBuffer.put(texture);
        textureBuffer.position(0);
        
        byteBuffer = ByteBuffer.allocateDirect(12 * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        squareBuffer = byteBuffer.asFloatBuffer();
 		        
		_maze = new RecursiveBT(num);
	
        x_inc = x_range / (float) num;
        y_inc = y_range / (float) num;
	}
	
	private void drawLine(GL10 gl, float x1, float y1, float x2, float y2){
		
		float[] vertices = {x1, y1, 0.0f,
							x2, y2, 0.0f};
		
		// put vertices into vertexBuffer
		vertexBuffer.put(vertices);
		 
        // set the cursor position to the beginning of the buffer
        vertexBuffer.position(0);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		// set the color for the line
		gl.glColor4f(0.0f, 1.0f, 0.0f, 0.5f);
		
		// Point to our vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glDrawArrays(GL10.GL_LINES, 0, vertices.length / 3);

		//Disable the client state before leaving	
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
	
	public void drawPlayer(GL10 gl, float x1, float y1){
		float[] _vertices = 
			{(float) (-1.0+(float)(x_inc/5.0)), (float)1.5-y_inc+(float)(y_inc/5.0), 0.0f,  // V1 - bottom left
			(float)-1.0+(float)(x_inc/5.0), (float)1.5-(float)(y_inc/5.0), 0.0f,				// v2 - top left
			(float)-1.0+x_inc-(float)(x_inc/5.0), y1-y_inc+(float)(y_inc/5.0), 0.0f,				// v3 - bottom right
			(float)-1.0+x_inc-(float)(x_inc/5.0), y1-(float)(y_inc/5.0), 0.0f};				// v4 - top right

		// put vertices into vertexBuffer
		squareBuffer.put(_vertices);

		// set the cursor position to the beginning of the buffer
		squareBuffer.position(0);

		 gl.glBindTexture(GL10.GL_TEXTURE_2D, images[0]);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);	
		
		gl.glFrontFace(GL10.GL_CW);
		
		// Point to our vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, squareBuffer);		
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, _vertices.length / 3);

		//Disable the client state before leaving	
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}
	
	/** The draw method for the square with the GL context */
	public void draw(GL10 gl) {
		
		gl.glEnable(GL10.GL_TEXTURE_2D);
		drawPlayer(gl,x_start, y_start);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		
		gl.glPushMatrix();
		gl.glLineWidth(4.0f);
		drawLine(gl, -1.0f, -1.5f,-1.0f,  1.5f);
		drawLine(gl, -1.0f+x_inc, 1.5f,1.0f,  1.5f);
		drawLine(gl, 1.0f, -1.5f,1.0f,  1.5f);
		drawLine(gl, 1.0f, -1.5f,-1.0f,  -1.5f);
		gl.glPopMatrix();		
		
		int walls = 0;
		
		for(int i = 0; i < num-1; i++){
			for(int j=0; j < num-1; j++){

				walls = _maze.getWalls(j, i);
				//walls = 1 if the current cell has right wall
				//      = 2 if it has bottom wall
				//      = 3 if it has both
				//      = 0 if none
				
				if(walls != 0){
					if((walls & 1) != 0){
						drawLine(gl, x_start+(x_inc * (float)(j+1)),
								y_start-(y_inc * (float)i),
								x_start+(x_inc * (float)(j+1)),
								y_start-(y_inc * (float)(i+1)));
					}
					if((walls & 2) != 0){
						drawLine(gl, x_start+(x_inc * (float)j),
								y_start-(y_inc * (float)(i+1)),
								x_start+(x_inc * (float)(j+1)),
								y_start-(y_inc * (float)(i+1)));
					}
				}
			}
		}
	}
	
	public void loadGLTexture(GL10 gl, Context context) {
		// loading texture
		
		//BitmapFactory.Options opts = new BitmapFactory.Options();
		//opts.inScaled = false;
		
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ram);

		// generate one texture pointer
		gl.glGenTextures(1, images, 0);
		// ...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, images[0]);

		// create nearest filtered texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);

		// Use Android GLUtils to specify a two-dimensional texture image from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		// Clean up
		bitmap.recycle();
	}
	

}
