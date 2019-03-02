import numpy as np
import time

# Infinity for search bounds
INF = float('inf')
# Constants to help maneuvering the board
UP = np.array([-1,0], dtype=int)
DN = np.array([1,0], dtype=int)
LF = np.array([0,-1], dtype=int)
RT = np.array([0,1], dtype=int)
SIGN = np.array([0,1,-1], dtype=int)

class Counter:
    def __init__(self): self.count = 0
    def inc(self): self.count += 1
    def reset(self): self.count = 0
    def __int__(self): return self.count
    def __str__(self): return str(self.count)

class Timer:
    def __init__(self): self.t = 0
    def start(self): self.t = time.time()
    def stop(self): self.t = time.time() - self.t
    def time(self): return self.t
    def __str__(self): return str(self.t)


CNT = Counter()
TMR = Timer()

# Create a new grid
def init():
    g = np.zeros((8,8), dtype=int)
    g[0:2, :] = 2
    g[6:8, :] = 1
    return g

# Check if the square is on the grid
def onGrid(sqr):
    r,c = sqr
    return r > -1 and c > -1 and r < 8 and c < 8

# Returns the number of the opposing player
def opps(player): return 2 if player == 1 else 1

# Returns the direction that is forward for that player
def forw(player): return UP if player == 1 else DN

# Get the piece at the given square on the grid
def get(g, sqr): return g[sqr[0], sqr[1]]

# Place a piece or a blank at the given square on the grid
def put(g, sqr, v): g[sqr[0], sqr[1]] = v

# Returns the winner if there is one
def winner(gr):
    if 1 in gr[0, :] or 2 not in gr: return 1
    if 2 in gr[7, :] or 1 not in gr: return 2

# Returns all legal moves for the piece at the given square
def pceMoves(gr, sqr):
    p = get(gr, sqr)
    if not p: return []
    fw = forw(p)
    front = sqr+fw
    moves = [sqr+fw+LF, sqr+fw+RT]
    if onGrid(front) and not get(gr, front): moves.append(front)
    return [m for m in moves if onGrid(m) and get(gr,m) != p]

# Returns all the legal moves for the given player
def allMoves(gr, player):
    return [(np.array(sqr, dtype=int), m)
            for sqr, p in np.ndenumerate(gr)
            for m in pceMoves(gr, sqr) if player == p]

# Plays the move on a copy of the board, and returns the new board and the evaluation score.
def moveScore(gr, m, t, h):
    gc = move(gr, m[0], m[1])
    score = SIGN[t] * h(gc)
    return (gc, score)

# Copies the board and plays the given move on it, and returns the copy
def move(gr, s, e):
    gc = np.copy(gr)
    put(gc, e, get(gc, s))
    put(gc, s, 0)
    return gc

# Minimax as one function using the Negamax variant
# Takes the grid, turn (player), current depth, and heuristic function (evaluation)
def negamax(gr, t, d, h):
    CNT.inc()
    if winner(gr) or not d: return (None, SIGN[t] * h(gr))
    bestVal = -INF
    bestMove = None
    for m in allMoves(gr, t):
        gnew = move(gr, m[0], m[1])
        _, val = negamax(gnew, opps(t), d-1, h)
        val = -val
        if val > bestVal:
            bestVal = val
            bestMove = gnew
    return (bestMove, bestVal)

# Alpha-beta as one function using the Negamax variant
# Takes the grid, turn (player), current depth, and heuristic function (evaluation)
def alphabeta(gr, t, d, a, b, h):
    CNT.inc()
    if winner(gr) or not d: return (None, SIGN[t] * h(gr))
    bestVal = -INF
    bestMove = None
    moves = [moveScore(gr,m,t,h) for m in allMoves(gr,t)]
    moves.sort(key=lambda x: x[1], reverse=True)
    for gnew,_ in moves:
        _, val = alphabeta(gnew, opps(t), d-1, -b, -a, h)
        val = -val
        if val > bestVal:
            bestVal = val
            bestMove = gnew
        a = max(a, val)
        if a >= b: break
    return (bestMove, bestVal)

# Weights for evaluation functions
W_PCS = 2
W_COV = 2
W_WIN = 20

W_DST = 1
W_ATK = 4
W_DNG = 8

W_DEF = 4
W_GAP = 4
W_CON = 6

# The offensive heuristic
def offense(gr):
    win = dst = cov = dng = atk = pcs = 0
    for (r,c), p in np.ndenumerate(gr):
        if not p: continue
        if p == 1:
            pcs += 1
            dst += (8-r)
            if r == 0: win += 1
            if r == 1: dng += 1
            for m in pceMoves(gr, (r,c)):
                cov += 1
                if get(gr, m) == 2: atk += 1
        else:
            pcs -= 1
            dst -= r
            if r == 7: win -= 1
            if r == 6: dng -= 1
            for m in pceMoves(gr, (r,c)):
                cov -= 1
                if get(gr, m) == 1: atk -= 1

    return dst*W_DST + cov*W_COV + dng*W_DNG + atk*W_ATK + pcs * W_PCS + win * W_WIN

# The defensive heuristic
def defense(gr):
    win = con = dst = cov = defn = pcs = 0
    cols1 = set([])
    cols2 = set([])
    for sq, p in np.ndenumerate(gr):
        if not p: continue
        cross = [sq+UP, sq+DN, sq+LF, sq+RT]
        if p == 1:
            pcs += 1
            dst -= (8-sq[0])
            cols1.add(sq[1])
            if sq[0] == 0: win += 1
            con += len([s for s in cross if onGrid(s) and get(gr,s) == 1])
            for m in pceMoves(gr, sq):
                cov += 1
                if get(gr, m) == 1: defn += 1
        else:
            pcs -= 1
            dst += sq[0]
            cols2.add(sq[1])
            if sq[0] == 7: win -= 1
            con -= len([s for s in cross if onGrid(s) and get(gr,s) == 2])
            for m in pceMoves(gr, sq):
                cov -= 1
                if get(gr, m) == 2: defn -= 1

    cols = len(cols1) - len(cols2)
    return con*W_CON + cov*W_COV + defn*W_DEF + cols*W_GAP + pcs*W_PCS + dst*W_DST + win*W_WIN

# A dummy evaluation function
def eval(gr):
    return np.where(gr == 1)[0].size - np.where(gr == 2)[0].size


# Runs minimax given the grid, turn, and heuristic
def runMM(gr, t, h):
    TMR.start()
    bestG, _ = negamax(gr, t, 3, h)
    TMR.stop()
    return bestG

# Runs alpha-beta given the grid, turn, and heuristic
def runAB(gr,t, h):
    TMR.start()
    bestG, _ = alphabeta(gr, t, 3, -INF, INF, h)
    TMR.stop()
    return bestG

# Prints stats of the final board state
def printStats(g, ti, moves, n1, n2):
    caps = (16 - np.where(g == 2)[0].size, 16 - np.where(g == 1)[0].size)
    print(g,'\n')
    print('The winner is P%d!' % winner(g))
    print('Total moves:', moves)
    print('Avg nodes expanded per move: %d' % (float(n1+n2) / moves) )
    print('Avg time per move: %ss' % (ti / float(moves)) )
    print('Nodes expanded:')
    print('\tP1: %d' % n1)
    print('\tP2: %d' % n2)
    print('Pieces captured')
    print('\tP1: %d' % caps[0])
    print('\tP2: %d' % caps[1])

# Runs a game given the search type for each player and their strategy as dictionaries
def run(types, strats):
    total_moves = total_time = 0
    nodes = [0, 0, 0]

    t = 1
    g = init()
    while not winner(g):
        CNT.reset()
        g = types[t](g,t, strats[t])
        total_time += TMR.time()
        total_moves += 1
        nodes[t] += CNT.__int__()
        t = opps(t)

    printStats(g, total_time, total_moves, nodes[1], nodes[2])

# run({1:runMM, 2:runMM}, {1:offense, 2:defense})
# run({1:runAB, 2:runAB}, {1:defense, 2:offense})
# run({1:runMM, 2:runAB}, {1:offense, 2:offense})
# run({1:runAB, 2:runMM}, {1:defense, 2:defense})



# run({1:runAB, 2:runAB}, {1:offense, 2:defense})
# run({1:runAB, 2:runAB}, {1:defense, 2:offense})
# run({1:runAB, 2:runAB}, {1:offense, 2:offense})
# run({1:runAB, 2:runAB}, {1:defense, 2:defense})
