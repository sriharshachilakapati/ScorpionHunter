#ScorpionHunter - A SilenceEngine Demo Game

This is a demo game written using the [SilenceEngine](https://github.com/sriharshachilakapati/SilenceEngine), a 2D/3D game engine. This game is currently using the development version of SilenceEngine, version 0.0.3 alpha.

This game is a 2D Top-Down shooter, a genre of 2D games where the entire world is rendered in a top-down way, and the player (means you) is engaged in a lone assault with the monsters. In this game, you are a man who hunts down scorpions, giant ones to gain score.

###Screen Shots

![Main Screen](http://puu.sh/dTowB/a1fe58bff2.png)

![Player](http://puu.sh/dToA5/0339f2b1f5.png)

![Scorpions](http://puu.sh/dTqyr/5778b4af24.png)

![Blood](http://puu.sh/dTqJt/47d5a369ad.png)

###Features Demonstrated

This game demonstrates the following 2D features of SilenceEngine, allowing you to focus on the game, rather on the background works. You still need a bit of math though.

  - Resource Loading and disposing
  - Polygon geometry creation
  - Level design with SceneGraph
  - Collision Detection and Response
  - Entity Transformations

Though this is a big list, you don't have to tackle with them, only because SilenceEngine manages all those for you. For more information, please go through the source code, and you will understand it.

###Current Issues

Currently, there is only one issue, which only occurs on the Mac OS X. To get the game running on Mac OS, launch the game with the following command.

    java -XstartOnFirstThread -jar ScorpionHunter.jar

This is required due to the way the JVM was implemented on Mac OS X, without which there occurs some issues between inter-operation of AWT and GLFW.

##Licence

This game, and its resources are created by me, Sri Harsha Chilakapati, and are licenced under the MIT licence, just like SilenceEngine.