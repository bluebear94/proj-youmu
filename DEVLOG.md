## Fluffy8x's devlog

### 2015-03-20

Woo, 5 in the morning!
TPrimitive2D is now a trait, which Primitive2D implements. This allows
having sprites be a separate class instead of something constructed
by a utility method.

It's still a long journey. I will have fun debugging things when everything
is in a playable state.

### 2015-03-18

Replaced the existing set that keeps primitive objects to encompass
all renderables and used a TreeMap instead to enable drawing based on
render priority. Yep, now you can have more than 101 render priorities!
Positions of primitives also are not always absolute anymore.

I also made system scripts have a function to display the pause menu
and the end menu.

This will be a long, long journey.