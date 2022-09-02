package implementation;

import java.io.*;
import java.util.*;


/**
 * 낚시왕 - BOJ17143
 * -----------------
 *
 * 맵을 R+1 x C+1 크기로 선언한 다음 0,0에서 시작한 어부가 x좌표가 C+1에 도달할때까지 상어 낚시를 진행하도록 구현한다.
 * Entity 클래스를 선언하여 각 객체의 기본 x, y 좌표, 이동 속력, 방향, 객체 식별 아이디 (uid)를 가지도록 하였고
 * 낚시꾼 Angler 클래스와 상어 Shark 클래스가 이를 상속하도록 하였다.
 * Movable 인터페이스를 만들어 Angler와 Shark가 각각 다른 방식으로 move 메소드를 구현하도록 하였다.
 * (낚시꾼의 경우 매 턴마다 0,0에서 우측방향으로 1씩만 움직이면 된다.)
 * (상어의 경우 문제에 따라 맵의 양쪽 끝에 다다르면 방향을 바꾸어 움직이도록 구현한다.)
 *
 * 1. 낚시꾼이 움직이고 난 후
 * 2. 낚시꾼의 x좌표를 기준으로 map의 y좌표를 탐색하여 가장 가까운 상어를 포획한다.
 * 3. 낚시꾼의 포획이 끝나면 상어를 움직이는데, 일단 좌표를 모두 갱신해놓고 갱신된 좌표를 기준으로 map에 재배치 한다.
 *    재배치 과정에서 중복되는 위치의 상어가 존재한다면 크기를 비교하여 크기가 큰 상어를 맵에 배치하며, 크기가 작은 상어는 isDied 값으로 마킹 해놨다가
 *    추후 iterator를 이용하여 list에서 제거한다. (앞의 과정에서 list를 순회하면서 제거할 경우 ConcurrentModificationException이 발생할 수 있으므로)
 *
 * + 상어의 이동을 구현함에 있어 speed가 곧 이동해야 할 거리인데 그 값이 최대 1000까지 들어오므로 반복문 1회마다 speed값을 1씩 줄여나가며 구현할 경우 시간초과가 발생한다.
 *   각 행 or 열에 대하여 상어가 현재 제자리에 오도록 하는 speed의 값은 행의 경우 2 * (R - 1) 이고, 열의 경우 2 * (C - 1)이다. 즉, 이만큼의 값은 반복되는 움직임이므로 제외해줘야 한다.
 *   즉, 상어가 최종적으로 움직여야 하는 거리 dist = speed % (2 * (R - 1)) (열 방향이면 C - 1) 이 된다.
 *   이 값만큼 dist값을 줄여나가며 상어의 좌표를 업데이트 해주면된다. (맵 끝에 도달할 경우 적절히 움직이는 방향을 바꿔준다.)
 *
 * -----------------
 * Input 1
 * 4 6 8
 * 4 1 3 3 8
 * 1 3 5 2 9
 * 2 4 8 4 1
 * 4 5 0 1 4
 * 3 3 1 2 7
 * 1 5 8 4 3
 * 3 6 2 1 2
 * 2 2 2 3 5
 *
 * Output 1
 * 22
 * -----------------
 * Input 2
 * 100 100 0
 *
 * Output 2
 * 0
 * -----------------
 * Input 3
 * 4 5 4
 * 4 1 3 3 8
 * 1 3 5 2 9
 * 2 4 8 4 1
 * 4 5 0 1 4
 *
 * Output 3
 * 22
 * -----------------
 * Input 4
 * 2 2 4
 * 1 1 1 1 1
 * 2 2 2 2 2
 * 1 2 1 2 3
 * 2 1 2 1 4
 *
 * Output 4
 * 4
 * -----------------
 */
public class BOJ17143 {

    private interface Movable {

        void move(Entity[][] map);
    }

    private static class Entity {

        enum MoveDir {

            UP(1), DOWN(2), RIGHT(3), LEFT(4);
            final int value;

            MoveDir(int value) {
                this.value = value;
            }
        }

        static final int[] dirX = {0, 0, 1, -1};    // right, left
        static final int[] dirY = {-1, 1, 0, 0};    // up, down
        int x, y, speed, dir, uid;

        public Entity(int x, int y, int speed, int dir, int uid) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.dir = dir;
            this.uid = uid;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Entity) {
                Entity e = (Entity) o;
                return uid == e.uid;
            }
            return false;
        }
    }

    private static class Angler extends Entity implements Movable {
        int score;

        public Angler(int x, int y, int speed, int dir, int uid) {
            super(x, y, speed, dir, uid);
        }

        @Override
        public void move(Entity[][] map) {
            map[y][x] = null;
            x += dirX[dir - 1];
            y += dirY[dir - 1];
            map[y][x] = this;
        }

        public void fishing(Entity[][] map) {
            for (int i = 1; i < R + 1; i++) {
                if (map[i][x] != null && map[i][x] instanceof Shark) {
                    Shark s = (Shark) map[i][x];
                    score += s.size;
                    map[i][x] = null;
                    sharks.remove(s);
                    break;
                }
            }
        }
    }

    private static class Shark extends Entity implements Movable {
        int size;
        boolean isDied;

        public Shark(int x, int y, int speed, int dir, int size, int uid) {
            super(x, y, speed, dir, uid);
            this.size = size;
        }

        @Override
        public void move(Entity[][] map) {
            map[y][x] = null;

            int dist;
            if (dir == MoveDir.LEFT.value || dir == MoveDir.RIGHT.value) {
                // left & right
                dist = speed % (2 * (C - 1));
                while (dist > 0) {
                    if (x == 1 && dir == MoveDir.LEFT.value) {
                        dir = MoveDir.RIGHT.value;
                    }
                    if (x == C && dir == MoveDir.RIGHT.value) {
                        dir = MoveDir.LEFT.value;
                    }
                    x += dirX[dir - 1];
                    dist--;
                }

            } else {
                // up & down
                dist = speed % (2 * (R - 1));
                while (dist > 0) {
                    if (y == 1 && dir == MoveDir.UP.value) {
                        dir = MoveDir.DOWN.value;
                    }

                    if (y == R && dir == MoveDir.DOWN.value) {
                        dir = MoveDir.UP.value;
                    }
                    y += dirY[dir - 1];
                    dist--;
                }
            }
        }
    }


    static int R, C, M;
    static Entity[][] map;
    static Angler angler;
    static List<Shark> sharks;

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        map = new Entity[R + 1][C + 1];
        map[0][0] = angler = new Angler(0, 0, 1, Entity.MoveDir.RIGHT.value, 0);
        sharks = new LinkedList<>();
        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine());
            int r = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            int s = Integer.parseInt(st.nextToken());
            int d = Integer.parseInt(st.nextToken());
            int z = Integer.parseInt(st.nextToken());
            sharks.add(new Shark(c, r, s, d, z, i + 1));
            map[r][c] = sharks.get(i);
        }

        while (angler.x < C) {
            // 1. 낚시왕이 오른쪽으로 한 칸 이동한다.
            angler.move(map);

            // 2. 낚시왕이 있는 열에 있는 상어 중에서 땅과 제일 가까운 상어를 잡는다. 상어를 잡으면 격자판에서 잡은 상어가 사라진다.
            angler.fishing(map);

            // 3. 상어가 이동한다.
            for (Shark s : sharks) {
                s.move(map);
            }

            // 4. 죽은 상어를 marking
            for (Shark s : sharks) {
                if (map[s.y][s.x] != null) {
                    if (map[s.y][s.x] instanceof Shark) {
                        Shark curr = (Shark) map[s.y][s.x];
                        if (s.size > curr.size) {
                            map[s.y][s.x] = s;
                            curr.isDied = true;
                        } else {
                            s.isDied = true;
                        }
                    }
                } else {
                    map[s.y][s.x] = s;
                }
            }

            // 5. 죽은 상어를 list에서 제거
            for (Iterator<Shark> it = sharks.listIterator(); it.hasNext(); ) {
                Shark s = it.next();
                if (s.isDied) it.remove();
            }
        }

        bw.write(String.valueOf(angler.score));

        // close the buffer
        br.close();
        bw.close();
    }
}