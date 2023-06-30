package org.fffd.l23o6.util.strategy.train;

import java.util.*;

import jakarta.annotation.Nullable;


public class KSeriesSeatStrategy extends TrainSeatStrategy {
    public static final KSeriesSeatStrategy INSTANCE = new KSeriesSeatStrategy();

    private final Map<Integer, String> SOFT_SLEEPER_SEAT_MAP = new HashMap<>();
    private final Map<Integer, String> HARD_SLEEPER_SEAT_MAP = new HashMap<>();
    private final Map<Integer, String> SOFT_SEAT_MAP = new HashMap<>();
    private final Map<Integer, String> HARD_SEAT_MAP = new HashMap<>();

    private final Map<KSeriesSeatType, Map<Integer, String>> TYPE_MAP = new HashMap<>() {{
        put(KSeriesSeatType.SOFT_SLEEPER_SEAT, SOFT_SLEEPER_SEAT_MAP);
        put(KSeriesSeatType.HARD_SLEEPER_SEAT, HARD_SLEEPER_SEAT_MAP);
        put(KSeriesSeatType.SOFT_SEAT, SOFT_SEAT_MAP);
        put(KSeriesSeatType.HARD_SEAT, HARD_SEAT_MAP);
    }};

    private final Map<KSeriesSeatStrategy.KSeriesSeatType, Double> MONEY_PER_STARTION_MAP = new HashMap<>() {{
        put(KSeriesSeatType.SOFT_SLEEPER_SEAT, 30.0);
        put(KSeriesSeatType.HARD_SLEEPER_SEAT, 20.0);
        put(KSeriesSeatType.SOFT_SEAT, 15.0);
        put(KSeriesSeatType.HARD_SEAT, 10.0);
    }};

    public Double price(KSeriesSeatStrategy.KSeriesSeatType type, int len) {
        return MONEY_PER_STARTION_MAP.get(type) * len;
    }

    private KSeriesSeatStrategy() {

        int counter = 0;

        for (String s : Arrays.asList("软卧1号上铺", "软卧2号下铺", "软卧3号上铺", "软卧4号上铺", "软卧5号上铺", "软卧6号下铺", "软卧7号上铺", "软卧8号上铺")) {
            SOFT_SLEEPER_SEAT_MAP.put(counter++, s);
        }

        counter = 0;

        for (String s : Arrays.asList("硬卧1号上铺", "硬卧2号中铺", "硬卧3号下铺", "硬卧4号上铺", "硬卧5号中铺", "硬卧6号下铺", "硬卧7号上铺", "硬卧8号中铺", "硬卧9号下铺", "硬卧10号上铺", "硬卧11号中铺", "硬卧12号下铺")) {
            HARD_SLEEPER_SEAT_MAP.put(counter++, s);
        }

        counter = 0;

        for (String s : Arrays.asList("1车1座", "1车2座", "1车3座", "1车4座", "1车5座", "1车6座", "1车7座", "1车8座", "2车1座", "2车2座", "2车3座", "2车4座", "2车5座", "2车6座", "2车7座", "2车8座")) {
            SOFT_SEAT_MAP.put(counter++, s);
        }

        counter = 0;

        for (String s : Arrays.asList("3车1座", "3车2座", "3车3座", "3车4座", "3车5座", "3车6座", "3车7座", "3车8座", "3车9座", "3车10座", "4车1座", "4车2座", "4车3座", "4车4座", "4车5座", "4车6座", "4车7座", "4车8座", "4车9座", "4车10座")) {
            HARD_SEAT_MAP.put(counter++, s);
        }
    }

    public enum KSeriesSeatType implements SeatType {
        SOFT_SLEEPER_SEAT("软卧"), HARD_SLEEPER_SEAT("硬卧"), SOFT_SEAT("软座"), HARD_SEAT("硬座"), NO_SEAT("无座");
        private String text;

        KSeriesSeatType(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public static KSeriesSeatType fromString(String text) {
            for (KSeriesSeatType b : KSeriesSeatType.values()) {
                if (b.text.equalsIgnoreCase(text)) {
                    return b;
                }
            }
            return null;
        }
    }


    public @Nullable String allocSeat(int startStationIndex, int endStationIndex, KSeriesSeatType type, boolean[][] seatMap) {
        // Get the seat type map.
        Map<Integer, String> seatTypeMap = TYPE_MAP.get(type);

        // Calculate offset for the seat type.
        int offset = 0;
        for (KSeriesSeatType seatType : KSeriesSeatType.values()) {
            if (seatType.equals(type)) {
                break;
            }
            offset += TYPE_MAP.get(seatType).size();
        }

        // Loop through all the seats of the desired type.
        for (int i = 0; i < seatTypeMap.size(); i++) {
            boolean isAvailable = true;
            // Check if the seat is available for all the stations between start and end.
            for (int station = startStationIndex; station < endStationIndex; station++) {
                if (seatMap[station][offset + i]) {
                    isAvailable = false;
                    break;
                }
            }

            // If seat is available, mark it as occupied and return seat name.
            if (isAvailable) {
                for (int station = startStationIndex; station < endStationIndex; station++) {
                    seatMap[station][offset + i] = true;
                }
                return seatTypeMap.get(i);
            }
        }

        // If no seat is available, return null.
        return null;
    }
    public boolean[][] refundSeat(int startStationIndex, int endStationIndex, String seat, boolean[][] seatMap) {
            int offset = 0;
        for (var x : KSeriesSeatStrategy.KSeriesSeatType.values()) {
            Map<Integer, String> t = TYPE_MAP.get(x);
            if(t==null)
                break;
            for(var m:t.entrySet()){
                if(seat.equals(m.getValue())){
                    Integer key = m.getKey();
                    for (int station = startStationIndex; station < endStationIndex; station++) {
                        seatMap[station][offset+key]=false;
                    }
                    return seatMap;
                }
            }
            offset+=t.size();
        }
        return seatMap;
    }
    public KSeriesSeatType seatToType( String seat) {
        for (var x : KSeriesSeatStrategy.KSeriesSeatType.values()) {
            Map<Integer, String> t = TYPE_MAP.get(x);
            if(t==null)
                break;
            for(var m:t.entrySet()){
                if(seat.equals(m.getValue())){
                        return x;
                }
            }
        }
        return null;
    }
    public Map<KSeriesSeatType, Integer> getLeftSeatCount(int startStationIndex, int endStationIndex, boolean[][] seatMap) {
        Map<KSeriesSeatType, Integer> leftSeatCount = new HashMap<>();

        // Calculate the left seats for each seat type.
        int offset = 0;
        for (KSeriesSeatType type : KSeriesSeatType.values()) {
            Map<Integer, String> seatTypeMap = TYPE_MAP.get(type);
            if (seatTypeMap == null)
                break;
            int count = 0;
            for (int i = 0; i < seatTypeMap.size(); i++) {
                boolean isAvailable = true;
                for (int station = startStationIndex; station < endStationIndex; station++) {
                    if (seatMap[station][offset + i]) {
                        isAvailable = false;
                        break;
                    }
                }
                if (isAvailable) {
                    count++;
                }
            }
            leftSeatCount.put(type, count);
            offset += seatTypeMap.size();
        }

        return leftSeatCount;
    }


    public boolean[][] initSeatMap(int stationCount) {
        return new boolean[stationCount - 1][SOFT_SLEEPER_SEAT_MAP.size() + HARD_SLEEPER_SEAT_MAP.size() + SOFT_SEAT_MAP.size() + HARD_SEAT_MAP.size()];
    }
}
