## project-youmu (name pending)

Bound to be a Danmakufu replacement that uses Scala.

### Potential Advantages

* Stronger type system
* Less verbosity
* Increased performance
* Cross-platform
* Unicode from the start
* Easier debugging
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

Copyright 2015 Fluffy8x.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Coroutine code (i. e. everything under the `continuations` package) from [Scala Continuations Meet Java Coroutines talk](https://github.com/milessabin/scala-cont-jvm-coro-talk), also licensed by Apache 2.
