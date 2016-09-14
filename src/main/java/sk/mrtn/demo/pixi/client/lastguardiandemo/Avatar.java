package sk.mrtn.demo.pixi.client.lastguardiandemo;

import sk.mrtn.pixi.client.Texture;

import javax.inject.Inject;

/**
 * Created by martinliptak on 14/09/16.
 */
public class Avatar {

    private String name;
    private Texture[] front;
    private Texture[] back;
    private Texture[] left;
    private Texture[] right;

    @Inject
    Avatar(){}

    public Texture[] getBack() {
        return back;
    }

    public Texture[] getLeft() {
        return left;
    }

    public Texture[] getRight() {
        return right;
    }

    public Avatar create(String name) {
        this.name = name;
        this.front = new Texture[]{Texture.fromFrame(name+"_fr1"), Texture.fromFrame(name+"_fr2")};
        this.back = new Texture[]{Texture.fromFrame(name+"_bk1"),Texture.fromFrame(name+"_bk2")};
        this.left = new Texture[]{Texture.fromFrame(name+"_lf1"),Texture.fromFrame(name+"_lf2")};
        this.right = new Texture[]{Texture.fromFrame(name+"_rt1"),Texture.fromFrame(name+"_rt2")};
        return this;
    }

    public String getName() {
        return name;
    }

    public Texture[] getFront() {
        return front;
    }
}
