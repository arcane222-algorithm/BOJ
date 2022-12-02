package implementation;

import java.io.*;
import java.util.*;


/**
 * 랭킹전 대기열 - BOJ20006
 * -----------------
 * category: implementation (구현)
 *           simulation (시뮬레이션)
 * -----------------
 * Input 1
 * 10 5
 * 10 a
 * 15 b
 * 20 c
 * 25 d
 * 30 e
 * 17 f
 * 18 g
 * 26 h
 * 24 i
 * 28 j
 *
 * Output 1
 * Started!
 * 10 a
 * 15 b
 * 20 c
 * 17 f
 * 18 g
 * Started!
 * 25 d
 * 30 e
 * 26 h
 * 24 i
 * 28 j
 * -----------------
 * Input 2
 * 10 3
 * 490 b
 * 500 a
 * 20 c
 * 25 d
 * 30 e
 * 17 f
 * 18 g
 * 26 h
 * 24 i
 * 28 j
 *
 * Output 2
 * Waiting!
 * 500 a
 * 490 b
 * Started!
 * 20 c
 * 25 d
 * 30 e
 * Started!
 * 17 f
 * 18 g
 * 26 h
 * Waiting!
 * 24 i
 * 28 j
 * -----------------
 * Input 3
 * 10 1
 * 10 a
 * 15 b
 * 20 c
 * 25 d
 * 30 e
 * 17 f
 * 18 g
 * 26 h
 * 24 i
 * 28 j
 *
 * Started!
 * 10 a
 * Started!
 * 15 b
 * Started!
 * 20 c
 * Started!
 * 25 d
 * Started!
 * 30 e
 * Started!
 * 17 f
 * Started!
 * 18 g
 * Started!
 * 26 h
 * Started!
 * 24 i
 * Started!
 * 28 j
 * -----------------
 */
public class BOJ20006 {

    private static class Player {
        public static final int MIN_LEVEL = 1;
        public static final int MAX_LEVEL = 500;

        String name;
        int lvl;

        public Player(String name, int lvl) {
            this.name = name;
            this.lvl = lvl;
        }

        private Room findProperRoom(List<Room> roomList) {
            for (Room room : roomList) {
                if (lvl < room.minLvl || room.maxLvl < lvl) continue;
                if (room.isStarted) continue;
                return room;
            }
            return null;
        }

        public void joinRoom(List<Room> roomList) {
            Room room = findProperRoom(roomList);
            if (room == null) {
                room = new Room(this);
                roomList.add(room);
            }
            room.accept(this);
        }
    }

    private static class Room {
        int minLvl, maxLvl;
        boolean isStarted, sorted;

        List<Player> playerList;

        public Room(Player host) {
            this.minLvl = Math.max(Player.MIN_LEVEL, host.lvl - 10);
            this.maxLvl = Math.min(Player.MAX_LEVEL, host.lvl + 10);
            this.playerList = new ArrayList<>();
        }

        public void accept(Player p) {
            if (isStarted) return;

            playerList.add(p);
            if (playerList.size() == m) isStarted = true;
        }


        @Override
        public String toString() {
            if (!sorted) {
                playerList.sort(Comparator.comparing(o -> o.name));
                sorted = true;
            }

            StringBuilder builder = new StringBuilder();
            builder.append(isStarted ? "Started!\n" : "Waiting!\n");
            for (int i = 0; i < playerList.size(); i++) {
                Player p = playerList.get(i);
                builder.append(p.lvl).append(' ').append(p.name);
                if (i < playerList.size() - 1) builder.append('\n');
            }
            return builder.toString();
        }
    }

    static int p, m;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        p = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        List<Room> roomList = new ArrayList<>();
        for (int i = 0; i < p; i++) {
            st = new StringTokenizer(br.readLine());
            int lvl = Integer.parseInt(st.nextToken());
            String name = st.nextToken();
            Player p = new Player(name, lvl);
            p.joinRoom(roomList);
        }

        for (Room room : roomList) {
            bw.write(room.toString());
            bw.write('\n');
        }

        // close the buffer
        br.close();
        bw.close();
    }
}