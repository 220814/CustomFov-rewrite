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
        }
    }

    private boolean isRenderingHand() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int i = 0; i < Math.min(stackTrace.length, 12); i++) {
            String methodName = stackTrace[i].getMethodName();
            if (methodName.equals("renderHand") || methodName.equals("method_3195")) {
                return true;
            }
        }
        return false;
    }
}
