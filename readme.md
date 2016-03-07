GO Strong Auth Plugin
=====================
An authentication plugin for Go CD that supports configurable that supports good password storage practices through:

- configurable algorithms, key-sizes, and iterations
- salts

TODO
----
- documentation
- reload configuration when user changes path to pwd file
- add configuration for how often to check file for reload, based on env variable
- strong default algorithm, keysize, and iteration count, if not specified


GO CD Core Recommendations
--------------------------
- Make logging api more delegate-friendly by making plugin id capable without constantly adding the static field
- Change behavior of auth extension so auth is enable by request for any auth plugin (rather than hard coded file and oauth)