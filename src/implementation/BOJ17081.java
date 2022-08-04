package implementation;

import java.io.*;
import java.util.*;


/**
 * RPG Extreme - BOJ17081
 * -----------------
 * -----------------
 * Input 1
 * 7 8
 * .&....&.
 * ..B.&..&
 * B...&...
 * .B@.B#..
 * .&....M.
 * .B...B..
 * ..B^^&..
 * RRRUULLULUDDDLDRDRDRRRURRULUULLU
 * 3 5 One 4 2 10 3
 * 2 5 Two 10 2 8 3
 * 1 2 Three 20 2 14 7
 * 5 2 Four 16 2 16 5
 * 7 6 Five 16 5 16 12
 * 5 7 Boss 2 9 20 2
 * 1 7 EO 20 1 1 4
 * 2 8 ET 10 5 4 10
 * 4 5 W 4
 * 2 3 O CO
 * 3 1 A 10
 * 4 2 A 2
 * 6 2 O DX
 * 7 3 O HU
 * 6 6 W 3
 *
 * Output 1
 * ......&.
 * .......&
 * B.......
 * .....#..
 * ......@.
 * ........
 * ...^^...
 * Passed Turns : 27
 * LV : 3
 * HP : 29/30
 * ATT : 6+3
 * DEF : 6+2
 * EXP : 14/15
 * YOU WIN!
 * -----------------
 * Input 2
 * 6 6
 * BBBB^&
 * ....&.
 * &...BB
 * B.B#.&
 * B&.@#B
 * ...#MB
 * UURRDDURDLRRLLLDUULUUUURRRLRRDLDRRDDDLRRD
 * 5 2 One 2 2 19 4
 * 3 1 Two 2 2 12 2
 * 2 5 Three 4 5 16 9
 * 4 6 Four 20 13 20 8
 * 6 5 Boss 10 20 11 20
 * 1 6 Etc 20 20 20 20
 * 1 1 O EX
 * 1 2 O CU
 * 1 3 O CO
 * 1 4 O CU
 * 3 5 O DX
 * 3 6 O RE
 * 4 1 A 10
 * 5 1 W 10
 * 4 3 W 5
 * 5 6 W 1
 * 6 6 A 1
 *
 * Output 2
 * ....^&
 * ......
 * ......
 * ..B#..
 * ....#.
 * ...#M.
 * Passed Turns : 38
 * LV : 3
 * HP : 0/30
 * ATT : 6+1
 * DEF : 6+1
 * EXP : 9/15
 * YOU HAVE BEEN KILLED BY Boss..
 * -----------------
 * Input 3
 * 6 6
 * @.BB.M
 * ..##B.
 * BB....
 * ##.###
 * &&.&&&
 * &#..#&
 * RRRRRDDRRDDLLDURRRRRDULLLUURRRU
 * 1 6 Boss 20 20 20 20
 * 5 2 One 5 2 2 5
 * 5 1 Two 5 4 6 6
 * 6 1 Three 5 4 10 6
 * 5 4 Four 8 4 8 7
 * 5 5 Five 8 4 8 10
 * 5 6 Six 3 3 10 20
 * 6 6 Seven 8 8 1 4
 * 1 3 O RE
 * 1 4 O HR
 * 3 1 W 2
 * 3 2 A 2
 * 2 5 A 10
 *
 * Output 3
 * .....M
 * ..##B@
 * ......
 * ##.###
 * ......
 * .#..#.
 * Passed Turns : 31
 * LV : 5
 * HP : 40/40
 * ATT : 10+2
 * DEF : 10+2
 * EXP : 4/25
 * Press any key to continue.
 * -----------------
 */
import javafx.util.Pair;

import java.io.*;
import java.util.*;

public class BOJ17081 {

    /**
     * ----------------------------------------------------
     * ------------------- Utility class ------------------
     * ----------------------------------------------------
     */
    private static class Constants {
        enum AccessoryType {
            HR(0),
            RE(1),
            CO(2),
            EX(3),
            DX(4),
            HU(5),
            CU(6);

            final int value;

            AccessoryType(int value) {
                this.value = value;
            }
        }

        enum PlayerStatus {
            CONTINUE(0),
            WIN(1),
            DIED(2);

            final int value;

            PlayerStatus(int value) {
                this.value = value;
            }
        }

        static final int[] dirX = {1, -1, 0, 0};
        static final int[] dirY = {0, 0, -1, 1};
    }


    /**
     * ----------------------------------------------------
     * ------------- Entity class, interface --------------
     * ----------------------------------------------------
     */
    private interface Attacker {
        void attack(MovableEntity victim, boolean effect);
    }

    private interface Victim {
        void hit(Entity attacker);
    }

    private static abstract class Entity implements Attacker {
        Pair<Integer, Integer> pos;
        String name;
        int att, maxDmg;

        public Entity(String name, int x, int y, int att) {
            this.name = name;
            this.pos = new Pair<>(x, y);
            this.att = att;
        }

        abstract void init();
    }

    private static abstract class MovableEntity extends Entity implements Victim {
        int def, hp, maxHp, exp;

        public MovableEntity(String name, int x, int y, int att, int def, int hp, int exp) {
            super(name, x, y, att);
            this.def = def;
            this.maxHp = this.hp = hp;
            this.exp = exp;
        }

        @Override
        public void init() {
            hp = maxHp;
        }
    }


    /**
     * ----------------------------------------------------
     * ------------ Trap, Monster, Player class -----------
     * ----------------------------------------------------
     */
    private static class SpikeTrap extends Entity {

        public SpikeTrap(int x, int y, int att) {
            super("SPIKE TRAP", x, y, att);
        }

        @Override
        void init() {
        }

        @Override
        public void attack(MovableEntity victim, boolean effect) {
            victim.hit(this);
        }
    }

    private static class Monster extends MovableEntity {
        boolean isBoss;

        public Monster(String name, int x, int y, int att, int def, int hp, int exp, boolean isBoss) {
            super(name, x, y, att, def, hp, exp);
            this.isBoss = isBoss;
        }

        @Override
        public void attack(MovableEntity victim, boolean effect) {
            maxDmg = effect && isBoss ? 0 : att;
            victim.hit(this);
        }

        @Override
        public void hit(Entity attacker) {
            int totalDmg = Math.max(attacker.maxDmg - def, 1);
            hp = Math.max(hp - totalDmg, 0);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("monster=[");
            builder.append("y=").append(pos.getValue());
            builder.append(", x=").append(pos.getKey());
            builder.append(", name=").append(name);
            builder.append(", ATT=").append(att);
            builder.append(", DEF=").append(def);
            builder.append(", HP=").append(hp);
            builder.append(", EXP=").append(exp);
            builder.append(", IsBoss=").append(isBoss).append(']');

            return builder.toString();
        }
    }

    private static class Player extends MovableEntity {

        static final int BASE_ATT_VALUE = 2;
        static final int BASE_DEF_VALUE = 2;
        static final int BASE_HP_VALUE = 20;
        static final int BASE_EXP_VALUE = 0;
        static final int MAX_ACCESSORY_SIZE = 4;

        Constants.PlayerStatus status = Constants.PlayerStatus.CONTINUE;
        Accessory[] accessories;
        int startX, startY, accessoryCnt;
        int weapon, armor, level, maxExp, expToGet;
        Entity killer;


        public Player(int x, int y) {
            super("Player", x, y, BASE_ATT_VALUE, BASE_DEF_VALUE, BASE_HP_VALUE, BASE_EXP_VALUE);
            this.startX = x;
            this.startY = y;
            this.level = 1;
            this.maxExp = this.level * 5;
            this.accessories = new Accessory[Accessory.MAX_ACCESSORY_TYPE_COUNT];
        }

        @Override
        public void attack(MovableEntity victim, boolean effect) {
            maxDmg = att + weapon;
            if (effect) {
                // CO, DX, HU 효과 체크
                boolean conditionCO = hasAccessory(Constants.AccessoryType.CO);
                boolean conditionDX = hasAccessory(Constants.AccessoryType.DX);
                boolean conditionHU = hasAccessory(Constants.AccessoryType.HU);

                if (conditionCO && conditionDX) {
                    accessories[Constants.AccessoryType.DX.value].effect(this);
                } else if (conditionCO) {
                    accessories[Constants.AccessoryType.CO.value].effect(this);
                }

                if (conditionHU) {
                    if (victim instanceof Monster && ((Monster) victim).isBoss)
                        accessories[Constants.AccessoryType.HU.value].effect(this);
                }
            }
            victim.hit(this);
        }

        @Override
        public void hit(Entity attacker) {
            if (attacker instanceof SpikeTrap) {
                if (hasAccessory(Constants.AccessoryType.DX)) {
                    // DX 장신구가 있으면 1만큼의 피해를 받음
                    hp = Math.max(hp - 1, 0);
                } else {
                    // DX 장신구가 없으면 5만큼의 피해를 받음
                    hp = Math.max(hp - attacker.att, 0);
                }
            } else if (attacker instanceof Monster) {
                if (attacker.maxDmg > 0) {
                    int totalDmg = Math.max(attacker.maxDmg - (def + armor), 1);
                    hp = Math.max(hp - totalDmg, 0);
                }
            }

            if (hp <= 0) {
                // RE 장신구가 있을 경우 시작 위치에서 부활
                if (hasAccessory(Constants.AccessoryType.RE)) {
                    accessories[Constants.AccessoryType.RE.value].effect(this);
                    removeAccessory(Constants.AccessoryType.RE);
                    attacker.init();
                } else {
                    status = Constants.PlayerStatus.DIED;
                    killer = attacker;
                }
            }
        }

        public boolean canGo(int x, int y) {
            if (x < 1 || x > M) return false;
            if (y < 1 || y > N) return false;
            if (map[y][x] == '#') return false;
            return true;
        }

        public void move(char dir) {
            int dirIdx = 0;
            if (dir == 'R') dirIdx = 0;
            else if (dir == 'L') dirIdx = 1;
            else if (dir == 'U') dirIdx = 2;
            else if (dir == 'D') dirIdx = 3;
            else dirIdx = -1;

            int currX = pos.getKey();
            int currY = pos.getValue();
            int nxtX = currX + Constants.dirX[dirIdx];
            int nxtY = currY + Constants.dirY[dirIdx];

            if (canGo(nxtX, nxtY)) {
                pos = new Pair<>(nxtX, nxtY);
                switch (map[nxtY][nxtX]) {
                    case '&':
                    case 'M':
                        processBattle(monsterMap.get(pos));
                        break;
                    case 'B':
                        processItemAcquisition();
                        break;
                    case '^':
                        hit(trap);
                        break;
                    default:
                        break;
                }
            } else {
                if (map[currY][currX] == '^') {
                    hit(trap);
                }
            }
        }

        private void processBattle(Monster monster) {
            for (int i = 0; ; i++) {
                // attack player -> monster
                attack(monster, i == 0);
                if (monster.hp <= 0) {
                    status = monster.isBoss ? Constants.PlayerStatus.WIN : Constants.PlayerStatus.CONTINUE;
                    expToGet = monster.exp;

                    // HR 장신구 효과 체크
                    if (hasAccessory(Constants.AccessoryType.HR))
                        accessories[Constants.AccessoryType.HR.value].effect(this);
                    // EX 장신구 효과 체크
                    if (hasAccessory(Constants.AccessoryType.EX))
                        accessories[Constants.AccessoryType.EX.value].effect(this);

                    player.exp += player.expToGet;
                    levelUp();
                    map[pos.getValue()][pos.getKey()] = '.';
                    break;
                }

                // attack monster -> player
                monster.attack(this, i == 0 && hasAccessory(Constants.AccessoryType.HU));
                if (status == Constants.PlayerStatus.DIED) {
                    break;
                }

                if(!player.pos.equals(monster.pos)) break;
            }
        }

        private void processItemAcquisition() {
            if (equipmentMap.containsKey(pos)) {
                Equipment e = equipmentMap.get(pos);
                if (e.type == 'W') weapon = e.value;
                else armor = e.value;
            }

            if (accessoryMap.containsKey(pos)) {
                Accessory a = accessoryMap.get(pos);
                addAccessory(a);
            }

            map[pos.getValue()][pos.getKey()] = '.';
        }

        private void levelUp() {
            if (exp >= maxExp) {
                exp = 0;
                level++;
                maxExp = level * 5;
                maxHp += 5;
                hp = maxHp;
                att += 2;
                def += 2;
            }
        }

        private boolean hasAccessory(Constants.AccessoryType type) {
            return accessories[type.value] != null;
        }

        private void addAccessory(Accessory a) {
            if (accessoryCnt < MAX_ACCESSORY_SIZE && !hasAccessory(a.type)) {
                accessories[a.type.value] = a;
                accessoryCnt++;
            }
        }

        private void removeAccessory(Constants.AccessoryType type) {
            if (hasAccessory(type)) {
                accessories[type.value] = null;
                accessoryCnt--;
            }
        }

        private String accessoriesToString() {
            StringBuilder builder = new StringBuilder();
            for (Constants.AccessoryType type : Constants.AccessoryType.values()) {
                if (hasAccessory(type)) {
                    builder.append(type).append(' ');
                }
            }

            return builder.toString();
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("player=[");
            builder.append("y=").append(pos.getValue());
            builder.append(", x=").append(pos.getKey());
            builder.append(", LVL=").append(level);
            builder.append(", ATT=").append(att);
            builder.append(", WEAPON=").append(weapon);
            builder.append(", DEF=").append(def);
            builder.append(", ARMOR=").append(armor);
            builder.append(", HP=").append(hp).append('/').append(maxHp);
            builder.append(", EXP=").append(exp).append('/').append(maxExp);
            builder.append(", ACCESSORIES=").append(accessoriesToString()).append(']');

            return builder.toString();
        }
    }


    /**
     * ----------------------------------------------------
     * ----------- Equipment, Accessory classes -----------
     * ----------------------------------------------------
     */
    private static class Equipment {
        char type;
        int value;

        public Equipment(char type, int value) {
            this.type = type;
            this.value = value;
        }
    }

    private static abstract class Accessory {
        static final int MAX_ACCESSORY_TYPE_COUNT = 7;
        Constants.AccessoryType type;

        public Accessory() {
            type = AccessoryFactory.cvtStringToAccessoryType(getClass().getSimpleName());
        }

        abstract void effect(Player player);
    }

    private static class AccessoryFactory {

        public static Constants.AccessoryType cvtStringToAccessoryType(String type) {
            switch (type) {
                case "HR":
                    return Constants.AccessoryType.HR;
                case "RE":
                    return Constants.AccessoryType.RE;
                case "CO":
                    return Constants.AccessoryType.CO;
                case "EX":
                    return Constants.AccessoryType.EX;
                case "DX":
                    return Constants.AccessoryType.DX;
                case "HU":
                    return Constants.AccessoryType.HU;
                case "CU":
                    return Constants.AccessoryType.CU;
                default:
                    return null;
            }
        }

        public static Accessory createAccessory(Constants.AccessoryType type) {
            switch (type) {
                case HR:
                    return new HR();
                case RE:
                    return new RE();
                case CO:
                    return new CO();
                case EX:
                    return new EX();
                case DX:
                    return new DX();
                case HU:
                    return new HU();
                case CU:
                    return new CU();
                default:
                    return null;
            }
        }
    }

    private static class HR extends Accessory {
        @Override
        public void effect(Player player) {
            player.hp = Math.min(player.hp + 3, player.maxHp);
        }
    }

    private static class RE extends Accessory {
        @Override
        public void effect(Player player) {
            player.pos = new Pair<>(player.startX, player.startY);
            player.hp = player.maxHp;
        }
    }

    private static class CO extends Accessory {
        @Override
        public void effect(Player player) {
            player.maxDmg *= 2;
        }
    }

    private static class EX extends Accessory {
        @Override
        public void effect(Player player) {
            player.expToGet = (int) (player.expToGet * 1.2);
        }
    }

    private static class DX extends Accessory {
        @Override
        public void effect(Player player) {
            player.maxDmg *= 3;
        }
    }

    private static class HU extends Accessory {
        @Override
        public void effect(Player player) {
            player.hp = player.maxHp;
        }
    }

    private static class CU extends Accessory {
        @Override
        public void effect(Player player) {
        }
    }


    /**
     * --------------------------------------------------------------
     * ---------------------- Static variables ----------------------
     * --------------------------------------------------------------
     */
    static final SpikeTrap trap = new SpikeTrap(-1, -1, 5);
    static int N, M, K, L, T;
    static char[][] map;
    static String commands;
    static Player player;
    static HashMap<Pair<Integer, Integer>, Monster> monsterMap = new HashMap<>();
    static HashMap<Pair<Integer, Integer>, Equipment> equipmentMap = new HashMap<>();
    static HashMap<Pair<Integer, Integer>, Accessory> accessoryMap = new HashMap<>();
    static StringBuilder result = new StringBuilder();


    /**
     * --------------------------------------------------------------
     * ----------------------- Static methods -----------------------
     * --------------------------------------------------------------
     */
    public static void initMap(BufferedReader br) throws IOException {
        StringTokenizer st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        map = new char[N + 1][M + 1];

        for (int i = 1; i < N + 1; i++) {
            String line = br.readLine();
            for (int j = 1; j < M + 1; j++) {
                char point = line.charAt(j - 1);
                switch (point) {
                    case '&':
                    case 'M':
                        K++;
                        break;
                    case 'B':
                        L++;
                        break;
                    case '@':
                        player = new Player(j, i);
                        break;
                }
                map[i][j] = point;
            }
        }
    }

    public static void initMonsters(BufferedReader br) throws IOException {
        StringTokenizer st;
        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());
            int y = Integer.parseInt(st.nextToken());
            int x = Integer.parseInt(st.nextToken());
            String name = st.nextToken();
            int att = Integer.parseInt(st.nextToken());
            int def = Integer.parseInt(st.nextToken());
            int hp = Integer.parseInt(st.nextToken());
            int exp = Integer.parseInt(st.nextToken());

            Monster monster = new Monster(name, x, y, att, def, hp, exp, map[y][x] == 'M');
            monsterMap.put(monster.pos, monster);
        }
    }

    public static void initItems(BufferedReader br) throws IOException {
        StringTokenizer st;
        for (int i = 0; i < L; i++) {
            st = new StringTokenizer(br.readLine());
            int y = Integer.parseInt(st.nextToken());
            int x = Integer.parseInt(st.nextToken());
            char type = st.nextToken().charAt(0);
            Pair<Integer, Integer> pos = new Pair<>(x, y);

            switch (type) {
                case 'W':
                case 'A':
                    int value = Integer.parseInt(st.nextToken());
                    equipmentMap.put(pos, new Equipment(type, value));
                    break;
                case 'O':
                    Constants.AccessoryType accessoryType = AccessoryFactory.cvtStringToAccessoryType(st.nextToken());
                    accessoryMap.put(pos, AccessoryFactory.createAccessory(accessoryType));
                    break;

            }
        }
    }

    public static void createGameResult() {
        int currX = player.pos.getKey();
        int currY = player.pos.getValue();
        int startX = player.startX;
        int startY = player.startY;
        map[startY][startX] = '.';

        String resultMsg = "";
        if (player.status == Constants.PlayerStatus.CONTINUE) {
            resultMsg = "Press any key to continue.";
            map[currY][currX] = '@';
        } else if (player.status == Constants.PlayerStatus.WIN) {
            resultMsg = "YOU WIN!";
            map[currY][currX] = '@';
        } else {
            resultMsg = String.format("YOU HAVE BEEN KILLED BY %s..", player.killer.name);
        }

        // print the map
        for (int i = 1; i < N + 1; i++) {
            for (int j = 1; j < M + 1; j++) {
                result.append(map[i][j]);
            }
            result.append('\n');
        }

        //print the status
        result.append("Passed Turns : ").append(T).append('\n');
        result.append("LV : ").append(player.level).append('\n');
        result.append("HP : ").append(player.hp).append('/').append(player.maxHp).append('\n');
        result.append("ATT : ").append(player.att).append('+').append(player.weapon).append('\n');
        result.append("DEF : ").append(player.def).append('+').append(player.armor).append('\n');
        result.append("EXP : ").append(player.exp).append('/').append(player.maxExp).append('\n');
        result.append(resultMsg);
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        // Init map
        initMap(br);

        // Init commands;
        commands = br.readLine();

        // Init monsters
        initMonsters(br);

        // Init items
        initItems(br);

        // Process commands
        for (int i = 0; i < commands.length(); i++) {
            char dir = commands.charAt(i);
            player.move(dir);
            T = i + 1;
            if (player.status != Constants.PlayerStatus.CONTINUE) {
                break;
            }
        }

        // Create game result
        createGameResult();

        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}