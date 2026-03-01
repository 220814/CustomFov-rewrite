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
        double originalFov = cir.getReturnValue();

        if (isRenderingHand()) {
            cir.setReturnValue(70.0);
            return;
        }

        double minFov = 30.0;
        double maxFov = 110.0;
        double targetMax = 170.0;

        double customFov = minFov + (originalFov - minFov) * (targetMax - minFov) / (maxFov - minFov);

        if (customFov < 0.0) customFov = 0.0;
        if (customFov > 170.0) customFov = 170.0;

        cir.setReturnValue(customFov);
    }

    private boolean isRenderingHand() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 0; i < Math.min(stackTrace.length, 10); i++) {
            String methodName = stackTrace[i].getMethodName();
            if (methodName.equals("renderHand") || methodName.equals("method_3195")) {
                return true;
            }
        }
        return false;
    }
}
