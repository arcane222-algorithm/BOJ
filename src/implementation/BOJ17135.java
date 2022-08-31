package implementation;

import java.io.*;
import java.util.*;


/**
 * 캐슬 디펜스 - BOJ17135
 * -----------------
 *
 *
 *  Entity 클래스를 정의하여 Archer 클래스와 Enemy 클래스가 상속하도록 한다.
 *  Entity 클래스에 정의되어 있는 dist 메소드를 이용하여 (dist : |x1 - x2| + |y1 - y2|) Archer 클래스에 findTarget 메소드를 정의한다.
 *  findTarget 메소드를 통해 3명의 Archer들이 enemies list에 있는 적들 중 1)거리가 가장 가깝고 2)그러한 적들 중 x좌표가 가장 작은 (가장 왼쪽에 있는) 적을 target으로 삼는다.
 *  해당 target들을 set에 넣어 중복을 제거해주면 매 시뮬레이션마다 Archer들이 제거하는 적의 숫자가 된다.
 *
 *  target searching이 끝나면 set에 추가적으로 아래로 움직이는 적들 중 성에 도달하는 적들까지 넣어준 후
 *  set 안의 index 값들을 이용하여 enemies list에서 적들을 제거한다. (제거한 적은 tmp list에 따로 수집한 후 모든 적이 제거되면 enemies list와 tmp list를 swap 한다.)
 *
 *  이러한 식으로 최대 input에 대하여 M이 15까지 들어오므로 archer 3명을 배치할 수 있는 경우의 수는
 *  15 x 14 x 13이고 brute-force하게 경우의 수를 탐색이 가능한다.
 *  각 경우에 대하여 위의 방식에 따라 target을 제거한 수를 구해준 후 이의 최댓값을 찾는다.
 *
 * -----------------
 * Input 1
 * 5 5 1
 * 0 0 0 0 0
 * 0 0 0 0 0
 * 0 0 0 0 0
 * 0 0 0 0 0
 * 1 1 1 1 1
 *
 * Output 1
 * 3
 * -----------------
 * Input 2
 * 5 5 1
 * 0 0 0 0 0
 * 0 0 0 0 0
 * 0 0 0 0 0
 * 1 1 1 1 1
 * 0 0 0 0 0
 *
 * Output 2
 * 3
 * -----------------
 * Input 3
 * 5 5 2
 * 0 0 0 0 0
 * 0 0 0 0 0
 * 0 0 0 0 0
 * 1 1 1 1 1
 * 0 0 0 0 0
 *
 * Output 3
 * 5
 * -----------------
 * Input 4
 * 5 5 5
 * 1 1 1 1 1
 * 1 1 1 1 1
 * 1 1 1 1 1
 * 1 1 1 1 1
 * 1 1 1 1 1
 *
 * Output 4
 * 15
 * -----------------
 * Input 5
 * 6 5 1
 * 1 0 1 0 1
 * 0 1 0 1 0
 * 1 1 0 0 0
 * 0 0 0 1 1
 * 1 1 0 1 1
 * 0 0 1 0 0
 *
 * Output 5
 * 9
 * -----------------
 * Input 6
 * 6 5 2
 * 1 0 1 0 1
 * 0 1 0 1 0
 * 1 1 0 0 0
 * 0 0 0 1 1
 * 1 1 0 1 1
 * 0 0 1 0 0
 *
 * Output 6
 * 14
 * -----------------
 */
public class BOJ17135 {

    private static class Entity {
        int x, y;

        public Entity() {
        }

        public Entity(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int dist(Entity entity) {
            return Math.abs(x - entity.x) + Math.abs(y - entity.y);
        }
    }

    private static class Archer extends Entity {
        private static final int MAX_ARCHER_CNT = 3;

        int range;

        public Archer(int x, int y, int range) {
            super(x, y);
            this.range = range;
        }

        public int findTarget() {
            int distMin = Integer.MAX_VALUE, xMin = 0, target = -1;

            for (int i = 0; i < enemies.size(); i++) {
                Enemy e = enemies.get(i);
                int distTmp = dist(e);
                if (distTmp > range) continue;

                if (distTmp < distMin) {
                    distMin = distTmp;
                    xMin = e.x;
                    target = i;
                } else if (distTmp == distMin) {
                    if (xMin > e.x) {
                        xMin = e.x;
                        target = i;
                    }
                }
            }

            return target;
        }
    }

    private static class Enemy extends Entity {
        int baseX, baseY;

        public Enemy(int baseX, int baseY) {
            this.baseX = baseX;
            this.baseY = baseY;
        }

        public void init() {
            x = baseX;
            y = baseY;
        }
    }

    static int N, M, D;
    public static List<Archer> archers = new ArrayList<>(Archer.MAX_ARCHER_CNT);
    public static List<Enemy> enemies = new LinkedList<>();

    public static int getKillCount() {
        List<Enemy> tmp = new LinkedList<>();
        int cnt = 0;
        for (Enemy e : enemies) {
            e.init();
        }

        TreeSet<Integer> targets = new TreeSet<>();
        while (enemies.size() > 0) {
            targets.clear();
            for (int i = 0; i < Archer.MAX_ARCHER_CNT; i++) {
                int target = archers.get(i).findTarget();
                if (target != -1) targets.add(target);
            }
            cnt += targets.size();

            for (int i = 0; i < enemies.size(); i++) {
                Enemy e = enemies.get(i);
                if (e.y + 1 == N) {
                    targets.add(i);
                } else {
                    e.y += 1;
                }
            }

            int i = 0;
            for (int idx : targets) {
                idx -= i;
                tmp.add(enemies.remove(idx));
                i++;
            }
        }
        enemies = tmp;

        return cnt;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = null;

        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        D = Integer.parseInt(st.nextToken());

        for (int i = 0; i < Archer.MAX_ARCHER_CNT; i++) {
            archers.add(new Archer(0, N, D));
        }

        for (int i = 0; i < N; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < M; j++) {
                if (Integer.parseInt(st.nextToken()) == 1) {
                    enemies.add(new Enemy(j, i));
                }
            }
        }

        int maxKillCount = 0;
        for (int i = 0; i < M - 2; i++) {
            archers.get(0).x = i;
            for (int j = i + 1; j < M - 1; j++) {
                archers.get(1).x = j;
                for (int k = j + 1; k < M; k++) {
                    archers.get(2).x = k;
                    maxKillCount = Math.max(maxKillCount, getKillCount());
                }
            }
        }

        bw.write(String.valueOf(maxKillCount));

        // close the buffer
        br.close();
        bw.close();
    }
}