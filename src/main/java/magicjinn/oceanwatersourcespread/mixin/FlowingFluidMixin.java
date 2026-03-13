package magicjinn.oceanwatersourcespread.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.WaterFluid;

@Mixin(FlowingFluid.class)
public class FlowingFluidMixin {

    @Inject(at = @At(value = "TAIL"), method = "tick")
    private void tick(ServerLevel level, BlockPos pos, BlockState blockState, FluidState fluidState,
            CallbackInfo info) {
        FlowingFluid self = (FlowingFluid) (Object) this;
        if (!(self instanceof WaterFluid))
            return;

        // if below sea level, and in an ocean biome, spread the water
        if (pos.getY() < level.getSeaLevel() && level.getBiome(pos).is(BiomeTags.IS_OCEAN)) {
            // set the block to full water
            level.setBlock(pos, Blocks.WATER.defaultBlockState(), 3);
            // getTickDelay and getSpreadDelay are the same thing
            level.scheduleTick(pos, self, self.getTickDelay(level));
        }

    }
}
