## project-youmu (name pending)

Bound to be a Danmakufu replacement that uses Scala.

### Potential Advantages

* Stronger type system
* Less verbosity
* Increased performance
* Cross-platform
* Unicode from the start
* More professional presentation
* Ability to use existing Java and Scala libraries

### Potential Issues

* Getting LWJGL3 to act nice with SBT. (No LWJGL calls yet, but they're bound to rise.) You could do everything in Eclipse, though.

### License

Licensed under Apache 2 (see LICENSE.txt).

Coroutine code (i. e. everything under the `continuations` package) from [Scala Continuations Meet Java Coroutines talk](https://github.com/milessabin/scala-cont-jvm-coro-talk), also licensed by Apache 2.