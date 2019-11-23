package com.FSCom2.Salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private LocalDateTime gameDate;



    @OneToMany(mappedBy = "game")
    private Set<GamePlayer> gamePlayers=new HashSet<>();

    @OneToMany(mappedBy = "game")
    private Set<Score> score=new HashSet<>();

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public long getId() {
        return id;
    }

public void addGamePlayer(GamePlayer e){
    this.getGamePlayers().add(e);
}

    public Game(){}

public Game(LocalDateTime gameDate) {
        this.gameDate = gameDate;

    }



    public LocalDateTime getGameDate() {
        return gameDate;
    }

    public void setGameDate(LocalDateTime gameDate) {
        this.gameDate = gameDate;
    }
    public Map<String, Object> gameDTO(){
        Map<String, Object> dto= new LinkedHashMap<String, Object>();
        dto.put("gameId", this.getId());
        dto.put("gameTime", this.getGameDate());
        dto.put("gamePlayer", this.gamePlayers.stream().map(GamePlayer::gamePlayerDTO).collect(Collectors.toList()));
        return dto;

    }


}
