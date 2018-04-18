package sk.mrtn.demo.pixi.client.soccerball;

import com.google.gwt.event.shared.HandlerRegistration;
import sk.mrtn.library.client.tweenengine.BaseTween;
import sk.mrtn.library.client.tweenengine.Timeline;
import sk.mrtn.library.client.tweenengine.Tween;
import sk.mrtn.library.client.tweenengine.TweenEquations;
import sk.mrtn.pixi.client.spine.SkeletonData;
import sk.mrtn.pixi.client.spine.Spine;
import sk.mrtn.pixi.client.spine.events.ISpineEventListener;
import sk.mrtn.pixi.client.spine.events.impl.SpineEventHandling;

/**
 * Objekt poskytuje {@link BaseTween} ktory dokaze zanimovat {@link Spine} "manualne" a nie podla Tickera.
 * <p>
 * Standardne {@link Spine} je navrhnuta tak aby bola animovana jednosmerne v case pomocou nejakeho Tickeru.
 * Preto tento objekt nastavi {@link Spine#autoUpdate} na false aby sa posielalo {@link Spine#update(double)} prostrednictvom Tweenov.
 * <p>
 * Pozor animacia spravne reaguje len v smere do predu. Pri reverznej sa odpaluju eventy nespravne.
 * Ale to zatial nieje problem, kedze to nepouzivame.
 * <p>
 * {@link Spine#update(double)} sa posiela ako zmena, a nie ako absolutny cas. Preto je tu to tak naimplementovane
 * <p>
 * UPOZORNENIE: Tato trieda nieje uplne dokonala, ma svoje muchy. Pouzivat s rozumom.
 * <p>
 * Edited by Tomas Ecker
 *
 * @author martinliptak
 */
public class SpineAnimation {

    private static final double TARGET_VALUE = 1;
    private final Spine spine;
    private final double duration;
    private final String name;

    private double actualTime;

    public SpineAnimation(
            SkeletonData skeletonData,
            String name
    ) {
        this(new Spine(skeletonData), name);
    }

    public SpineAnimation(Spine spine, String name) {
        SpineAccessor.ensureRegistered();
        this.spine = spine;
        this.name = name;
        this.spine.autoUpdate = false;
        this.duration = this.spine.stateData.skeletonData.findAnimation(name).duration / this.spine.state.timeScale;
        initialize();
    }

    public Spine getSpine() {
        return this.spine;
    }

    /**
     * nastavenie skeletonu a update na 0.0 je potrebne pre to
     * aby sa dala pouzivat ta ista spine animacia viac krat a nepadla
     * na to prisiel filipko
     * inicializaciu je potrebne vykonat pred kazdym {@link #asTimeline()} volanim
     */
    private void initialize() {
        this.actualTime = 0;
        this.spine.skeleton.setToSetupPose();
        this.spine.update(0.0);
        this.spine.state.setAnimation(0, this.name, false);
    }

    public HandlerRegistration addUserEventListener(ISpineEventListener.IUserEventListener handlerRegistration) {
        SpineEventHandling spineEventHandling = SpineEventHandling.get(getSpine());
        return spineEventHandling.addUserEventListener(handlerRegistration);
    }

    /**
     * Edit: 15.12.2017 Marian Liptak
     * doplnene volania inicialize() koli reciklacii animacii
     * aby sa pred samotnym spustenim animacie (spine) zavolala inicialize() a nastavylo sa vsetko co treba
     *
     * @return timeline with initialize() on Start
     */
    public Timeline asTimeline() {
        Timeline timeline = Timeline.createSequence();
        timeline.push(Tween.call((i, baseTween) -> {
            initialize();
        }));
        timeline.push(Tween.to(this, SpineAccessor.SPINE, (float) duration).target((float) TARGET_VALUE).ease(TweenEquations.easeNone));
        return timeline;
    }

    /**
     * pozor je to relativna hodnota 0..1
     *
     * @return v akom case na nachadza animacia
     */
    public double getCurrentTime() {
        return this.actualTime;
    }

    /**
     * pozor je to celkovy cas animacie v relativnej hodnote, tj. 0..1
     */
    public void setCurrentTime(double actualTime) {
        double delta = actualTime - this.actualTime;
        this.spine.update(delta * duration);
        this.actualTime = actualTime;
    }
}
