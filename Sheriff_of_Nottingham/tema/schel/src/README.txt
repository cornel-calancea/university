Această arhivă conține implementarea temei 1 la POO - Sheriff of Nottingham.

Structura

4 package-uri :
	- common(conține doar fișierul cu constante)
	- goods(nemodificat)
	- player() :
		- Player
		- Basic(extinde Player, definește comportamentul unui jucător basic)
		- Greedy(extinde Basic, întrucât sunt foarte asemănători în comportament, 
			definește comportamentul unui jucător Greedy)
		- Bribe(extinde Basic deoarece au multe trăsături comune)
		- Strategy(un enum cu toate strategiile)
		- Declaration(conține datele declarate și relevante pentru sheriff la inspecție)
	- main - clasa main și clase legate de cursul jocului în general :
		- Game(câmpuri și metode ce definesc cursul și starea jocului)
		- GameState(conține unificat informații ce definesc starea unui joc)
		- Round(definește mersul unei runde)
		- Turn(definește mersul unui tur(subrundă))

Fluxul jocului e structurat ierarhic prin compunere : Game->Round->Turn(fiecare joc are runde, 
fiecare rundă are subrunde). Astfel se structurează mersul jocului și se deleagă acțiunile 
corespunzătoare pe nivele, respectiv în caz de necesitate va fi mai ușor de modificat mersul unui
joc. Pe fiecare dintre cele 3 nivele se pot efectua modificări la starea jocului(GameState).

Tot ierarhic, dar prin moștenire, sunt structurate și clasele ce definesc jucătorii : 
Player(cea mai generală definire de comportament) -> Basic -> Bribe, Greedy(cel mai specific comportament)