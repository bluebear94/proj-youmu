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
* Writing resource loaders since slick-util is not compatible with 
LWJGL3

### Trying out proj-youmu

* Get git and SBT
* Clone this repository
* CD into this directory and run SBT (i. e. type `sbt`; what else do you 
think?)
* Type `compile` to compile the project
* Type `test` to run tests
* Type `console` for the Scala REPL
* Type `run` if you have any objects with a `main` method
* Type `eclipse` to generate Eclipse project files

### License

Licensed under Apache 2 (see LICENSE.txt).

Coroutine code (i. e. everything under the `continuations` package) from [Scala Continuations Meet Java Coroutines talk](https://github.com/milessabin/scala-cont-jvm-coro-talk), also licensed by Apache 2.
