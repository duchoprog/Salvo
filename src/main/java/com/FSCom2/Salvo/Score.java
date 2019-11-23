package com.FSCom2.Salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
public class Score {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    private Player player;

    private float score;

    private LocalDateTime endDate;
//constructores


    public Score() {
    }

    public Score(Game game, Player player, float score, LocalDateTime endDate) {
        this.game = game;
        this.player = player;
        this.score = score;
        this.endDate = endDate;
    }
    public Score(Game game, Player player,  LocalDateTime endDate) {
        this.game = game;
        this.player = player;
        this.score = (float) 0.0;
        this.endDate = endDate;
    }

    public Score(Game game,   LocalDateTime endDate) {
        this.game = game;

        this.score = (float) 0.0;
        this.endDate = endDate;
    }


    //getters y setters
    public Long getId() {
        return id;
    }

    @JsonIgnore
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
@JsonIgnore
    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public LocalDateTime getendDate() {
        return endDate;
    }

    public void setendDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }


    public Map<String, Object> scoreDTO(){
        Map<String, Object> dto= new LinkedHashMap<String, Object>();
        dto.put("Este_score", this.getScore());
        dto.put("Finalizado", this.getendDate());

        return dto;

    }
}
