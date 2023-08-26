package io.github.viccch.footprints.ui.me;

import java.util.ArrayList;
import java.util.List;

public class People {
    public String id;
    public int subscribe;
    public int fans;
    public String head;

    public List<String> subscribeList;//关注
    public List<String> fansList;//粉丝

    private People(String id, int subscribe, int fans, String head) {
        this.id = id;
        this.subscribe = subscribe;
        this.fans = fans;
        this.head = head;

        subscribeList = new ArrayList<>();
        fansList = new ArrayList<>();
    }

    public People(String id) {
        this(id, 0, 0, null);
    }

    public static class PeopleSubscribe {
        public int id;
        public String user_id;
        public String user_subscribe;
    }
}
