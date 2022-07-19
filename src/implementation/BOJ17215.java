package implementation;

import java.io.*;
import java.util.*;


/**
 * 볼링 점수 계산 - BOJ17215
 * -----------------
 * Input 1
 * 9-8P72S9P-9S-P9-SS8
 *
 * Output 1
 * 150
 * -----------------
 * Input 2
 * SSSSSSSSSSSS
 *
 * Output 2
 * 300
 * -----------------
 */
public class BOJ17215 {

    static final int MAX_SCORE = 10;
    static final int FINAL_FRAME = 10;

    public static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public static int toDigit(char c) {
        if (isDigit(c)) return c - '0';
        else return -1;
    }

    public static int nextScore(String scoreBoard, int start, int size) {
        int score = 0;

        for (int i = start; i < start + size; i++) {
            if (i > scoreBoard.length() - 1) break;

            char c = scoreBoard.charAt(i);
            if (isDigit(c)) {
                // score is number
                score += toDigit(c);
            } else if (c == 'P') {
                // score is spare
                score += (MAX_SCORE - toDigit(scoreBoard.charAt(i - 1)));
            } else if (c == 'S') {
                // score is strike
                score += MAX_SCORE;
            }
        }

        return score;
    }

    public static int getScore(String scoreBoard) {
        int score = 0, tmp = 0;
        int numStack = 0, frame = 1;

        for (int i = 0; i < scoreBoard.length(); i++) {
            char c = scoreBoard.charAt(i);

            if (isDigit(c)) {
                // score is number
                tmp = toDigit(c);
                score += tmp;
                numStack++;
            } else {
                if (c == 'P') {
                    // score is spare
                    score += (MAX_SCORE - tmp);
                    if (frame < FINAL_FRAME) {
                        score += nextScore(scoreBoard, i + 1, 1);
                    }
                } else {
                    // score is strike
                    score += 10;
                    if (frame < FINAL_FRAME) {
                        score += nextScore(scoreBoard, i + 1, 2);
                    }
                }
                numStack = 2;
            }

            //System.out.println(frame + " / " + score + " / " + c);
            if (numStack == 2) {
                numStack = 0;
                if (frame < FINAL_FRAME) frame++;
            }
        }

        return score;
    }

    public static void main(String[] args) throws Exception {
        // Input & Output stream
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));

        // get score board
        String scoreBoard = br.readLine();
        scoreBoard = scoreBoard.replaceAll("-", "0");

        // write the result
        bw.write(String.valueOf(getScore(scoreBoard)));

        // close the buffer
        br.close();
        bw.close();
    }
}
