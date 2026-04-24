package net.wxyttransit.data;

import mtr.client.ClientCache;
import mtr.client.ClientData;
import mtr.data.*;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.List;
import java.util.Set;

public class DataObject {
    public Platform platform;
    public Station station;
    public Set<ScheduleEntry> schedules;
    public Set<Station> connectingStations;
    public List<ClientCache.PlatformRouteDetails> details;
    public Depot depot;
    public Siding siding;
    public Route route;
    public int arrow;
    public Long id;
    public DataObject(Long platformId){
        platform = ClientData.DATA_CACHE.platformIdMap.get(platformId);
        station = ClientData.DATA_CACHE.platformIdToStation.get(platformId);
        schedules = ClientData.SCHEDULES_FOR_PLATFORM.get(platformId);
        details = ClientData.DATA_CACHE.requestPlatformIdToRoutes(platformId);
        connectingStations = ClientData.DATA_CACHE.stationIdToConnectingStations.get(station);
    this.id = platformId;
    }
    public DataObject(Long platformId,int arrow){
        this(platformId);
        this.arrow = arrow;

    }
}