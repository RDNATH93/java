FROM gitpod/workspace-full

SHELL ["/bin/bash", "-c"]
RUN source "/home/gitpod/.sdkman/bin/sdkman-init.sh"  \
    && sdk install java 21.0.5-oracle < /dev/null

RUN  sudo apt-get update && sudo apt install httpie -y  
