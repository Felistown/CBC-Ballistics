package net.felis.cbc_ballistics.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class RangefinderResults {

    private int x;
    private int y;
    private int z;
    private boolean finish;
    private boolean fail;
    private boolean cat;

    public RangefinderResults() {
        int x = 0;
        int y = 0;
        int z = 0;
        finish = false;
        fail = false;
        cat = false;
    }

    @Override
    public String toString() {
        if(!finish) {
            return "Rangefinding";
        } else {
            if(fail) {
                return "Too far";
            } else {
                if(cat) {
                    return "Meow";
                    //no killing cats you monster
                } else {
                    return "X = " + x + ", Y = " + y + ", Z = " + z;
                }
            }
        }
    }


    public void fail() {
        fail = true;
        finish = true;
    }

    public void setResults(BlockPos pos) {
        x = pos.getX();
        y = pos.getY();
        z = pos.getZ();
        finish = true;
    }

    public void setResults(Vec3 pos) {
        x = (int)Math.floor(pos.x);
        y = (int)Math.floor(pos.y);
        z = (int)Math.floor(pos.z);
        finish = true;
    }

    public void setResults(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        finish = true;
    }

    public void cat() {
        cat = true;
        finish = true;
    }
}
