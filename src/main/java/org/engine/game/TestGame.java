package org.engine.game;

import java.util.Random;

import org.engine.core.Camera;
import org.engine.core.ILogic;
import org.engine.core.MouseInput;
import org.engine.core.ObjectLoader;
import org.engine.core.WindowManager;
import org.engine.core.entity.Entity;
import org.engine.core.entity.Material;
import org.engine.core.entity.Model;
import org.engine.core.entity.ModelManager;
import org.engine.core.entity.SceneManager;
import org.engine.core.entity.Texture;
import org.engine.core.entity.TextureManager;
import org.engine.core.entity.terrain.BlendMapTerrain;
import org.engine.core.entity.terrain.Terrain;
import org.engine.core.entity.terrain.TerrainTexture;
import org.engine.core.lighting.DirectionalLight;
import org.engine.core.lighting.PointLight;
import org.engine.core.lighting.SpotLight;
import org.engine.core.rendering.RenderManager;
import org.engine.utils.Consts;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

public class TestGame implements ILogic {

    private final RenderManager renderer;
    private final ObjectLoader loader;
    private final WindowManager window;
    private final SceneManager sceneManager;

    private Camera camera;
    Vector3f cameraInc;

    public TestGame() {
        renderer = new RenderManager();
        window = Launcher.getWindow();
        loader = new ObjectLoader();
        sceneManager = new SceneManager(-90);
        camera = new Camera(new Vector3f(0, 100, 300), new Vector3f(0, 0, 0));
        cameraInc = new Vector3f(0, 0, 0);
    }

    @Override
    public void init() throws Exception {
        renderer.init();
        ModelManager.loadModelFiles();
        TextureManager.loadTextureFiles();

        Material terrainMaterial = new Material(new Vector4f(1.0f, 1.0f, 1.0f, 0.5f), 0.0f);
        TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("textures/grass.png"));
        TerrainTexture redTerrainTexture = new TerrainTexture(loader.loadTexture("textures/red.png"));
        TerrainTexture greenTerrainTexture = new TerrainTexture(loader.loadTexture("textures/green.png"));
        TerrainTexture blueTerrainTexture = new TerrainTexture(loader.loadTexture("textures/blue.png"));
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("textures/blendMap.png"));

        BlendMapTerrain blendMapTerrain = new BlendMapTerrain(backgroundTexture, redTerrainTexture, greenTerrainTexture, blueTerrainTexture);
        for (int i = -5; i < 5; i++) {
            for (int j = -5; j < 5; j++) {
                sceneManager.addTerrain(new Terrain(new Vector3f(800 * i, 0, 800 * j), loader, terrainMaterial, blendMapTerrain, blendMap));
            }
        }

        Model cubeModel = ModelManager.getModel("cube");
        Model treeModel = ModelManager.getModel("tree");
        Model bunnyModel = ModelManager.getModel("bunny");
        Model teapotModel = ModelManager.getModel("teapot");
        Model cowModel = ModelManager.getModel("cow");
        Model pumpkinModel = ModelManager.getModel("pumpkin");
        Model teddyBearModel = ModelManager.getModel("teddyBear");

        Texture redTexture = TextureManager.getTexture("red");
        cubeModel.setTexture(redTexture);

        Texture greenTexture = TextureManager.getTexture("green");
        treeModel.setTexture(greenTexture);

        Texture whiteTexture = TextureManager.getTexture("white");
        bunnyModel.setTexture(whiteTexture);

        Texture blackTexture = TextureManager.getTexture("black");
        teapotModel.setTexture(blackTexture);

        Texture yellowTexture = TextureManager.getTexture("yellow");
        cowModel.setTexture(yellowTexture);

        Texture purpleTexture = TextureManager.getTexture("purple");
        pumpkinModel.setTexture(purpleTexture);

        Texture orangeTexture = TextureManager.getTexture("orange");
        teddyBearModel.setTexture(orangeTexture);

        Entity cube = new Entity(cubeModel, new Vector3f(200, 50, 0));
        Entity tree = new Entity(treeModel, new Vector3f(0, 0, 0));
        Entity bunny = new Entity(bunnyModel, new Vector3f(400, 0, 0));
        Entity teapot = new Entity(teapotModel, new Vector3f(600, 0, 0));
        Entity cow = new Entity(cowModel, new Vector3f(800, 80, 0));
        Entity pumpkin = new Entity(pumpkinModel, new Vector3f(1000, 150, 0));
        Entity teddyBear = new Entity(teddyBearModel, new Vector3f(1200, 50, 0));

        sceneManager.addEntity(cube);
        sceneManager.addEntity(tree);
        sceneManager.addEntity(bunny);
        sceneManager.addEntity(teapot);
        sceneManager.addEntity(cow);
        sceneManager.addEntity(pumpkin);
        sceneManager.addEntity(teddyBear);

        float lightIntensity;
        Vector3f lightPosition, lightColor, lightDirection;

        // Directional Light
        lightIntensity = 1;
        lightPosition = new Vector3f(-1, 0, 0);
        lightColor = new Vector3f(1, 1, 1);
        sceneManager.setDirectionalLight(new DirectionalLight(lightColor, lightPosition, lightIntensity));
    }

    @Override
    public void input() {
        cameraInc.set(0, 0, 0);
        if(window.isKeyPressed(GLFW.GLFW_KEY_W))
            cameraInc.z = -10;
        if(window.isKeyPressed(GLFW.GLFW_KEY_S))
            cameraInc.z = 10;
            if(window.isKeyPressed(GLFW.GLFW_KEY_A))
            cameraInc.x = -10;
        if(window.isKeyPressed(GLFW.GLFW_KEY_D))
            cameraInc.x = 10;
        if(window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT))
            cameraInc.y = -10;
        if(window.isKeyPressed(GLFW.GLFW_KEY_SPACE))
            cameraInc.y = 10;
        if(window.isKeyPressed(GLFW.GLFW_KEY_Q))
            camera.moveRotation(0.0f, 0.0f, -0.5f);
        if(window.isKeyPressed(GLFW.GLFW_KEY_E))
            camera.moveRotation(0.0f, 0.0f, 0.5f);
        if(window.isKeyPressed(GLFW.GLFW_KEY_R))
            camera.setRotation(camera.getRotation().x, camera.getRotation().y, 0);
    }

    @Override
    public void update(float interval, MouseInput mouse) {
        camera.movePosition(cameraInc.x * Consts.CAMERA_MOVE_SPEED, cameraInc.y * Consts.CAMERA_MOVE_SPEED, cameraInc.z * Consts.CAMERA_MOVE_SPEED);

        if (mouse.isRightButtonPress()) {
            Vector2f rotVec = mouse.getDisplVec();
            camera.moveRotation(rotVec.x * Consts.MOUSE_SENSITIVITY, rotVec.y * Consts.MOUSE_SENSITIVITY, 0);
        }

        sceneManager.getEntities().get(0).incRotation(0, 0.5f, 0);
        sceneManager.getEntities().get(1).incRotation(0, 0, 0.5f);
        sceneManager.getEntities().get(2).incRotation(0, 0.5f, 0);
        sceneManager.getEntities().get(3).incRotation(0, 0.5f, 0);
        sceneManager.getEntities().get(4).incRotation(0, 0.5f, 0);
        sceneManager.getEntities().get(5).incRotation(0, 0, 0.5f);
        sceneManager.getEntities().get(6).incRotation(0, 0.5f, 0);
        // Render entities and terrains
        for (Entity entity : sceneManager.getEntities()) {
            renderer.processEntity(entity);
        }
        
        for (Terrain terrain : sceneManager.getTerrains()) {
            renderer.processTerrain(terrain);
        }
    }

    @Override
    public void render() {
        renderer.render(camera, sceneManager);
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        loader.cleanup();
    }
}
