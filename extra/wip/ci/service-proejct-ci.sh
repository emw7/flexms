#!/usr/bin/env bash

# For more information and licensing read more at https://github.com/emw7/@@REPOSITORY@@/blob/main/@@DIRECTORY@@/README.md

# set -eE: exits in case of error.
# set -u: error in case of missing variable.
# Use X=${X-default} (set X to default if X is not set) or X=${X:-default} 
#  (set X to default if X is not set or empty|null)
#  more information at 
#  https://www.gnu.org/software/bash/manual/html_node/Shell-Parameter-Expansion.html:
#   Put another way, if the colon is included, the operator tests for both 
#   parameter’s existence and that its value is not null; if the colon is 
#   omitted, the operator tests only for existence.
set -eEu

# enabled if and as needed: 
#  https://phoenixnap.com/kb/bash-trap-command
#  https://www.linuxjournal.com/content/bash-trap-command
#trap cleanup EXIT 1 2 3 6 15

help ()
{
    cat <<-EOD
        ci.sh mode arguments
            mode:
                 help
                 build
                 compose_build
                 compose_up
EOD
}

project_version ()
{
    mvn help:evaluate -Dexpression=project.version -q -DforceStdout
}

project_name ()
{
    mvn help:evaluate -Dexpression=project.name -q -DforceStdout
}

build ()
{
    rm -f target/*.jar target/*.jar.original
    mvn --settings ../../mvn_settings.xml package
}

compose_build ()
{
    docker compose --env-file=configuration.conf build
}

compose_up ()
{
    docker compose --env-file=configuration.conf up -d
}

touch -a configuration.conf pom.xml
sed -i \
    -e 's/^VERSION=.*$/VERSION='"$(project_version)"'/1'\
    -e 's/^NAME=.*$/NAME='"$(project_name)"'/1' \
    configuration.conf

. configuration.conf

MODE=help

if [ $# -ge 1 ] ; then
    MODE=$1
    shift
fi

${MODE} "$@"
