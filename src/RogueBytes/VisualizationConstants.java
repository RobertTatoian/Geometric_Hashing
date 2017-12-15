package RogueBytes;

public interface VisualizationConstants {

    //=====================================================
    //  WINDOW DIMENSIONS
    //=====================================================
    //  Dimensions of the window
    int WIN_WIDTH = 1500, WIN_HEIGHT = 800;

    //=====================================================
    //  WORLD DIMENSIONS and SCALING
    //=====================================================
    //  origin of the world at middle bottom of the window
    float WORLD_ORIGIN_X = WIN_WIDTH/2, WORLD_ORIGIN_Y = WIN_HEIGHT/2;

    //  dimensions of my world
    float WORLD_WIDTH = 0.5f;
    float WORLD_HEIGHT = 0.4f;

    float XMAX = WORLD_WIDTH/2, XMIN = -WORLD_WIDTH/2;
    float YMAX = WORLD_HEIGHT/2, YMIN = -WORLD_HEIGHT/2;

    //  conversion factors
    float PIXEL_TO_WORLD = WORLD_WIDTH / WIN_WIDTH;
    float WORLD_TO_PIXEL = 1.0f / PIXEL_TO_WORLD;

    //=====================================================

}
