package com.FSCom2.Salvo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String lastName;
    private String firstName;
    private String email;
    private String pass;
    private int puntos;
    private String fullName;


    public long getId() {
        return id;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }



    public void addGamePlayers(GamePlayer gp) {
        this.getGamePlayers().add(gp);
    }

    @OneToMany(mappedBy = "player")
    private Set<GamePlayer> gamePlayers=new HashSet<>();

    @OneToMany(mappedBy = "player")
    private Set<Score> scores=new HashSet<>();


    public Player() {
    }

    public Player(String email, String pass) {
        this.email = email;
        this.pass = pass;
        this.lastName=" ";
        this.firstName=email;
    }

    public Player(String lastName, String firstName, String email, String pass) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.pass = pass;
    }

    public Player( String email,  String firstName,String lastName) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.pass = pass;
    }

    public String getFullName() {
        fullName=firstName+" "+lastName;
        return fullName;
    }

    public Map<String, Object> playerDTO(){
        Map<String, Object> dto= new LinkedHashMap<String, Object>();
        dto.put("playerId", this.getId());
        dto.put("fullName", this.getFullName());
        dto.put("email", this.getEmail());

        return dto;

    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }


//methods
    public String nombreCompl() {
        return firstName + " " + lastName;
    }
public String saludar(){
    return "Hola, soy"+ this.firstName;
}

@JsonIgnore
public Score getScore(Game game){
       //return scores.stream().filter(cada->cada.getGame().getId()==game.getId() ).findFirst().orElse(null);

       for (Score cadaScore: scores){
           if (cadaScore.getGame().getId()==game.getId()){
               return cadaScore;
           }
       }
return null;

//miGameView.put("Mis barcos",origen.getShips().stream().map(cada->cada.shipDTO()).collect(Collectors.toList()) );


}

}

