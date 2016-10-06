package sk.mrtn.demo.pixi.client.common;

import sk.mrtn.pixi.client.Container;

/**
 * Created by martinliptak on 02/10/16.
 */
public interface IResponsiveController {

    void onResized(double width, double height);

    Container getContainer();

    void onDetached();

}
