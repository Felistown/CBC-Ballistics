package net.felis.cbc_ballistics.block.entity;

import net.felis.cbc_ballistics.screen.custom.Ballistic_CalculatorScreen;
import net.felis.cbc_ballistics.util.calculator.Cannon;
import net.felis.cbc_ballistics.util.calculator.Projectile;
import net.felis.cbc_ballistics.util.calculator.Target;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class CalculatorBlockEntity extends BlockEntity implements MenuProvider {

    private static final Component CAST_IRON = Component.translatable("block.cbc_ballistics.ballistic_calculator.castIron");
    private static final Component STEEL = Component.translatable("block.cbc_ballistics.ballistic_calculator.steel");
    private static final Component BRONZE = Component.translatable("block.cbc_ballistics.ballistic_calculator.bronze");
    private static final Component NETHER_STEEL = Component.translatable("block.cbc_ballistics.ballistic_calculator.netherSteel");
    private static final Component WROUGHT_IRON = Component.translatable("block.cbc_ballistics.ballistic_calculator.wroughtIron");

    private String cannonPos;
    private String targetPos;
    private String minPitch;
    private String maxPitch;
    private String length;
    private String minCharge;
    private String maxCharge;
    private int material;
    private String gravity;
    private String drag;

    private boolean calculate;
    private boolean isDirectFire;

    private int[] cPos;
    private int[] tPos;
    private int minP;
    private int maxP;
    private int len;
    private int minC;
    private int maxC;
    private double grav;
    private double drg;

    private Projectile[] results;
    private boolean ready;


    public CalculatorBlockEntity( BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CALCULATORBLOCKENTITY.get(), pPos, pBlockState);
        cPos = new int[3];
        tPos = new int[3];
        calculate = false;
        isDirectFire = false;
        ready = false;
        material = 3;
        maxPitch = "60";
        minPitch = "-30";
        gravity = "0.05";
        drag = "0.99";
        minP = -30;
        maxP = 60;
        grav =  0.05;
        drg = 0.99;
        minC = 1;
        maxC = 1;
    }

    public boolean calculate(Ballistic_CalculatorScreen screen) {
        Cannon myCannon = new Cannon(cPos[0] , cPos[1], cPos[2], len, minP, maxP, getMaterialString());
        myCannon.setDrag(drg);
        myCannon.setGravity(grav);
        Target myTarget = new Target( tPos[0], tPos[1], tPos[2], myCannon);
        myCannon.setTarget(myTarget);
        try {
            results = myCannon.interpolateTarget(minC, maxC);
            screen.setAllowPress();
            ready = true;
            return true;
        } catch (RuntimeException e) {
            ready = false;
            screen.setAllowPress();
            return false;
        }
    }

    public boolean isReady() {
        return ready;
    }

    public Projectile getResult() {
        if(results != null) {
            if (isDirectFire) {
                return results[0];
            } else {
                return results[1];
            }
        }
        return null;
    }


    @Override
    public Component getDisplayName() {
        return Component.translatable("block.cbc_ballistics.ballistic_calculator");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return null;
    }

    public Component getMaterial() {
        switch (material) {
            case 0:
                return CAST_IRON;
            case 1:
                return WROUGHT_IRON;
            case 2:
                return BRONZE;
            case 3:
                return STEEL;
            case 4:
                return NETHER_STEEL;
            default:
                material = 0;
                return Component.literal("Unknown material");
        }
    }

    public String getCannonPos() {
        return cannonPos;
    }

    public String getDrag() {
        return drag;
    }

    public String getGravity() {
        return gravity;
    }

    public String getLength() {
        return length;
    }

    public String getMinCharge() {
        return minCharge;
    }

    public String getMaxCharge() {
        return maxCharge;
    }

    public String getMaxPitch() {
        return maxPitch;
    }

    public String getMinPitch() {
        return minPitch;
    }

    public String getTargetPos() {
        return targetPos;
    }

    public boolean getCalculateButton() {
        return calculate;
    }

    public boolean isDirectFire() {
        return isDirectFire;
    }

    public boolean setCannonPos(String cannonPos) {
        this.cannonPos = cannonPos;
        setChanged();
        int[] array = new int[3];
        boolean result = tryLong(cannonPos, array);
        if(result) {
            cPos = array;
            return true;
        } else {
            return false;
        }
    }

    public boolean setDrag(String drag) {
        this.drag = drag;
        try {
            drg = Double.parseDouble(drag);
            return drg > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean setMaxCharge(String maxCharge) {
        this.maxCharge = maxCharge;
        try {
            maxC = Integer.parseInt(maxCharge);
            return maxC >= minC && maxC > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean setMinCharge(String minCharge) {
        this.minCharge = minCharge;
        try {
            minC = Integer.parseInt(minCharge);
            return minC <= maxC && minC > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean setGravity(String gravity) {
        this.gravity = gravity;
        try {
            grav = Double.parseDouble(gravity);
            return grav > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean setLength(String length) {
        this.length = length;
        try {
            len = Integer.parseInt(length);
            return len > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void cycleMaterial() {
        material ++;
        if(material > 4) {
            material = 0;
        }
    }

    public void cycleMode() {
        isDirectFire = !isDirectFire;
    }

    public boolean setMaxPitch(String maxPitch) {
        this.maxPitch = maxPitch;
        try {
            maxP = Integer.parseInt(maxPitch);
            return maxP > minP && maxP < 90;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String getMaterialString() {
        switch (material) {
            case 0:
                return "castiron";
            case 1:
                return "wroughtiron";
            case 2:
                return "bronze";
            case 3:
                return "steel";
            case 4:
                return "nethersteel";
            default:
                material = 0;
                return "Unknown material";
        }
    }

    public boolean setMinPitch(String minPitch) {
        this.minPitch = minPitch;
        try {
            minP = Integer.parseInt(minPitch);
            return minP < maxP && minP > -90;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean setTargetPos(String targetPos) {
        this.targetPos = targetPos;
        setChanged();
        int[] array = new int[3];
        boolean result = tryLong(targetPos, array);
        if(result) {
            tPos = array;
            return true;
        } else {
            return false;
        }
    }

    public Component getMode() {
        if(isDirectFire) {
            return Component.translatable("block.cbc_ballistics.ballistic_calculator.direct");
        } else {
            return Component.translatable("block.cbc_ballistics.ballistic_calculator.indirect");
        }
    }

    private static boolean tryLong(String string, int[] array) {
        for(int i = 0; i <= 2; i ++) {
            boolean pass = false;
            boolean found = false;
            int index = 0;
            for (int j = 0; j < string.length(); j++) {
                try {
                    Integer.valueOf(string.substring(j, j + 1));
                    if(!found) {
                        index = j;
                        found = true;
                    }
                } catch (NumberFormatException e) {
                    if (found) {
                        try {
                            array[i] = Integer.parseInt(string.substring(index, j));
                        } catch (NumberFormatException e1) {
                            return false;
                        }
                        string = string.substring(j);
                        pass = true;
                        break;
                    } else {
                        if (string.charAt(j) == '-') {
                            index = j;
                            found = true;
                        }
                    }
                }
            }
            if(!pass) {
                if(found) {
                    try {
                        array[i] = Integer.parseInt(string.substring(index));
                        string = "";
                    } catch (NumberFormatException e) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return string.isEmpty();
    }
}
