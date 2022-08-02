package implementation;

import java.io.*;
import java.util.*;


/**
 * RPG Extreme - BOJ17081
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
public class BOJ17081 {

    /**
     * -------------------- static equipment, accessory classes --------------------
     */
    private static class Equipment {

        char type;
        int hash, value;

        public Equipment(int hash, int value, char type) {
            this.hash = hash;
            this.value = value;
            this.type = type;
        }
    }

    private static abstract class AbstractAccessory {
        enum AccessoryType {
            HR("HR"),
            RE("RE"),
            CO("CO"),
            EX("EX"),
            DX("DX"),
            HU("HU"),
            CU("CU");

            private final String value;

            AccessoryType(String value) {
                this.value = value;
            }
        }

        int hash;
        AccessoryType type;

        public AbstractAccessory(int hash, AccessoryType type) {
            this.hash = hash;
            this.type = type;
        }

        abstract void processEffect(Player player);
    }

    private static class AccessoryFactory {

        public static AbstractAccessory createAccessory(int hash, AbstractAccessory.AccessoryType type) {
            switch (type) {
                case HR:
                    return new AccessoryHR(hash, type);
                case RE:
                    return new AccessoryRE(hash, type);
                case CO:
                    return new AccessoryCO(hash, type);
                case EX:
                    return new AccessoryEX(hash, type);
                case DX:
                    return new AccessoryDX(hash, type);
                case HU:
                    return new AccessoryHU(hash, type);
                case CU:
                    return new AccessoryCU(hash, type);
                default:
                    return null;
            }
        }
    }

    private static class AccessoryHR extends AbstractAccessory {

        public AccessoryHR(int idx, AccessoryType type) {
            super(idx, type);
        }

        @Override
        void processEffect(Player player) {
            player.hp = Math.min(player.hp + 3, player.maxHp);
        }
    }

    private static class AccessoryRE extends AbstractAccessory {

        public AccessoryRE(int idx, AccessoryType type) {
            super(idx, type);
        }

        @Override
        void processEffect(Player player) {
            player.x = player.startX;
            player.y = player.startY;
            player.hp = player.maxHp;
        }
    }

    private static class AccessoryCO extends AbstractAccessory {

        public AccessoryCO(int idx, AccessoryType type) {
            super(idx, type);
        }

        @Override
        void processEffect(Player player) {
            player.totalDmg = player.totalDmg << 1;
        }
    }

    private static class AccessoryEX extends AbstractAccessory {

        public AccessoryEX(int idx, AccessoryType type) {
            super(idx, type);
        }

        @Override
        void processEffect(Player player) {
            player.expToGet = (int) (player.expToGet * 1.2);
        }
    }

    private static class AccessoryDX extends AbstractAccessory {

        public AccessoryDX(int idx, AccessoryType type) {
            super(idx, type);
        }

        @Override
        void processEffect(Player player) {
            player.totalDmg = (player.totalDmg >> 1) * 3;
        }
    }

    private static class AccessoryHU extends AbstractAccessory {

        public AccessoryHU(int idx, AccessoryType type) {
            super(idx, type);
        }

        @Override
        void processEffect(Player player) {
            player.hp = player.maxHp;
        }
    }

    private static class AccessoryCU extends AbstractAccessory {

        public AccessoryCU(int idx, AccessoryType type) {
            super(idx, type);
        }

        @Override
        void processEffect(Player player) {

        }
    }

    /**
     * -------------------- static monster, player classes --------------------
     */
    private static class Monster {
        String name;
        int x, y;
        int att, def, hp, maxHp, exp, totalDmg;
        boolean isBoss;

        public Monster(String name, int x, int y, int att, int def, int hp, int exp, boolean isBoss) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.att = att;
            this.def = def;
            this.maxHp = this.hp = hp;
            this.exp = exp;
            this.isBoss = isBoss;
        }

        public void setDamage(int damage) {
            hp -= Math.max(1, damage - def);
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("y=");
            builder.append(y);
            builder.append(", ");
            builder.append("x=");
            builder.append(x);
            builder.append(", name: ");
            builder.append(name);
            builder.append(", att: ");
            builder.append(att);
            builder.append(", def: ");
            builder.append(def);
            builder.append(", hp: ");
            builder.append(hp);
            builder.append(", exp: ");
            builder.append(exp);
            builder.append(", isBoss: ");
            builder.append(isBoss);

            return builder.toString();
        }
    }

    private static class Player {

        enum EventResult {
            CONTINUE(0),
            WIN(1),
            KILLED_BY_MONSTER(2),
            KILLED_BY_SPIKE_TRAP(3),
            REINCARNATION(4);

            private final int value;

            EventResult(int value) {
                this.value = value;
            }

            public int getValue() {
                return value;
            }
        }

        static final int dirX[] = {1, -1, 0, 0};
        static final int dirY[] = {0, 0, -1, 1};
        static final int MAX_ACCESSORY_SIZE = 4;
        static final int SPIKE_DAMAGE = 5;
        private int startX, startY, maxHp, maxExp;
        private int x, y, level, hp, att, def, exp, totalDmg, expToGet;
        private Equipment weapon, armor;
        private List<AbstractAccessory> accessories = new LinkedList<>();

        public Player(int x, int y) {
            this.startX = this.x = x;
            this.startY = this.y = y;
            this.level = 1;
            this.maxExp = this.level * 5;
            this.maxHp = this.hp = 20;
            this.att = this.def = 2;
            this.exp = 0;
        }

        public static int posToHash(int x, int y) {
            return Objects.hash(x, y);
        }

        private boolean canGo(int x, int y) {
            if (x < 0 || x > M - 1) return false;
            if (y < 0 || y > N - 1) return false;
            if (map[y][x] == '#') return false;
            return true;
        }

        private void levelUp() {
            level++;
            exp = 0;
            maxExp = level * 5;

            maxHp += 5;
            hp = maxHp;
            att += 2;
            def += 2;
        }

        public EventResult move(char dir) {
            int idx;
            if (dir == 'R') idx = 0;
            else if (dir == 'L') idx = 1;
            else if (dir == 'U') idx = 2;
            else if (dir == 'D') idx = 3;
            else idx = -1;

            EventResult result = EventResult.CONTINUE;
            int nxtX = x + dirX[idx];
            int nxtY = y + dirY[idx];
            if (canGo(nxtX, nxtY)) {
                x = nxtX;
                y = nxtY;
                result = processEvent(map[nxtY][nxtX]);
            } else {
                if (map[y][x] == '^') {
                    setDamage(SPIKE_DAMAGE, EventResult.KILLED_BY_SPIKE_TRAP);
                }
            }

            return result;
        }

        private EventResult processEvent(char eventType) {
            EventResult result = EventResult.CONTINUE;

            switch (eventType) {
                case '&':
                case 'M':
                    result = processBattle();
                    break;
                case '^':
                    result = setDamage(SPIKE_DAMAGE, EventResult.KILLED_BY_SPIKE_TRAP);
                    break;
                case 'B':
                    processItemAcquisition();
                    break;
            }

            return result;
        }

        private EventResult processBattle() {
            int idx = -1;
            Monster monster = monsterMap.get(posToHash(x, y));
            EventResult eventResult = EventResult.CONTINUE;
            System.out.println(monster);

            for (int i = 0; ; i++) {
                totalDmg = att + getWeaponValue();
                monster.totalDmg = monster.att;
                if (i == 0) {
                    // process CO, DX accessory effect
                    idx = findAccessory(AbstractAccessory.AccessoryType.CO);
                    if (idx > -1) {
                        accessories.get(idx).processEffect(this);
                        idx = findAccessory(AbstractAccessory.AccessoryType.DX);
                        if (idx > -1) {
                            accessories.get(idx).processEffect(this);
                            System.out.println(totalDmg + " x3");
                        }
                    }

                    // process HU accessory effect
                    idx = findAccessory(AbstractAccessory.AccessoryType.HU);
                    if (idx > -1) {
                        accessories.get(idx).processEffect(this);
                        monster.totalDmg = 0;
                    }
                }
                monster.setDamage(totalDmg);
                if (monster.hp <= 0) break;

                eventResult = setDamage(monster.totalDmg, EventResult.KILLED_BY_MONSTER);
                if (hp <= 0) break;
                if (eventResult == EventResult.REINCARNATION) {
                    monster.hp = monster.maxHp;
                    break;
                }
            }

            if (monster.hp <= 0) {
                // restore hp
                idx = findAccessory(AbstractAccessory.AccessoryType.HR);
                if (idx > -1) accessories.get(idx).processEffect(this);

                // get exp
                player.expToGet = monster.exp;
                idx = findAccessory(AbstractAccessory.AccessoryType.EX);
                if (idx > -1) accessories.get(idx).processEffect(this);
                player.exp += player.expToGet;
                if (player.exp >= player.maxExp) {
                    levelUp();
                }

                if (monster.isBoss) eventResult = EventResult.WIN;
                map[player.y][player.x] = '.';
            }

            return eventResult;
        }

        private void processItemAcquisition() {
            int hash = posToHash(x, y);
            Equipment equipment = equipmentMap.get(hash);
            AbstractAccessory accessory = accessoryMap.get(hash);

            if (equipment != null) {
                if (equipment.type == 'W') setWeapon(equipment);
                else setArmor(equipment);
            }

            if (accessory != null) {
                setAccessory(accessory);
            }
            map[y][x] = '.';
        }

        public int getWeaponValue() {
            return this.weapon == null ? 0 : this.weapon.value;
        }

        public int getArmorValue() {
            return this.armor == null ? 0 : this.armor.value;
        }

        public void setWeapon(Equipment weapon) {
            this.weapon = weapon;
        }

        public void setArmor(Equipment armor) {
            this.armor = armor;
        }

        public void setAccessory(AbstractAccessory accessory) {
            int size = accessories.size();
            if (size > MAX_ACCESSORY_SIZE) return;
            for (int i = 0; i < size; i++) {
                AbstractAccessory current = accessories.get(i);
                if (current.type.value.equals(accessory.type.value)) {
                    return;
                }
            }
            accessories.add(accessory);
        }

        public EventResult setDamage(int damage, EventResult result) {
            switch (result) {
                case KILLED_BY_MONSTER:
                    if (damage > 0) hp = Math.max(hp - Math.max(1, damage - def - getArmorValue()), 0);
                    break;

                case KILLED_BY_SPIKE_TRAP:
                    int idx = findAccessory(AbstractAccessory.AccessoryType.DX);
                    if (idx > -1) hp -= 1;
                    else hp -= damage;
                    break;
            }

            if (hp <= 0) {
                int idx = findAccessory(AbstractAccessory.AccessoryType.RE);
                if (idx > -1) {
                    accessories.get(idx).processEffect(this);
                    accessories.remove(idx);
                    result = EventResult.REINCARNATION;
                }
            } else {
                result = EventResult.CONTINUE;
            }

            return result;
        }

        public int findAccessory(AbstractAccessory.AccessoryType type) {
            for (int i = 0; i < accessories.size(); i++) {
                if (accessories.get(i).type.value.equals(type.value)) {
                    return i;
                }
            }

            return -1;
        }
    }

    /**
     * -------------------- static variables --------------------
     */

    static int N, M, K, L;
    static char[][] map;
    static String command;
    static Player player;
    static HashMap<Integer, Monster> monsterMap;
    static HashMap<Integer, Equipment> equipmentMap;
    static HashMap<Integer, AbstractAccessory> accessoryMap;
    static StringBuilder result = new StringBuilder();


    /**
     * -------------------- static methods --------------------
     */
    private static void initMap(BufferedReader br) throws IOException {
        if (map == null) map = new char[N][M];
        for (int i = 0; i < N; i++) {
            String line = br.readLine();
            for (int j = 0; j < M; j++) {
                char point = line.charAt(j);
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

    private static void initMonster(BufferedReader br) throws IOException {
        if (monsterMap == null) monsterMap = new HashMap<>();

        StringTokenizer st;
        for (int i = 0; i < K; i++) {
            st = new StringTokenizer(br.readLine());
            int y = Integer.parseInt(st.nextToken()) - 1;
            int x = Integer.parseInt(st.nextToken()) - 1;
            int hash = Player.posToHash(x, y);
            String name = st.nextToken();
            int w = Integer.parseInt(st.nextToken());
            int a = Integer.parseInt(st.nextToken());
            int h = Integer.parseInt(st.nextToken());
            int e = Integer.parseInt(st.nextToken());
            Monster m = new Monster(name, x, y, w, a, h, e, map[y][x] == 'M');
            monsterMap.put(hash, m);
        }
    }

    private static void initItems(BufferedReader br) throws IOException {
        if (equipmentMap == null) equipmentMap = new HashMap<>();
        if (accessoryMap == null) accessoryMap = new HashMap<>();

        StringTokenizer st;
        for (int i = 0; i < L; i++) {
            st = new StringTokenizer(br.readLine());
            int y = Integer.parseInt(st.nextToken()) - 1;
            int x = Integer.parseInt(st.nextToken()) - 1;
            int hash = Player.posToHash(x, y);
            char type = st.nextToken().charAt(0);
            switch (type) {
                case 'W':
                case 'A':
                    int value = Integer.parseInt(st.nextToken());
                    equipmentMap.put(hash, new Equipment(hash, value, type));
                    break;
                case 'O':
                    AbstractAccessory.AccessoryType accessoryType = AbstractAccessory.AccessoryType.valueOf(st.nextToken());
                    accessoryMap.put(hash, AccessoryFactory.createAccessory(hash, accessoryType));
                    break;
            }
        }
    }

    private static void makeGameResult(int turn, Player.EventResult eventResult) {
        String resStr = "";
        switch (eventResult) {
            case REINCARNATION:
            case CONTINUE:
                map[player.y][player.x] = '@';
                map[player.startY][player.startX] = '.';
                resStr = "Press any key to continue.";
                break;

            case WIN:
                map[player.y][player.x] = '@';
                map[player.startY][player.startX] = '.';
                resStr = "YOU WIN!";
                break;

            case KILLED_BY_MONSTER:
                map[player.startY][player.startX] = '.';
                Monster m = monsterMap.get(Player.posToHash(player.x, player.y));
                resStr = "YOU HAVE BEEN KILLED BY " + m.name + "..";
                break;

            case KILLED_BY_SPIKE_TRAP:
                map[player.startY][player.startX] = '.';
                resStr = "YOU HAVE BEEN KILLED BY SPIKE TRAP..";
                break;
        }

        // print map result
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                result.append(map[i][j]);
            }
            result.append('\n');
        }

        // print status result
        result.append("Passed Turns : ").append(turn).append('\n');
        result.append("LV : ").append(player.level).append('\n');
        result.append("HP : ").append(player.hp).append('/').append(player.maxHp).append('\n');
        result.append("ATT : ").append(player.att).append('+').append(player.getWeaponValue()).append('\n');
        result.append("DEF : ").append(player.def).append('+').append(player.getArmorValue()).append('\n');
        result.append("EXP : ").append(player.exp).append('/').append(player.maxExp).append('\n');
        result.append(resStr);
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st;

        // Parse N, M
        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        // Init Map
        initMap(br);

        // Init Movement Command
        command = br.readLine();

        // Init Monsters
        initMonster(br);

        // Init Items
        initItems(br);

        // Process Commands
        int turn = 0;
        char dir = '\0';
        Player.EventResult eventResult = null;
        for (int i = 1; i < command.length() + 1; i++) {
            turn = i;
            dir = command.charAt(i - 1);
            eventResult = player.move(dir);
            if ((eventResult != Player.EventResult.CONTINUE) && (eventResult != Player.EventResult.REINCARNATION)) {
                break;
            }
        }

        // Make Game Result
        makeGameResult(turn, eventResult);

        // write the result
        bw.write(result.toString());

        // close the buffer
        br.close();
        bw.close();
    }
}
