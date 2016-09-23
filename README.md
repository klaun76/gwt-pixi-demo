# Gwt-pixi-demo
This is just demo project for [Pixi gwt](https://github.com/klaun76/gwt-pixi). Really nothing to be proud of. Main purpose of this demo is to quickly test all possible problems caused by changes and updates in [Pixi gwt](https://github.com/klaun76/gwt-pixi).
This version has fixed settings.gradle, so folder names will match names of repositories. Previous settings caused errors in gradle build.
Functional build is here [demo](http://mrtn.sk/demogwtpixi/)
# How to use
Just copy gwt-pixi-demo, [Pixi gwt](https://github.com/klaun76/gwt-pixi) and [library](https://github.com/klaun76/library) in same folder. Or fix gradle settings according to projects location. This repository was created as request from gwt google plus group member.
# Warning
Try to avoid spaces within path to projects. Gradle (or maybe my poor settings of project) have problem with space and gradle tasks throw errors
# Current project content:
* four different tests which were prepared just to test pixi functionality
    * movieclips tokitori
    * movieclips lastguardian
    * unit tests - for now just working with textures, different imports, testing of actual fields and methods of objects
    * test with particles
        * background image on which few filters are applied
        * one larger bunny with mouse click interaction + some filter
        * second bunny with no interaction
        * one test particle form bunnies (changing color)
        * animated particle created from coins (resources taken from [Pixi particles demo](https://github.com/pixijs/pixi-particles))
        * animated particle created from bubbles (resources taken from [Pixi particles demo](https://github.com/pixijs/pixi-particles))
        * coins and bubbles particles are placed into one common ParticlesContainer
# Warning
This demo will be updated along with [Pixi gwt](https://github.com/klaun76/gwt-pixi) because this library is in early development stages and this testing project is closely dependent on [Pixi gwt](https://github.com/klaun76/gwt-pixi) library