import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL33;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Square {

    private float[] vertices;

    private static final int[] indices = {
            0, 1, 2,
            1, 2, 3,
    };



    // 4x4 -> FloatBuffer of size 16




    private int squareVaoId;
    private int squareVboId;
    private int squareEboId;
    private int colorsId;


    public static int uniform;
    public Matrix4f matrix;
    public FloatBuffer matrixFB;
    private final FloatBuffer colorsFB;

    private float x;
    private float y;
    private final float width;

    public float[] red;
    public float[] green;

    public Square(float x, float y, float width) {
        vertices = new float[12];
        squareVaoId = GL33.glGenVertexArrays();
        squareVboId = GL33.glGenBuffers();
        squareEboId = GL33.glGenBuffers();
        colorsId = GL33.glGenBuffers();

        this.x = x;
        this.y = y;
        this.width = width;
        matrix = new Matrix4f().identity();
        matrixFB = BufferUtils.createFloatBuffer(16);


        for (int i = 0; i < 4; i++) {
            vertices[i * 3] = x + width * (i % 2);
            vertices[i * 3 + 1] = y - width * (Math.round(i / 2));
            vertices[i * 3 + 2] = 0.0f;
        }



        float[] colors = {
                1f, 1f, 1f, 1f,
                1f, 1f, 1f, 1f,
                1f, 1f, 1f, 1f,
                1f, 1f, 1f, 1f,
        };

        red = new float[]{
                1f, 0f, 0f, 1f,
                1f, 0f, 0f, 1f,
                1f, 0f, 0f, 1f,
                1f, 0f, 0f, 1f,
        };
        green = new float[]{
                0f, 1f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 1f, 0f, 0f,
        };

        colorsFB = BufferUtils.createFloatBuffer(red.length).put(red).flip();
        uniform = GL33.glGetUniformLocation(Shaders.shaderProgramId, "matrix");

        //VERTICES
        GL33.glBindVertexArray(squareVaoId);
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, squareVboId);
        FloatBuffer fb = BufferUtils.createFloatBuffer(vertices.length)
                .put(vertices)
                .flip();
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, fb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(0, 3, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(0);


        // COLORS
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, colorsId);
        FloatBuffer cfb = BufferUtils.createFloatBuffer(colors.length).put(colors).flip();
        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, cfb, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(1, 4, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(1);

        //EBO
        GL33.glBindBuffer(GL33.GL_ELEMENT_ARRAY_BUFFER, squareEboId);
        IntBuffer ib = BufferUtils.createIntBuffer(indices.length)
                .put(indices)
                .flip();
        GL33.glBufferData(GL33.GL_ELEMENT_ARRAY_BUFFER, ib, GL33.GL_STATIC_DRAW);

        //MATRIX
        GL33.glUseProgram(Shaders.shaderProgramId);
        matrix.get(matrixFB);
        GL33.glUniformMatrix4fv(uniform, false, matrixFB);



    }
    public void update(long window) {
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_D) == GLFW.GLFW_PRESS) {
            matrix = matrix.translate(0.009f, 0f, 0f);
            this.x = x + 0.009f; // SPEED
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_A) == GLFW.GLFW_PRESS) {
            matrix = matrix.translate(-0.009f, 0f, 0f);
            this.x = x - 0.009f; // SPEED
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_W) == GLFW.GLFW_PRESS) {
            matrix = matrix.translate(0f, 0.009f, 0f);
            this.y = y + 0.009f; // SPEED
        }
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_S) == GLFW.GLFW_PRESS) {
            matrix = matrix.translate(0, -0.009f, 0f);
            this.y = y - 0.009f; // SPEED
        }
        matrix.get(matrixFB);
        GL33.glUniformMatrix4fv(uniform, false, matrixFB);
    }

    public void draw() {

        matrix.get(matrixFB);
        GL33.glUniformMatrix4fv(uniform, false, matrixFB);

        GL33.glUseProgram(Shaders.shaderProgramId);

        GL33.glBindVertexArray(squareVaoId);
        GL33.glDrawElements(GL33.GL_TRIANGLES, indices.length, GL33.GL_UNSIGNED_INT, 0);
    }



    public void red() {
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, colorsId);
        colorsFB.put(red).flip();

        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, colorsFB, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(1, 4, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(1);
    }

    public void green() {
        GL33.glBindBuffer(GL33.GL_ARRAY_BUFFER, colorsId);
        colorsFB.put(green).flip();

        GL33.glBufferData(GL33.GL_ARRAY_BUFFER, colorsFB, GL33.GL_STATIC_DRAW);
        GL33.glVertexAttribPointer(1, 4, GL33.GL_FLOAT, false, 0, 0);
        GL33.glEnableVertexAttribArray(1);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }
}

