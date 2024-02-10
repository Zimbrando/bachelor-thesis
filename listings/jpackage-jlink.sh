# Salvataggio delle dipendenze
jdeps --print-module-deps --add-modules=ALL-MODULE-PATH --ignore-missing-deps --no-recursive alchemist-full-VERSION-all.jar > dependencies.txt

DEPENDENCIES = $(< dependencies.txt)

# Creazione della java-runtime ridotta
jlink --no-header-files --no-man-pages --add-modules=$DEPENDENCIES --output java-runtime