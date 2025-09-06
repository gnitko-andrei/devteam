cd ..
find . -type d \( -name .git -o -name .idea -o -name .mvn -o -name target \) -prune -o -print \
  | sed -e 's;[^/]*/;|   ;g;s;|   \([^|]\);+--- \1;' > project-tree.txt
