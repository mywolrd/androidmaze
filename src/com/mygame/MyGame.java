package com.mygame;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

public class MyGame implements Renderer {

	private final String _texturevertexShader =
			"uniform mat4 uMVPMatrix;" +
			"attribute vec4 aPosition;" +
			"attribute vec2 aTexture;" +
			
			"varying vec2 vTexture;" +
					
			"void main() {" +
			"	vTexture = aTexture;" +
			"	gl_Position = aPosition * uMVPMatrix;" +
			"}";
	
	private final String _texturefragmentShader =
			"precision mediump float;" +
			"varying vec2 vTexture;" +
			"uniform sampler2D uSampler;" +
			"void main() {" +
			"  gl_FragColor = texture2D(uSampler, vTexture);" +
		    //"  gl_FragColor = vec4(vTexture.x, vTexture.y, 0.0, 1.0);" +
		    "}";
	
	private final String _vertexShader =
			"uniform mat4 uMVPMatrix;" +
			"attribute vec4 aPosition;" +
			"void main() {" +
			"	gl_Position = aPosition * uMVPMatrix;" +
			"}";
	
	private final String _fragmentShader =
		    "void main() {" +
		    "  gl_FragColor = vec4 (0.5443, 0.2345, 0.8664, 1.0);" +
		    "}";

	private int[] viewport = new int[16];
	
	private float[] MVMatrix = new float[16];	//Model view
	private float[] PMatrix = new float[16];	//Projection
	private float[] MVPMatrix = new float[16];	//Model view * Projection
	
	private float[] points = new float[4];
	
	private int matrixHandle;
	private int lineprogramHandle;
	private int textureprogramHandle;
	private int positionHandle;
	private int textureuniformHandle;
	private int texturePositionHandle;
	
	private FloatBuffer vertexBuffer;   // buffer holding the vertices
	
	private FloatBuffer textureBuffer;  // for texture 
	private FloatBuffer squareBuffer;   // place holder for texture
	
	private RecursiveBT _maze;
	
	private float x_right;
	private float y_bottom;
	
	private float x_left;
	private float y_top;
	
	private float x_inc;
	private float y_inc;

	private byte num = 15;
	
	private int[] images = new int[1];
	
	private Context _context;

	private float texture[] = {
			// Mapping coordinates for the vertices			
			0.0f, 0.0f,     // bottom left  (V1)
			0.0f, 1.0f,     // top left     (V2)
			1.0f, 0.0f,      // bottom right (V3)
			1.0f, 1.0f     // top right    (V4)
		};
	
	public MyGame(Context context){
		_context = context;
	}
	
	private void setUpBuffers(){	
						
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        // allocates the memory from the byte buffer
        vertexBuffer = byteBuffer.asFloatBuffer();
 		
        byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        textureBuffer = byteBuffer.asFloatBuffer();
        textureBuffer.put(texture);
        
        byteBuffer = ByteBuffer.allocateDirect(8 * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        squareBuffer = byteBuffer.asFloatBuffer();
	}
	
	private void setUpOpenGLProgramHandle(){
		
		lineprogramHandle = GLES20.glCreateProgram();
		
		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, _vertexShader);
		int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, _fragmentShader);
		
		GLES20.glAttachShader(lineprogramHandle, vertexShader);
		GLES20.glAttachShader(lineprogramHandle, fragmentShader);
		GLES20.glLinkProgram(lineprogramHandle);
		
		textureprogramHandle = GLES20.glCreateProgram();
		
		vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, _texturevertexShader);
		fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, _texturefragmentShader);
		
		GLES20.glAttachShader(textureprogramHandle, vertexShader);
		GLES20.glAttachShader(textureprogramHandle, fragmentShader);
		GLES20.glLinkProgram(textureprogramHandle);		
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {

		_maze = new RecursiveBT(num);	

		setUpBuffers();
		setUpOpenGLProgramHandle();
		loadGLTexture();
				
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	}
	
	@Override	
	public void onDrawFrame(GL10 gl) {
	
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
		
		//Draw
		draw();
	}
	
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {

		GLES20.glViewport(0, 0, width, height);
		
		float ratio = (float) width / height;
		
		viewport[0] = 0;
		viewport[1] = 0;
		viewport[2] = width;
		viewport[3] = height;
		
		//set up the projection matrix 
		Matrix.frustumM(PMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
		
		//set up the model view matrix
		Matrix.setLookAtM(MVMatrix, 0, 0, 0, 3, 0f, 0f, 0f, 
				0f, 1.0f, 0f);
		
		//find the max x, y coordinate in the object space
		GLU.gluUnProject(0f, 0f, 0f, MVMatrix, 0, PMatrix, 0, viewport, 0, points, 0);
		x_left = points[0] * 3f;
		y_bottom = points[1] * 3f;
		
		GLU.gluUnProject((float)width, height, 0f, MVMatrix, 0, PMatrix, 0, viewport, 0, points, 0);
		x_right = points[0] * 3f;
		y_top = points[1] * 3f;
				
		x_inc = (x_right - x_left) / (float) num;
		y_inc = (y_top - y_bottom) / (float) num;
	}

	public static int loadShader(int type, String shaderCode){

	    // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
	    // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
	    int shader = GLES20.glCreateShader(type);

	    // add the source code to the shader and compile it
	    GLES20.glShaderSource(shader, shaderCode);
	    GLES20.glCompileShader(shader);

	    return shader;
	}
	
	private void drawLine(float x1, float y1, float x2, float y2){
		
		float[] vertices = {x1, y1,
							x2, y2};
		// put vertices into vertexBuffer
		vertexBuffer.put(vertices);
		 
        // set the cursor position to the beginning of the buffer
        vertexBuffer.position(0);
		
        GLES20.glUseProgram(lineprogramHandle);
               
		positionHandle = GLES20.glGetAttribLocation(lineprogramHandle, "aPosition");
		matrixHandle = GLES20.glGetUniformLocation(lineprogramHandle, "uMVPMatrix");
        
        GLES20.glEnableVertexAttribArray(positionHandle);
        
        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT,
        		false, 0, vertexBuffer);

		//calculate MVMatrix * PMatrix
		Matrix.multiplyMM(MVPMatrix, 0, PMatrix, 0, MVMatrix, 0);			

        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, MVPMatrix, 0);

        GLES20.glLineWidth(4f);
        
        GLES20.glDrawArrays(GLES20.GL_LINES, 0, 2);
        
        GLES20.glDisableVertexAttribArray(positionHandle);
	}
	
	private void drawPlayer(float x1, float y1){
		float[] _vertices = 
			{	x1+(x_inc/5f), y1-(y_inc/5f),
				x1+(x_inc/5f), y1-y_inc+(y_inc/5f),
				x1+x_inc-(x_inc/5f), y1-(y_inc/5f),
				x1+x_inc-(x_inc/5f), y1-y_inc+(y_inc/5f)	};
		// put vertices into vertexBuffer
		squareBuffer.put(_vertices);

		// set the cursor position to the beginning of the buffer
		squareBuffer.position(0);
		textureBuffer.position(0);

		GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
		
        GLES20.glUseProgram(textureprogramHandle);

        positionHandle = GLES20.glGetAttribLocation(textureprogramHandle, "aPosition");
		matrixHandle = GLES20.glGetUniformLocation(textureprogramHandle, "uMVPMatrix");        
        texturePositionHandle = GLES20.glGetAttribLocation(textureprogramHandle,
        		"aTexture");
        textureuniformHandle = GLES20.glGetUniformLocation(textureprogramHandle, 
        		"uSampler");
        
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, images[0]);
        
        GLES20.glUniform1i(textureuniformHandle, 0);
		
        //Draw a square that will hold the texture        
        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT,
        		false, 0, squareBuffer);
        GLES20.glEnableVertexAttribArray(positionHandle);
                
        GLES20.glVertexAttribPointer(texturePositionHandle, 2, GLES20.GL_FLOAT, 
        		false, 0, textureBuffer);
        GLES20.glEnableVertexAttribArray(texturePositionHandle);
        
        Matrix.multiplyMM(MVPMatrix, 0, PMatrix, 0, MVMatrix, 0);			

        GLES20.glUniformMatrix4fv(matrixHandle, 1, false, MVPMatrix, 0);
        
      //  GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
      //  GLES20.glEnable(GLES20.GL_BLEND);
        
		GLES20.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
		//GLES20.glDisable(GLES20.GL_BLEND);
		GLES20.glDisableVertexAttribArray(positionHandle);
		GLES20.glDisableVertexAttribArray(texturePositionHandle);
	}
	
	/** The draw method for the square with the GL context */
	private void draw() {
		
		drawPlayer(x_left, y_top);
		
		drawLine(x_left, y_top, x_right, y_top);
		drawLine(x_left, y_top, x_left, y_bottom);
		drawLine(x_left, y_bottom, x_right, y_bottom);
		drawLine(x_right, y_top, x_right, y_bottom);
		
		int walls = 0;
		
		for(int i = 0; i < num; i++){
			for(int j=0; j < num; j++){

				walls = _maze.getWalls(i, j);
				//walls = 1 if the current cell has right wall
				//      = 2 if it has bottom wall
				//      = 3 if it has both
				//      = 0 if none
				
				if(walls != 0){
					if((walls & 1) != 0){
						drawLine(x_left+(x_inc * (float)(i+1)),
								y_top-(y_inc * (float)j),
								x_left+(x_inc * (float)(i+1)),
								y_top-(y_inc * (float)(j+1)));
					}
					if((walls & 2) != 0){
						drawLine(x_left+(x_inc * (float)i),
								y_top-(y_inc * (float)(j+1)),
								x_left+(x_inc * (float)(i+1)),
								y_top-(y_inc * (float)(j+1)));
					}
				}
			}
		}
	}
	
	public void loadGLTexture() {
		// loading texture
		
		//BitmapFactory.Options opts = new BitmapFactory.Options();
		//opts.inScaled = false;
		
		Bitmap bitmap = BitmapFactory.decodeResource(_context.getResources(),
				R.raw.ram);//, opts);

		// generate one texture pointer
		GLES20.glGenTextures(1, images, 0);
		// ...and bind it to our array
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, images[0]);

		// create nearest filtered texture
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, 
				GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, 
				GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
		
		//GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
         //       GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        //GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
          //      GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

		// Use Android GLUtils to specify a two-dimensional texture image from our bitmap
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
		Log.e(""+GLES20.glGetError(), "texImage2D");
		// Clean up
		bitmap.recycle();
	}
}
