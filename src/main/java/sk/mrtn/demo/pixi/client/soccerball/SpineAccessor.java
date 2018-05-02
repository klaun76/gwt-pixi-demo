package sk.mrtn.demo.pixi.client.soccerball;

import sk.mrtn.library.client.tweenengine.Tween;
import sk.mrtn.library.client.tweenengine.TweenAccessor;

/**
 * Objekt sluzi na tweenovanie {@link SpineAnimation}
 *
 * @author Tomas Ecker
 */
public class SpineAccessor implements TweenAccessor<SpineAnimation> {

    protected static final int SPINE = 1;

    private static boolean isRegistered;

    public static void ensureRegistered() {
        if (!isRegistered) {
            Tween.registerAccessor(SpineAnimation.class, new SpineAccessor());
            isRegistered = true;
        }
    }

    @Override
    public void setValues(SpineAnimation target, int tweenType, float[] newValues) {
        if (target != null) {
            target.setCurrentTime(newValues[0]);
        }
    }

    @Override
    public int getValues(SpineAnimation target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case SPINE:
                returnValues[0] = (float) target.getCurrentTime();
                return 1;
            default:
                assert false;
                return 0;
        }
    }
}
