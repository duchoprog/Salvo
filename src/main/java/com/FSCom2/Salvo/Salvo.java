package com.FSCom2.Salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Salvo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long salvoID;
    @ManyToOne
    private GamePlayer gamePlayer;
    private int turno;
    @ElementCollection
    private List<String> salvoLoc;

    //constructores

    public Salvo() {
    }

    public Salvo( int turno, List<String> salvoLoc) {
        this.turno = turno;
        this.salvoLoc = salvoLoc;
    }
public Salvo(  List<String> salvoLoc) {

        this.salvoLoc = salvoLoc;
    }

    //getters y setters
    public Long getSalvoID() {
        return salvoID;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public int getTurno() {
        return turno;
    }

    public void setTurno(int turno) {
        this.turno = turno;
    }

    public List<String> getSalvoLoc() {
        return salvoLoc;
    }

    public void addSalvoLoc(String loc) {
        this.getSalvoLoc().add(loc);
    }

    public Map<String, Object> salvoDTO(){
        Map<String, Object> dto= new LinkedHashMap<String, Object>();
        dto.put("salvoTurno", this.getTurno());
        dto.put("salvoLocs", this.getSalvoLoc());
        dto.put("GamePlayer", this.getGamePlayer().getId());
        return dto;

    }


}
