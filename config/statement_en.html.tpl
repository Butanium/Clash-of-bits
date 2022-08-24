<!--suppress ALL, HtmlUnknownTarget -->
<!-- LEAGUES level1 level2 level3 -->

<div id="statement_back" class="statement_back" style="display:none"></div>
<div class="statement-body">
     <!-- BEGIN level1 -->
     <div style="color: #7cc576; 
background-color: rgba(124, 197, 118,.1);
padding: 20px;
margin-right: 15px;
margin-left: 15px;
margin-bottom: 10px;
text-align: left;">
    <div style="text-align: center; margin-bottom: 6px">
      <img src="//cdn.codingame.com/smash-the-code/statement/league_wood_04.png" />
    </div>

    <!-- BEGIN level1 -->
    <p style="text-align: center; font-weight: 700; margin-bottom: 6px;">
      This is a <b>league based</b> game.
    </p>
    <!-- END -->
    <!-- _BEGIN level2 level3 level4 -->
    <!-- <p style="text-align: center; font-weight: 700; margin-bottom: 6px;">
      <b>Starter Kit</b>
    </p> -->
    <!-- _END -->

    <div class="statement-league-alert-content">
     
      For this challenge, multiple leagues for the same game are available. Once you have proven yourself against the
      first Boss, you will access a higher league and extra rules will be available.<br><br>
     
    <!--
      Starter AIs are available in the
      <a target="_blank" href="https://github.com/Butanium/Clash-of-bits/starterAIs">Starter Kit</a>.
      They can help you get started with your own bot.
      <br><br>
      -->

    </div>

  </div>
  <!-- END -->
    <!-- BEGIN level1 level2 -->
    <div>
        <div style="text-align: left;padding-left: 15px">
            Welcome to clash of bots ! <br>
            If you are lost and don't understand how you are supposed to extract data from the basic code,
            make sure to check <a
                href="https://tech.io/playgrounds/57689/how-to-extract-values-from-the-game-loop-in-clash-of-bots"> this
            playground </a>.
            It'll help you to get started.
        </div>
    </div>
    <br>
    <!-- END -->
    <!-- GOAL -->
    <div class="statement-section statement-goal">
        <h1>
            <span class="icon icon-goal">&nbsp;</span>

            <span>The Goal</span>
        </h1>
        <div class="statement-goal-content">
            In this game you have to destroy the enemy team, while keeping at least one of your bots alive. If the time
            is up, the player who has destroyed the most bots win.
        </div>
    </div>
    <!-- RULES -->
    <div class="statement-section statement-rules">
        <h1>
            <span class="icon icon-rules">&nbsp;</span>
            <span>Rules</span>
        </h1>
        <div>
            <div class="statement-rules-content">
                Unfortunately,
                your bots aren't very smart and cannot provide you that much information as X,Y coordinates or their
                exact
                health. You will have to exploit the little information given to you.
                <!-- BEGIN level1 level2 -->
                <br>
                But don't worry your bots get smarter and can give you more info in the following leagues !
                <!-- END -->
                <br><br>
                <!-- BEGIN level2 level3 -->
                                <div class="statement-new-league-rule"><p>
                                    In this league your bots realized they could calculate and give you <!-- BEGIN level2 --> 5 <!-- END --> additional pieces of information. More details in the <a href="#game_input"> game input section</a>.
                                    </div>
                <!-- END -->
                A lot of information given by your bots are distance between entities. The game use 4 different <b>ranges</b>
                :
                <ul>
                    <li><b>0</b> : Short range (dist <=
                        <const>3 m</const>
                        )
                    </li>
                </ul>
                <ul>
                    <li><b>1</b> : Medium range (
                        <const>3 m</const>
                        < dist <=
                        <const>8 m</const>
                        )
                    </li>
                </ul>
                <ul>
                    <li><b>2</b> : Long range (
                        <const>8 m</const>
                        < dist <=
                        <const>15 m</const>
                        )
                    </li>
                </ul>
                <ul>
                    <li><b>3</b> : Out of range (dist >
                        <const>15 m</const>
                        )
                    </li>
                </ul>

                <br>

                The Arena is a square, a bot cannot get out of the Arena.
                The objects in the arena are called <b>entities</b>.
                For now, the bots are the only entities.

                <br>
                A bot has 2 health bar :
                <ul>
                    <li>
                        The blue one represents its shield : if a bot doesn't take any damage during 12 turns its
                        shield
                        will regenerate every turn. An empty shield will take 12 turn without damage to be
                        completely restored
                    </li>
                </ul>
                <ul>
                    <li>
                        The purple one represents your health. It cannot be regenerated but will decrease only if the
                        bot's shield is empty
                    </li>
                </ul>
                <br>
                Your bots are not smart enough to give you the exact shield and health value of bots in the arena.
                So they use an approximation :
                <ul>
                    <li>For the health they give you either
                        <const>0</const>
                        |
                        <const>25</const>
                        |
                        <const>50</const>
                        |
                        <const>75</const>
                        |
                        <const>100</const>
                        ,
                        <const>25</const>
                        meaning that your life is
                        <const>>= 25%</const>
                        but
                        <const>< 50%</const>
                        of your max life
                    </li>
                    <li>For the shield they give you either
                        <const>0</const>
                        |
                        <const>1</const>
                        |
                        <const>25</const>
                        |
                        <const>50</const>
                        |
                        <const>75</const>
                        |
                        <const>100</const>
                        ,
                        <const>1</const>
                        meaning that the shield is
                        <const> > 0%</const>
                        and
                        <const>< 25%</const>
                        of the max shield and
                        <const>0</const>
                        that the shield is empty
                    </li>

                </ul>
                Your bots can perform 3 different actions :
                <ul>
                    <li>
                        <b>ATTACK</b> one enemy bot. Your damage are done depending on the <b>range</b> your enemy is at
                        (short
                        medium long or close). The closer it is the more
                        damage you'll do. If you're attacking an enemy out of range, you won't deal any damage. For more
                        details,
                        see expert rules.
                    </li>

                    <li>
                        <b>MOVE </b>to a group of entities, which makes you moving toward the average position of
                        the group. If the group is an only entity, the bot will move toward it.
                    </li>

                    <li>
                        <b>FLEE </b> from a group of entities, which makes you moving backward from the average position
                        of the group
                    </li>
                    <li>
                        <b>IDLE </b> The bot wait for this turn and start thinking about the meaning of life.
                        <br><i>
                        "A bot must obey the orders given it by human beings except where such orders would
                        conflict with the First Law."
                    </i><br>
                        It's the default action if you don't send an order for a bot.
                    </li>
                </ul>
                <br>
                You <strong>lose</strong> if :
                <ul>
                    <li>All your bots got destroyed</li>
                    <li>Your enemy has destroyed more bots than you at the end of the match</li>
                    <li>
                        You send invalid orders to your bots making their operating system crash.
                        <br>
                        But they are smart enough to tell you why you made them crash,
                        so be sure to check the little dot in the replay.
                    </li>
                </ul>
                <!-- BEGIN level3 -->
                <div class="statement-new-league-rule">
                <br>
                <strong>
                    Now your bots spawn randomly on the map.
                </strong>
                <br>
                <br>
                The bot spawns of both teams are <strong>symmetric</strong>.
                <br>
                The symmetry can be :
                <ul>
                    <li>
                        <strong>central</strong> (across the center of the arena)
                    </li>
                    <li>
                        <strong>horizontal</strong> (across the horizontal line through the center of the arena).
                    </li>
                </ul>
                There are 2 rules that those spawn will always respect :
                <ul>
                    <li>
                        The distance between 2 bots has to be at least <strong>2 meters</strong>
                    </li>
                    <li>
                        The distance between 2 bots of different team has to be at least <strong>8.1 meters</strong>
                    </li>
                </ul>
                </div>
                <!-- END -->
            </div>
        </div>
    </div>
    <!-- EXPERT RULES -->
    <div class="statement-section statement-expertrules">
        <h1>
            <span class="icon icon-expertrules">&nbsp;</span>
            <span>Expert Rules</span>
        </h1>
        <div class="statement-expert-rules-content">
            A turn duration for the simulation is 250 ms
            <br>
            Concerning <strong>attack</strong> :
            <ul>
                <li>
                    In order to fire bullet at an opponent, a bot has to attack the <b>same target</b> for <b>aim
                    duration</b>
                    turns. Then it'll fire <b>bullet per shot</b> bullets each turn for <b>shot duration</b> turns
                </li>
                <li>The moment a bullet is fired, the game engine determine if it will hit the targeted bot
                    based on the <b>current</b> range of the
                    target with a probability of <b>precision [target range]</b>. Note that attacking out of range has a
                    0%
                    precision.
                </li>
                <li> The game is deterministic, even if there is some RNG in shots, the 2 teams have the same random
                    seed
                    to determine if their shot will hit. So a game between the 2 same AIs will <b>always</b> result as a
                    draw.
                </li>


            </ul>
            You can see all bot properties per class in this table

            <style type="text/css" ;>
                table.tableizer-table {

                    font-size: 12px;
                    border: 1px solid #CCC;
                    font-family: Arial, Helvetica, sans-serif;
                }

                .tableizer-table td {
                    padding: 4px;
                    margin: 3px;
                    border: 1px solid #CCC;
                }

                .tableizer-table th {
                    background-color: #323232;
                    color: #FFF;
                    font-weight: bold;
                }
            </style>
            <div style="overflow-x:auto;">

                <table class="tableizer-table" ;
                >
                    <thead>
                    <tr class="tableizer-firstrow" ;>
                        <th>Bot class</th>
                        <th>Damage per bullet</th>
                        <th>Bullet per shot</th>
                        <th>Aim duration (turn)</th>
                        <th>Shot duration (turn)</th>
                        <th>Precision at <br>short / mid / long range</th>
                        <th>Speed</th>
                        <th>Health</th>
                        <th>Shield</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr align="center" ;>
                        <td>Assault</td>
                        <td>300</td>
                        <td>3</td>
                        <td>4</td>
                        <td>2</td>
                        <td>95% / 55% / 15%</td>

                        <td>1.2</td>
                        <td>5000</td>
                        <td>3000</td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <br>
            You can check the source code of the game on its <a href="https://github.com/Butanium/Clash-of-bots">github
            repo</a>
        </div>
    </div>

    <!-- WARNING -->
    <div class="statement-section statement-warning">
        <h1>
            🐞 Debugging tips
        </h1>
        <div class="statement-warning-content">
            In this game, there are many features to help you understand what is happening on the battlefield:
            <div class="statement-example-container">
                <div class="statement-example">
                    <img src="assets/statement/camera.png">
                    <div class="legend">
                        <div class="description">
                            You can activate the dynamic zoom of the camera with the camera mode toggle.
                        </div>
                    </div>
                </div>
                <div class="statement-example">
                    <img src="assets/statement/ranges.png">
                    <div class="legend">
                        <div class="description">
                            The different
                            <const>ranges</const>
                            are displayed when you hover over a bot with your mouse. <br>
                            You can also see the
                            <const>target</const>
                            of the bot, it's current
                            <const>action</const>
                            . The tooltip gives you more information about the bot.
                        </div>
                    </div>
                </div>
                <div class="statement-example">
                    <img src="assets/statement/displayOnHover.png">
                    <div class="legend">
                        <div class="description">
                            You can
                            <const>LEFT CLICK</const>
                            on a bot to make the target display permanent and
                            <const>ALT + LEFT CLICK</const>
                            to remove all targets.
                        </div>
                    </div>
                </div>

                <div class="statement-example">
                    <img src="assets/statement/debug.png">
                    <div class="legend">
                        <div class="description">
                            You can enable the debug overlay with the debug toggle to see the bot's id and current
                            action
                        </div>
                    </div>
                </div>

                <div class="statement-example">
                    <img src="assets/statement/hitmarker.png">
                    <div class="legend">
                        <div class="description">
                            Hitmarkers are displayed when a bot is hit by a bullet.
                        </div>
                    </div>
                </div>

            </div>
        </div>


    </div>
    <!-- STORY -->
    <div class="statement-story-background">
        <div class="statement-story-cover"
             style="background-size: cover; background-color: grey">
            <div class="statement-story">
                <h1>Where does this idea come from ?</h1>
                <div class="story-text"><strong> This challenge is highly inspired by the game "GLADIABOTS".<br> It's
                    almost a copy actually, but I made it with the GFX47 (the gladiabots dev) permission. So if you
                    enjoy this challenge or need some inspiration don't hesitate to <a href="https://gladiabots.com/">give
                        it a look</a> !</strong>
                    <br> <br>
                </div>
            </div>
        </div>
    </div>
    <!-- PROTOCOL -->
    <div class="statement-section statement-protocol">

        <h1>
            <span class="icon icon-protocol">&nbsp;</span>
            <a id="game_input"><span>Game Input</span></a>
        </h1>
        <!-- Protocol block -->
        <div class="blk">
            <div class="title">Initialization input</div>
            <div class="text">
                <span class="statement-lineno"> Line 1: </span> one integer <var>botPerPlayer</var>,
                the amount of bots controlled by each players at the beginning of the game
                <br>
                <span class="statement-lineno"> Line 2: </span> one integer <var>mapSize</var>,
                the arena size in meters.
                <br>
            </div>
        </div>

        <!-- Protocol block -->
        <div class="blk">
            <div class="title">Input for one game turn</div>
            <div class="text">
                <span class="statement-lineno">First line: </span>an integer <var>allyBotAlive</var>, the amount of your
                bot which are still alive.
                <br>
                <span class="statement-lineno">Next line</span>: an integer <var>totalEntities</var>, the amount of
                entities in the arena<br>
                <br>
                <br> For each entity your bots give
                <!-- BEGIN level1 -->
                <const>6</const>
                <!-- END -->
                <!-- BEGIN level2 -->
                <const> 8</const>
                <!-- END -->
                <!-- BEGIN level3 -->
                <const> 13</const>
                <!-- END -->
                pieces of information : <var>entId</var>, <var>entType</var>, <var>health</var>, <var>shield</var>,
                <var>action</var>, <var>targets</var>
                <!-- BEGIN level2 level3 -->
                , <var>enemyRange</var>,
                <var>borderDist</var>
                <!-- END -->
                <!-- BEGIN level3 -->
                , <var>distEnRank</var>,
                <var>borderDistRank</var>, <var>shieldRank</var>, <var>healthRank</var>, <var>totalRank</var>
                <!-- END -->
                . Ranges is are given as integer as follow :   <const>0</const>
                for
                <const>SHORT RANGE</const>
                ,
                <const>1</const>
                for
                <const>MEDIUM RANGE</const>
                ,
                <const>2</const>
                for
                <const>LONG RANGE</const>
                ,
                <const>3</const>
                for
                <const>OUT OF RANGE</const>

                <!-- BEGIN level1 level2 -->
                The others are just 0 and will be used in the next leagues.
                <!-- END -->
                <br>
                <br><var>entId</var> is the unique entity id, stay the same for the whole game.
                <br><br>
                <var>entType</var> indicates the type of entity. The value can be :
                <ul style="padding-bottom: 0;">
                    <li>
                        <const>"ALLY"</const>
                        for one of your Bots
                    </li>
                    <li>
                        <const>"ENEMY"</const>
                        for an enemy Bot
                    </li>

                </ul>
                <var>health</var>, <var>shield</var> for the approximate entity's health and shield.
                <br>
                <br>
                <var>action</var> indicates the action executed by the bot last turn. (
                <const>NONE</const>
                if the entity is not a bot) The value can be
                <const>"ATTACK"</const>
                ,
                <const>"MOVE"</const>
                ,
                <const>"FLEE"</const>
                ,
                <const>"IDLE"</const>.
                <br>
                <br>
                <var>targets</var> is the list of the entities id targeted by the bot last turn separated with
                <const>";"</const>
                :
                <const>"id1;id2;id3..."</const>
                <!-- if the entity is a bot --> (the target for IDLE is the bot itself). <!-- Else 
                <const>-1</const>
                .-->
                <!-- BEGIN level2 level3 -->
                <br> <br>
                <!-- BEGIN level2 -->
                <div class="statement-new-league-rule">
                <!-- END -->

                <var>enemyRange</var> : the range from the closest enemy. If the entity is an enemy bot it returns the
                range to its closest ally
                
                <br><br>
                <var>borderDist</var> : the range from the closest border (among the left, right, top and bottom one)
                <!-- BEGIN level2 -->
                </div>
                <!-- END -->
                <!-- END -->
                <!-- BEGIN level3 -->
                <br>
                <br>
                <div class="statement-new-league-rule">
                The next values are <strong> ranks</strong> : Entities are sorted per an attribute in <strong>ascending
                order</strong> :
                <ul>
                    <li>
                        <var>distEnRank</var> : the attribute is the exact distance between the entity and the closest
                        enemy.
                        If the entity is an enemy bot the distance is calculated based on its distance to its closest
                        ally
                    </li>
                    <li>
                        <var>borderDistRank</var> : the attribute is the exact distance between the entity and the
                        closest border.

                    </li>
                    <li><var>shieldRank</var> : the attribute is the exact shield value.

                    </li>
                    <li><var>healthRank</var> : the attribute is the exact health value</li>
                    <li><var>totalRank</var> : the attribute is the sum of exact health and shield</li>
                </ul>
                </div>
                <!-- END -->

                <br><br><br> Then all of your bots become one after the other <strong>ON AIR</strong>. An <strong>ON AIR </strong> bot gives for each entity
                <!-- BEGIN level1 -->
                <const> 4</const>
                <!-- END -->
                <!-- BEGIN level2 level3 -->
                <const> 7</const>
                <!-- END -->
                information from <strong><u>its perspective :</u></strong> <var>entId</var>, <var>entType</var>, <var>range</var>,
                <var>distMeRank</var>
                <!-- BEGIN level2 level3 -->
                , <var>shieldComp</var>, <var>healthComp</var>, <var>totComp</var>
                <!-- END -->
                .
                <!-- BEGIN level1 -->
                The others are just 0 and will be used in the next leagues.
                <!-- END -->
                For the first iteration, it sends its own information so <var>entType</var> is
                <const> "SELF"</const>
                <i>so that you can get the <strong> ON AIR</strong> ally bot id</i>. Then it sends information about other
                allies then enemies.
                <br>
                <br><var>entId</var> is the unique entity id.
                <br><br> <var>entType</var>
                indicates the type of entity. The value can be:
                <ul style="padding-bottom: 0;">
                    <li>
                        <const>"ALLY"</const>
                        for one of your Bots
                    </li>
                    <li>
                        <const>"ENEMY"</const>
                        for an enemy Bot
                    </li>
                    <li>
                        <const> "SELF"</const>
                        for the bot <strong> ON AIR </strong></li>

                </ul>
                <var>range</var> for the range at which the entity is from your <strong> ON AIR </strong> bot.
              
                <br><br>
                <var>distMeRank</var> the rank of the entity in a ranking based on the exact distance between the
                entities and the <strong> on air </strong> bot
                <br>
                <!-- BEGIN level2 level3 -->
                <br>
                <!-- BEGIN level2 -->
                <div class="statement-new-league-rule">
                <!-- END -->
                <var>shieldComp</var>, <var>healthComp</var>, <var>totComp</var>
                compare an attribute between the <strong>ON AIR</strong> bot and the other entity if the entity is a bot.
                <br><var>shieldComp</var> compare the <strong>shields</strong> of the 2 bots,
                <var>healthComp</var> compare the healths of the 2 bots,
                <var>totComp</var> compare the sum of healths and shields of the 2 bots.
                <br>
                The comps can be either :
                <ul style="padding-bottom: 0;">
                    <li>
                        <const>-1</const>
                        if the other bot has more attribute than the <strong>ON AIR </strong> bot
                    </li>
                    <li>
                        <const>0</const>
                        if the other bot has the same value for the attribute than the <strong> ON AIR </strong> bot
                    </li>
                    <li>
                        <const>1</const>
                        if the other bot has less attribute than the <strong>ON AIR </strong> bot
                    </li>
                </ul>
                <!-- BEGIN level2 -->
                </div>
                <!-- END -->
                <!-- END -->
                <br>

            </div>

            <!-- Protocol block -->
            <div class="blk">
                <div class="title">Output for one game turn</div>
                <div class="text"> One line containing all your orders separated by
                    <const>";"</const>
                    like this
                    <const> "order1;order2;order3;..."</const>
                    <br> An order has to respect the following synthax :
                    <action>yourBotID [ACTION] [TARGETS]</action>
                    <br>
                    <action>ACTION</action>
                    has to be a valid action :
                    <action>ATTACK</action>
                    ,
                    <action>MOVE</action>
                    ,
                    <action>FLEE</action>
                    ,
                    <action>IDLE</action>

                    <action> TARGETS</action>
                    has to use this synthax :
                    <action>targetID1,targetID2,targetID3</action>
                    <br>
                    It has also to follow specifics rules depending on the action you try to perform :
                    <ul>
                        <li>If the action is
                            <action>ATTACK</action>
                            ,
                            <action>TARGETS</action>
                            has to contains
                            <const>1</const>
                            <strong> enemy bot </strong> id, not more, not less, don't try to attack yourself or your
                            allies.
                            <br>
                            <i>
                                "A bot must protect its own existence as long as such protection does not conflict
                                with the First or Second Law."
                            </i>
                        </li>
                        <li>If the action is
                            <action> MOVE</action>
                            or
                            <action> FLEE</action>
                            ,
                            <action>TARGETS</action>
                            has to contain at least 1 entity id
                        </li>
                        <li>If the action is
                            <action>IDLE</action>
                            ,
                            <action>TARGETS</action>
                            can contain <strong> anything </strong> or just <strong> nothing</strong>, in any case it'll
                            be ignored.
                            <del> it allows you to REALLY make your bots thinking about the meaning of life</del>
                        </li>
                    </ul>
                    If you send 2 differents orders concerning the same bot you'll loose the game because your bot will
                    run out of memory (┬┬﹏┬┬)
                    <br> If you don't send any output, all your bots will
                    <action> IDLE</action>
                    . If you don't send any order concerning one or more of your bot, they will
                    <action>IDLE</action>

                </div>
                <br>
                <!-- Protocol block -->
                <div class="blk">
                    <div class="title">Constraints</div>
                    <div class="text">The arena is a square of size between
                        <const>20</const>
                        and
                        <const>60</const>
                        meters (for now it's
                        <const>40</const>
                        )<br>
                        <br>Allotted response time to output
                        is <=
                        <const>50</const>
                        ms.
                        <br>Allotted response time to output for the first turn
                        is <=
                        <const>1000</const>
                        ms.
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
