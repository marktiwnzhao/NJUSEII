package org.fffd.l23o6.util.strategy.train;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import jakarta.annotation.Nullable;


public class GSeriesSeatStrategy extends TrainSeatStrategy {
    public static final GSeriesSeatStrategy INSTANCE = new GSeriesSeatStrategy();
    private final Map<Integer, String> BUSINESS_SEAT_MAP = new HashMap<>();
    private final Map<Integer, String> FIRST_CLASS_SEAT_MAP = new HashMap<>();
    private final Map<Integer, String> SECOND_CLASS_SEAT_MAP = new HashMap<>();

    private final Map<GSeriesSeatType, Map<Integer, String>> TYPE_MAP = new HashMap<>() {{
        put(GSeriesSeatType.BUSINESS_SEAT, BUSINESS_SEAT_MAP);
        put(GSeriesSeatType.FIRST_CLASS_SEAT, FIRST_CLASS_SEAT_MAP);
        put(GSeriesSeatType.SECOND_CLASS_SEAT, SECOND_CLASS_SEAT_MAP);
    }};
    private final Map<GSeriesSeatType, Double> MONEY_PER_STARTION_MAP = new HashMap<>() {{
        put(GSeriesSeatType.BUSINESS_SEAT, 60.0);
        put(GSeriesSeatType.FIRST_CLASS_SEAT, 40.0);
        put(GSeriesSeatType.SECOND_CLASS_SEAT, 20.0);
    }};

    public Double price(GSeriesSeatStrategy.GSeriesSeatType type, int len) {
        return MONEY_PER_STARTION_MAP.get(type) * len;
    }

    private GSeriesSeatStrategy() {

        int counter = 0;

        for (String s : Arrays.asList("1车1A", "1车1C", "1车1F")) {
            BUSINESS_SEAT_MAP.put(counter++, s);
        }

        counter = 0;

        for (String s : Arrays.asList("2车1A", "2车1C", "2车1D", "2车1F", "2车2A", "2车2C", "2车2D", "2车2F", "3车1A", "3车1C", "3车1D", "3车1F")) {
            FIRST_CLASS_SEAT_MAP.put(counter++, s);
        }

        counter = 0;

        for (String s : Arrays.asList("4车1A", "4车1B", "4车1C", "4车1D", "4车2F", "4车2A", "4车2B", "4车2C", "4车2D", "4车2F", "4车3A", "4车3B", "4车3C", "4车3D", "4车3F")) {
            SECOND_CLASS_SEAT_MAP.put(counter++, s);
        }

    }

    public enum GSeriesSeatType implements SeatType {
        BUSINESS_SEAT("商务座"), FIRST_CLASS_SEAT("一等座"), SECOND_CLASS_SEAT("二等座"), NO_SEAT("无座");
        private String text;

        GSeriesSeatType(String text) {
            this.text = text;
        }

        public String getText() {
            return this.text;
        }

        public static GSeriesSeatType fromString(String text) {
            for (GSeriesSeatType b : GSeriesSeatType.values()) {
                if (b.text.equalsIgnoreCase(text)) {
                    return b;
                }
            }
            return null;
        }
    }


    public @Nullable String allocSeat(int startStationIndex, int endStationIndex, GSeriesSeatType type, boolean[][] seatMap) {
        // Get the seat type map.
        Map<Integer, String> seatTypeMap = TYPE_MAP.get(type);

        // Calculate offset for the seat type.
        int offset = 0;
        for (GSeriesSeatType seatType : GSeriesSeatType.values()) {
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
    /**
     * @Description: 退款操作, 首先遍历TYPE_MAP, 去看values里面, value是一个图, 看图的value有么有和seat一样的(肯定有), 找到之后, key就是他相对他们那一组的偏移, 加上偏移就是结果
     * @Param: [startStationIndex, endStationIndex, seat, seatMap]
     * @return: void
     * @Date: 2023/6/29
     */
    public boolean[][] refundSeat(int startStationIndex, int endStationIndex, String seat, boolean[][] seatMap) {
        int offset = 0;
        for (var x : GSeriesSeatType.values()) {
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
    public GSeriesSeatType seatToType(String seat) {
        for (var x : GSeriesSeatStrategy.GSeriesSeatType.values()) {
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
    public Map<GSeriesSeatType, Integer> getLeftSeatCount(int startStationIndex, int endStationIndex, boolean[][] seatMap) {
        Map<GSeriesSeatType, Integer> leftSeatCount = new HashMap<>();

        // Calculate the left seats for each seat type.
        int offset = 0;
        for (GSeriesSeatType type : GSeriesSeatType.values()) {
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
        return new boolean[stationCount - 1][BUSINESS_SEAT_MAP.size() + FIRST_CLASS_SEAT_MAP.size() + SECOND_CLASS_SEAT_MAP.size()];
    }
}
