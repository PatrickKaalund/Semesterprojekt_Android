package com.graphics;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by thor on 10/16/16.
 */

public class ShaderHandler {

    //---------------Shader files available------------
    public final static String VERTEX_SHADER_SOLIDCOLOR = "VertexShader_solidColor.glsl";
    public final static String FRAGMENT_SHADER_SOLID_COLOR = "FragmentShader_solidColor.glsl";
    public final static String VERTEX_SHADER_TEXTURE = "VertShader_texture.glsl";
    public final static String FRAGMENT_SHADER_TEXTURE = "FragShader_texture.glsl";
    //-------------------------------------------------


    private int vertexShaderID;
    private int fragmentShaderID;
    private int shaderProgramID;


    private Context context;


    public ShaderHandler(Context context) {
        this.context = context;
    }

    /**
     * Load shader program from files
     * @param type       shader type (GLES20.GL_VERTEX_SHADER)(GLES20.GL_FRAGMENT_SHADER)
     * @param shaderFile string to shader file
     * @return
     */
    private int loadShader(String shaderFile, int type) {

        InputStream is = null;
        String shaderCode = null;
        try {

            is = getContext().getAssets().open("shaders/" + shaderFile);
            shaderCode = IOUtils.toString(is);
        } catch (IOException e) {
            Log.e("Shader",
                    "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n " +
                            "No shader loaded.. check your filename and make sure the shader file is in assets/shaders!!\n" +
                            "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n"
            );
            e.printStackTrace();
        }
        return loadShaderSource(shaderCode,type);
    }

    /**
     * Load shader program from source
     * @param type       shader type (GLES20.GL_VERTEX_SHADER)(GLES20.GL_FRAGMENT_SHADER)
     * @param shaderCode string with shader source code
     * @return
     */
    private int loadShaderSource(String shaderCode, int type) {
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);
        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        // return the shader
        return shader;
    }


    /**
     * Install shaders from file
     * @param vertexShaderFile
     * @param fragmentShaderFile
     * @return
     */
    public int installShaderFiles(String vertexShaderFile,String fragmentShaderFile) {
        vertexShaderID = loadShader(vertexShaderFile,GLES20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentShaderFile,GLES20.GL_FRAGMENT_SHADER);

//        vertexShaderID = loadShaderSource(riGraphicTools.vs_Image,GLES20.GL_VERTEX_SHADER);
//        fragmentShaderID = loadShaderSource(riGraphicTools.fs_Image,GLES20.GL_FRAGMENT_SHADER);


        shaderProgramID = GLES20.glCreateProgram(); // create empty OpenGL ES Program
        GLES20.glAttachShader(getShaderProgramID(), getVertexShaderID());   // add the vertex shader to program
        GLES20.glAttachShader(getShaderProgramID(), getFragmentShaderID()); // add the fragment shader to program
        GLES20.glLinkProgram(getShaderProgramID());                  // creates OpenGL ES program executables
        GLES20.glValidateProgram(getShaderProgramID());
//        int[] compiled = new int[1];
//        GLES20.glGetShaderiv(getShaderProgramID(), GLES20.GL_COMPILE_STATUS, compiled, 0);
//        if (compiled[0] == 0) {
//            Log.e("VOC", "Could not compile shader");
//            Log.e("VOC", GLES20.glGetShaderInfoLog(getShaderProgramID()));
//            GLES20.glDeleteShader(getShaderProgramID());
//            shaderProgramID = 0;
//        }
        startProgram();
        return getShaderProgramID();
    }

    public void startProgram(){
        GLES20.glUseProgram(getShaderProgramID());

    }

    public void stopProgram(){
        GLES20.glUseProgram(0);
    }

    public void free(){
        stopProgram();
        GLES20.glDetachShader(getShaderProgramID(), getVertexShaderID());
        GLES20.glDetachShader(getShaderProgramID(), getFragmentShaderID());
        GLES20.glDeleteShader(getVertexShaderID());
        GLES20.glDeleteShader(getFragmentShaderID());
        GLES20.glDeleteProgram(getShaderProgramID());
    }


    public int installShaderFiles(){
        return installShaderFiles(VERTEX_SHADER_TEXTURE, FRAGMENT_SHADER_TEXTURE);
    }


    public int getVertexShaderID() {
        return vertexShaderID;
    }

    public int getFragmentShaderID() {
        return fragmentShaderID;
    }

    public int getShaderProgramID() {
        return shaderProgramID;
    }

    public Context getContext() {
        return context;
    }
}