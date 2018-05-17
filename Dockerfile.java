FROM gerhardlr/tango_dependencies:latest

RUN runtimeDeps='default-jre' \
    && DEBIAN_FRONTEND=noninteractive apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get -y install --no-install-recommends $runtimeDeps \
    && rm -rf /var/lib/apt/lists/* 

RUN TANGO_VERSION=9.2.5a \
    && TANGO_DOWNLOAD_URL=https://netcologne.dl.sourceforge.net/project/tango-cs/tango-$TANGO_VERSION.tar.gz \
    && buildDeps='build-essential curl file libmariadbclient-dev libmariadbclient-dev-compat default-jdk pkg-config python' \
    && DEBIAN_FRONTEND=noninteractive apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get -y install --no-install-recommends $buildDeps \
    && rm -rf /var/lib/apt/lists/* \
    && mkdir -p /usr/src/tango \
    && cd /usr/src/tango \
    && curl -fsSL "$TANGO_DOWNLOAD_URL" -o tango.tar.gz \
    && tar xf tango.tar.gz -C /usr/src/tango --strip-components=1 \
    && ./configure --with-zmq=/usr/local --with-omni=/usr/local --with-mysqlclient-prefix=/usr --enable-static=no \
    && make -C /usr/src/tango -j$(nproc) \
    && make -C /usr/src/tango install \
    && ldconfig \
    && apt-get purge -y --auto-remove $buildDeps \
    && rm -r /usr/src/tango

ENV HOME /home/tango
RUN useradd --create-home --home-dir $HOME tango \
    && chown -R tango:tango $HOME

WORKDIR $HOME
USER tango    
