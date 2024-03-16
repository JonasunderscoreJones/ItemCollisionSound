package dev.jonasjones.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.ItemEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
	@Unique
	private boolean hasCollidedXUp = false;
	@Unique
	private boolean hasCollidedXDown = false;
	@Unique
	private boolean hasCollidedYDown = false;
	@Unique
	private boolean hasCollidedZUp = false;
	@Unique
	private boolean hasCollidedZDown = false;
	@Inject(at = @At("HEAD"), method = "tick")
	private void onItemTick(CallbackInfo info) {
		ItemEntity itemEntity = (ItemEntity) (Object) this; // Cast Entity to ItemEntity
		Vec3d itemPosition = itemEntity.getPos(); // Get item's position
		BlockPos blockPos = new BlockPos(new Vec3i((int) itemPosition.x, (int) itemPosition.y, (int) itemPosition.z));
		World world = itemEntity.getEntityWorld(); // Get item's world

		//TODO: Try to get this system working by detecting a stop in motion

		if (!world.isClient) {
			BlockPos blockPosXDown = new BlockPos(new Vec3i((int) itemPosition.x - 1, (int) itemPosition.y, (int) itemPosition.z));
			if (itemPosition.x % 1 == 0.125 && isCollidableBlock(world, blockPosXDown)) {
				if (!hasCollidedXDown) {
					hasCollidedXDown = true;
					playSound(world, blockPos);
				}
			} else {
				hasCollidedXDown = false;
			}

			BlockPos blockPosXUp = new BlockPos(new Vec3i((int) itemPosition.x + 1, (int) itemPosition.y, (int) itemPosition.z));
			if (itemPosition.x % 1 == 0.875 && isCollidableBlock(world, blockPosXUp)) {
				if (!hasCollidedXUp) {
					hasCollidedXUp = true;
					playSound(world, blockPos);
				}
			} else {
				hasCollidedXUp = false;
			}

			BlockPos blockPosYDown = new BlockPos(new Vec3i((int) itemPosition.x, (int) itemPosition.y - 1, (int) itemPosition.z));
			if (itemPosition.y % 1 == 0 && isCollidableBlock(world, blockPosYDown)) {
				if (!hasCollidedYDown) {
					hasCollidedYDown = true;
					playSound(world, blockPos);
				}
			} else {
				hasCollidedYDown = false;
			}

			BlockPos blockPosZDown = new BlockPos(new Vec3i((int) itemPosition.x, (int) itemPosition.y, (int) itemPosition.z - 1));
			if (itemPosition.z % 1 == 0.125 && isCollidableBlock(world, blockPosZDown)) {
				if (!hasCollidedZDown) {
					hasCollidedZDown = true;
					playSound(world, blockPos);
				}
			} else {
				hasCollidedZDown = false;
			}

			BlockPos blockPosZUp = new BlockPos(new Vec3i((int) itemPosition.x, (int) itemPosition.y, (int) itemPosition.z + 1));
			if (itemPosition.z % 1 == 0.875 && isCollidableBlock(world, blockPosZUp)) {
				if (!hasCollidedZUp) {
					hasCollidedZUp = true;
					playSound(world, blockPos);
				}
			} else {
				hasCollidedZUp = false;
			}
		}
	}
	@Unique
	private void playSound(World world, BlockPos blockPos) {
		world.playSound(null, blockPos, SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 1f, 1f);
	}

	@Unique
	private boolean isCollidableBlock(World world, BlockPos blockPos) {
		BlockState blockState = world.getBlockState(blockPos);
		return blockState.isSolidBlock(world, blockPos);
	}
}