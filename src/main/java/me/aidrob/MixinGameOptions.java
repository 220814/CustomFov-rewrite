package me.aidrob.mixin;

import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GameOptions.class)
public class MixinGameOptions {

    @ModifyArg(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/option/SimpleOption;maxInclusiveValue(Ljava/lang/Object;)Lnet/minecraft/client/option/SimpleOption$MaxInclusiveValueValidator;"
        ),
        index = 0
    )
    private Object modifyMaxFov(Object value) {
        if (value instanceof Integer val && val == 110) {
            return 170;
        }
        return value;
    }
}

