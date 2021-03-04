'''
Created on 6 Nov. 2017

@author: gjhay
'''
import axelrod as axl
# Couldn't figure out how to import properly.
from ass2 import GA, IPD

# Generate a strategy from the genetic algorithm
bitstring = GA.geneticAlgorithm(3)

# Play all strategies against one another
players = [
    x() for x in axl.all_strategies
    ]
# Add my strategy
players.append(IPD.GeneticAlgorithm(3, bitstring))
tournament = axl.Tournament(players, turns=20, repetitions=1)
results = tournament.play(keep_interactions=True)
for index_pair, interaction in sorted(results.interactions.items()):
    player1 = tournament.players[index_pair[0]]
    player2 = tournament.players[index_pair[1]]
    match = axl.Match([player1, player2], turns=5)
    match.result = interaction[0]
    print('%s vs %s: %s' % (player1, player2, match.result))
    print('....... Scores:      %s' % ( match.scores()))