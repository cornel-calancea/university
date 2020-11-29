:- ensure_loaded('chat.pl').

% Returneaza true dacă regula dată ca argument se potriveste cu
% replica data de utilizator. Replica utilizatorului este
% reprezentata ca o lista de tokens. Are nevoie de
% memoria replicilor utilizatorului pentru a deduce emoția/tag-ul
% conversației.
% match_rule/3
% match_rule(_Tokens, _UserMemory, rule(_, _, _, _, _)) :- fail.

match_rule(Tokens, _, rule(Pattern, _, _, _, _)) :- Pattern = Tokens.


% Primeste replica utilizatorului (ca lista de tokens) si o lista de
% reguli, iar folosind match_rule le filtrează doar pe cele care se
% potrivesc cu replica dată de utilizator.
% find_matching_rules/4
% find_matching_rules(+Tokens, +Rules, +UserMemory, -MatchingRules)

find_matching_rules(Tokens, Rules, UserMemory, MatchingRules) :- findall(Rule, (member(Rule, Rules), match_rule(Tokens, UserMemory, Rule)), MatchingRules).

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

get_rule_answer(rule(_, AllAnswers, _, _, _), Answer) :- member(Answer, AllAnswers).
get_rule_action(rule(_, _, Action, _, _), Action) :- true.
get_rule_emotion(rule(_, _, _, Emotion, _), Emotion) :- true.
get_rule_tag(rule(_, _, _, _, Tag), Tag) :- true.

% metapredicate!!!!
select_answer(Tokens, UserMemory, BotMemory, Answer, Actions) :- rules(TokSet, Rules), ord_subset(TokSet, Tokens), !,
																		find_matching_rules(Tokens, Rules, UserMemory, MatchingRules),

																		%Daca exista raspuns corespunzator emotiei - il aleg pe el
																		(get_emotion(UserMemory, Emotion),
																			(bagof((Answer, Freq), 
																	  	(member(Rule, MatchingRules),
																	  		get_rule_answer(Rule, Answer), 
																	  		 get_answer(Answer, BotMemory, Freq),
																	  		 get_rule_action(Rule, Actions),
																	  		 get_rule_emotion(Rule, [Emotion])),
																	  		 EmotionAnswers),
																		EmotionAnswers \= [],
																		min_element(EmotionAnswers, Answer));

																		%Daca exista raspuns corespunzator subiectului - il aleg pe el
																		(get_tag(UserMemory, Tag),
																			bagof((Answer, Freq), 
																	  	(member(Rule, MatchingRules),
																	  		get_rule_answer(Rule, Answer), 
																	  		 get_answer(Answer, BotMemory, Freq),
																	  		 get_rule_action(Rule, Actions),
																	  		 get_rule_tag(Rule, [Tag])),
																	  		 TagAnswers),
																		TagAnswers \= [],
																		min_element(TagAnswers, Answer));

																	  %Altfel - aleg un raspuns fara tag si emotie
																	  (bagof((Answer, Freq), 
																	  	(member(Rule, MatchingRules),
																	  		get_rule_answer(Rule, Answer), 
																	  		 get_answer(Answer, BotMemory, Freq),
																	  		 get_rule_action(Rule, Actions),
																	  		 get_rule_emotion(Rule, []),
																	  		 get_rule_tag(Rule, [])),
																	  		 PossibleAnswers),
																	  min_element(PossibleAnswers, Answer))).


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

% Functie ajutatoare care adauga in dictionar Count la numarul de aparitii
% al lui Word, si intoarce dictionarul actualizat
add_occurences(Word, Count, Memory, NewMemory) :-
	(Val = Memory.get(Word), !; Val = 0),
	NewVal is Val + Count,
	NewMemory = Memory.put(Word, NewVal).

% Functie care primeste o lista care contine duplicate
% cu cuvinte si frecventele lor. Le adauga pe rand intr-un dictionar
% si elimina duplicatele, si sumeaza frecventele de aparitie
find_occ_helper([], _, Result, Result).
find_occ_helper([(Word, Freq)|Tail], UserMemory, Dict, Result) :- add_occurences(Word, Freq, Dict, NewDict),
														  		  find_occ_helper(Tail, UserMemory, NewDict, Result).

find_occurrences(UserMemory, Result) :-	findall((Word, Freq), 
											(Freq = UserMemory.get(Reply),
											words(Reply, List), member(Word, List)), MyList),
											find_occ_helper(MyList, UserMemory, memory{}, Result).


% Atribuie un scor pentru fericire (de cate ori au fost folosit cuvinte din predicatul happy(X))
% cu cât scorul e mai mare cu atât e mai probabil ca utilizatorul să fie fericit.
% get_happy_score/2
% get_happy_score(+UserMemory, -Score)

% Functie ce calculeaza suma elementelor din lista
sum_list([], 0).
sum_list([H|T], Sum) :-
   sum_list(T, Rest),
   Sum is H + Rest.

get_happy_score(UserMemory, Score) :- find_occurrences(UserMemory, WordOcc),
										dict_keys(WordOcc, Words),
										findall(Count, (member(Word, Words), happy(Word), Count = WordOcc.get(Word)), HappyWordsCount),
										sum_list(HappyWordsCount, Score).

% Atribuie un scor pentru tristețe (de cate ori au fost folosit cuvinte din predicatul sad(X))
% cu cât scorul e mai mare cu atât e mai probabil ca utilizatorul să fie trist.
% get_sad_score/2
% get_sad_score(+UserMemory, -Score)
get_sad_score(UserMemory, Score) :- find_occurrences(UserMemory, WordOcc),
										dict_keys(WordOcc, Words),
										findall(Count, (member(Word, Words), sad(Word), Count = WordOcc.get(Word)), SadWordsCount),
										sum_list(SadWordsCount, Score).


% Pe baza celor doua scoruri alege emoția utilizatorul: `fericit`/`trist`,
% sau `neutru` daca scorurile sunt egale.
% e.g:
% ?- get_emotion(memory{'sunt trist': 1}, Emotion).
% Emotion = trist.
% get_emotion/2
% get_emotion(+UserMemory, -Emotion)
get_emotion(UserMemory, Emotion) :- get_happy_score(UserMemory, HappyScore),
									  get_sad_score(UserMemory, SadScore),
									  ((HappyScore > SadScore, Emotion = fericit);
									  	(HappyScore < SadScore, Emotion = trist);
									  	(HappyScore == SadScore, Emotion = neutru)).

% Atribuie un scor pentru un Tag (de cate ori au fost folosit cuvinte din lista tag(Tag, Lista))
% cu cât scorul e mai mare cu atât e mai probabil ca utilizatorul să vorbească despre acel subiect.
% get_tag_score/3
% get_tag_score(+Tag, +UserMemory, -Score)
get_tag_score(Tag, UserMemory, Score) :- find_occurrences(UserMemory, WordOcc),
										dict_keys(WordOcc, Words),
										findall(Count, (member(Word, Words), tag(Tag, TagList), member(Word, TagList), Count = WordOcc.get(Word)), TagWordsCount),
										sum_list(TagWordsCount, Score).

% Pentru fiecare tag calculeaza scorul și îl alege pe cel cu scorul maxim.
% Dacă toate scorurile sunt 0 tag-ul va fi none.
% e.g:
% ?- get_tag(memory{'joc fotbal': 2, 'joc box': 3}, Tag).
% Tag = sport.
% get_tag/2
% get_tag(+UserMemory, -Tag)
get_tag(UserMemory, Tag) :- get_tag_score(sport, UserMemory, SportScore),
									  get_tag_score(film, UserMemory, FilmScore),
									  ((SportScore > FilmScore, Tag = sport);
									  	(SportScore < FilmScore, Tag = film);
									  	(SportScore == FilmScore, Tag = none)).
