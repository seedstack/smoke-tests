#!/bin/sh
#
# Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
#
# This Source Code Form is subject to the terms of the Mozilla Public
# License, v. 2.0. If a copy of the MPL was not distributed with this
# file, You can obtain one at http://mozilla.org/MPL/2.0/.
#


(
    git clone https://github.com/seedstack/smoke-tests.git target/gh-pages
    cd target/gh-pages || exit
    git checkout gh-pages || exit
    cp -f ../results/*.json . || exit
    git config user.name "Travis-CI"
    git config user.email "travis@seedstack.org"
    git add .
    git commit -m "Test results of ${TEST_SERVER}"
    git push --quiet "https://${GITHUB_TOKEN}@github.com/seedstack/smoke-tests" gh-pages:gh-pages > /dev/null 2>&1
)
