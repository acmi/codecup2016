/*
 * Decompiled with CFR 0_119.
 */
package com.a.a.a.a;

import com.codeforces.commons.collection.MapBuilder;
import java.util.Map;

public final class e {
    private static final Map<String, String> a = new MapBuilder<String, String>().put("Byte".toLowerCase(), "Bytes").put("Char".toLowerCase(), "Chars").put("Character".toLowerCase(), "Characters").put("Int".toLowerCase(), "Ints").put("Integer".toLowerCase(), "Integers").put("Long".toLowerCase(), "Longs").put("String".toLowerCase(), "Strings").put("ActionType".toLowerCase(), "ActionTypes").put("Ball".toLowerCase(), "Balls").put("Battery".toLowerCase(), "Batteries").put("Bolt".toLowerCase(), "Bolts").put("Bomb".toLowerCase(), "Bombs").put("Bomber".toLowerCase(), "Bombers").put("Bonus".toLowerCase(), "Bonuses").put("Building".toLowerCase(), "Buildings").put("BonusType".toLowerCase(), "BonusTypes").put("Car".toLowerCase(), "Cars").put("CellType".toLowerCase(), "CellTypes").put("Codeballer".toLowerCase(), "Codeballers").put("Direction".toLowerCase(), "Directions").put("Disciple".toLowerCase(), "Disciples").put("Fighter".toLowerCase(), "Fighters").put("FireType".toLowerCase(), "FireTypes").put("Game".toLowerCase(), "Games").put("Hero".toLowerCase(), "Heroes").put("Hockeyist".toLowerCase(), "Hockeyists").put("HockeyistState".toLowerCase(), "HockeyistStates").put("HockeyistType".toLowerCase(), "HockeyistTypes").put("Message".toLowerCase(), "Messages").put("Minion".toLowerCase(), "Minions").put("MinionType".toLowerCase(), "MinionTypes").put("Missile".toLowerCase(), "Missiles").put("Move".toLowerCase(), "Moves").put("Obstacle".toLowerCase(), "Obstacles").put("OilSlick".toLowerCase(), "OilSlicks").put("Player".toLowerCase(), "Players").put("PlayerContext".toLowerCase(), "PlayerContexts").put("Projectile".toLowerCase(), "Projectiles").put("Puck".toLowerCase(), "Pucks").put("Robot".toLowerCase(), "Robots").put("Rocket".toLowerCase(), "Rockets").put("Shell".toLowerCase(), "Shells").put("ShellType".toLowerCase(), "ShellTypes").put("Skill".toLowerCase(), "Skills").put("SkillType".toLowerCase(), "SkillTypes").put("Source".toLowerCase(), "Sources").put("Status".toLowerCase(), "Statuses").put("Tank".toLowerCase(), "Tanks").put("TankType".toLowerCase(), "TankTypes").put("Trooper".toLowerCase(), "Troopers").put("TrooperStance".toLowerCase(), "TrooperStances").put("TrooperType".toLowerCase(), "TrooperTypes").put("Trap".toLowerCase(), "Traps").put("Tree".toLowerCase(), "Trees").put("Unit".toLowerCase(), "Units").put("Wizard".toLowerCase(), "Wizards").put("Wobot".toLowerCase(), "Wobots").put("World".toLowerCase(), "Worlds").buildUnmodifiable();

    public static String a(String string) {
        String string2 = a.get(string.toLowerCase());
        if (string2 != null) {
            return string2;
        }
        throw new IllegalArgumentException("Can't pluralize word '" + string + "'.");
    }
}

