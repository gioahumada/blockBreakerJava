package puppy.code.factory;

import puppy.code.blocks.Block;
import puppy.code.powerups.FallingPowerUp;

public interface GameElementFactory {
    Block createBlock(int x, int y, int width, int height);
    FallingPowerUp createPowerUp(int x, int y);
    int getInitialLives();
    float getBallSpeed();
} 