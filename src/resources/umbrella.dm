%% --- Decision Maker Agent save file ---
%% Name :
Stand alone Decision Tool agent

%% Knowledge :

c ; 1.0
r :- c ; 0.7
-r :- -c ; 0.5
-w :- u ; 1.0
w :- r,-u ; 1.0

%% Preferences :

-w ; 1.0
-u ; 0.3

%% Decisions :

u
-u

