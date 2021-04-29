<!--suppress ALL, HtmlUnknownTarget -->
<!-- LEAGUES level1 level2 level3 -->
<div id="statement_back" class="statement_back" style="display:none"></div>
<div class="statement-body">

    <!-- BEGIN level1 -->
    <div>
        Welcome to clash of bots ! <br>
        If you you are lost and don't understand how you are supposed to extract data from the basic code,
        make sure to check <a
            href="https://tech.io/playgrounds/57689/how-to-extract-values-from-the-game-loop-in-clash-of-bots"> this
        playground </a>.
        It'll help you to get started.
    </div>
    <!-- END -->
    <!-- GOAL -->
    <div class="statement-section statement-goal">
        <h1>
            <span class="icon icon-goal">&nbsp;</span>

            <span>The Goal</span>
        </h1>
        <div class="statement-goal-content">
            In this game you have to destroy the enemy team, while keeping at least one of your bots alive. <br>Unfortunately,
            your bots aren't very smart and cannot provide you that much information as X,Y coordinates or their exact
            health, so you'll have to exploit
            the little information given to you.

            <br>
            But don't worry your bots get smarter and can give you more info in the following leagues
            <br>

        </div>
    </div>
    <!-- RULES -->
    <div class="statement-section statement-rules">
        <h1>
            <span class="icon icon-rules">&nbsp;</span>
            <span>Rules</span>
        </h1>
        <div>
            <div class="statement-rules-content">You begin the game with certain amount of bots<br>
                A lot of information are distance between entities. The game use 4 different <b>range</b> :
                <ul>
                    <li>0 : Short range (dist <=
                        <const>3 m</const>
                        )
                    </li>
                </ul>
                <ul>
                    <li>1 : Medium range (
                        <const>3 m</const>
                        < dist <=
                        <const>8 m</const>
                        )
                    </li>
                </ul>
                <ul>
                    <li>2 : Long range (
                        <const>8 m</const>
                        < dist <=
                        <const>15 m</const>
                        )
                    </li>
                </ul>
                <ul>
                    <li>3 : Out of range or "OOR" (dist >
                        <const>15 m</const>
                        )
                    </li>
                </ul>

                <br>

                The Arena is a square, bot cannot get out of the Arena
                <ul>
                    <li>There is nothing on the map for now</li>
                </ul>
                <br>
                Your bots have 2 health bar :
                <ul>
                    <li>The blue one represents your shield, if you don't take any damage during 12 frames your shield
                        will regen every frame. If you start regenerating an empty shield, it'll take 12 frame to be
                        completely restored
                    </li>
                </ul>
                <ul>
                    <li>The purple one represents your health. It cannot be regenerated but will decrease only if you
                        take more damage than your current shield
                    </li>
                </ul>
                <br>
                Your bots are not smart enough to give you the exact shield and health value of robots on the map.
                So they use an approximation :
                <ul>
                    <li>For the health they give you
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
                        <const>= 25%</const>
                        but
                        <const>< 50%</const>
                        of your max life
                    </li>
                    <li>For the shield they give you Can be
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
                        meaning that your shield is
                        <const> >= 1%</const>
                        and
                        <const>< 25%</const>
                        of your max shield and
                        <const>0</const>
                        that you have no more shield left
                    </li>

                </ul>
                Your bots can perform 3 different actions :
                <ul>
                    <li><b>ATTACK</b> one enemy bot. Your damage are done depending on the <b>range</b> your enemy is at
                        (short
                        medium long or close). The closer it is the more
                        damage you'll do. If you're attacking an enemy OOR, you won't deal any damage. For more details,
                        see expert rules.
                    </li>

                    <li><b>MOVE </b>to a group of bots, which makes you moving forward to the average position of the
                        group
                    </li>

                    <li><b>FLEE </b> from a group of bots, which makes you moving backward from the average position of
                        the
                        group
                    </li>
                    <li><b>IDLE </b> The bot wait for this frame and start thinking about the meaning of life. It's the
                        default action if you don't send an order for a bot.
                    </li>
                </ul>
                <br>
                You <strong>lose</strong> if:
                <ul>
                    <li>All your bots got destroyed</li>
                    <li>Your enemy destroyed more bots than you at the end of the match</li>
                    <li>You send invalid orders to your bots making their operating system crash (but they are smart
                        enough to tell you why you made them crash, so be sure to check the little dot in the replay)
                    </li>

                </ul>
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
            A frame duration for the simulation is 250 ms
            <br>
            Concerning <strong>attack</strong> :
            <ul>
                <li>In order to fire bullet at an opponent, a bot has to attack the <b>same target</b> for <b>aimDuration</b>
                    frames. Then it'll fire <b>bullet per shot</b> bullets each frame for <b>shot duration</b> frames
                </li>
                <li>When a bullet is created the robot determine if it'll hit based on the <b>current</b> range of the
                    target with a probability of <b>precision [target range]</b>. Note that attacking OOR has a 0%
                    precision.
                </li>
                <li> The game is determinist, even if there is some RNG in shots, the 2 teams have the same random seed
                    to determine if their shot will hit.
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
            <table class="tableizer-table" ;
            >
                <thead>
                <tr class="tableizer-firstrow" ;>
                    <th>Bot class</th>
                    <th>Damage per bullet</th>
                    <th>Bullet per shot</th>
                    <th>Aim duration (frame)</th>
                    <th>Shot duration (frame)</th>
                    <th>Precision short range</th>
                    <th>Precision mid range</th>
                    <th>Precision long range</th>
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
                    <td>95%</td>
                    <td>55%</td>
                    <td>15%</td>
                    <td>1.2</td>
                    <td>5000</td>
                    <td>3000</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>

    <!-- WARNING -->
    <div class="statement-section statement-warning">
        <h1>
            <span class="icon icon-warning">&nbsp;</span>
            <span>Note</span>
        </h1>
        <div class="statement-warning-content">
            You can navigate within the map in the same way you would on google maps: zoom/unzoom with the mouse wheel
            and move using drag'n drop.
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
                        it a look</a></strong>
                    <br> <br>
                </div>
            </div>
        </div>
    </div>
    <!-- PROTOCOL -->
    <div class="statement-section statement-protocol">
        <h1>
            <span class="icon icon-protocol">&nbsp;</span>
            <span>Game Input</span>
        </h1>
        <!-- Protocol block -->
        <div class="blk">
            <div class="title">Initialization input</div>
            <div class="text">
                <span class="statement-lineno"> Line 1: </span> one integer <var>botPerPlayer</var>,
                the amount of bot controlled by each players at the beginning of the game
                <br>
                <span class="statement-lineno"> Line 2: </span> one integer <var>mapSize</var>,
                the map size in meters. It's here just in case I need to change it during the contest.
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

                <const><!-- BEGIN level1 -->6 <!-- END --> <!-- BEGIN level2 -->8 <!-- END --> <!-- BEGIN level3 -->13
                    <!-- END --></const>
                information : <var>entId</var>, <var>entType</var>, <var>health</var>, <var>shield</var>,
                <var>action</var>, <var>targets</var><!-- BEGIN level2 level3 -->, <var>distEn</var>,
                <var>borderDist</var>
                <!-- END --><!-- BEGIN level3 -->, <var>distEnRank</var>,
                <var>borderDistRank</var>, <var>shieldRank</var>, <var>healthRank</var>, <var>totalRank</var>
                <!-- END -->.
                <!-- BEGIN level1 level2 -->The others are just 0 and will be used in the next leagues. <!-- END -->
                <br>
                <br><var>entId</var> is the unique gameEntity id, stay the same for the whole game.
                <br>
                <var>entType</var> indicates the type of entity. The value can be:
                <ul style="padding-bottom: 0;">
                    <li>
                        <const>"ALLY"</const>
                        for one of your Robots
                    </li>
                    <li>
                        <const>"ENEMY"</const>
                        for an enemy Robot
                    </li>

                </ul>
                <var>health</var>, <var>shield</var> for the approximate entity's health and shield.
                <br>
                <br>
                <var>action</var> indicates the action executed by the robot last turn. (
                <const>none</const>
                if the entity is not a robot) The value can be
                <const>"ATTACK"</const>
                ,
                <const>"MOVE"</const>
                ,
                <const>"FLEE"</const>
                ,
                <const>"IDLE"</const>
                <br> <var>targets</var> is the list of the entities id targeted by the robot last turn separated with
                <const>";"</const>
                :
                <const>"id1;id2;id3..."</const>
                if the gameEntity is a robot(the target for IDLE is the robot itself). Else
                <const>-1</const>
                .<!-- BEGIN level2 level3 -->
                <br>
                <var>distEn</var> : the range from the closest enemy. If the entity is an enemy bot it returns the
                distance to its closest ally
                <br>
                <var>borderDist</var> : the range from the closest border (among the left, right, top and bottom one)
                <!-- END -->
                <!-- BEGIN level3 -->
                The next values are <strong> ranks</strong> : Entites are sorted per an attribute in <strong>ascending
                order</strong> :
                <ul>
                    <li>
                        <var>distEnRank</var> : the attribute is the real distance between the entity and the closest
                        enemy.
                        If the entity is an enemy bot the distance is calculated based on its distance to its closest
                        ally
                    </li>
                    <li>
                        <var>borderDistRank</var> : the attribute is the real distance between the entity and the
                        closest border.

                    </li>
                    <li><var>shieldRank</var> : the attribute is the real shield value </li>
                    <li><var>healthRank</var> : the attribute is the real health value </li>
                    <li><var>totalRank</var> : the attribute is the sum of real health and shield </li>
                </ul>
                <!-- END -->

                <br><br><br> Then all of your ally become one after the other <strong>on air</strong>. An <strong>on
                air </strong> ally give for each entity
                <const><!-- BEGIN level1 --> 4 <!-- END --> <!-- BEGIN level2 level3 --> 7 <!-- END -->
                </const>
                information from <strong><u>its perspective :</u></strong> <var>entId</var>, <var>entType</var>, <var>distMe</var>,
                <var>distMeRank</var>
                <!-- BEGIN level2 level3 -->, <var>shieldComp</var>, <var>healthComp</var>, <var>totComp</var>
                <!-- END -->. <!-- BEGIN level1 -->The others are just 0 and will be used in the next leagues.
                <!-- END -->
                For the first iteration, it sends its own information so <var>entType</var> is
                <const> "SELF"</const>
                <i>so that you can get the <strong> on air</strong> ally id</i>. Then it sends information about other
                allies then enemies.
                <br>
                <br><var>entId</var> is the unique gameEntity id.
                <br> <var>entType</var>
                indicates the type of entity. The value can be:
                <ul style="padding-bottom: 0;">
                    <li>
                        <const>"ALLY"</const>
                        for one of your Robots
                    </li>
                    <li>
                        <const>"ENEMY"</const>
                        for an enemy Robot
                    </li>
                    <li>
                        <const> "SELF"</const>
                        for the robot <strong> on air </strong></li>

                </ul>
                <var>distMe</var> for the range at which the robot is from your <strong> on air </strong> robot.
                <const>0</const>
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
                <br>
                <var>distMeRank</var> the rank of the entity in a ranking based on the exact distance between the
                entities and the <strong> on air </strong> robot
                <br>
                <!-- BEGIN level2 level3 -->
                <var>shieldComp</var>, <var>healthComp</var>, <var>totComp</var>
                compare an attribut between the <strong>ON AIR</strong> and the other entity if the entity is a robot.
                <var>shieldComp</var> compare the <strong>shields</strong> of the 2 bots,
                <var>healthComp</var> compare the healths of the 2 bots,
                <var>totComp</var> compare the sum of healths and shields of the 2 bots.
                <br>
                The comps can be either :
                <ul style="padding-bottom: 0;">
                    <li>
                        <const>-1</const>
                        if the other robot has more attribut than the <strong>ON AIR </strong> bot
                    </li>
                    <li>
                        <const>0</const>
                        if the other robot has the same value for the attribut than the <strong> ON AIR </strong> bot
                    </li>
                    <li>
                        <const>1</const>
                        if the other robot has less attribut than the <strong>ON AIR </strong> bot
                    </li>
                </ul>
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
                    <br> An order has to follow the following synthax :
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
                            allies
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
                    If you send 2 differents action concerning the same bot you'll loose the game because your bot will
                    run out of memory (┬┬﹏┬┬)
                    <br> If you don't send any input all your bots will
                    <action> IDLE</action>
                    . If you don't send any order concerning one or more of your bot, they will
                    <action>IDLE</action>

                </div>

                <!-- Protocol block -->
                <div class="blk">
                    <div class="title">Constraints</div>
                    <div class="text"><var>The mapsize</var> is a square of size between
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