package sk.mrtn.demo.pixi.client.tween;

import com.google.gwt.core.client.GWT;
import sk.mrtn.library.client.tweenengine.TweenAccessor;
import sk.mrtn.pixi.client.DisplayObject;

import java.util.Arrays;

public class DisplayObjectAccessor implements TweenAccessor<DisplayObject> {
    public static final int X = 1;
    public static final int Y = 2;
    public static final int XY = 3;
 
    public int getValues(DisplayObject target, int tweenType, float[] returnValues) {
          switch (tweenType) {
              case X: returnValues[0] = (float) target.position.x; return 1;
              case Y: returnValues[0] = (float) target.position.y; return 1;
              case XY:
                  returnValues[0] = (float) target.position.x;
                  returnValues[1] = (float) target.position.y;
                  return 2;
              default: assert false; return 0;
          }
      }
 
    public void setValues(DisplayObject target, int tweenType, float[] newValues) {
        GWT.log("twenam: " + tweenType +":"+ Arrays.toString(newValues));
          switch (tweenType) {
              case X: target.position.set(newValues[0],target.position.y); break;
              case Y: target.position.set(target.position.x,newValues[1]); break;
              case XY:
                  target.position.set(newValues[0],newValues[1]);
                  break;
              default: assert false; break;
          }
      }
}
