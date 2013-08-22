%% --- Decision Maker Agent save file ---
%% Name :
Stand alone Decision Tool agent

%% Knowledge :

se :- sg ; 1.0
-se :- -sg ; 1.0
-ll :- sg ; 1.0
ll :- ca,-sg ; 1.0
-ll :- -ca ; 1.0
ca :- cp ; 0.7
cp ; 1.0

%% Preferences :

-ll ; 1.0
-se ; 0.5

%% Decisions :

sg
-sg


