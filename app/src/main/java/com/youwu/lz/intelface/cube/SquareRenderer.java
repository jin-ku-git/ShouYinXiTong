/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.youwu.lz.intelface.cube;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

public class SquareRenderer implements GLSurfaceView.Renderer {
    private Square shape;
    private float mX;
    private float mY;
    private float mZ;
    private boolean mDrawable = true;

    public SquareRenderer() {
        shape = new Square();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        if (mDrawable) {
            gl.glMatrixMode(GL11.GL_MODELVIEW);
            gl.glLoadIdentity();
            gl.glTranslatef(0f, 0f, -8.0f);
            // gl.glScalef(1.5f, 1.5f, 1.5f);
            gl.glRotatef(mX, 1.0f, 0, 0); // rotate about the axis (1,1,1) (NEW)
            gl.glRotatef(mY, 0, 1.0f, 0);
            gl.glRotatef(mZ, 0, 0, 1.0f);
            shape.draw(gl);                      // Draw the cube (NEW)
        }
    }

    public void setDrawable(boolean draw) {
        this.mDrawable = draw;
    }

    public void setAngle(float x, float y, float z) {
        mX = x;
        mY = y;
        mZ = z;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glEnable(GL11.GL_NORMALIZE);
        gl.glMatrixMode(GL11.GL_PROJECTION);
        GLU.gluPerspective(gl, 40.0f, (float) width / (float) height, 0.1f, 8.0f);
        gl.glMatrixMode(GL11.GL_MODELVIEW);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glDisable(GL11.GL_DITHER);
        gl.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_FASTEST);
        gl.glClearColor(0, 0, 0, 0);
        gl.glEnable(GL11.GL_CULL_FACE);
        gl.glFrontFace(GL11.GL_CCW);
        gl.glShadeModel(GL11.GL_SMOOTH);
        gl.glEnable(GL11.GL_DEPTH_TEST);
    }

}
