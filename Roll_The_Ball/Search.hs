{-# OPTIONS_GHC -Wall #-}
{-# LANGUAGE MultiParamTypeClasses #-}

module Search where
import Data.List as L
import ProblemState
import RollTheBall()
import Data.Maybe

{-
    A node in our state-space search, contains info regarding :
    lvl - the current state
    action - the move that lead to its state
    depth - its depth in the state-space graph
    daddy - the parent of the current node(may be none)
    offspring - children nodes(where the current node may lead by all 
                possible actions)
-}

data Node s a = Node
    { lvl :: s
    , action :: Maybe a
    , depth :: Int
    , daddy :: Maybe (Node s a)
    , offspring :: [Node s a]
    }

instance (Eq a, Eq s) => Eq (Node s a) where
  (==) (Node l1 _ _ _ _) (Node l2 _ _ _ _) = (l1 == l2)

instance (Show s) => Show (Node s a) where
    show (Node l _ _ _ _) = show l


{-
    Getters used to access node fields
-}
nodeState :: Node s a -> s
nodeState (Node level _ _ _ _) = level

nodeAction :: Node s a -> Maybe a
nodeAction (Node _ act _ _ _) = act

nodeDepth :: Node s a -> Int
nodeDepth (Node _ _ dep _ _) = dep

nodeParent :: Node s a -> Maybe (Node s a)
nodeParent (Node _ _ _ par _) = par

nodeChildren :: Node s a -> [Node s a]
nodeChildren (Node _ _ _ _ children) = children

{-
   generates the tree(successors) of the current node
-}
generateNodeTree :: (ProblemState s a) => Node s a -> Node s a
generateNodeTree root@(Node levl actn dpth parent _) = (Node levl actn dpth parent children)
    where succNodes = [(Node l (Just act) (dpth+1) (Just root) []) | (act, l) <- successors levl]
          children = map generateNodeTree succNodes

{-
    Given a level, generates its state space
-}
createStateSpace :: (ProblemState s a, Eq s) => s -> Node s a
createStateSpace levl = generateNodeTree (Node levl Nothing 0 Nothing [])


nextStep :: (Eq a, Eq s) => ([Node s a], [Node s a]) -> [Node s a] -> [([Node s a], [Node s a])]
nextStep (_, []) _ = [([], [])]
nextStep (_, oldFront) visited = (newAdded, newFront) : (nextStep (newAdded, newFront) newVisited)
    where newAdded = filter (`notElem` visited) (offspring (head oldFront))
          newFront = ((tail oldFront) ++ newAdded)
          newVisited = union visited newAdded

{-
    given a node, returns a stream of pairs that contains :
    - the list of nodes added to the frontier in the current step
    - the current frontier
-}
bfs :: (Eq a, Ord s) => Node s a -> [([Node s a], [Node s a])]
bfs node = ([node], [node]) : (nextStep ([node], [node]) [])



{-
    Receives the initial and final states and returns the node where their frontiers
    intersect
-}

bidirBFS :: (Eq a, Ord s, Show s) => Node s a -> Node s a -> (Node s a, Node s a)
bidirBFS n1 n2 = search (bfs n1) (bfs n2)
    where
    search l1 l2
        |size1 >= 1 = (head elem1, head elem2)
        |size2 >= 1 = (head elem3, head elem4)
        |otherwise = search (tail l1) (tail l2)
        where
        list1 = head l1
        list2 = head l2
        elem1 = [x | x <- (fst list1), x `elem` (snd list2)]
        elem2 = [x | x <- (snd list2), x == (head elem1)]
        elem3 = [x | x <- (snd list1), x `elem` (fst list2)]
        elem4 = [x | x <- (fst list2), x == (head elem3)]
        size1 = length elem1
        size2 = length elem3

{-
    given a node, recreates the path to the root node, by tracking the links 
    to the node parents
-}
extractPath :: Node s a -> [(Maybe a, s)]
extractPath (Node lev act _ par _) = rest ++ [(act, lev)]
        where rest
                |(isNothing par) = []
                |otherwise = extractPath $ fromJust par


reverseAct :: (ProblemState s a) => (Maybe a, s) -> (Maybe a, s)
reverseAct (act, levl)
    |(isNothing act) = (Nothing, levl)
    |otherwise = (Just a1, l1)
        where (a1, l1) = reverseAction (fromJust act, levl)

{- 
    given the initial and final states, creates the problem state space and 
    uses a bidirectional BFS to find the solution, returns a list of states and actions
    that lead us from the initial state to the final winning state
-}
solve :: (ProblemState s a, Ord s, Show s, Eq a)
      => s          -- Starea inițială de la care se pornește
      -> s          -- Starea finală la care se ajunge
      -> [(Maybe a, s)]   -- Lista perechilor
solve start final = firstHalfPath ++ sndHalfPath
    where startState = createStateSpace start
          endState = createStateSpace final
          (stNode, enNode) = bidirBFS startState endState
          firstHalfPath = extractPath stNode
          sndHalfPath = map (reverseAct) $ (reverse $ tail $ extractPath enNode)
