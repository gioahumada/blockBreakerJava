package puppy.code.factory;

import puppy.code.blocks.Block;
import puppy.code.blocks.HardBlock;
import puppy.code.blocks.NormalBlock;
import puppy.code.powerups.ExtraLifePowerUp;
import puppy.code.powerups.FallingPowerUp;
import puppy.code.powerups.SpeedUpPowerUp;
import puppy.code.powerups.TripleBallPowerUp;

public class HardGameElementFactory implements GameElementFactory {
    @Override
    public Block createBlock(int x, int y, int width, int height) {
        if (Math.random() > 0.6) {
            return new HardBlock(x, y, width, height);
        }
        return new NormalBlock(x, y, width, height);
    }

    @Override
    public FallingPowerUp createPowerUp(int x, int y) {
        double random = Math.random();
        if (random < 0.2) {
            return new ExtraLifePowerUp(x, y);
        } else if (random < 0.5) {
            return new TripleBallPowerUp(x, y);
        }
        return new SpeedUpPowerUp(x, y);
    }

    @Override
    public int getInitialLives() {
        return 3;
    }

    @Override
    public float getBallSpeed() {
        return 7f;
    }
}
