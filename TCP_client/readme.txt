©Corneliu Calancea, 2020
Tema 2 - Protocoale de Comunicație
-----------------------------------------------------------------------------------
Această arhivă conține implementarea temei.
În linii generale, am structurat problema după cum urmează :
- serverul face în totalitate prelucrarea mesajelor, detectarea erorilor
- clientul doar se ocupă de afișare, cunoscând protocolul utilizat deasupra TCP
-----------------------------------------------------------------------------------
Protocol utilizat deasupra TCP :
- primii 4 octeți reprezintă un int cu dimensiunea mesajului ce urmează
- urmează datele în sine, după dimensiunea specificată poate urma alt int și tot așa mai departe
-----------------------------------------------------------------------------------
Serverul :
- menține o listă a tuturor clienților(este eliberată doar la deconectarea serverului)
- menține o listă de topicuri, fiecare topic are și o listă de subscriberi, actualizată încontinuu

Când primește un mesaj de pe socketul UDP, folosește o funcție handler care prelucrează mesajul(îl duce în forma finală pentru client), și ulterior trimite acest mesaj. Similar și pentru mesajele TCP - sunt repartizate unei funcții care verifică posibile cazuri de mesaj prost format, iar dacă e un mesaj valid - retrimite către funcțiile de subscribe/unsubscribe.
-----------------------------------------------------------------------------------
Store&Forward

Mesajele pending le stochez în fișiere. La pornirea serverului, acesta crează un director ”pending”, care la început este gol. În cazul în care trebuie trimise mesaje către un client inactiv, acesta le atașează într-un fișier ce are ca nume ID-ul clientului. Mesajele sunt stocate în forma prelucrată - când clientul devine activ, acestea sunt trimise direct.

