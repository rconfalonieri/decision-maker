Decision Maker v0.1
===================

An implementation of ASP algorithms to compute a decision making under uncertainty problem according to possibilistic qualitative decsion making

HOWTO:

1. Import in Eclipse, set the classpath, compile and export as executable jar
1. Lanch the executable jar (double click or "java -jar decision-maker.jar" if jar was named decision-maker.jar)
2. Load a .dm file which contains the description of a decision making under uncertainty problem
3. Select the reasoner and the attitude of the decision maker and press "Compute"
4. Press "Details" for internal computation details


REASONERS:

The prototype provides the implementation of the algorithms based on the LPPODs and LPODs semantics, 
as well as a DLV compliant implementation.

The LPODs based algorithms are implemented by using the "psmodels" solver (available at http://www.tcs.hut.fi/Software/smodels/priority/)
The LPPODs based algorithms are implemented by using the "posPsmodels" solver (developed by one of the author, https://github.com/rconfalonieri/LPPODsolver)
The DLV compliant algorithms are implemented by using the "DLV system" (available at www.dlvsystem.com).


PORTABILITY:

The DLV implementation can be run on OS X, Linux Ubuntu, and Windows.

The LPODs and LPPODs implementations can be ONLY run on OS X and Linux Ubuntu,
since several components for the "psmodels" and "posPsmodels" solvers are not available for Windows.
 
 
CREDITS:

This Java project is an implementation of the algorithms proposed in the paper:

Confalonieri, R., Prade, H. "Using Possibilistic Logic for Modeling Qualitative Decision: Answer Set Programming Algorithms"

Currently being evaluated for IJAR.
