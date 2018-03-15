package Utils;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.google.common.collect.Table;

import huawei.exam.ReturnCodeEnum;
import huawei.exam.SubwayException;
import huawei.model.InvalidCard;
import huawei.model.Subways;
import huawei.model.Subways.DistanceInfo;

public class DistenceUtil {
    private Table<String, String, DistanceInfo> distenceTable;
    private static LinkedList<String> staticRoute;
    private static int minMeter;
    public DistenceUtil(Subways subways) {
        super();
        this.distenceTable = subways.getStationDistances();
    }

    public int getdistence(String enterStation, String exitStation) throws SubwayException {
        staticRoute = new LinkedList<>();
        minMeter = Integer.MAX_VALUE;
        if (!distenceTable.containsColumn(enterStation)
                || (!distenceTable.containsColumn(exitStation)))
            throw new SubwayException(ReturnCodeEnum.E07, InvalidCard.getInstance());
        if (enterStation.equals(exitStation)) {
            return 0;
        }

        Set<String> mark = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        Map<String, DistanceInfo> cMap = distenceTable.column(enterStation);
        //TODO print route
        LinkedList<String> route = new LinkedList<>();
        route.add(enterStation);
        for (Map.Entry<String, DistanceInfo> entry : cMap.entrySet()) {
            mark.add(enterStation + "-" + entry.getKey());
            queue.add(entry.getKey());
        }

        int minDistence = Integer.MAX_VALUE;
        while (!queue.isEmpty()) {
            String station = queue.poll();
            int distence;
            distence = getDistence(station, exitStation,mark,route);
            int sumdistence;
            if (distence == Integer.MAX_VALUE)
                sumdistence = Integer.MAX_VALUE;
            else
                sumdistence = distenceTable.get(enterStation,station).getDistance() + distence;

            minDistence = Math.min(minDistence, sumdistence);
        }
        System.out.println("distence:" + minDistence);
        return minDistence;

    }

    private int getDistence(String station, String destination, Set<String> mark,LinkedList<String> fatherRoute) {
        LinkedList<String>route = new LinkedList<>(fatherRoute);
        route.add(station);
        if(station.equals(destination))
            System.out.println(route);
        if (station.equals(destination)) {
            return 0;
        }
        Queue<String> queue = new LinkedList<>();
        Map<String, DistanceInfo> cMap = distenceTable.column(station);
        //加点

        for (Map.Entry<String, DistanceInfo> entry : cMap.entrySet()) {
            if (!mark.contains(station + "-" + entry.getKey()) && !mark.contains(entry.getKey() + "-" + station)) {
                mark.add(station + "-" + entry.getKey());
                queue.add(entry.getKey());
            }
        }
        int minDistence = Integer.MAX_VALUE;
        if (queue.isEmpty()) {
//			return minDistence;
            route.removeLast();
            return Integer.MAX_VALUE;

        }
        while (!queue.isEmpty()) {
            String nextStation = queue.poll();
            int distence;
            distence = getDistence(nextStation, destination, mark,route);
            int sumdistence;
            if (distence == Integer.MAX_VALUE)//不可达跳过计算
                sumdistence = Integer.MAX_VALUE;
            else
                sumdistence = distenceTable.get(station,nextStation).getDistance() + distence;

            minDistence = Math.min(minDistence, sumdistence);
        }
        return minDistence;
    }
}
