<!--suppress ALL, HtmlUnknownTarget -->
<!-- LEAGUES level1 level2 level3 -->

<div id="statement_back" class="statement_back" style="display:none"></div>
<div class="statement-body">
    <div style="color: #7cc576;
background-color: rgba(124, 197, 118,.1);
padding: 20px;
margin-right: 15px;
margin-left: 15px;
margin-bottom: 10px;
text-align: left;">
        <div style="text-align: center; margin-bottom: 6px">
            <img src="//cdn.codingame.com/smash-the-code/statement/league_wood_04.png"/>
        </div>

        <!-- BEGIN level1 -->
        <p style="text-align: center; font-weight: 700; margin-bottom: 6px;">
            Ce challenge se déroule en <b>ligues</b>.
        </p>
        <!-- END -->
        <!-- BEGIN level2 level3 level4 -->

        <!-- END -->

        <div class="statement-league-alert-content">
            <!-- BEGIN level1 -->
            Pour ce challenge, plusieurs ligues pour le même jeu seront disponibles. Quand vous aurez prouvé votre valeur
            contre le premier Boss, vous accéderez à la ligue supérieure et débloquerez de nouveaux adversaires.
            <br><br>
            <!-- END -->
            <p style="text-align: center; font-weight: 700; margin-bottom: 6px;">
                <b>Kit de démarrage</b>
            </p>
            Des IAs de base sont disponibles dans le
            <a target="_blank" href="https://github.com/Butanium/Clash-of-bits/tree/master/starterAIs">kit de démarrage</a>.
            Elles peuvent vous aider à appréhender votre propre IA.
            <br><br>
        </div>

    </div>
    <!-- BEGIN level1 -->
    <div>
        <div style="text-align: left;padding-left: 15px">
            Bienvenue dans <b>Clash of Bits</b> ! <br>
            Si après avoir lu la présentation, tu es un peu perdu et que tu ne sais pas trop comment faire
            une IA basique, essaie de jeter un coup d' œil à ce
            <a href="https://tech.io/playgrounds/e3215408eac2f4587826d3335d3c402597548/building-a-basic-ai-for-clash-of-bits">
                playground</a> : cela t'aidera à commencer !
        </div>
    </div>
    <br>
    <!-- END -->
    <!-- GOAL -->
    <div class="statement-section statement-goal">
        <h1>
            <span class="icon icon-goal">&nbsp;</span>

            <span>But du jeu</span>
        </h1>
        <div class="statement-goal-content">
            Dans ce jeu, le but est de détruire l'équipe ennemie tout en gardant au moins l'un de ses bots en vie.
            Si le temps est écoulé, l'équipe qui a détruit le plus de bots gagne.
        </div>
    </div>
    <!-- RULES -->
    <div class="statement-section statement-rules">
        <h1>
            <span class="icon icon-rules">&nbsp;</span>
            <span>Règles</span>
        </h1>
        <div>
            <div class="statement-rules-content">
                Vos bots combattent dans une arène carrée dont ils ne peuvent s'échapper.
                Malheureusement, ils ne sont pas très malins et ne peuvent pas vous fournir des informations
                aussi précises que les coordonnées cartésiennes des bots ennemies ou leur nombre de points de vie exact.
                Vous allez donc devoir exploiter le peu d'informations qu'ils pourront vous donner...
                <!-- BEGIN level1 level2 -->
                <br>
                Mais ne vous inquiétez pas, vos bots deviendront plus intelligents et pourront vous donner plus
                d'informations dans les ligues suivantes !
                <!-- END -->
                <br><br>
                <!-- BEGIN level2 level3 -->
                <div class="statement-new-league-rule"><p>
                    Dans cette ligue, vos bots ont réalisé qu'ils pouvaient calculer et vous donner 5
                    informations supplémentaires. Plus de détails dans la section <a href="#game_input">entrées du jeu</a>.
                </div>
                <!-- END -->
                Beaucoup d'informations sont des distances entre 2 bots.
                Vos bots utilisent 4 <b>portées</b> différentes pour décrire ces distances :
                <ul>
                    <li>
                        <b>0</b> : Courte portée (dist ≤ <const>3 m</const>)
                    </li>
                </ul>
                <ul>
                    <li>
                        <b>1</b> : Moyenne portée (<const>3 m</const> < dist ≤ <const>8 m</const>)
                    </li>
                </ul>
                <ul>
                    <li>
                        <b>2</b> : Longue portée (<const>8 m</const> < dist ≤ <const>15 m</const>)
                    </li>
                </ul>
                <ul>
                    <li>
                        <b>3</b> : Hors de portée (dist > <const>15 m</const>)
                    </li>
                </ul>

                <br>
                <br>
                Vos bots ont 2 barres de vie :
                <ul>
                    <li>
                        Une bleue pour leur bouclier. Si un bot ne prend pas de dégâts pendant 12 tours de jeu, son
                        bouclier commence à se régénérer. Un bouclier complètement vide met 12 tours sans dégâts pour
                        se recharger.
                    </li>
                </ul>
                <ul>
                    <li>
                        Une violette pour leurs points de vie. Les points de vie ne se régénèrent pas, mais ne baissent
                        que si le bouclier est vide.
                    </li>
                </ul>
                <br>
                Vos bots ne sont pas assez intelligents pour vous donner des valeurs exactes pour
                les boucliers et le nombre de points de vie des bots sur la carte.
                Ils utilisent donc des approximations :
                <ul>
                    <li>
                        Pour les points de vie, ils donnent
                        <const>0</const>
                        |
                        <const>25</const>
                        |
                        <const>50</const>
                        |
                        <const>75</const>
                        |
                        <const>100</const> ,
                        <const>25</const>
                        correspondant à une santé
                        <const>≥ 25%</const>
                        mais
                        <const>< 50%</const>
                        du nombre maximal de points de vie
                    </li>
                    <li>
                        Pour les boucliers, ils donnent :
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
                        quand le bouclier est
                        <const> > 0%</const>
                        et
                        <const>< 25%</const>
                        du bouclier maximal et
                        <const>0</const>
                        que le bouclier est vide
                    </li>

                </ul>
                Vos bots peuvent faire 3 actions différentes :
                <ul>
                    <li>
                        <b>ATTACK</b> pour attaquer un bot ennemi. Les dégâts infligés dépendent de la <b>portée</b>
                        à laquelle vous tirez (courte, moyenne ou longue). Les dégâts étant maximaux à courte portée.
                        Notez qu'attaquer un ennemi hors de portée n'inflige aucun dégât. Pour plus de détails,
                        consulter les <a href="#expert-rules">détails techniques</a>.
                    </li>

                    <li>
                        <b>MOVE</b> pour se déplacer vers un groupe de bots. Votre bot se déplacera
                        vers la position moyenne du groupe. Si un groupe n'est composé que d'une seule cible,
                        le bot se déplacera donc dans la direction dudit bot.
                    </li>

                    <li>
                        <b>FLEE </b> pour fuire un groupe de bots. Le bot s'éloigne de la position moyenne du
                        groupe.
                    </li>
                    <li>
                        <b>IDLE </b> Le bot attend tout en réfléchissant au sens de sa vie.
                        <br><i>
                        "Un bot doit obéir aux ordres donnés par les êtres humains, sauf si de tels ordres entrent en
                        contradiction avec la première loi."
                    </i><br>
                        C'est l'action par défaut qu'exécutera le bot si vous ne lui donnez pas d'ordres.
                    </li>
                </ul>
                <!-- BEGIN level2 level3 -->
                <br>
                <!-- BEGIN level2 -->
                <div class="statement-new-league-rule">
                    <strong>
                        Maintenant vos bots commencent à des emplacements aléatoires dans l'arène.
                    </strong>
                    <br>
                    <br>
                    <!-- END -->
                    <!-- BEGIN level3 -->
                    Les bots sont placés aléatoirement dans l'arène.
                    <!-- END -->
                    Les emplacements des deux équipes sont <strong>symétriques</strong>. La symétrie peut être :
                    <ul>
                        <li>
                            <strong>centrale</strong> (par rapport au centre de l'arène)
                        </li>
                        <li>
                            <strong>horizontale</strong> (par rapport à la ligne horizontale passant par le centre de
                            l'arène).
                        </li>
                    </ul>
                    Il y a 2 règles que ces emplacements de départ respecteront toujours :
                    <ul>
                        <li>
                            La distance entre 2 bots doit être d'au moins <strong>2 mètres</strong>
                        </li>
                        <li>
                            La distance entre 2 bots d'équipes différentes doit être d'au moins <strong>8.1 mètres</strong>
                        </li>
                    </ul>
                <!-- BEGIN level2 -->
                </div>
                <!-- END -->
                <!-- END -->
                <br>
                <!-- Victory conditions -->
                <div class="statement-victory-conditions">
                    <div class="icon victory"></div>
                    <div class="blk">
                        <div class="title">Conditions de Victoire</div>
                        <div class="text">
                            <ul style="padding-top:0; padding-bottom: 0;">
                                <li>
                                    Vous avez détruit tous les robots de votre adversaire
                                </li>
                                <li>
                                    Vous avez plus de robot en vie que votre adversaire après
                                    <strong>
                                        <const>300</const>
                                        tours.
                                    </strong>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <!-- Lose conditions -->
                <div class="statement-lose-conditions">
                    <div class="icon lose"></div>
                    <div class="blk">
                        <div class="title">Conditions de Défaite</div>
                        <div class="text">
                            <ul style="padding-top:0; padding-bottom: 0;">
                                <li>Tous vos bots sont détruits</li>
                                <li>
                                    Vous envoyez des ordres invalides à vos bots, faisant ainsi exploser leur cerveau positronique.
                                    <br>
                                    <i>Heureusement, ils sont assez intelligents pour vous dire pourquoi ils ont explosé, pensez à passer votre souris sur la petite pastille dans le replay.</i>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="statement-section statement-rules">
        <h1>
            🐞 Conseils de débogage
        </h1>
        <div class="statement-warning-content">
            Dans ce jeu, il y a beaucoup de fonctionnalités pour vous aider à comprendre ce qui se passe dans l'arène :
            <div class="statement-example-container">
                <div class="statement-example">
                    <img src="https://cdn-games.codingame.com/community/4099691-1619052595646/73304b9b2a7922e90a5afbd0c294f778e16b56d2d0892e375b34a7303deaf409.png">
                    <div class="legend">
                        <div class="description">
                            Vous pouvez activer le zoom dynamique de la caméra avec l'interrupteur <b>camera mode</b>.
                        </div>
                    </div>
                </div>
                <div class="statement-example">
                    <img src="https://cdn-games.codingame.com/community/4099691-1619052595646/c290091469a911be12204aefc90c47d95ed7e05167111a8d74fed1e46d2577d3.png">
                    <div class="legend">
                        <div class="description">
                            Les différentes
                            <const>portées</const>
                            sont affichées lorsque vous passez la souris sur un bot. <br>
                            Vous pouvez aussi voir la
                            <const>cible</const>
                            du bot, son
                            <const>action</const> actuelle. Le tooltip donne plus d'informations sur le bot.
                        </div>
                    </div>
                </div>
                <div class="statement-example">
                    <img src="https://cdn-games.codingame.com/community/4099691-1619052595646/35c51763ed79a37a817574006bef28dae3235dd230a284de2c6ffc19f7726c0f.png">
                    <div class="legend">
                        <div class="description">
                            Vous pouvez
                            <const>CLIC GAUCHE</const>
                            sur un bot pour faire apparaître la cible de manière permanente et
                            <const>ALT + CLIC GAUCHE</const> n'importe où sur le lecteur pour supprimer toutes les cibles.
                        </div>
                    </div>
                </div>

                <div class="statement-example">
                    <img src="https://cdn-games.codingame.com/community/4099691-1619052595646/51e96c95edb1532dd2f2b2e20d51ad52ff1cac8ef09e36d94763719937ec8acd.png">
                    <div class="legend">
                        <div class="description">
                            Vous pouvez activer l'overlay de débogage avec l'interrupteur <b>debug overlay</b>
                            pour voir l'id du bot et son action actuelle.
                        </div>
                    </div>
                </div>

                <div class="statement-example">
                    <img src="https://cdn-games.codingame.com/community/4099691-1619052595646/1710e57847d75cc01f517521d99b37b0e740bbedc080ec2969d5d7bc7ff1f969.png">
                    <div class="legend">
                        <div class="description">
                            Les marqueurs de dégâts sont affichés lorsqu'un bot est touché par une balle.
                        </div>
                    </div>
                </div>

            </div>
        </div>


    </div>

    <!-- EXPERT RULES -->
    <div class="statement-section statement-expertrules">
        <h1>
            <span class="icon icon-expertrules">&nbsp;</span> <span><a id="expert-rules">Détails techniques </a></span>
        </h1>
        <div class="statement-expert-rules-content">
            Le temps entre chaque tour de jeu est 250 ms
            <br>
            En ce qui concerne l'<strong>attaque</strong> :
            <ul>
                <li>
                    Pour tirer des balles sur un ennemi, un bot doit attaquer <b>la même cible</b> pendant
                    <b>temps de visée</b> tours. Ensuite, il tirera <b>balles par tir</b> balles chaque tour pendant
                    <b>temps de tir</b> tours.
                </li>
                <li>
                    À l'instant où une balle est tirée, le moteur de jeu détermine si elle va toucher sa cible ou non en
                    fonction
                    de sa portée <b>actuelle</b> avec une probabilité de <b>précision [portée de la cible]</b>.
                    N'oubliez pas qu'attaquer une cible hors de portée ne peut pas toucher.
                </li>
                <li>
                    Le jeu est "déterministe", même s'il y a de l'aléatoire dans les tirs, les deux équipes ont la
                    même "seed" de random pour savoir si les tirs vont toucher. À chaque fois qu'un bot tire, il
                    prend la prochaine valeur de la seed de son équipe pour savoir s'il touche.
                    <br>
                    Un match entre 2 équipes identiques sera donc <b>toujours</b> un match nul -
                    sauf si l'IA utilise le fait que les ids des bots commencent à 0 pour une équipe et 7 pour l'autre dans ses décisions.
                </li>


            </ul>
            Voici les caractéristiques des différentes classes de bots.

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
                        <th>Classe de bot</th>
                        <th>Dommages par balle</th>
                        <th>Balles par tirs</th>
                        <th>Temps de visée (tour)</th>
                        <th>Temps de tir (tour)</th>
                        <th>Précision à <br>courte / moyenne / longue portée</th>
                        <th>Vitesse</th>
                        <th>points de vie max</th>
                        <th>Bouclier max</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr align="center" ;>
                        <td>Assaut</td>
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
            Vous pouvez consulter le code source du jeu sur mon <a href="https://github.com/Butanium/Clash-of-bits">dépôt
            GitHub</a>.
        </div>
    </div>

    <!-- STORY -->
    <div class="statement-story-background">
        <div class="statement-story-cover"
             style="background-size: cover; background-color: grey">
            <div class="statement-story">
                <h1>D'où est-ce que vient cette idée ?</h1>
                <div class="story-text">
                    <strong>
                        Ce challenge est très fortement inspiré du jeu "GLADIABOTS".
                        <br>
                        On se rapproche en réalité plus de la copie que de l'inspiration, c'est pour cela que j'ai
                        demandé la permission de son développeur indépendant GFX47. Si le jeu vous intrigue où que vous
                        cherchez de nouvelles stratégies à expérimenter, n'hésitez pas à y
                        <a href="https://gladiabots.com/">jeter un coup d'œil</a> !
                    </strong>
                    <br> <br>
                </div>
            </div>
        </div>
    </div>
    <div style="margin-top: 10px; margin-bottom: 10 px; margin-left: 20px; margin-right:20px">
        <i>
            Je voudrais remercier <a href="https://www.codingame.com/profile/8374201b6f1d19eb99d61c80351465b65150051">eulerscheZahl</a>
            pour ses précieux conseils lorsque j'ai commencé ce projet, <a href="https://twitter.com/gfx47">GFX47</a> qui a
            accepté que je copie beaucoup de ses idées et <a href="https://github.com/DamnSake">DamnSake</a> et <b>Deniw</b>
            qui m'ont aidé pour les graphismes.
        </i>
    </div>
    <!-- PROTOCOL -->
    <div class="statement-section statement-protocol">
        <h1>
            <span class="icon icon-protocol">&nbsp;</span>
            <span><a id="game_input">Entrées du jeu</a></span>
        </h1>
        <!-- Protocol block -->
        <div class="blk">
            <div class="title">Entrées d'initialisation</div>
            <div class="text">
                <span class="statement-lineno"> Ligne 1 : </span> un nombre entier <var>botPerPlayer</var>,
                le nombre de bots contrôlés par joueur au début de la partie
                <br>
                <span class="statement-lineno"> Ligne 2 : </span> un nombre entier <var>mapSize</var>,
                la taille de la carte en mètre. C'est ici au cas où la taille de la carte vienne à changer
                durant le challenge.
                <br>
            </div>
        </div>

        <!-- Protocol block -->
        <div class="blk">
            <div class="title">Entrée dans un tour de jeu</div>
            <div class="text">
                <span class="statement-lineno">Première ligne : </span>un nombre entier <var>allyBotAlive</var>,
                le nombre de vos bots qui sont encore en vie.
                <br>
                <span class="statement-lineno">Ligne suivante</span> : un nombre entier <var>botCount</var>,
                le nombre de bots dans l'arène
                <br>
                <br>
                <br> Pour chaque bot, vos bots vous envoient
                <!-- BEGIN level1 -->
                <const>6</const>
                <!-- END -->
                <!-- BEGIN level2 -->
                <const> 8</const>
                <!-- END -->
                <!-- BEGIN level3 -->
                <const> 13</const>
                <!-- END -->
                informations : <var>botId</var>, <var>botType</var>, <var>health</var>, <var>shield</var>,
                <var>action</var>, <var>targets</var>
                <!-- BEGIN level2 level3 -->
                , <var>enemyRange</var>,
                <var>borderDist</var>
                <!-- END -->
                <!-- BEGIN level3 -->
                , <var>distEnRank</var>,
                <var>borderDistRank</var>, <var>shieldRank</var>, <var>healthRank</var>, <var>totalRank</var>
                <!-- END -->
                .
                <!-- BEGIN level1 level2 -->
                Les autres sont juste 0 et ne seront utilisées que dans les prochaines ligues.
                <!-- END -->
                Les portées sont données par un entier comme suit :
                <const>0</const>
                pour
                <const>COURTE PORTÉE</const>
                ,
                <const>1</const>
                pour
                <const>MOYENNE PORTÉE</const>
                ,
                <const>2</const>
                pour
                <const>LONGUE PORTÉE</const>
                et
                <const>3</const>
                pour
                <const>HORS DE PORTÉE</const>.
                <br>
                <br><var>botId</var> est l'id unique du bot, il sera le même pendant toute la partie.
                <br><br>
                <var>botType</var> indique le type du bot. Cela peut être :
                <ul style="padding-bottom: 0;">
                    <li>
                        <const>"ALLY"</const>
                        pour l'un de vos bots
                    </li>
                    <li>
                        <const>"ENEMY"</const>
                        pour un bot ennemi
                    </li>

                </ul>
                <var>health</var>, <var>shield</var> pour la santé et le bouclier approximatif du bot
                si c'est un bot, 0 sinon.
                <br>
                <br>
                <var>action</var> indique l'action réalisée par le bot au tour précédent.
                Les différentes valeurs possibles sont :
                <const>"ATTACK"</const>
                ,
                <const>"MOVE"</const>
                ,
                <const>"FLEE"</const>
                ,
                <const>"IDLE"</const>.
                <br>
                <br>
                <var>targets</var> est la liste des ids des bots visés par la dernière action du bot au dernier
                tour. Les ids sont séparés par des
                <const>","</const> :
                <const>"id1,id2,id3..."</const>
                <!-- si l'entité est un bot --> (la cible de IDLE étant le bot lui-même). <!--Sinon
                <const>-1</const>
                .-->
                <!-- BEGIN level2 level3 -->
                <br> <br>
                <!-- BEGIN level2 -->
                <div class="statement-new-league-rule">
                    <!-- END -->
                    <var>enemyRange</var> : la portée à laquelle est le plus proche ennemi. Si le bot est dans l'équipe ennemie,
                    c'est la portée du plus proche allié qui est donnée.
                    <br><br>
                    <var>borderDist</var> : la portée à laquelle est le plus proche côté de l'arène (entre gauche,
                    droit, haut, bas).
                    <!-- BEGIN level2 -->
                </div>
                <!-- END -->
                <!-- END -->
                <!-- BEGIN level3 -->
                <br>
                <br>
                <div class="statement-new-league-rule">
                    Les données suivantes sont des <strong>rangs</strong> : les bots sont triés selon les différents attributs par <strong>ordre croissant</strong> :
                    <ul>
                        <li>
                            <var>distEnRank</var> : L'attribut considéré est leur distance au bot ennemi
                            le plus proche. Si le bot est dans l'équipe ennemie, la distance considérée est celle à son plus
                            proche allié.
                        </li>
                        <li>
                            <var>borderDistRank</var> : l'attribut est la distance au plus proche côté.
                        </li>
                        <li>
                            <var>shieldRank</var> : l'attribut est la valeur exacte du bouclier.
                        </li>
                        <li>
                            <var>healthRank</var> : l'attribut est le nombre exact des points de vie.
                        </li>
                        <li>
                            <var>totalRank</var> : l'attribut est la somme du nombre exact de points de vie et de bouclier
                        </li>
                    </ul>
                </div>
                <!-- END -->
                <!-- A noter que pour ces 3 derniers attributs les entités qui ne sont pas des bots ont le rang
                <const>-1</const> -->

                <br><br><br>
                Ensuite, vos bots deviennent chacun leur tour <strong>actifs</strong>.
                Un bot <strong>actif</strong> donne pour chaque bot dans l'arène
                <!-- BEGIN level1 -->
                <const> 4</const>
                <!-- END -->
                <!-- BEGIN level2 level3 -->
                <const> 7</const>
                <!-- END -->
                données calculées selon <strong><u>sa perspective :</u></strong> <var>botId</var>, <var>botType</var>,
                <var>range</var>, <var>distMeRank</var>
                <!-- BEGIN level2 level3 -->
                , <var>shieldComp</var>, <var>healthComp</var>, <var>totComp</var>
                <!-- END -->
                .
                <!-- BEGIN level1 -->
                Les autres sont juste 0 et seront utilisés dans les ligues suivantes.
                <!-- END -->
                Lors de la première itération, le bot envoie les données le concernant.
                Donc <var>entType</var> est
                <const> "ON_AIR"</const>
                <i>vous pouvez ainsi récupérer l'id du bot <strong>actif</strong></i>.
                Ensuite, le bot vous envoie les informations sur chaque bot.
                <br>
                <br><var>botId</var> est l'id unique du bot.
                <br><br> <var>botType</var>
                indique le type du bot. Cela peut être :
                <ul style="padding-bottom: 0;">
                    <li>
                        <const>"ALLY"</const>
                        pour un bot allié
                    </li>
                    <li>
                        <const>"ENEMY"</const>
                        pour un bot ennemi
                    </li>
                    <li>
                        <const> "ON_AIR"</const>
                        pour le bot<strong> actif</strong>
                    </li>
                </ul>
                <var>range</var> la portée à laquelle se trouve le bot par rapport au bot
                <strong> actif </strong>
                <br><br>
                <var>distMeRank</var> le rang du bot dans un classement du plus proche au plus loin du bot
                <strong>actif </strong> basé sur la distance exacte les séparant.
                <br>
                <!-- BEGIN level2 level3 -->
                <br>
                <!-- BEGIN level2 -->
                <div class="statement-new-league-rule">
                    <!-- END -->
                    <var>shieldComp</var>, <var>healthComp</var>, <var>totComp</var>
                    comparent un attribut entre le bot <strong>actif</strong> et un autre bot.
                    <br><var>shieldComp</var> compare la valeur exacte des <strong>bouclier</strong> des 2 bots,
                    <var>healthComp</var> compare la santé exacte des 2 bots,
                    <var>totComp</var> compare la somme de la santé et du bouclier des 2 bots.
                    <br>
                    Ces variables peuvent prendre les valeurs :
                    <ul style="padding-bottom: 0;">
                        <li>
                            <const>-1</const>
                            si l'autre bot possède plus d'attributs que le bot <strong>actif</strong>
                        </li>
                        <li>
                            <const>0</const>
                            si l'autre bot possède autant d'attributs que le bot <strong>actif</strong>
                        </li>
                        <li>
                            <const>1</const>
                            si l'autre bot possède moins d'attributs que le bot <strong>actif</strong>
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
                <div class="title">Sortie pour un tour de jeu</div>
                <div class="text">
                    <div style="margin-bottom: 7px"><span class="statement-lineno">1 ligne</span> contenant tous les
                        ordres donnés à vos bots séparés par
                        <const>";"</const> :
                        <const> "ordre1;ordre2;ordre3;..."</const>.
                    </div>
                    Un ordre doit respecter le format suivant
                    <action>idDeVotreBot [ACTION] [CIBLES]</action>
                    <ul>
                        <li>
                            <action>ACTION</action>
                            doit être une action valide parmi :
                            <action>ATTACK</action>
                            ,
                            <action>MOVE</action>
                            ,
                            <action>FLEE</action>
                            ou
                            <action>IDLE</action>.
                        </li>
                        <li>
                            <action> CIBLES</action>
                            doit respecter le format suivant :
                            <action>cibleID1,cibleID2,cibleID3</action>
                            <br>
                            Vous devez aussi respecter certaines règles en fonction de l'action que vous souhaitez réaliser.
                            <ul>
                                <li>Si l'action est
                                    <action>ATTACK</action>,
                                    <action>CIBLES</action>
                                    doit contenir
                                    <const>1</const>
                                    <strong>id de bot ennemi</strong>, ni plus, ni moins. Un bot ne peut ni s'attaquer lui
                                    même<sup>1</sup> ni attaquer ses alliés.
                                    <br>
                                    1.
                                    <i>
                                        "Un bot doit protéger son existence dans la mesure où cette protection n'entre
                                        pas en contradiction avec la première ou la deuxième loi."
                                    </i>
                                </li>
                                <li>
                                    Si l'action est
                                    <action> MOVE</action>
                                    ou
                                    <action> FLEE</action>,
                                    <action>CIBLES</action>
                                    doit contenir au moins un id de bot.
                                </li>
                                <li>Si l'action est
                                    <action>IDLE</action>,
                                    <action>CIBLES</action>
                                    peut contenir tout et n'importe quoi (ou rien du tout), dans tous les cas, cela sera ignoré.
                                    <del> Cela vous permet de faire réfléchir vos bots au sens de la vie</del>
                                </li>
                            </ul>
                        <li>
                            Si vous envoyez 2 ordres différents s'adressant au même bot, vous perdrez la partie, car
                            votre bot subira le même sort que
                            <a href = "https://asimov.fandom.com/wiki/R._Jander_Panell">R. Jander Panell </a>,
                            entraînant dans sa chute toute votre équipe.
                        </li>
                        <li>
                            Si vous n'envoyez pas de sortie, tous vos bots feront l'action
                            <action>IDLE</action>.
                            Si vous n'envoyez pas d'ordre à un ou plusieurs bots, ils effectueront
                            <action>IDLE</action>.
                        </li>
                    </ul>
                </div>
                <br>
                <!-- Protocol block -->
                <div class="blk">
                    <div class="title">Contraintes</div>
                    <div class="text">L'arène est un carré de côté compris entre
                        <const>20</const>
                        et
                        <const>60</const>
                        mètres (pour le moment c'est toujours
                        <const>40</const>)
                        <br>
                        <br>Temps de réponse max par tour :
                        <const>50</const>
                        ms.
                        <br>Temps de réponse max pour le premier tour :
                        <const>1000</const>
                        ms.
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- SHOW_SAVE_PDF_BUTTON -->