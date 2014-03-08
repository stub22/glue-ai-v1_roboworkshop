
This directory contains a patched version of the Felix framework jar, version 3.2.2.

The only difference from the original is a single line added to the default.properties
file, for each of the three reasonably modern JVM versions (1.5, 1.6, 1.7), which allows 
the Felix framework, when running under one of these JVM versions, to supply the 
sun.misc.Unsafe package for use by other bundles.

The problem we are working around is discussed here:

http://lists.apidesign.org/pipermail/netigso/2011-December/000203.html

