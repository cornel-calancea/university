©Corneliu Calancea, 2019
Arhiva dată conține implementarea temei 1 la IOCLA - Prefix AST.

Odată citită în getAST, adresa rădăcinii este trimisă funcției ”evaluate”,
care, după cum sugerează numele, evaluează rezultatul tuturor operațiilor
dintr-un arbore.
 
Procedura evaluării : 
- Se verifică dacă în secțiunea ”data” a structurii se află un operator sau un operand,
  cu ajutorul funcției ”isOperand”;
- Dacă e operand, string-ul este convertit în integer cu ajutorul funcției ”toInt”;
- Dacă e operator, se apelează recursiv ”evaluate” pentru ramura stângă, se pune pe 
  stivă rezultatul, apoi se apelează și pentru ramura dreaptă;
- După ce au fost evaluate ambele ramuri, se decide ce operație aritmetică trebuie
  efectuată pe cei doi operanzi;
- Se efectuează operația și se întoarce rezultatul în EAX.

În interiorul ”evaluate” sunt apelate funcțiile ”toInt” și ”isOperand”. Acestea lucrează
în felul următor :

toInt - convertește un string în număr întreg :
- verifică dacă primul caracter e ”-”, rezultatul verificării e stocat în EDX și pus pe 
  stivă;
- Se calculează modulul numărului. Fiecare caracter(până la întâlnirea null terminator)
  se convertește în valoarea sa numerică și se adaugă la EAX. Cel din urmă este înmulțit
  cu 10 la fiecare pas(este deplasat cu o poziție la stânga în zecimal).
- Când se întâlnește null terminator, se ia din stivă semnul numărului, și dacă e ”-”,
  se neagă numărul(prin operația neg) și se întoarce rezultatul

isOperand - verifică dacă un string reprezintă un operand(număr) - întoarce 1=Da sau 0=Nu
- se verifică ultimul caracter al string-ului primit, dacă e semnul unei operații, se
  întoarce 0, dacă e caracterul ASCII pentru un număr - se întoarce 1.
Am ales să verific ultimul caracter pentru că operația de scădere și numerele negative 
încep ambele cu ”-”, ceea ce complică procesul de verificare.




