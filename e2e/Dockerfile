FROM gocd/gocd-dev:latest
MAINTAINER Daniel Somerfield <dsomerfi@thoughtworks.com>

RUN mkdir -p /var/lib/go-server/plugins/external
RUN mkdir -p /var/lib/go-server/plugins/bundled
RUN chown -R go:go /var/lib/go-server

ADD deploy/strong-auth* /var/lib/go-server/plugins/external/
VOLUME /var/lib/go-server/plugins/external/

RUN mkdir /tmp/test-helpers
ADD test-helpers/* /tmp/test-helpers/
RUN chmod +x /tmp/test-helpers/*