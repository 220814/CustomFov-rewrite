package me.aidrob.mixin;

import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = GameRenderer.class, priority = 1000)
public class MixinGameRenderer {

    @Redirect(
        method = "getFov",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/option/SimpleOption;getValue()Ljava/lang/Object;"
        )
    )
    private Object redirectFovValue(SimpleOption<Integer> option) {
        Integer value = option.getValue();

        if (isRenderingHandContext()) {
            return value;
        }

        double minFov = 30.0;
        double maxFov = 110.0;
        double targetMax = 170.0;

        if (value > minFov) {
            double customFov = minFov + (value - minFov) * (targetMax - minFov) / (maxFov - minFov);
            return (int) Math.round(customFov);
        }

        return value;
    }

    private boolean isRenderingHandContext() {
        StackTraceElement[] frames = Thread.currentThread().getStackTrace();
        for (int i = 0; i < Math.min(frames.length, 15); i++) {
            String m = frames[i].getMethodName();
            if (m.equals("renderHand") || m.equals("method_3195") || m.equals("renderItemInHand")) {
                return true;
            }
        }
        return false;
    }
}
            
