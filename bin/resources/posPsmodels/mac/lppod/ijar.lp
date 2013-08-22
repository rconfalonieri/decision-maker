100 -w :- ci, r.
100 w :- b, r.
100 f :- ci.
100 -f :- b,s,ba1.
100 f :- b,s,ba.
80 r :- c.
60 s :- h.
100 c.
100 h.

100 -ass(b) x ass(b).
100 -ass(ci) x ass(ci).
100 -ass(ba) x ass(ba).
100 -ass(ba1) x ass(ba1).

100 b :- ass(b).
100 ci :- ass(ci).
100 ba :- ass(ba).
100 ba1 :- ass(ba1).

%f.
%b.
%-w.
%:- not f.
%:- not b.
%:- not -w.
