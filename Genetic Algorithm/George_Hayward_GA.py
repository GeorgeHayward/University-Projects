'''
Created on 2 Nov. 2017

@author: gjhay
'''

import itertools
import random
from numpy.random import choice

# This object will hold this genetic algorithms variables
class gaStorage:
    def __init__(self, memoryDepth, population, populationSize, permMap):
        self.memoryDepth = memoryDepth
        self.population = population
        self.populationSize = populationSize
        self.permMap = permMap
    def getMemoryDepth(self):
        return self.memoryDepth
    def getPopulation(self):
        return self.population
    def setPopulation(self, population):
        self.population = population
    def getPopulationSize(self):
        return self.populationSize
    def getPermMap(self):
        return self.permMap

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

class strategy:
    # This strategies score
    score = 0
    # initialize the strategy based on the input chromosome.
    def __init__(self, chromosome):
        self.chromosome = chromosome
    # update the score based on the input number
    def updateScore(self, number):
        self.score = self.score + number
    # get this strategies score
    def getScore(self):
        return self.score
    # get this strategies chromosome
    def getChromosome(self):
        return self.chromosome
    # flips a single bit in the chromosome.
    def mutation(self):
        # get the point for mutation
        mutatePoint = random.randint(0, len(self.getChromosome()) - 1)
        # turn chromosome into a mutable list
        tempChromosome = list(self.getChromosome())
        # flip the bit
        if(self.chromosome[mutatePoint] == '0'):
            tempChromosome[mutatePoint] = '1'
        else:
            tempChromosome[mutatePoint] = '0'
        # make the mutated chromosome the new chromosome
        self.chromosome = "".join(tempChromosome)
# This method will update these strategies scores by playing the moves in the index's of their chromosomes.
def playRound(strat1, strat1index, strat2, strat2index):
    # If round is DD(11) - both players get 1 points.
    strat1Move = strat1.getChromosome()[strat1index]
    strat2Move = strat2.getChromosome()[strat2index]
    if(strat1Move == '1') and (strat2Move == '1'):
        strat1.updateScore(1)
        strat2.updateScore(1)
    # If round is CC(00) - both players get 3 points.    
    elif(strat1Move == '0') and (strat2Move == '0'):
        strat1.updateScore(3)
        strat2.updateScore(3)
    # If strategy 1 D(1). strategy 2 C(0). s1 gets 5. s2 gets 0.
    elif(strat1Move == '1') and (strat2Move == '0'):
        strat1.updateScore(5)
    # If strategy 1 C(0). strategy 2 D(!). s1 gets 0. s2 gets 5.
    elif(strat1Move == '0') and (strat2Move == '1'):
        strat2.updateScore(5)
        
# Play two strategies against each other.
def verse(strat1, strat2, gaVariables):
    gameRounds = len(strat1.getChromosome()) # This is the amount of rounds that will be played in the game.
    roundsPlayed = 0 # This will count the amount of rounds played.
    gameMemory = "" # This will be a reference to all moves made so far.
    strat1Moves = "" # This will keep track of strat1's moves. To be used as a reference prior to reaching memory depth.
    strat2Moves = "" # Same as above ^^^^
    # Play the obligatory 'first moves'. Will always be the first bit of the chromosome.
    playRound(strat1, 0, strat2, 0)
    # Add both first moves to the gameMemory.
    gameMemory += strat1.getChromosome()[0] + strat2.getChromosome()[0]
    strat1Moves += strat1.getChromosome()[0]
    strat2Moves += strat2.getChromosome()[0]
    # Add 1 round to rounds played.
    roundsPlayed += 1
    # Play preMemoryDepth games until memoryDepth is reached.
    while(roundsPlayed < gaVariables.getMemoryDepth()):
        # Get the next moves from the dictionary according to the opponents previous moves.
        s1move = gaVariables.getPermMap().get("preMemoryDepth").get(strat2Moves)
        s2move = gaVariables.getPermMap().get("preMemoryDepth").get(strat1Moves)
        # Input moves and update scores.
        playRound(strat1, s1move, strat2, s2move)
        # Update the games memory
        gameMemory += strat1.getChromosome()[s1move] + strat2.getChromosome()[s2move]
        # Update the strategies moves
        strat1Moves += strat1.getChromosome()[s1move]
        strat2Moves += strat2.getChromosome()[s2move]
        roundsPlayed += 1 # Update at the end of each round.
    memoryDepth = -(gaVariables.getMemoryDepth() * 2)
    # Keep playing rounds until all gameRounds are played.
    while(roundsPlayed < gameRounds):
        # Get a substring from the memory. The current end point - 6.
        bothMoves = gaVariables.getPermMap().get(gameMemory[memoryDepth:])
        # Play based off this substring
        playRound(strat1, bothMoves, strat2, bothMoves)
        # Update the games memory
        gameMemory = gameMemory + strat1.getChromosome()[bothMoves] + strat2.getChromosome()[bothMoves]
        roundsPlayed += 1 # Update at the end of each round.
# Run a tournament between all the strategies
def tournament(gaVariables):
    # Play every strategy against one another once.
    for strat, opponent in itertools.combinations(gaVariables.getPopulation(), 2):
        # Match the two opponents. Send through dictionary and memoryDepth so the game can run according to those variables.
        verse(strat, opponent, gaVariables)

# This will select the most fit chromosomes for crossover. Additionally there is a chance of mutations.
def evolution(gaVariables):
    # Get the combined score for every chromosome.
    totalScore = 0
    for strategy in gaVariables.getPopulation():
        totalScore = totalScore + strategy.getScore()
    # This will contain the weights for every member of the population in order.
    # The weights being what portion of the score makes up the totalScore.
    fitnessWeights = list()
    for strategy in gaVariables.getPopulation():
        fitnessWeights.append(strategy.getScore() / totalScore)
    # This will be the newly evolved list of chromosomes.
    evolvedPopulation = list()
    # Keep adding members to the evolved population until it is as large as the original population.
    while(len(evolvedPopulation) <= len(gaVariables.getPopulation())):
        # Select two chromosomes randomly, though based on their weight probability.
        firstSelection = choice(gaVariables.getPopulation(), p = fitnessWeights)
        secondSelection = choice(gaVariables.getPopulation(), p = fitnessWeights)
        if firstSelection is not secondSelection:
            evolvedPopulation = crossover(firstSelection, secondSelection, evolvedPopulation)
    mutate(evolvedPopulation)
    gaVariables.setPopulation(evolvedPopulation)

# This will go through every member of the evolvedPopulation and possibly mutate them. (change bits in their chromosome)
def mutate(evolvedPopulation):
    # Mutation rate = 1%
    mutationRate = 1
    for strategy in evolvedPopulation:
        rollMutate = random.randint(1, 101)
        # Go ahead with the mutation if we received 1. (1% chance)
        if(rollMutate == mutationRate):
            strategy.mutation()

# This will perform crossover on two selected individuals and return a new population with them in it.
def crossover(firstSelection, secondSelection, evolvedPopulation):
    # Recombination rate: The chance that crossover will take place.
    recombinationRate = random.randint(1,101)
    # Do crossover
    if recombinationRate < 76:
        # The crossover point in the chromosomes length.
        crossoverPoint = random.randint(1, len(firstSelection.getChromosome()) - 2)
        # First child
        evolvedPopulation.append(strategy(
            firstSelection.getChromosome()[:crossoverPoint] + 
                                 secondSelection.getChromosome()[crossoverPoint:]))
        # Second child
        evolvedPopulation.append(strategy(
            secondSelection.getChromosome()[:crossoverPoint] + 
                                 firstSelection.getChromosome()[crossoverPoint:]))        
    # Don't do crossover. Add the strategies back to the population.
    else:
        evolvedPopulation.append(strategy(firstSelection.getChromosome()))
        evolvedPopulation.append(strategy(secondSelection.getChromosome()))
    return evolvedPopulation

def geneticAlgorithm(memoryDepth):
    permMap = generateDictionary(memoryDepth) # Create a dictionary of bitstrings based on the memoryDepth.
    aDictSize = len(permMap["preMemoryDepth"]) + len(permMap) - 1 # Length of dictionary including inner dictionary        
    populationSize = 20 # This is the size of the population of strategies.
    generations = 200 # The amount of generations the GA will run.
    population = list() # List which will contain the population of strategies.
    i = 0 # Iterator to create all populationSize strategies.
    # Create the population of random strategy objects
    while(i < populationSize):
        tempChromosome = ''.join(random.choices('0' + '1', k=aDictSize))
        # Add new strategy to list
        population.append(strategy(tempChromosome))
        i = i + 1 
    gaVariables = gaStorage(memoryDepth, population, populationSize, permMap)
    # Run the genetic algorithm concept.
    while(generations > 1):
        # Put through the population, memoryDepth and dictionary to read choices
        tournament(gaVariables)
        evolution(gaVariables)
        print("Generation: " + str(generations))
        generations = generations - 1
    # Find the fittest individual at the end of the GA and return it.
    tournament(gaVariables)
    highest = strategy("Low")
    for strat in gaVariables.getPopulation():
        if strat.getScore() > highest.getScore():
            highest = strat
    return "Best Chromosome = " + highest.getChromosome() + ". Score = " + str(highest.getScore())

def main():
    #import cProfile
    #cProfile.run('geneticAlgorithm(3)')
    print(geneticAlgorithm(3))

if __name__ == '__main__': main()