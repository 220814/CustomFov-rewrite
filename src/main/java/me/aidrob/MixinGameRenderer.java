package me.aidrob.mixin;

import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GameRenderer.class, priority = 1000)
public class MixinGameRenderer {

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void onGetFov(CallbackInfoReturnable<Double> cir) {
        if (isRenderingHand()) {
            cir.setReturnValue(70.0);
            return;
        }

        double original = cir.getReturnValue();
        
        if (original > 30.0) {
            double customFov = 30.0 + (original - 30.0) * (170.0 - 30.0) / (110.0 - 30.0);
            cir.setReturnValue(customFov);
        }
    }

    private boolean isRenderingHand() {
        StackTraceElement[] frames = Thread.currentThread().getStackTrace();
        for (int i = 0; i < Math.min(frames.length, 12); i++) {
            String m = frames[i].getMethodName();
            if (m.equals("renderHand") || m.equals("method_3195")) {
                return true;
            }
        }
        return false;
    }
}
