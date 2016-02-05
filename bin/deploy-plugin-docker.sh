PLUGIN_NAME=go-strong-auth

DOCKER ADD strong-auth-1.0-SNAPSHOT.jar
    sh -c 'OUTFILE=/var/lib/go-server/plugins/external/"$PLUGIN_NAME".jar; cat >/tmp/temp.go.plugin; chown go:go /tmp/temp.go.plugin; mv /tmp/temp.go.plugin "$OUTFILE"' <""$PLUGIN_NAME".jar