package com.FSCom2.Salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String shipType;
    private  int shipSize;

    @ElementCollection
    private List<String> shipLoc;
    @ManyToOne
    private GamePlayer gamePlayer;

    public Ship() {
    }

    public Ship(String shipType, int shipSize, List<String> shipLoc) {
        this.shipType = shipType;
        this.shipSize = shipSize;
        this.shipLoc=shipLoc;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getShipSize() {
        return shipSize;
    }

    public void setShipSize(int shipSize) {
        this.shipSize = shipSize;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public List<String> getShipLoc() {
        return shipLoc;
    }

    public void addShipLoc(String loc) {
        this.getShipLoc().add(loc);
    }

    public Map<String, Object> shipDTO(){
        Map<String, Object> dto= new LinkedHashMap<String, Object>();
        dto.put("shipId", this.getId());
        dto.put("shipType", this.getShipType());
        dto.put("shipLocs", this.getShipLoc());
        return dto;

    }

}
