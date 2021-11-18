/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package com.youwu.lz.intelface.cube;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Square {
    // Our vertices.
    private float[] vertices = {
            // Vertices of the 6 faces
            // FRONT
            -1.0f, -1.0f, 1.0f,  // 0. left-bottom-front
            1.0f, -1.0f, 1.0f,  // 1. right-bottom-front
            -1.0f, 1.0f, 1.0f,  // 2. left-top-front
            1.0f, 1.0f, 1.0f,  // 3. right-top-front
            // BACK
            1.0f, -1.0f, -1.0f,  // 6. right-bottom-back
            -1.0f, -1.0f, -1.0f,  // 4. left-bottom-back
            1.0f, 1.0f, -1.0f,  // 7. right-top-back
            -1.0f, 1.0f, -1.0f,  // 5. left-top-back
            // LEFT
            -1.0f, -1.0f, -1.0f,  // 4. left-bottom-back
            -1.0f, -1.0f, 1.0f,  // 0. left-bottom-front
            -1.0f, 1.0f, -1.0f,  // 5. left-top-back
            -1.0f, 1.0f, 1.0f,  // 2. left-top-front
            // RIGHT
            1.0f, -1.0f, 1.0f,  // 1. right-bottom-front
            1.0f, -1.0f, -1.0f,  // 6. right-bottom-back
            1.0f, 1.0f, 1.0f,  // 3. right-top-front
            1.0f, 1.0f, -1.0f,  // 7. right-top-back
            // TOP
            -1.0f, 1.0f, 1.0f,  // 2. left-top-front
            1.0f, 1.0f, 1.0f,  // 3. right-top-front
            -1.0f, 1.0f, -1.0f,  // 5. left-top-back
            1.0f, 1.0f, -1.0f,  // 7. right-top-back
            // BOTTOM
            -1.0f, -1.0f, -1.0f,  // 4. left-bottom-back
            1.0f, -1.0f, -1.0f,  // 6. right-bottom-back
            -1.0f, -1.0f, 1.0f,  // 0. left-bottom-front
            1.0f, -1.0f, 1.0f   // 1. right-bottom-front
    };

    private float[][] colors = {  // Colors of the 6 faces
            {0.0f, 1.0f, 0.0f, 0.5f},  // 0. orange
            {0.0f, 0.0f, 1.0f, 1f},  // 1. violet
            {0.0f, 0.0f, 1.0f, 1f},  // 2. green
            {0.0f, 0.0f, 1.0f, 1f},  // 3. blue
            {0.0f, 0.0f, 1.0f, 1f},  // 4. red
            {0.0f, 0.0f, 1.0f, 1f}   // 5. yellow
    };

    private byte[] indices = {
            0, 4, 5, 0, 5, 1,
            1, 5, 6, 1, 6, 2,
            2, 6, 7, 2, 7, 3,
            3, 7, 4, 3, 4, 0,
            4, 7, 6, 4, 6, 5,
            3, 0, 1, 3, 1, 2
    };

    // Our vertex buffer.
    private FloatBuffer vertexBuffer;

    // Our index buffer.
    private ByteBuffer indexBuffer;

    public Square() {
        // a float is 4 bytes, therefore we multiply the number if
        // vertices with 4.
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vertexBuffer = vbb.asFloatBuffer();
        vertexBuffer.put(vertices);
        vertexBuffer.position(0);
        // short is 2 bytes, therefore we multiply the number if
        // vertices with 2.
        indexBuffer = ByteBuffer.allocateDirect(indices.length);
        indexBuffer.put(indices);
        indexBuffer.position(0);

    }

    /**
     * This function draws our square on screen.
     *
     * @param gl
     */
    int numFaces = 6;

    public void draw(GL10 gl) {
        gl.glFrontFace(GL10.GL_CCW);    // Front face in counter-clockwise orientation
        gl.glEnable(GL10.GL_CULL_FACE); // Enable cull face
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

        // Render all the faces
        for (int face = 0; face < numFaces; face++) {
            // Set the color for each of the faces
            gl.glColor4f(colors[face][0], colors[face][1], colors[face][2], colors[face][3]);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, face * 4, 4);
        }
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisable(GL10.GL_CULL_FACE);
    }
}