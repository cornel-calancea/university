:- ensure_loaded('chat.pl').

% Returneaza true dacă regula dată ca argument se potriveste cu
% replica data de utilizator. Replica utilizatorului este
% reprezentata ca o lista de tokens. Are nevoie de
% memoria replicilor utilizatorului pentru a deduce emoția/tag-ul
% conversației.
% match_rule/3
% match_rule(_Tokens, _UserMemory, rule(_, _, _, _, _)) :- fail.

% match_rule(_Tokens, _UserMemory, Rule) :- rules([Token], Rule), member(Token, _Tokens).

reply_unifies(_Tokens, [rule(UserReply, _, _, _, _)|Rest]) :- (write("User Reply : "), writeln(UserReply), 
																write("Tokens : "), writeln(_Tokens), UserReply = _Tokens, writeln("TRUE!")), !;
																reply_unifies(_Tokens, Rest).

match_rule(_Tokens, _UserMemory, Rule) :- rules(RuleTokens, Rule), ord_subset(RuleTokens, _Tokens), 
											write("Rule : "), writeln(Rule),
											 reply_unifies(_Tokens, Rule).



% Primeste replica utilizatorului (ca lista de tokens) si o lista de
% reguli, iar folosind match_rule le filtrează doar pe cele care se
% potrivesc cu replica dată de utilizator.
% find_matching_rules/4
% find_matching_rules(+Tokens, +Rules, +UserMemory, -MatchingRules)

find_matching_rules(_Tokens, _Rules, _UserMemory, _MatchingRules) :- findall(Rule, (rules(_, Rule), match_rule(_Tokens, _UserMemory, Rule)), _MatchingRules), writeln("Reached end!").

% Intoarce in Answer replica lui Gigel. Selecteaza un set de reguli
% (folosind predicatul rules) pentru care cuvintele cheie se afla in
% replica utilizatorului, in ordine; pe setul de reguli foloseste
% find_matching_rules pentru a obtine un set de raspunsuri posibile.
% Dintre acestea selecteaza pe cea mai putin folosita in conversatie.
%
% Replica utilizatorului este primita in Tokens ca lista de tokens.
% Replica lui Gigel va fi intoarsa tot ca lista de tokens.
%
% UserMemory este memoria cu replicile utilizatorului, folosita pentru
% detectarea emotiei / tag-ului.
% BotMemory este memoria cu replicile lui Gigel și va si folosită pentru
% numararea numarului de utilizari ale unei replici.
%
% In Actions se vor intoarce actiunile de realizat de catre Gigel in
% urma replicii (e.g. exit).
%
% Hint: min_element, ord_subset, find_matching_rules

% select_answer/5
% select_answer(+Tokens, +UserMemory, +BotMemory, -Answer, -Actions)

get_rule_answer([rule(_, AllAnswers, _, _, _)|Rest], Answer) :- (member(Answer, AllAnswers), !); get_rule_answer(Rest, Answer).
get_rule_action([rule(_, _, Action, _, _)|Rest], Action) :- true.

% metapredicate!!!!
select_answer(_Tokens, _UserMemory, _BotMemory, _Answer, _Actions) :- find_matching_rules(_Tokens, _Rules, _UserMemory, MatchingRules),
																	  bagof((Answer, Freq), 
																	  	(member(Rule, MatchingRules), !,
																	  		get_rule_answer(Rule, Answer), 
																	  		 get_answer(Answer, _BotMemory, Freq),
																	  		 get_rule_action(Rule, _Actions)),
																	  		 PossibleAnswers),
																	  write("PossibleAnswers : "), writeln(PossibleAnswers),
																	  min_element(PossibleAnswers, _Answer).


% Esuează doar daca valoarea exit se afla in lista Actions.
% Altfel, returnează true.
% handle_actions/1
% handle_actions(+Actions)
handle_actions([]) :- true.


% Caută frecvența (numărul de apariți) al fiecarui cuvânt din fiecare
% cheie a memoriei.
% e.g
% ?- find_occurrences(memory{'joc tenis': 3, 'ma uit la box': 2, 'ma uit la un film': 4}, Result).
% Result = count{box:2, film:4, joc:3, la:6, ma:6, tenis:3, uit:6, un:4}.
% Observați ca de exemplu cuvântul tenis are 3 apariți deoarce replica
% din care face parte a fost spusă de 3 ori (are valoarea 3 în memorie).
% Recomandăm pentru usurința să folosiți înca un dicționar în care să tineți
% frecvențele cuvintelor, dar puteți modifica oricum structura, această funcție
% nu este testată direct.

% find_occurrences/2
% find_occurrences(+UserMemory, -Result)
find_occurrences(_UserMemory, _Result) :- fail.

% Atribuie un scor pentru fericire (de cate ori au fost folosit cuvinte din predicatul happy(X))
% cu cât scorul e mai mare cu atât e mai probabil ca utilizatorul să fie fericit.
% get_happy_score/2
% get_happy_score(+UserMemory, -Score)
get_happy_score(_UserMemory, _Score) :- fail.

% Atribuie un scor pentru tristețe (de cate ori au fost folosit cuvinte din predicatul sad(X))
% cu cât scorul e mai mare cu atât e mai probabil ca utilizatorul să fie trist.
% get_sad_score/2
% get_sad_score(+UserMemory, -Score)
get_sad_score(_UserMemory, _Score) :- fail.

% Pe baza celor doua scoruri alege emoția utilizatorul: `fericit`/`trist`,
% sau `neutru` daca scorurile sunt egale.
% e.g:
% ?- get_emotion(memory{'sunt trist': 1}, Emotion).
% Emotion = trist.
% get_emotion/2
% get_emotion(+UserMemory, -Emotion)
get_emotion(_UserMemory, _Emotion) :- fail.

% Atribuie un scor pentru un Tag (de cate ori au fost folosit cuvinte din lista tag(Tag, Lista))
% cu cât scorul e mai mare cu atât e mai probabil ca utilizatorul să vorbească despre acel subiect.
% get_tag_score/3
% get_tag_score(+Tag, +UserMemory, -Score)
get_tag_score(_Tag, _UserMemory, _Score) :- fail.

% Pentru fiecare tag calculeaza scorul și îl alege pe cel cu scorul maxim.
% Dacă toate scorurile sunt 0 tag-ul va fi none.
% e.g:
% ?- get_tag(memory{'joc fotbal': 2, 'joc box': 3}, Tag).
% Tag = sport.
% get_tag/2
% get_tag(+UserMemory, -Tag)
get_tag(_UserMemory, _Tag) :- fail.
