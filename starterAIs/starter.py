import sys

enemyBots = []
ally_bots = []
bots = {}
ALLY = "ALLY"
ENEMY = "ENEMY"
ATTACK = "ATTACK"
MOVE = "MOVE"
IDLE = "IDLE"
FLEE = "FLEE"
SHORT_RANGE = 0
MEDIUM_RANGE = 1
LONG_RANGE = 2
OUT_OF_RANGE = 3

# Control your bots in order to destroy the enemy team !
bot_per_player = int(input())  # the amount of bot you control
# the arena size in meters (just here in case the creator change the arena size during the contest)
arena_size = int(input())
orders = ""


class BotInfo:
    def __init__(self, range_from_bot, dist_bot_rank, shield_comp, health_comp, total_comp):
        self.range_from_bot = range_from_bot
        self.dist_bot_rank = dist_bot_rank
        self.shield_comp = shield_comp
        self.health_comp = health_comp
        self.total_comp = total_comp

    def __str__(self):
        return "rangeFromBot: {}, distBotRank: {}, shieldComp: {}, healthComp: {}, totalComp: {}".format(
            self.range_from_bot, self.dist_bot_rank, self.shield_comp, self.health_comp, self.total_comp)


def dead_bot(bot_id):
    return Bot(bot_id, "", -1, -1, "", "", -1, -1, -1, -1, -1, -1, -1)


class Bot:
    # Decorator to handle Bot and id for function argument, ignore the warnings of the IDE
    def __bot_or_id(f):
        # noinspection PyCallingNonCallable
        def wrapper(self, *args):
            return f(self, *[arg.bot_id if isinstance(arg, Bot) else arg for arg in args])

        return wrapper

    def __init__(self, bot_id, team, shield, health, action, targets, enemy_range, dist_en_rank, health_rank,
                 shield_rank, total_rank, border_range, border_dist_rank):
        self.bot_id = bot_id
        self.team = team
        self.shield = shield
        self.health = health
        self.action = action
        self.targets = map(int, targets.split(","))
        self.enemy_range = enemy_range
        self.dist_en_rank = dist_en_rank
        self.health_rank = health_rank
        self.shield_rank = shield_rank
        self.total_rank = total_rank
        self.border_range = border_range
        self.border_dist_rank = border_dist_rank
        self.attacking_me = set()
        self.fleeing_me = set()
        self.approaching_me = set()
        self.info_from_my_perspective = {}

    def __str__(self):
        # we can't display all attributes of a bot, so we display only the ones we need
        return f"{self.team} bot {self.bot_id} with {self.health} health and {self.shield} shield"

    def get_targets(self):
        # If one of the target is dead, we add a deadBot to the targets
        self.targets = list(map(lambda bot_id: bots.get(bot_id, dead_bot(bot_id)), self.targets))
        for target in self.targets:
            target.add_targeting_me(self)

    def add_targeting_me(self, targeting_me):
        if targeting_me.action == ATTACK:
            self.attacking_me.add(targeting_me)
        elif targeting_me.action == FLEE:
            self.fleeing_me.add(targeting_me)
        elif targeting_me.action == MOVE:
            self.approaching_me.add(targeting_me)

    @__bot_or_id  # now attack is a function that takes a bot or an id as argument
    def attack(self, target):
        global orders
        orders += f"{self.bot_id} {ATTACK} {target};"

    def __movement(self, action, *targets):
        global orders
        f = lambda bot: str(bot.bot_id) if isinstance(bot, Bot) else str(bot)
        orders += f"{self.bot_id} {action} {','.join(map(f, targets))};"

    def move(self, targets):
        self.__movement(MOVE, targets)

    def flee(self, targets):
        self.__movement(FLEE, targets)

    def idle(self):
        global orders
        orders += f"{self.bot_id} {IDLE};"

    def add_info_from_my_perspective(self, bot_id, info):
        self.info_from_my_perspective[bot_id] = info

    def viewed_by(self, bot):
        assert bot.team == ALLY, "You can only view info from an ally bot perspective"
        try:
            return bot.info_from_my_perspective[self.bot_id]
        except KeyError:
            raise ValueError("The bot on which you want info died after last turn (so no info available)," +
                             " he was alive and targeted last turn which is why it appears in your code")

    def get_closest_enemy(self):
        assert self.team == ALLY, "You can only call closestEnemy function on an ally bot"
        return min(enemyBots, key=lambda bot: bot.viewed_by(self).dist_bot_rank)

    def is_dead(self):
        return self.health == -1


def main():
    # game loop
    while True:
        global orders, enemyBots, ally_bots, bots
        enemyBots = []
        ally_bots = []
        bots = {}
        orders = ""
        ally_bot_alive = int(input())  # the amount of your bot which are still alive
        bot_count = int(input())  # the amount of bots in the arena
        for i in range(bot_count):
            # bot_id: the unique bot id, stay the same for the whole game
            # bot_type: the bot type in a string. It can be ALLY | ENEMY
            # health: the approximate bot health. Can be 0 | 25 | 50 | 75 | 100, 25 meaning that your life is >= 25% and < 50% of your max life
            # shield: the approximate bot shield. Can be 0 | 1 | 25 | 50 | 75 | 100, 1 meaning that your shield is >= 1% and < 25% of your max shield and 0 that you have no more shield left
            # action: action executed by the bot last turn
            # targets: list of the targets id targeted by the bot last turn ("id1;id2;id3...") if the bot is a bot, else -1 (the target for IDLE is the bot itself)
            # enemy_range: /!\ALWAYS 0 FOR THE FIRST LEAGUE/!\ then, the RANGE from to the closest enemy (if the bot is an enemy it returns the range to its closest ally)
            # border_range: /!\ALWAYS 0 FOR THE FIRST LEAGUE/!\ approximate distance between the bot and the closest border
            # border_dist_rank: /!\ALWAYS 0 FOR THE FIRST 2 LEAGUES/!\ bot are sorted in ascending order based on their distance to the closest border
            # dist_en_rank: /!\ALWAYS 0 FOR THE FIRST 2 LEAGUES/!\ bots are sorted by ascending order based on their distance to the closest enemy
            # health_rank: /!\ALWAYS 0 FOR THE FIRST 2 LEAGUES/!\ bots are sorted in ascendant order based on their amount of health, this is the rank of the current bot in the sorted list
            # shield_rank: /!\ALWAYS 0 FOR THE FIRST 2 LEAGUES/!\ bots are sorted in ascendant order based on their amount of shield
            # total_rank: /!\ALWAYS 0 FOR THE FIRST 2 LEAGUES/!\ bots are sorted in ascendant order based on their amount of health + shield
            bot_id, bot_type, health, shield, action, targets, enemy_range, border_range, border_dist_rank, dist_en_rank, health_rank, shield_rank, total_rank = input().split()
            bot_id = int(bot_id)
            health = int(health)
            shield = int(shield)
            enemy_range = int(enemy_range)
            border_range = int(border_range)
            border_dist_rank = int(border_dist_rank)
            dist_en_rank = int(dist_en_rank)
            health_rank = int(health_rank)
            shield_rank = int(shield_rank)
            total_rank = int(total_rank)
            bot = Bot(bot_id, bot_type, shield, health, action, targets, enemy_range, dist_en_rank, health_rank,
                      shield_rank, total_rank, border_range, border_dist_rank)
            bots[bot_id] = bot
            if bot_type == ALLY:
                ally_bots.append(bot)
            else:
                enemyBots.append(bot)
        for i in range(ally_bot_alive):
            on_air_id = int(input().split(" ")[0])  # the id of the bot which is on air
            for j in range(bot_count - 1):
                # bot_id: the unique bot id
                # bot_type: the bot type in a string. It can be ALLY | ENEMY
                # range_from_on_air_bot: approximate distance between the target and the ON AIR bot. Can be 0 to 3 for short, medium, long and out of range
                # dist_me_rank: bots are sorted by ascending order based on their distance to the ON AIR bot
                # shield_comp: /!\ALWAYS 0 FOR THE FIRST LEAGUE/!\ -1 if the bot has more shield than the ON AIR bot, 0 if it's equal, 1 if the ON AIR bot has more shield
                # health_comp: /!\ALWAYS 0 FOR THE FIRST LEAGUE/!\ same as shieldComp but for the health
                # tot_comp: /!\ALWAYS 0 FOR THE FIRST LEAGUE/!\ same as shieldComp but based on the sum of health+shield
                bot_id, _, range_from_on_air_bot, dist_me_rank, shield_comp, health_comp, tot_comp = input().split()
                bot_id = int(bot_id)
                range_from_on_air_bot = int(range_from_on_air_bot)
                dist_me_rank = int(dist_me_rank)
                shield_comp = int(shield_comp)
                health_comp = int(health_comp)
                tot_comp = int(tot_comp)
                info = BotInfo(range_from_on_air_bot, dist_me_rank, shield_comp, health_comp, tot_comp)
                bots[on_air_id].add_info_from_my_perspective(bot_id, info)

        for ally_bot in ally_bots:
            ally_bot.move(ally_bot.get_closest_enemy())

        # Write an answer using print
        # To debug: print("Debug messages...", file=sys.stderr)

        print(orders)


main()
