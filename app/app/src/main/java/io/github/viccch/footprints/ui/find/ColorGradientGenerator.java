package io.github.viccch.footprints.ui.find;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class ColorGradientGenerator {
    public static List<Integer> get_red2green(int size) {
        List<Integer> list = new ArrayList<Integer>();
        list.add(Color.rgb(255, 0, 0));
        list.add(Color.rgb(255, 100, 0));
        list.add(Color.rgb(255, 200, 0));
        list.add(Color.rgb(255, 255, 0));
        list.add(Color.rgb(200, 255, 0));
        list.add(Color.rgb(100, 255, 0));
        list.add(Color.rgb(0, 255, 0));
        return list;
    }
}
