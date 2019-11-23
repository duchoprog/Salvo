package com.FSCom2.Salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class GamePlayer {
@Id
@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
@GenericGenerator(name = "native", strategy = "native")
private Long id;
private LocalDateTime joinDate;
@ManyToOne (fetch = FetchType.EAGER)
private Game game;

@ManyToOne(fetch = FetchType.EAGER)
private Player player;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Ship> ships=new HashSet<>();
    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Salvo> salvoes=new HashSet<>();

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void addShips(Ship sh) {
        this.ships.add(sh);
        sh.setGamePlayer(this);
    }
    public Set<Salvo> getSalvoes() {
        return salvoes;
    }

    public void addSalvoes(Salvo slv) {
        this.salvoes.add(slv);
        slv.setGamePlayer(this);
    }

    public GamePlayer() {
    }


    public GamePlayer(LocalDateTime joinDate, Game game, Player player) {
        this.joinDate = joinDate;
        this.game = game;
        this.player = player;
    }

    public GamePlayer(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    public Map<String, Object> gamePlayerDTO(){
        Map<String, Object> dto= new LinkedHashMap<String, Object>();
        dto.put("gPid", this.getId());
        dto.put("gPtime", this.getJoinDate());
        dto.put("player", this.player.playerDTO());
        if(this.getScore()!=null) {
            dto.put("ElScore", this.getScore().scoreDTO());
        }

        return dto;
    }
    public Map <String, Object> leaderboardDTO(){
        Map<String, Object> dto= new LinkedHashMap<String, Object>();
        dto.put("player", this.getPlayer().getFullName());
        if(this.getScore()!=null) {
        dto.put("puntos", this.getScore().getScore());
        }
        return dto;
    }

    public Score getScore(){
        return this.getPlayer().getScore(this.getGame());

    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDateTime joinDate) {
        this.joinDate = joinDate;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


}
