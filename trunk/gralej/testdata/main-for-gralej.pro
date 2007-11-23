%%

:- use_module(library(system)).
:- op(1100,fx,recs).

% parses a string and sends the result to GRALE
recs(S) :-
  general_tokenize_sentence_string(S,Words,Desc),
  !,
  rec(Words,FS,Desc,Residue,Index),
  portray_cat(Words,Desc,FS,Residue,Index).

% list all words in the lexicon
words :- findall(W,--->(W,_),Ls), write(Ls), nl.

% reads a line and parses it
recs_each_line :-
  repeat,
  read_line(Line),
  ( (Line=end_of_file ; Line=[])
    -> true
  ; recs(Line),
    fail
  ).

% start and connect to Gralej

:- 
  start_client(localhost,1080),
  nl, write('Try: '), nl,
  write('    words.'), nl,
  write('    lex kim.'), nl,
  write('    rec [every,student,likes,a,book].'), nl,
  write('    recs "every student likes a book".'), nl,
  write('    recs_each_line.'), nl,
  nl.

