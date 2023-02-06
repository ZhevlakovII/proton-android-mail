#
# Copyright (c) 2022 Proton Technologies AG
# This file is part of Proton Technologies AG and Proton Mail.
#
# Proton Mail is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# Proton Mail is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with Proton Mail. If not, see <https://www.gnu.org/licenses/>.
#

#!/bin/bash

set -e

release_notes_file_path="$1"

# Write a release notes file for Firebase App Distribution containing the branch name and the latest 5 commit messages.
echo "BRANCH - $CI_COMMIT_BRANCH" > "$release_notes_file_path"
git log --format="%h - %s%n%b" -n 5 >> "$release_notes_file_path"
