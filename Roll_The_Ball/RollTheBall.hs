{-# OPTIONS_GHC -Wall #-}
{-# LANGUAGE EmptyDataDecls, MultiParamTypeClasses,
             TypeSynonymInstances, FlexibleInstances,
             InstanceSigs #-}
module RollTheBall where
import Pipes
import ProblemState
import Data.Array as A

{-
    Directions in which a block can move
-}

data Directions = North | South | West | East
    deriving (Show, Eq, Ord)


type Position = (Int, Int)

{-Data type for representing a cell-}
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
    Data type for representing a level
-}
data Level = MakeLevel
    {gameMap :: (A.Array (Int, Int) Char)
    , mapBounds :: Position
    , startCell :: Position
    , endCell :: Position}
    deriving (Eq, Ord)


{-
   displays the current state of the level
-}

instance Show Level 
    where show (MakeLevel matrix _ _ _) = '\n' : elems matrix

{-
    Function that receives the coordinates of the lower-right corner
    and returns a Level filled with EmptySpace. The upper left corner is (0, 0)
-}

emptyLevel :: Position -> Level
{--emptyLevel pos = A.array ((0, 0), pos) [((x, y), CellCons emptyCell) | x <- [0..fst pos], y <- [0..snd pos]]-}
emptyLevel pos = MakeLevel emptyArr pos (-1, -1) (-1, -1)
    where emptyArr = A.array ((0, 0), (fst pos, 1 + snd pos)) [((x, y), someChar) | x <- [0..fst pos], y <- [0..1+(snd pos)], let someChar = if y == 1+(snd pos) then '\n' else emptySpace]

{-
    Function that adds a Pipe to the level, in case the given position is free(emptySpace).
-}

addCell :: (Char, Position) -> Level -> Level
addCell (x, position) lvl@(MakeLevel matrix bound start end)
    |outOfBounds position bound == True = lvl
    |(x `elem` winningCells) == True = MakeLevel (matrix A.// [(position, x)]) bound start position
    |(x `elem` startCells) == True = MakeLevel (matrix A.// [(position, x)]) bound position end
    |otherwise = MakeLevel (matrix A.// [(position, x)]) bound start end


{-
    Receives the coordinates of the lower right corner of the map and a
    list of Pipes to be added. Returns the Level of the given bounds, and
    with the given Pipes added to it.
-}
 
createLevel :: Position -> [(Char, Position)] -> Level
createLevel bound blocks = foldr addCell (emptyLevel bound) blocks


{-
    Receives a position, and the bounds of the level, returns true if the
        given position is out of the level's bounds.
-}

outOfBounds :: Position -> Position -> Bool
outOfBounds (posX, posY) (boundX, boundY)
    |(posX >= 0 && posX <= boundX) && (posY >= 0 && posY <= boundY) = False
    |otherwise = True

{-
    Given a position, a direction, and a level, checks whether the pipe
    at the given position can move in the given direction.
-}
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

{-
    Moves the cell in the given position in the given direction, if possible
-}
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
    Given two Pipes(p1, and p2), and a direction(dir), checks whether there is
    a connection from p1 to p2 by going dir
-}
connection :: Char -> Char -> Directions -> Bool
connection p1 p2 dir
    |dir == East = (p1 `elem` pointsEast) && (p2 `elem` pointsWest)
    |dir == West = (p1 `elem` pointsWest) && (p2 `elem` pointsEast)
    |dir == North = (p1 `elem` pointsNorth) && (p2 `elem` pointsSouth)
    |otherwise = (p1 `elem` pointsSouth) && (p2 `elem` pointsNorth)

{-
    receives the position, the direction and map bounds, checks whether
    the pipe in the given position has a neighbor in the given direction
-}
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

{-primeste directia din care pipe-ul primit este vecin
    receives a Pipe and the direction by which it was reached,
    and returns the direction where it leads
    (ex. if we have a topLeft pipe(╔) as northern neighbor,
    it will lead us to the East)
-}
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

{-
    given the start position and the direction where the start pipe
    leads, checks whether there is a continuous path from start to end
-}
isContinuousPath :: Position -> Directions -> Level -> Bool
isContinuousPath pos dir lvl@(MakeLevel gameMatrix bound _ _)
    |(cell1 `elem` winningCells) = True
    |(hasNeighbor pos dir bound) && (connection cell1 nextCell dir) = isContinuousPath neighbor nextDir lvl
    |otherwise = False
    where   cell1 = gameMatrix A.! pos
            neighbor = getNeighbor pos dir
            nextCell = gameMatrix A.! neighbor
            nextDir = getNextDir nextCell dir


reverseDir :: Directions -> Directions
reverseDir dir
        |dir == East = West
        |dir == West = East
        |dir == North = South
        |otherwise = North

{-
    given a level, checks whether it is in a winning state
-}
wonLevel :: Level -> Bool
wonLevel lvl@(MakeLevel gameMatrix _ start _) = isContinuousPath start (getNextDir startPipe East) lvl
    where startPipe = gameMatrix A.! start

instance ProblemState Level (Position, Directions) where
    successors lvl@(MakeLevel _ (maxRow, maxCol) _ _) = [((p1, d1), l1) | let positions = [(x, y) | x <- [0..maxRow], y<-[0..maxCol]], p1 <- positions, d1 <- [East, West, North, South], canMove p1 d1 lvl == True, let l1 = moveCell p1 d1 lvl]
    isGoal = wonLevel
    reverseAction ((pos, dir), lvl) = ((getNeighbor pos dir, reverseDir dir), moveCell (getNeighbor pos dir) (reverseDir dir) lvl)
