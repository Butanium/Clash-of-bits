<!--suppress ALL, HtmlUnknownTarget -->
<!-- LEAGUES level1 level2 level3 -->
<div id="statement_back" class="statement_back" style="display:nun"></div>
<div class="statement-body">

    <!-- BEGIN level1 level2 -->
    <div>
        <div style="text-align: left;padding-left: 15px">

            Bienvenue dans <b>clash of bots</b> ! <br>
            Si après avoir lu la présentation, tu es un peu perdu et que tu ne sais pas trop que faire de toutes les
            données qui te sont fournies, essaie de jeter un coup d'oeil à ce
            <a href="https://tech.io/playgrounds/57689/how-to-extract-values-from-the-game-loop-in-clash-of-bots">
                playground </a> : cela t'aidera à commencer !
        </div>
        <br>
    </div>
    <!-- END -->
    <!-- GOAL -->
    <div class="statement-section statement-goal">
        <h1>
            <span class="icon icon-goal">&nbsp;</span>

            <span>But du jeu</span>
        </h1>
        <div class="statement-goal-content">
            Dans ce jeu, le but et de détruire l'équipe ennemie tout en gardant au moins l'un de ses robots en vie.
            <br>
            Malheureusement, vos robots ne sont pas très malin et ne peuvent pas vous fournir des informations
            aussi précise que les coordonnées cartésiennes des robots ennemies ou leur nombre de point de vies exacte.
            Vous allez donc devoir exploiter le peu d'informations qu'ils pourront vous donner...
            <br>
            Mais ne vous inquiétez pas, vos robots deviendront plus intelligents et pourront vous donner plus
            d'informations dans les ligues suivantes.
            <br>
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
                Vous commencez la partie avec un certain nombre de robots
                <br>
                Beaucoup d'informations sont des distance entre plusieurs entités.
                Le jeu utilise 4 différentes <b>portées</b> :
                <ul>
                    <li>0 : Courte portée (dist <=
                        <const>3 m</const>
                        )
                    </li>
                </ul>
                <ul>
                    <li>1 : Moyenne portée (
                        <const>3 m</const>
                        < dist <=
                        <const>8 m</const>
                        )
                    </li>
                </ul>
                <ul>
                    <li>2 : Longue portée (
                        <const>8 m</const>
                        < dist <=
                        <const>15 m</const>
                        )
                    </li>
                </ul>
                <ul>
                    <li>3 : Hors de portée (dist >
                        <const>15 m</const>
                        )
                    </li>
                </ul>

                <br>
                L'arène est un carré dont les robots ne peuvent sortir
                <ul>
                    <li>Les objets présents sur la carte sont appelés <b>entités</b>. Pour l'instant il n'y a que les
                        robots
                    </li>
                </ul>
                <br>
                Vos robots ont 2 barres de vie :
                <ul>
                    <li>
                        Une bleu pour leur bouclier. Si un robot ne prends pas de dégâts pendant 12 tours de jeu, son
                        bouclier commencer à se régénérer. Un bouclier complètement vide met 12 tours sans dégâts pour
                        se recharger
                    </li>
                </ul>
                <ul>
                    <li>
                        Un violet qui pour leurs points de vie. Les points de vie ne se régènerent pas mais ne baissent
                        que si le bouclier est vide.
                    </li>
                </ul>
                <br>
                Vos bots ne sont pas assez intelligents pour vous donner des valeurs exactes pour
                les bouclier et le nombre de points de vie des robots sur la carte.
                Ils utilisent donc des approximations :
                <ul>
                    <li>
                        Pour les points de vie ils donnent
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
                        correspondant à une santé
                        <const>>= 25%</const>
                        mais
                        <const>< 50%</const>
                        du nombre maximal de point de vie
                    </li>
                    <li>
                        Pour les boucliers ils donnent :
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
                        <const> >= 1%</const>
                        mais
                        <const>< 25%</const>
                        du bouclier maximal et
                        <const>0</const>
                        que le bouclier est vide
                    </li>

                </ul>
                Vos robots peuvent faire 3 actions différentes :
                <ul>
                    <li>
                        <b>ATTACK</b> pour attaquer un robot ennemi. Les dégâts infligés dépendent de la <b>portée</b>
                        à laquelle vous tirez (courte, moyenne ou longue). Les dégâts étant maximum à courte portée.
                        Notez qu'attaquer un ennemi hors de portée ne va faire aucun dégât. Pour plus de détails,
                        consulter les règles experts.
                    </li>

                    <li>
                        <b>MOVE</b> pour se déplacer vers un groupe d'entités, ce qui fait bouger votre robot
                        vers la position moyenne du groupe. Si un groupe n'est composé que d'une seule entité,
                        le robot se déplacera donc dans la direction de ladite entité.
                    </li>

                    <li>
                        <b>FLEE </b> pour fuire un groupe d'entités. Le robot s'éloigne de la position moyenne du
                        groupe.
                    </li>
                    <li>
                        <b>IDLE </b> Le robot attend tout en réfléchissant au sens de sa vie.
                        <br><i>
                        "Un robot doit obéir
                        aux ordres donnés par les êtres humains, sauf si de tels ordres entrent en contradiction avec
                        la première loi".
                    </i><br>
                        C'est l'action par défaut qu'exécutera le robot si vous ne lui donnez pas d'ordres.
                    </li>
                </ul>
                <br>
                Vous <strong>perdez</strong> si :
                <ul>
                    <li>Tous vos robots sont détruits</li>
                    <li>Votre ennemi a détruits plus de robots que vous à la fin de la partie</li>
                    <li>
                        Vous envoyez des ordres invalides à vos robots, faisant ainsi exploser le cerveau positronique.
                        <br>
                        Heureusement ils sont assez intelligents pour vous dire pourquoi ils ont explosé, pensez à
                        passer votre souris sur la petite pastille dans le replay.
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <!-- EXPERT RULES -->
    <div class="statement-section statement-expertrules">
        <h1>
            <span class="icon icon-expertrules">&nbsp;</span>
            <span>Règles experts</span>
        </h1>
        <div class="statement-expert-rules-content">
            Le temps entre chaque tours de jeu est 250 ms
            <br>
            En ce qui conrcerne l'<strong>attaque</strong> :
            <ul>
                <li>
                    Pour tirer des balles sur un ennemi, un robot doit attaquer <b>la même cible</b> pendant
                    <b>temps de visée</b> tours. Ensuite il tirera <b>balles par tir</b> balles chaque tour pendant
                    <b>temps de tir</b> tours.
                </li>
                <li>
                    A l'instant où une balle est tirée, le moteur de jeu détermine si elle va touchée sa cible ou non en
                    fonction
                    de sa portée <b>actuelle</b> avec une probabilité de <b>précision [portée de la cible]</b>.
                    N'oubliez pas qu'attaquer une cible hors dep portée ne peut pas toucher.
                </li>
                <li>
                    Le jeu est "déterministe", même s'il y a de l'aléatoire dans les tirs, les deux équipes ont la
                    même "seed" de random pour savoir si les tirs vont toucher. Un match entre 2 équipe identique
                    sera <b>toujours</b> un match nul.
                </li>


            </ul>
            Voici les caractéristiques des différentes classes de robots.

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
                        <th>Classe de robot</th>
                        <th>Dommages par balle</th>
                        <th>Balles par tirs</th>
                        <th>Temps de visée (tour)</th>
                        <th>Temps de tir (tour)</th>
                        <th>Precision à courte portée</th>
                        <th>Precision à moyenne portée</th>
                        <th>Precision à longue portée</th>
                        <th>Vitesse</th>
                        <th>Point de vie max</th>
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
            <br>
            Vous pouvez consulter le code source du jeu sur mon <a href="https://github.com/Butanium/Clash-of-bots">dépôt
            github</a>
        </div>
    </div>

    <!-- WARNING -->
    <div class="statement-section statement-warning">
        <h1>
            <span class="icon icon-warning">&nbsp;</span>
            <span>Note</span>
        </h1>
        <div class="statement-warning-content">
            Vous pouvez changer le mode de la caméra entre dynamique et statique dans le bouton "camera mode"
            des paramètres.
        </div>


    </div>
    <!-- STORY -->
    <div class="statement-story-background">
        <div class="statement-story-cover"
             style="background-size: cover; background-color: grey">
            <div class="statement-story">
                <h1>D'où est ce que vient cette idée ?</h1>
                <div class="story-text">
                    <strong>
                        Ce challenge est très pourtement inspiré du jeu "GLADIABOTS".
                        <br>
                        On se rapproche en réalité plus de la copie que de l'inspiration, c'est pour cela que j'ai
                        demandé la permission de son développeur indépendant GFX47. Si le jeu vous intrigue où que vous
                        cherchez de nouvelles stratégie à expérimenter, n'hésitez pas à y
                        <a href="https://gladiabots.com/">jeter un coup d'oeil</a>
                    </strong>
                    <br> <br>
                </div>
            </div>
        </div>
    </div>
    <!-- PROTOCOL -->
    <div class="statement-section statement-protocol">
        <h1>
            <span class="icon icon-protocol">&nbsp;</span>
            <span>Entrées du jeu</span>
        </h1>
        <!-- Protocol block -->
        <div class="blk">
            <div class="title">Entrées d'initialisation</div>
            <div class="text">
                <span class="statement-ligneno"> Ligne 1: </span> un nombre entier <var>botPerPlayer</var>,
                le nombre de bot contrôlé par joueur au début de la partie
                <br>
                <span class="statement-ligneno"> ligne 2: </span> un nombre entier <var>mapSize</var>,
                la taille de la carte en mètre. C'est ici au cas où la taille de la carte vienne à changer
                durant le challenge.
                <br>
            </div>
        </div>

        <!-- Protocol block -->
        <div class="blk">
            <div class="title">Entrée dans un tour de jeu</div>
            <div class="text">
                <span class="statement-ligneno">Première ligne: </span>un nombre entier <var>allyBotAlive</var>,
                le nombre de vos bots qui sont encore en vie.
                <br>
                <span class="statement-ligneno">Ligne suivante</span>: un nombre entier <var>totalEntities</var>,
                le nombre d'entités dans l'arène
                <br>
                <br>
                <br> Pour chaque entité, vos robots vous envoient

                <!-- BEGIN level1 -->
                <const> 6</const>
                <!-- END -->
                <!-- BEGIN level2 -->
                <const> 8</const>
                <!-- END -->
                <!-- BEGIN level3 -->
                <const> 13</const>
                <!-- END -->
                informations : <var>entId</var>, <var>entType</var>, <var>health</var>, <var>shield</var>,
                <var>action</var>, <var>targets</var>
                <!-- BEGIN level2 level3 -->
                , <var>distEn</var>,
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
                <br>
                <br><var>entId</var> est l'id unique de l'entité, il sera le même pendant toute la partie.
                <br><br>
                <var>entType</var> indique le type d'entité. Cela peut être :
                <ul style="padding-bottom: 0;">
                    <li>
                        <const>"ALLY"</const>
                        pour l'un de vos robots
                    </li>
                    <li>
                        <const>"ENEMY"</const>
                        pour un robot ennemi
                    </li>

                </ul>
                <var>health</var>, <var>shield</var> pour la senté et le bouclier approximatif de l'entité
                si c'est un robot, 0 sinon.
                <br>
                <br>
                <var>action</var> indique l'action réalisé par le robot au tour précédent (
                <const>NONE</const>
                si l'entité n'est pas un robot). Les différentes valeurs possibles sont :
                <const>"ATTACK"</const>
                ,
                <const>"MOVE"</const>
                ,
                <const>"FLEE"</const>
                ,
                <const>"IDLE"</const>
                <br>
                <br>
                <var>targets</var> est la liste des ids des entités visées par la dernière action du robot au dernier
                tour. Les ids sont séparés par des
                <const>";"</const>
                :
                <const>"id1;id2;id3..."</const>
                si l'entité est un robot (la cible de IDLE étant le robot lui même). Sinon
                <const>-1</const>
                .
                <!-- BEGIN level2 level3 -->
                <br> <br>
                <var>distEn</var> : la portée à laquelle est le plus proche ennemi. Si l'entité est un robot ennemi,
                alors c'est la portée du plus proche allié qui est donnée.
                <br><br>
                <var>borderDist</var> : la portée à laquelle est le plus proche côté de l'arène (entre gauche,
                droit, haut, bas)
                <!-- END -->
                <!-- BEGIN level3 -->
                <br>
                <br>
                Les données suivantes sont des <strong>rangs</strong> : les entités sont triées selon les différents
                attributs par <strong>ordre croissant</strong> :
                <ul>
                    <li>
                        <var>distEnRank</var> : L'attribut considéré est leur distance au robot ennemi
                        le plus proche. Si l'entité est un robot ennemi, la distance considérée est celle à son plus
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
                        <var>totalRank</var> : l'attribut est la somme du nombre exact de point de vie et de bouclier

                    </li>

                </ul>
                A noter que pour ces 3 derniers attributs les entités qui ne sont pas des robots ont le rang
                <const>-1</const>
                <!-- END -->

                <br><br><br>
                Ensuite, vos robots deviennent chacun leur tour <strong>actifs</strong>.
                Un robot <strong>actif</strong> donne pour chaque entité dans l'arène
                <!-- BEGIN level1 -->
                <const> 4</const>
                <!-- END -->
                <!-- BEGIN level2 level3 -->
                <const> 7</const>
                <!-- END -->

                données calculées selon <strong><u>sa perspective :</u></strong> <var>entId</var>, <var>entType</var>,
                <var>distMe</var>,
                <var>distMeRank</var>
                <!-- BEGIN level2 level3 -->
                , <var>shieldComp</var>, <var>healthComp</var>, <var>totComp</var>
                <!-- END -->
                .
                <!-- BEGIN level1 -->
                Les autres sont juste 0 et seront utilisés dans les ligues suivantes.
                <!-- END -->
                Lors de la première itération, le robot envoie les données le concernant.
                Donc <var>entType</var> est
                <const> "SELF"</const>
                <i>comme ça vous pouvez facilment récupérer l'id du robot<strong> actif</strong></i>.
                Ensuite le robot vous envoie les informations sur chaque ennemi.
                <br>
                <br><var>entId</var> est l'id unique de l'entité.
                <br><br> <var>entType</var>
                indicates the type of entity. The value can be:
                <ul style="padding-bottom: 0;">
                    <li>
                        <const>"ALLY"</const>
                        pour un robot allié
                    </li>
                    <li>
                        <const>"ENEMY"</const>
                        pour un robot ennemi
                    </li>
                    <li>
                        <const> "SELF"</const>
                        pour le robot<strong> actif</strong></li>

                </ul>
                <var>distMe</var> la portée à laquelle se trouve l'entité par rapport au robot
                <strong> actif </strong>
                <const>0</const>
                pour
                <const>courte portée</const>
                ,
                <const>1</const>
                pour
                <const>moyenne portée</const>
                ,
                <const>2</const>
                pour
                <const>longue portée</const>
                ,
                <const>3</const>
                pour
                <const>hors de portée</const>
                <br><br>
                <var>distMeRank</var> le rang de l'entité dans un classement du plus proche au plus loin du robot
                <strong>actif </strong> basé sur la distance exacte les séparant.
                <br>
                <!-- BEGIN level2 level3 -->
                <br>
                <var>shieldComp</var>, <var>healthComp</var>, <var>totComp</var>
                comparent un attribut entre le robot <strong>actif</strong> et un autre robot.
                <br><var>shieldComp</var> compare la valeur exacte des <strong>bouclier</strong> des 2 robots,
                <var>healthComp</var> compare la santé exacte des 2 robots,
                <var>totComp</var> compare la somme de la santé et du bouclier des 2 robots.
                <br>
                Ces variables peuvent prendre les valeurs :
                <ul style="padding-bottom: 0;">
                    <li>
                        <const>-1</const>
                        si l'autre robot possède plus d'attributs que le robot <strong>actif</strong>
                    </li>
                    <li>
                        <const>0</const>
                        si l'autre robot possède autant d'attributs que le robot <strong>actif</strong>
                    </li>
                    <li>
                        <const>1</const>
                        si l'autre robot possède moins d'attributs que le robot <strong>actif</strong>
                    </li>
                </ul>
                <!-- END -->
                <br>

            </div>

            <!-- Protocol block -->
            <div class="blk">
                <div class="title">Sortie pour un tour de jeu</div>
                <div class="text"> Une ligne contenant tous les ordres donnés à vos robots séparés par
                    <const>";"</const>
                    comme ceci
                    <const> "ordre1;ordre2;ordre3;..."</const>
                    <br> Un ordre doit respecter la synthaxe suivante :
                    <action>idDuRobot [ACTION] [CIBLES]</action>
                    <br>
                    <action>ACTION</action>
                    doit être une action valide parmis :
                    <action>ATTACK</action>
                    ,
                    <action>MOVE</action>
                    ,
                    <action>FLEE</action>
                    ,
                    <action>IDLE</action>

                    <action> CIBLES</action>
                    doit respecter la synthaxe suivante :
                    <action>cibleID1,cibleID2,cibleID3</action>
                    <br>
                    Vous devez aussi respecter certaines règles en fonction de l'action que vous souhaitez réaliser.
                    <ul>
                        <li>Si l'action est
                            <action>ATTACK</action>
                            ,
                            <action>CIBLES</action>
                            doit contenir
                            <const>1</const>
                            id de <strong> robot ennemi</strong>, ni plus, ni moins. Un robot ne peu ni s'attaquer lui
                            même ni attaquer ses alliés.
                            <br>
                            <i>
                                "Un robot doit protéger son existence dans la mesure où cette protection n'entre
                                pas en contradiction avec la première ou la deuxième loi."
                            </i>
                        </li>
                        <li>
                            Si l'action est
                            <action> MOVE</action>
                            ou
                            <action> FLEE</action>
                            ,
                            <action>CIBLES</action>
                            doit contenir au moins un id d'entité.
                        </li>
                        <li>Si l'action est
                            <action>IDLE</action>
                            ,
                            <action>CIBLES</action>
                            peut contenir tout et n'importe quoi (ou rien du tout), dans tous les cas cela sera ignoré.
                            <del> Cela vous permet de faire réfléchir vos robots au sens de la vie</del>
                        </li>
                    </ul>
                    Si vous envoyez 2 ordres différents s'adressant au même robot, vous perdrez la partie car votre
                    robot sera à cours de mémoire vive (┬┬﹏┬┬)
                    <br> Si vous n'envoyez pas de sortie, tous vos bots feront l'action
                    <action>IDLE</action>
                    . Si vous n'envoyez pas d'ordre à un ou plusieurs robots, ils effectueront
                    <action>IDLE</action>

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
                        <const>40</const>
                        )<br>
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