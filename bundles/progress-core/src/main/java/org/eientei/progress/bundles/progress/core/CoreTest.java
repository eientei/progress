package org.eientei.progress.bundles.progress.core;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Validate;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Created by Alexander Tumin on 2016-08-30
 */
@Component
@Instantiate
public class CoreTest implements Runnable {
    private Logger log;
    private long window;

    @Validate
    public void starting() {
        new Thread(this, "CoreThread").start();
    }

    @Override
    public void run() {
        log = LoggerFactory.getLogger(CoreTest.class);
        try {
            init();
            loop();

            glfwDestroyWindow(window);
        } finally {
            glfwTerminate();
            glfwSetErrorCallback(null).free();
        }
        System.exit(0);
    }

    private void loop() {
        log.info("Starting main loop");
        createCapabilities();
        glClearColor(0.2f, 0.2f, 0.2f, 1.0f);

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glfwSwapBuffers(window);
            glfwPollEvents();
        }

        log.info("Main loop done");
    }

    private void init() {
        log.info("Init");
        GLFWErrorCallback.createPrint(System.out).set();
        glfwInit();
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        int width = 300;
        int height = 300;

        window = glfwCreateWindow(width, height, "Progress Core", NULL, NULL);
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);
        log.info("Initialized");
    }
}
