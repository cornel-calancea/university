{-# OPTIONS_GHC -Wall #-}
{-# LANGUAGE EmptyDataDecls, MultiParamTypeClasses,
             TypeSynonymInstances, FlexibleInstances,
             InstanceSigs #-}
module RollTheBall where
import Pipes
import ProblemState
import Data.Array as A

{-
    Direcțiile în care se poate mișca o piesa pe tablă
-}

data Directions = North | South | West | East
    deriving (Show, Eq, Ord)

{-
    Sinonim tip de date pentru reprezetarea unei perechi (Int, Int)
    care va reține coordonatele celulelor de pe tabla de joc
-}

type Position = (Int, Int)

{-
    Tip de date pentru reprezentarea celulelor tablei de joc
    - Are pozitie
    - Poate fi fixată/nu
    - Are un Pipe
-}
data Cell = Char
pointsEast :: [Char]
pointsEast = [horPipe, topLeft, botLeft, startRight, winRight]

pointsWest :: [Char]
pointsWest = [horPipe, botRight, topRight, startLeft, winLeft]

pointsNorth :: [Char]
pointsNorth = [verPipe, botLeft, botRight, startUp, winUp]

pointsSouth :: [Char]
pointsSouth = [verPipe, topLeft, topRight, startDown, winDown]
{-
    Tip de date pentru reprezentarea nivelului curent
-}
data Level = MakeLevel
    {gameMap :: (A.Array (Int, Int) Char)
    , mapBounds :: Position
    , startCell :: Position
    , endCell :: Position}
    deriving (Eq, Ord)
{-
    *** Optional *** 
  
    Dacă aveți nevoie de o funcționalitate particulară,
    instantiați explicit clasele Eq și Ord pentru Level.
    În cazul acesta, eliminați deriving (Eq, Ord) din Level.
-}

{-
    *** TODO ***

    Instanțiati Level pe Show. 
    Atenție! Fiecare linie este urmată de \n (endl in Pipes).
-}

instance Show Level 
    where show (MakeLevel matrix _ _ _) = '\n' : elems matrix

{--instance Show Cell
    where show (CellCons c) = show c--}

{-
    *** TODO ***
    Primește coordonatele colțului din dreapta jos a hărții.
    Intoarce un obiect de tip Level în care tabla este populată
    cu EmptySpace. Implicit, colțul din stânga sus este (0,0)
-}

emptyLevel :: Position -> Level
{--emptyLevel pos = A.array ((0, 0), pos) [((x, y), CellCons emptyCell) | x <- [0..fst pos], y <- [0..snd pos]]-}
emptyLevel pos = MakeLevel emptyArr pos (-1, -1) (-1, -1)
    where emptyArr = A.array ((0, 0), (fst pos, 1 + snd pos)) [((x, y), someChar) | x <- [0..fst pos], y <- [0..1+(snd pos)], let someChar = if y == 1+(snd pos) then '\n' else emptySpace]

{-
    *** TODO ***

    Adaugă o celulă de tip Pipe în nivelul curent.
    Parametrul char descrie tipul de tile adăugat: 
        verPipe -> pipe vertical
        horPipe -> pipe orizontal
        topLeft, botLeft, topRight, botRight -> pipe de tip colt
        startUp, startDown, startLeft, startRight -> pipe de tip initial
        winUp, winDown, winLeft, winRight -> pipe de tip final
    Parametrul Position reprezintă poziția de pe hartă la care va fi adaugată
    celula, dacă aceasta este liberă (emptySpace).
-}

addCell :: (Char, Position) -> Level -> Level
addCell (x, position) lvl@(MakeLevel matrix bound start end)
    |outOfBounds position bound == True = lvl
    |(x `elem` winningCells) == True = MakeLevel (matrix A.// [(position, x)]) bound start position
    |(x `elem` startCells) == True = MakeLevel (matrix A.// [(position, x)]) bound position end
    |otherwise = MakeLevel (matrix A.// [(position, x)]) bound start end


{-
    *** TODO *** 

    Primește coordonatele colțului din dreapta jos al hărții și o listă de 
    perechi de tipul (caracter_celulă, poziția_celulei).
    Întoarce un obiect de tip Level cu toate celeule din listă agăugate pe
    hartă.
    Observatie: Lista primită ca parametru trebuie parcursă de la dreapta 
    la stanga.
-}
 
createLevel :: Position -> [(Char, Position)] -> Level
createLevel bound blocks = foldr addCell (emptyLevel bound) blocks


{-
    *** TODO ***

    Mișcarea unei celule în una din cele 4 direcții 
    Schimbul se poate face doar dacă celula vecină e goală (emptySpace).
    Celulele de tip start și win sunt imutabile.

    Hint: Dacă nu se poate face mutarea puteți lăsa nivelul neschimbat.
-}

outOfBounds :: Position -> Position -> Bool
outOfBounds (posX, posY) (boundX, boundY)
    |(posX >= 0 && posX <= boundX) && (posY >= 0 && posY <= boundY) = False
    |otherwise = True

canMove :: Position -> Directions -> Level -> Bool
canMove (x, y) dir (MakeLevel gameMatrix bound _ _)
    {-Start cells, winning and empty spaces cells cannot move-}
    |(gameMatrix A.! (x, y)) `elem` (emptySpace : startCells ++ winningCells) = False
    |dir == East && (outOfBounds (x, y+1) bound) == True = False
    |dir == East && ((gameMatrix A.! (x, y+1)) /= emptySpace) = False
    |dir == West && (outOfBounds (x, y-1) bound) == True = False
    |dir == West && ((gameMatrix A.! (x, y-1)) /= emptySpace) = False
    |dir == North && (outOfBounds (x-1, y) bound) == True = False
    |dir == North && ((gameMatrix A.! (x-1, y)) /= emptySpace) = False
    |dir == South && (outOfBounds (x+1, y) bound) == True = False
    |dir == South && ((gameMatrix A.! (x+1, y)) /= emptySpace) = False
    |otherwise = True

moveCell :: Position -> Directions -> Level -> Level
moveCell pos@(x, y) dir lvl@(MakeLevel gameMatrix bound start end)
    |outOfBounds pos bound == True = lvl {-Pozitia de schimbat e invalida-}
    |canMove pos dir lvl == False = lvl
    |dir == North = MakeLevel (emptyMat A.// [((x-1, y), myElem)]) bound start end
    |dir == South = MakeLevel (emptyMat A.// [((x+1, y), myElem)]) bound start end
    |dir == East = MakeLevel (emptyMat A.// [((x, y+1), myElem)]) bound start end
    |dir == West = MakeLevel (emptyMat A.// [((x, y-1), myElem)]) bound start end
    |otherwise = lvl
    where
        myElem = gameMatrix A.! pos
        emptyMat = gameMatrix A.// [(pos, emptySpace)]

{-
    *** HELPER ***

    Verifică dacă două celule se pot conecta.
    Atenție: Direcția indică ce vecin este a
    doua celulă pentru prima.

    ex: connection botLeft horPipe East = True (╚═)
        connection horPipe botLeft East = False (═╚)
-}
{-+ de verificat daca nu a dat in margine-}
connection :: Char -> Char -> Directions -> Bool
connection p1 p2 dir
    |dir == East = (p1 `elem` pointsEast) && (p2 `elem` pointsWest)
    |dir == West = (p1 `elem` pointsWest) && (p2 `elem` pointsEast)
    |dir == North = (p1 `elem` pointsNorth) && (p2 `elem` pointsSouth)
    |otherwise = (p1 `elem` pointsSouth) && (p2 `elem` pointsNorth)

{--isNeighbor :: Position -> Position -> Directions -> Bool
isNeighbor (row1, col1) (row2, col2) dir
    |dir == East = (row1 == row2) && (col1 + 1 == col2)
    |dir == West = (row1 == row2) && (col1 - 1 == col2)
    |dir == North = (row1 - 1 == row2) && (col1 == col2)
    |dir == South = (row1 + 1 == row2) && (col1 == col2)
    |otherwise = False -}

{-receives the position, the direction and map bounds-}
hasNeighbor :: Position -> Directions -> Position -> Bool
hasNeighbor (row, col) dir bound
    |dir == East = ((outOfBounds (row, col+1) bound) == False)
    |dir == West = ((outOfBounds (row, col-1) bound) == False)
    |dir == North = ((outOfBounds (row-1, col) bound) == False)
    |dir == South = ((outOfBounds (row+1, col) bound) == False)
    |otherwise = False

getNeighbor :: Position -> Directions -> Position
getNeighbor (row, col) dir
    |(dir == East) = (row, col+1)
    |(dir == West) = (row, col-1)
    |(dir == North) = (row-1, col)
    |(dir == South) = (row+1, col)
    |otherwise = undefined

{-primeste directia din care pipe-ul primit este vecin-}
getNextDir :: Char -> Directions -> Directions
getNextDir pipe dir
    |(pipe == verPipe) || (pipe == horPipe) = dir
    |(pipe == topLeft) && (dir == West)= South
    |(pipe == topLeft) && (dir == North) = East
    |(pipe == botLeft) && (dir == South) = East
    |(pipe == botLeft) && (dir == West) = North
    |(pipe == botRight) && (dir == East) = North
    |(pipe == botRight) && (dir == South) = West
    |(pipe == topRight) && (dir == East) = South
    |(pipe == topRight) && (dir == North) = West
    |(pipe == startUp) = North
    |(pipe == startDown) = South
    |(pipe == startLeft) = West
    |(pipe == startRight) = East
    |otherwise = undefined

{-primeste pozitia de start, vecinul-}
isContinuousPath :: Position -> Directions -> Level -> Bool
isContinuousPath pos dir lvl@(MakeLevel gameMatrix bound _ _)
    |(cell1 `elem` winningCells) = True
    |(hasNeighbor pos dir bound) && (connection cell1 nextCell dir) = isContinuousPath neighbor nextDir lvl
    |otherwise = False
    where   cell1 = gameMatrix A.! pos
            neighbor = getNeighbor pos dir
            nextCell = gameMatrix A.! neighbor
            nextDir = getNextDir nextCell dir

{-
    *** TODO ***

    Va returna True dacă jocul este câștigat, False dacă nu.
    Va verifica dacă celulele cu Pipe formează o cale continuă de la celula
    de tip inițial la cea de tip final.
    Este folosită în cadrul Interactive.
-}
reverseDir :: Directions -> Directions
reverseDir dir
        |dir == East = West
        |dir == West = East
        |dir == North = South
        |otherwise = North

wonLevel :: Level -> Bool
wonLevel lvl@(MakeLevel gameMatrix _ start _) = isContinuousPath start (getNextDir startPipe East) lvl
    where startPipe = gameMatrix A.! start

instance ProblemState Level (Position, Directions) where
    successors lvl@(MakeLevel _ (maxRow, maxCol) _ _) = [((p1, d1), l1) | let positions = [(x, y) | x <- [0..maxRow], y<-[0..maxCol]], p1 <- positions, d1 <- [East, West, North, South], canMove p1 d1 lvl == True, let l1 = moveCell p1 d1 lvl]
    isGoal = wonLevel
    reverseAction ((pos, dir), lvl) = ((getNeighbor pos dir, reverseDir dir), moveCell (getNeighbor pos dir) (reverseDir dir) lvl)
