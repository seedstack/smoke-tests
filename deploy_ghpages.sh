#!/bin/sh

(
    git clone https://github.com/seedstack/smoke-tests.git target/gh-pages
    cd target/gh-pages || exit
    git co gh-pages || exit
    cp -f ../results/*.json . || exit
    git config user.name "Travis-CI"
    git config user.email "travis@seedstack.org"
    git add .
    git commit -m "Test results of ${TEST_SERVER}"
    git push --quiet "https://${GITHUB_TOKEN}@github.com/seedstack/smoke-tests" gh-pages:gh-pages > /dev/null 2>&1
)
