import sys
import math

# Control your bots in order to destroy the enemy team !

bot_per_player = int(input())  # the amount of bot you control

# game loop
while True:
    ally_bot_alive = int(input())  # the amount of your bot which are still alive
    total_entities = int(input())  # the amount of entities in the arena
    for i in range(total_entities):
        # ent_id: the unique gameEntity id, stay the same for the whole game
        # ent_type: the gameEntity type in a string. It can be ALLY | ENEMY
        # health: the approximate gameEntity health. Can be 0 | 25 | 50 | 75 | 100, 25 meaning that your life is >= 25% and < 50% of your max life
        # shield: the approximate gameEntity shield. Can be 0 | 1 | 25 | 50 | 75 | 100, 1 meaning that your shield is >= 1% and < 25% of your max shield and 0 that you have no more shield left
        # action: action executed by the gameEntity last turn
        # targets: list of the targets id targeted by the robot last turn ("id1;id2;id3...") if the gameEntity is a robot, else -1 (the target for IDLE is the robot itself)
        # border_dist: approximate distance between the gameEntity and the closest border
        # border_dist_rank: gameEntity are sorted in ascending order based on their distance to the closest border
        # dist_en_rank: entities are sorted by ascending order based on their distance to the closest enemy
        # health_rank: entities are sorted in ascendant order based on their amount of health, this is the rank of the current gameEntity in the sorted list
        # shield_rank: entities are sorted in ascendant order based on their amount of shield
        # total_rank: entities are sorted in ascendant order based on their amount of health + shield
        ent_id, ent_type, health, shield, action, targets, dist_en, border_dist, border_dist_rank, dist_en_rank, health_rank, shield_rank, total_rank = input().split()
        ent_id = int(ent_id)
        health = int(health)
        shield = int(shield)
        dist_en = int(dist_en)
        border_dist = int(border_dist)
        border_dist_rank = int(border_dist_rank)
        dist_en_rank = int(dist_en_rank)
        health_rank = int(health_rank)
        shield_rank = int(shield_rank)
        total_rank = int(total_rank)
    for i in range(ally_bot_alive):
        for j in range(total_entities):
            # ent_id: the unique gameEntity id
            # ent_type: the gameEntity type in a string. It can be SELF | ALLY | ENEMY
            # dist_me: approximate distance between the target and the current bot. Can be 0 to 4 for short, medium, long and out of range
            # dist_me_rank: entities are sorted by ascending order based on their distance to the current bot
            # shield_comp: -1 if the gameEntity has more shield than the current bot, 0 if it's equal, 1 if your bot as more shield
            # health_comp: same as shieldComp but for the health
            # tot_comp: same as shieldComp but based on the sum of health+shield
            ent_id, ent_type, dist_me, dist_me_rank, shield_comp, health_comp, tot_comp = input().split()
            ent_id = int(ent_id)
            dist_me = int(dist_me)
            dist_me_rank = int(dist_me_rank)
            shield_comp = int(shield_comp)
            health_comp = int(health_comp)
            tot_comp = int(tot_comp)
    for i in range(ally_bot_alive):

        # Write an answer using print
        # To debug: print("Debug messages...", file=sys.stderr)

        print("0 MOVE 4")