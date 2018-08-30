package br.ufal.laccan.wylken.collectactivities.commons;

public class Math {

    public static float mean(float[] m) {
        float sum = 0;
        for (int i = 0; i < m.length; i++) {
            sum += m[i];
        }
        return sum / m.length;
    }
}
