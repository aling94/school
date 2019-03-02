# By Alvin Ling
# CS440 - MP2 Part 1 - CPS SUDOKU
# Variables: Words
# Domain: (Orientation, Row, Column) = {H,V} X {0, ... , 8} X {0, ... , 8}
# Constraints:
#   AlLdiff(rows), Alldiff(columns), Alldiff(divisions)
#       - Division refers to the 3x3 blocks that the grid can be divided into
#   X(i,j) =/= "" for i,j : [0,8]

class Codedoku:

    def __init__(self, gfile, wfile):
        self.backtracks = 0     # Number of backtrack calls
        self.grid = []          # Word grid
        self.actions = []       # Stack of remembering actions (assignments and pruned domains)
        self.overlaps = []      # Track how many overlaps on a specific square
        self.assignOrder = []   # Track assignment order of words and location spaced
        self.assigned = set([]) # Track words assigned so far in the search

        # Constraints are what letters have been used in a specific row, column, or division, as well as any hints
        self.constraints = {'row': [set([]) for i in range(9)],
                            'col': [set([]) for i in range(9)],
                            'div': [set([]) for i in range(9)],
                            'hints': set([])}
        for i in range(9): self.grid.append(['']*9)
        for i in range(9): self.overlaps.append([0]*9)
        # Init word list and grid from files
        with open(wfile) as wf: words = [w.strip('\r\n').upper() for w in wf.readlines()]
        with open(gfile) as gf: rows =  [l.strip('\r\n').upper() for l in gf.readlines()]
        for i, row in enumerate(rows):
            for j, c in enumerate(row):
                if c.isalpha():
                    self.setLetter(i,j,c)
                    self.constraints['hints'].add((i,j,c))
        digits = range(9)
        # Init the domain of each word and prune it
        self.domains = dict([(w, set([(d, x, y) for d in 'HV' for x in digits for y in digits])) for w in words])
        self.forwardCheck()


    # Checks to see if placing the letter c in the square (x,y) conflicts with any row, col, or div constraints
    def conflicts(self, x, y, c):
        return c in self.constraints['row'][x] or \
               c in self.constraints['col'][y] or \
               c in self.constraints['div'][self.getDiv(x,y)]


    # Returns the division number of the square (x,y)
    def getDiv(self, x, y):
        if   x >= 6: div = 6
        elif x >= 3: div = 3
        else:        div = 0
        if   y >= 6: div += 2
        elif y >= 3: div += 1
        return div


    # Sets a grid square to a letter and updates the appropriate constraints
    # Also tracks how many times a square is used by a word
    def setLetter(self, x, y, c):
        self.constraints['row'][x].add(c)
        self.constraints['col'][y].add(c)
        self.constraints['div'][self.getDiv(x,y)].add(c)
        self.grid[x][y] = c
        self.overlaps[x][y] += 1


    # Removes a letter from the grid and updates the appropriate constraints
    # If there is a still another word using that square, the letter will not be removed
    # If the square was supplied as a hint from the grid file, it is left intact
    def rmvLetter(self, x, y, c):
        if (x,y,c) in self.constraints['hints']: return
        self.overlaps[x][y] -= 1
        if self.overlaps[x][y] == 0:
            self.constraints['row'][x].discard(c)
            self.constraints['col'][y].discard(c)
            self.constraints['div'][self.getDiv(x,y)].discard(c)
            self.grid[x][y] = ''


    # Checks if you can add a letter at this specific square
    def canAddLetter(self, x, y, c):
        gridC = self.grid[x][y]
        return gridC == c or (gridC == '' and not self.conflicts(x,y,c))


    # Checks if you can add a word in this specific orientation/direction d, starting at the square (x,y)
    def canAddWord(self, w, d, x, y):
        wlen = len(w)
        able = x + wlen - 1 < 9 if d == 'V' else y + wlen - 1 < 9
        i = 0
        if d == 'V':
            while able and i < wlen:
                able = able and self.canAddLetter(x+i, y, w[i])
                i += 1
        else:
            while able and i < wlen:
                able = able and self.canAddLetter(x, y+i, w[i])
                i += 1
        return able


    # Adds a word to the grid
    def addWord(self, w, d, x, y):
        if d == 'V':
            for i,c in enumerate(w): self.setLetter(x+i,y,c)
        else:
            for i,c in enumerate(w): self.setLetter(x,y+i,c)
        self.assigned.add(w)
        self.assignOrder.append((w,d,x,y))


    # Removes a word to the grid
    def rmvWord(self, w, d, x, y):
        if d == 'V':
            for i,c in enumerate(w): self.rmvLetter(x+i,y,c)
        else:
            for i,c in enumerate(w): self.rmvLetter(x,y+i,c)
        self.assigned.discard(w)
        self.assignOrder.pop()


    # Forward checks and prunes the domains according to the constraints
    # Returns the array of pruned domains, or None if one of the domains runs empty
    def forwardCheck(self):
        pruned = []
        for w, dom in self.domains.items():
            if w in self.assigned: continue
            removed = []
            for (d,x,y) in dom:
                if not self.canAddWord(w,d,x,y): removed.append((d,x,y))
            if removed:
                if len(removed) == len(dom): return None
                pruned.append((w, removed))

        for (w, locs) in pruned: self.domains[w].difference_update(locs)
        return pruned


    # Undo the effects of a assignment
    # Removes a word assigned from the grid and restores the domain of the words to before the assignment
    def undo(self):
        if self.actions:
            (w,d,x,y, pruned) = self.actions.pop()
            self.rmvWord(w,d,x,y)
            for (w, locs) in pruned: self.domains[w].update(locs)


    # Counts the number of overlaps this assignment would cause
    def countOverlaps(self, w, d, x, y):
        count = 0
        if d == 'V':
            for i in range(len(w)):
                if w[i] == self.grid[x+i][y]: count += 1
        else:
            for i in range(len(w)):
                if w[i] == self.grid[x][y+i]: count += 1
        return count

    # Backtrack search
    def backtrack(self, parent=''):
        # Return if complete or if words are assigned by grid is still not filled
        if len(self.assigned) == len(self.domains):
            if all('' not in x for x in self.grid): return self.assignOrder
            return None
        self.backtracks += 1
        # Take the word with the smallest domain (most constrained variable), tie-break by length
        w,_, _ = min([(w, len(dom), -len(w)) for w, dom in self.domains.items() if w not in self.assigned],
                      key = lambda t: (t[1],t[2]))
        # Sort the placement option in descending order by number of constraints
        sqrs = [(d,x,y, self.countOverlaps(w,d,x,y)) for d,x,y in self.domains[w]]
        sqrs.sort(key = lambda t: t[3], reverse=True)
        for d,x,y,k in sqrs:
            if self.canAddWord(w, d,x,y):
                self.addWord(w, d,x,y)
                if len(self.assigned) == len(self.domains):
                    if all('' not in x for x in self.grid): return self.assignOrder
                    return None
                pruned = self.forwardCheck()
                # If the prune did not detect an early failure, backtrack again
                # If it did fail, remove the placement (word, orientation, and location)
                if pruned != None:
                    self.actions.append((w,d,x,y, pruned))
                    result = self.backtrack()
                    if result: return result
                    else: self.undo()
                else: self.rmvWord(w,d,x,y)
        return None


    # Prints the grid
    def show(self):
        for row in self.grid:
            x = ''
            for c in row:
                if c: x += c
                else: x += '_'
            print(x)


import time
# Runs the solver with the specified files
# Prints out the grid, the assignment order, the number of nodes expanded, and the execution time
def run(gfile, wfile):
    b = Codedoku(gfile, wfile)
    start_time = time.time()
    b.backtrack()
    end_time = time.time() - start_time
    b.show()
    print('')
    for w,d,x,y in b.assignOrder:
        print('%s,%d,%d : %s' % (d,x,y,w))
    print('')
    print('Number of backtracks/nodes expanded:', b.backtracks)
    print("Execution time: %s seconds" % end_time)

grid1 = '../data/grid1.txt'
bank1 = '../data/bank1.txt'
grid2 = '../data/grid2.txt'
bank2 = '../data/bank2.txt'

run(grid1, bank1)
run(grid2, bank2)
