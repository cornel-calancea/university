În mapa dată găsiți implementarea temei 1 la disciplina „Structuri de Date”.

Pentru implementarea funcțiilor de bază de prelucrare a listelor, am mai creat câteva funcții auxiliare cu rolurile respective :

- make_player_copy : returnează un pointer către o zonă proaspăt alocată de memorie în care a creat o copie a jucătorului primit
- get_position : returneaza pointer către jucătorul după care trebuie să inserăm următorul element în listă, luând în considerare criteriul după care se efectuează sortarea
- go_further - verifică dacă trebuie să mergem mai departe în listă când vrem să adăugăm un jucător
- getClubAddress, getPlayerAddress, getInjuredPlayerAddress - găsesc în listă clubul sau jucătorul și returnează pointer către acesta
- add_to_list - deoarece funcția add_player lucrează cu cluburi, o parte din funcționalitatea acesteia am delegat-o către add_to_list, pentru a facilita lucrul cu liste din partea a doua a temei, și a primi opțiunea de a adăuga jucătorii cu sortare atât după poziție, cât și după nume
- add_to_injured_list, remove_injured_player - adaugă, respectiv elimină din listă jucătorul accidentat

În fișierul TeamExtractor.h am adăugat următoarele funcții auxiliare :
- union_lists - realizează reuniunea a două liste
- find_position - găsește primul jucător în poziția cerută dintr-un anumit club(dacă acesta există)
- compare_attributes - primește pointeri la doi jucători și îi compară după scor, iar în cazul în care au același scor, după nume
- best - primește pointeri la doi jucători : unul în care este stocată adresa celui mai bun jucător găsit până la moment, și al doilea în care este stocată adresa jucătorului cu care comparăm. După prelucrare, în variabila ”salah” se vor stoca atributele celui mai bun jucător

Memoria alocată dinamic este eliberată în totalitate, erori în Valgrind nu sunt.