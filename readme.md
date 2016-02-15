GO Strong Auth Plugin
=====================
An authentication plugin for Go CD that supports configurable that supports good password storage practices through:

- configurable algorithms, key-sizes, and iterations
- salts

TODO
----
- configuration UI
- strong default algorithm, keysize, and iteration count, if not specified
- User configuration support


GO CD Core Recommendations
--------------------------
- Make logging api more delegate-friendly by making plugin id capable without constantly adding the static field
- Change behavior of auth extension so auth is enable by request for any auth plugin (rather than hard coded file and oauth)