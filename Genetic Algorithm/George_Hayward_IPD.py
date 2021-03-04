'''
Created on 6 Nov. 2017

@author: gjhay
'''

import itertools
from axelrod.actions import Actions, Action
from axelrod.player import Player

C, D = Actions.C, Actions.D

# Returns a dictionary with all possible permutations of memoryDepth(and less) sized bit-strings.
def generateDictionary(memoryDepth):
    aDict = {} # Define the dictionary that will be built in this method.
    moveDepth = memoryDepth * 2 # Define moveDepth. The amount of actual moves that have been made in the game.
    # Create dictionary which will hold moves pre-memory depth. Since memory depth is always >= 2,
    # Add first move, opponents possible first moves. (0(C) and 1(D))
    preMemoryDepth = {'firstMove' : 0,
                      '0' : 1,
                      '1' : 2
                      } 
    bitCounter = 3 # Define a bit counter which will keep track of how many bits have been added.
    # If the memory depth is greater than 2. Then we need to make all possible opponent moves prior to memory depth.
    if(memoryDepth > 2): 
        itter = 4 # Define a game iterator.
        # Create all opponents possible moves. This will be half the total moveDepth.
        while(itter < moveDepth):
            # Create a list of permutations base off of the itter/2. This will get all combinations of the opponents moves.
            permList = ["".join(seq) for seq in itertools.product("01", repeat=(itter//2))]
            # Add this generation of permList to the preMemoryDepth
            for permutation in permList:
                preMemoryDepth[permutation] = bitCounter
                bitCounter = bitCounter + 1              
            # Add 2 to itter to move to the next game.
            itter = itter + 2   
    # Now that all preMemoryDepth permutations for opponents moves have been added to preMemoryDepth{}, add it to aDict.        
    aDict['preMemoryDepth'] = preMemoryDepth 
    # Add all permutations for the memory depth.   
    permList = ["".join(seq) for seq in itertools.product("01", repeat=moveDepth)]
    for permutation in permList:
        aDict[permutation] = bitCounter
        bitCounter = bitCounter + 1
    
    return aDict

##########
class GeneticAlgorithm(Player):
    """Reacts to the games history according to the imported strategy bit-string.
    
    
    Names:

    - George Hayward GA Strategy: 
    """
    
    def __init__(self, MemoryDepth, Chromosome):
        self.MemoryDepth = MemoryDepth
        self.Chromosome = Chromosome
        self.dict = generateDictionary(self.MemoryDepth)
    def getMemoryDepth(self):
        return self.MemoryDepth
    def getChromosome(self):
        return self.Chromosome
    def getDict(self):
        return self.dict
    
    name = "Genetic Algorithm Strategy"
    classifier = {
        'memory_depth': getMemoryDepth(),  # Custom memory depth
        'stochastic': False,
        'makes_use_of': set(),
        'long_run_time': False,
        'inspects_source': False,
        'manipulates_source': False,
        'manipulates_state': False
    }
    
    @staticmethod
    def strategy(self, opponent: Player) -> Action:
        # If this is the first move.
        if len(self.history) == 0:
            choice = self.getChromosome()[0]
            if choice == "0":
                return C
            else:
                return D
        # If still in preMemoryDepth phase of game.
        elif len(self.history) < self.getMemoryDepth():
            # Turn opponents history into a readable binary string.
            opponentsHistory = ""
            for move in opponent.history:
                if move == C:
                    opponentsHistory += '0'
                else:
                    opponentsHistory += '1'
            # Find the choice in the preMemoryDepth dictionary based on available history.
            choice = self.getDict().get("preMemoryDepth").get(opponentsHistory)
            if self.getChromosome()[choice] == '0':
                return C
            else:
                return D
        # If we have access to the entire memory depth.
        elif len(self.history) >= self.getMemoryDepth():
            # Retrieve the last 6 games.
            gameHistory = ""
            i = - self.getMemoryDepth()
            while(i < 0):
                if self.history[i] == C:
                    gameHistory += '0'
                else:
                    gameHistory += '1'
                if opponent.history[i] == C:
                    gameHistory += '0'
                else:
                    gameHistory += '1'
                i += 1
            # Find the choice in the dictionary based on the game's history.
            choice = self.getDict().get(gameHistory)
            if self.getChromosome()[choice] == '0':
                return choice
            else:
                return D