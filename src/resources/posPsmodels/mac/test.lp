100 -w :- u.
100 w :- r, u1.
90 r :- c.
100 c.
100 :- not u1.

100 -ass(u) :- not ass(u).
100 ass(u) :- not -ass(u).
100 -ass(u1) :- not ass(u1).
100 ass(u1) :- not -ass(u1).
100 minimize[u=1].
100 minimize[u1=1].
 
