package com.FSCom2.Salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {


    private String autorizado;
    @Autowired
    private GameRepository gameRepo;

    @Autowired
    private PlayerRepository playerRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GamePlayerRepository gamePlayerRepo;
    @Autowired
    private ShipRepository shipRepo;
    @Autowired
    private SalvoRepository salvoRepo;
    @Autowired
    private ScoreRepository scoreRepo;

    //public Player yo;

    @RequestMapping("/newGame")
    public ResponseEntity<Map<String,Object>> newGame( Authentication auth){
        Map<String,Object> map = new HashMap<>();
        if (isGuest(auth)){
            map.put("data","Logueate para crear un juego");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }else{
            Player jugador=playerRepo.findByEmail(auth.getName());
            Game juego=new Game(LocalDateTime.now());
            gameRepo.save(juego);
            GamePlayer nuevoGP=new GamePlayer(LocalDateTime.now(),juego,jugador);
            gamePlayerRepo.save(nuevoGP);
            map.put("data",nuevoGP.getId() );
            return new ResponseEntity<>(map, HttpStatus.OK);

        }
    }
    @RequestMapping("/games")
    public Object getAll() {
        List<Object> todaLaInfo = new ArrayList<>();
        todaLaInfo = gameRepo.findAll().stream().map(cada -> cada.gameDTO()).collect(Collectors.toList());
        return todaLaInfo;
    }

    @RequestMapping("/games/players/{gamePlayerId}/ships")
    public ResponseEntity<Map<String,Object>> deployShips(@PathVariable final Long gamePlayerId, @RequestBody List <Ship> barcos, Authentication auth){
        Map<String,Object> map = new HashMap<>();

        GamePlayer gamePlayer = gamePlayerRepo.findById(gamePlayerId).orElse(null);
        Ship ship;
        if (gamePlayerRepo.findById(gamePlayerId).get().getPlayer().getEmail().equals(getLogueado(auth)) ) {
            if(barcos.size()!=0)
{
          if (barcos.size()==5)
{            barcos.stream().forEach(cada -> {
                gamePlayer.addShips(cada);
                //System.out.println(cada.getShipLoc());
            });
            gamePlayerRepo.save(gamePlayer);
Object culo= new Object();
culo="culo";
            map.put("data",culo);
            return new ResponseEntity<>(map, HttpStatus.OK);
}else{
              map.put("data","los barcos deben ser 5");
              return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);

          }


}else{
                map.put("data","ya estan los barcos de este juego");
                return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
            }


        }else{


        map.put("data","solo podes elegir barcos en tus juegos");
        return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
}

    }
////salvoes

    @RequestMapping("/games/players/{gamePlayerId}/salvoes")
    public ResponseEntity<Map<String,Object>> fireSalvoes(@PathVariable final Long gamePlayerId, @RequestBody List <String> bombas, Authentication auth){
        Map<String,Object> map = new HashMap<>();
        AtomicInteger contador= new AtomicInteger(0);
        GamePlayer gamePlayer = gamePlayerRepo.findById(gamePlayerId).orElse(null);

        if (gamePlayerRepo.findById(gamePlayerId).get().getPlayer().getEmail().equals(getLogueado(auth)))
        {

            int salvoCount=gamePlayer.getSalvoes().size()+1;
            Salvo salvo=salvoRepo.save(new Salvo(salvoCount,bombas));
            //System.out.println(salvo);
            gamePlayer.addSalvoes(salvo);
            gamePlayerRepo.save(gamePlayer);

            map.put("data",salvoCount);
            return new ResponseEntity<>(map, HttpStatus.OK);

        }else{


            map.put("data","eso no lo podes hacer");
            return new ResponseEntity<>(map, HttpStatus.FORBIDDEN);
        }

    }




    /////logueado
    @RequestMapping("/logueado")
    public String getLogueado(Authentication authentication) {
        if(isGuest(authentication)){
            return "guest";
        }
        //yo=playerRepo.findByEmail(authentication.getName());
        return playerRepo.findByEmail(authentication.getName()).getEmail();
    }

    @RequestMapping("/players")
    public Object createPlayer(@RequestParam  String name, @RequestParam String pwd){
/////
            Map <String,String> incompleto=new HashMap< >();
            incompleto.put("error", "Media pila, completá email y password");

            if (name.isEmpty() || pwd.isEmpty()  ) {
                return new ResponseEntity<>(incompleto, HttpStatus.FORBIDDEN);
            }

        Map <String,String> repetido=new HashMap< >();
        repetido.put("error", "Ese email ya está usado");

        if (playerRepo.findByEmail(name) !=  null) {
                return new ResponseEntity<>(repetido, HttpStatus.FORBIDDEN);
            }
            Player elNuevo=(new Player(name, passwordEncoder.encode(pwd)));
            playerRepo.save(elNuevo);
            return new ResponseEntity<Object>(elNuevo,HttpStatus.CREATED);
        }

 /////////////////////




    @RequestMapping("/gamePlayersPropios")
    public Object getInfo(Authentication authentication) {
        String elLogueado= getLogueado(authentication);
        autorizado=elLogueado;
        List<Object> todaLaInfo = new ArrayList<>();
        List<GamePlayer> gamePlayersPropios= gamePlayerRepo.findAll().stream()
                .filter(cada -> cada.getPlayer().getEmail().equals(elLogueado) ).collect(Collectors.toList());
        List<String> aVer=gamePlayersPropios.stream().map(cada->cada.getPlayer().getEmail()).collect(Collectors.toList());
        if (aVer.isEmpty()){
            return "no hay partidos de este jugador";
        }
        todaLaInfo = gameRepo.findAll().stream().map(cada -> cada.gameDTO()).collect(Collectors.toList());
        //return todaLaInfo;
        return gamePlayersPropios;
    }
    @RequestMapping("/game/{gid}/players")
    public ResponseEntity<Map<String,Object>> joinGame(@PathVariable final Long gid, Authentication auth){
        Map<String,Object> map = new HashMap<>();
        Player jugador;
        Game juego;
        if (isGuest(auth)){
            map.put("data","Logueate para sumarte a un juego");
            return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);
        }else{
            jugador=playerRepo.findByEmail(auth.getName());
            juego= gameRepo.findById(gid).orElse(null);

            if(juego.getGamePlayers().size()>1){
                map.put("data","Ya estamos todos");
                return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);//
            }
            else if (juego.getGamePlayers().stream().anyMatch(cada-> cada.getPlayer().getId() == jugador.getId())){
                map.put("data","No, cómico, no podés jugar contra vos mismo");
                return new ResponseEntity<>(map, HttpStatus.UNAUTHORIZED);//

            }
            else{
                if (jugador!=null){
                    GamePlayer newGP=new GamePlayer(LocalDateTime.now(),juego,jugador);
                    gamePlayerRepo.save(newGP);
                    map.put("data",newGP.getId());
                    return new ResponseEntity<>(map, HttpStatus.OK);//
                }
            }

        }
        map.put("data","tururu");
    return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @RequestMapping("/game_view/{id}")
    public Object getGemeView(@PathVariable final Long id, Authentication authentication){
        String elLogueado= getLogueado(authentication);
        autorizado=elLogueado;
        Map<String, Object> miGameView= new LinkedHashMap<>();
        GamePlayer origen=gamePlayerRepo.findById(id).get();
        System.out.println(origen.getScore());
        // voy a chequear que solo muestre del autorizado     esto era el if: gamePlayerRepo.findById(id).get().getPlayer().getEmail().equals(autorizado)
        if (gamePlayerRepo.findById(id).get().getPlayer().getEmail().equals(autorizado)){
        GamePlayer gPRival = origen.getGame().getGamePlayers().stream().filter(cada->cada.getId()!=id).findFirst().orElse(null);
        miGameView.put("Score",origen.getScore());
        if(origen.getGame().getGamePlayers().size()==2) {
            miGameView.put("flotaRival", gPRival.getShips().size());

        }else{
            miGameView.put("flotaRival",0);
        }


        miGameView.put("yo",origen.getPlayer().getFullName());
        miGameView.put("Mi juego", origen.getGame().gameDTO() );
        miGameView.put("Mis barcos",origen.getShips().stream().map(cada->cada.shipDTO()).collect(Collectors.toList()) );
    Object miGP= origen.getGame().gameDTO();
    Object miRival= new Object();
    List<String> barcosRivales= new ArrayList<>();
    List<String> barcosMios= new ArrayList<>();
    List<Map<String,Object>>lePegue=new ArrayList<>();
    List<Map<String,Object>>mePego=new ArrayList<>();
    Map<String, Object> rivalesHundidos= new HashMap<>();
    Map<String, Object> misHundidos= new HashMap<>();

//////
            if(origen.getGame().getGamePlayers().size()==2){
    gPRival.getShips().forEach(cadaBarco->barcosRivales.addAll(cadaBarco.getShipLoc()));}
origen.getSalvoes().forEach(cadaSalvo->{
    long salvoID=cadaSalvo.getSalvoID();
    cadaSalvo.getSalvoLoc().forEach(cadaTiro->{
if (barcosRivales.contains(cadaTiro)){
            Map<String,Object> map = new HashMap<>();
            map.put("acertado",cadaTiro);
            map.put("salvo",salvoID);
            lePegue.add(map);

        }
    });
});
            if(origen.getGame().getGamePlayers().size()==2)
            {
           gPRival.getShips().forEach(cadaBarco->{
               String queBarco=cadaBarco.getShipType();
               List  hitsEnEste=new ArrayList();
               cadaBarco.getShipLoc().forEach(cadaLoc->{
                   Map <String,Object> hundidoCheck=new HashMap<>();
                   for(int i=0; i< lePegue.size() ; i++){
                   if(lePegue.get(i).containsValue(cadaLoc)){
                       hitsEnEste.add("Ay");
                   }}
               });
               if(hitsEnEste.size()>=cadaBarco.getShipLoc().size()){
                   rivalesHundidos.put(queBarco, cadaBarco.getShipLoc());
               }
               miGameView.put("rivalesHundidos",rivalesHundidos);
           });}
      //////////para mis barcos
            origen.getShips().forEach(cadaBarco->barcosMios.addAll(cadaBarco.getShipLoc()));
            //System.out.println(origen.getSalvoes().size());

            if(origen.getGame().getGamePlayers().size()==2) {

                gPRival.getSalvoes().forEach(cadaSalvo -> {
                    long salvoID = cadaSalvo.getSalvoID();
                    if (cadaSalvo.getGamePlayer().getId() == gPRival.getId()) {
                        //System.out.println("me dispara " + cadaSalvo.getGamePlayer());
                        cadaSalvo.getSalvoLoc().forEach(cadaTiro -> {
                            if (barcosMios.contains(cadaTiro)) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("acertado", cadaTiro);
                                map.put("salvo", salvoID);
                                mePego.add(map);

                            }
                        });
                    }
                });

            }
            origen.getShips().forEach(cadaBarco->{
                String queBarco=cadaBarco.getShipType();
                List  hitsEnEste=new ArrayList();
                cadaBarco.getShipLoc().forEach(cadaLoc->{
                    Map <String,Object> hundidoCheck=new HashMap<>();
                    for(int i=0; i< mePego.size() ; i++){
                        if(mePego.get(i).containsValue(cadaLoc)){
                            hitsEnEste.add("Ay");
                            //System.out.println(cadaBarco.getShipType()+" tamagno "+cadaBarco.getShipLoc().size() +" aya");
                        }}
                });
                if(hitsEnEste.size()==cadaBarco.getShipLoc().size()){
                    misHundidos.put(queBarco, cadaBarco.getShipLoc());
                }
                miGameView.put("misHundidos",misHundidos);
            });



      //////////
        miGameView.put("Salvoes",origen.getGame().getGamePlayers().stream().flatMap(cada->cada.getSalvoes().stream().map(cadasalvo->cadasalvo.salvoDTO())));
        miGameView.put("misAciertos",lePegue);
        String estado = null;
            System.out.println("a ver");
////controles estado. primero controlo que haya 2 gp

            if(origen.getGame().getGamePlayers().size()<2)
            {miGameView.put("Estado", "sinrival");
            }
            else    //hay 2 gp, sigo con los controles
            {
                System.out.println(origen.getShips().size() +" <-mis barc, mis hun->"+ misHundidos.size()
                );
                System.out.println(gPRival.getSalvoes().size() +" <-sus disp, mis disp->"+ origen.getSalvoes().size());
                System.out.println(rivalesHundidos.size() +" <-sus hun, sus bar->"+ gPRival.getShips().size());
                ////perdi?

                if (5 == misHundidos.size()
                        && gPRival.getSalvoes().size() == origen.getSalvoes().size()
                        && rivalesHundidos.size() < 5
                ) {
                    estado = "perdi";
                    System.out.println("perdi");
                    miGameView.put("Estado", estado);
                    if(origen.getScore()==null) {
                        System.out.println("escribo resultado");
                        scoreRepo.save(new Score(origen.getGame(), origen.getPlayer(), (float) 0.0, LocalDateTime.now()));
                        scoreRepo.save(new Score(gPRival.getGame(), gPRival.getPlayer(), (float) 1.0, LocalDateTime.now()));
                    }
                }


                ////gane?

                if (5 > misHundidos.size()
                        && gPRival.getSalvoes().size() == origen.getSalvoes().size()
                        && rivalesHundidos.size() == 5
                ) {
                    estado = "gane";
                    System.out.println("gane");
                    miGameView.put("Estado", estado);
                    if(origen.getScore()==null) {
                        System.out.println("escribo resultado");
                        scoreRepo.save(new Score(origen.getGame(), origen.getPlayer(), (float) 1.0, LocalDateTime.now()));
                        scoreRepo.save(new Score(gPRival.getGame(), gPRival.getPlayer(), (float) 0.0, LocalDateTime.now()));
                    }
                }

                ////empate?

                if (5 == misHundidos.size()
                        && gPRival.getSalvoes().size() == origen.getSalvoes().size()
                        && rivalesHundidos.size() == 5
                ) {
                    estado = "empate";
                    System.out.println("empate");
                    miGameView.put("Estado", estado);
                 if(origen.getScore()==null){
                     System.out.println("escribo resultado");
                    scoreRepo.save(new Score(origen.getGame(),origen.getPlayer(), (float) 0.5, LocalDateTime.now()));
                    scoreRepo.save(new Score(gPRival.getGame(),gPRival.getPlayer(), (float) 0.5, LocalDateTime.now()));}
                }

/// quien dispara?
                if (gPRival.getSalvoes().size() < origen.getSalvoes().size() 
                        && estado !="perdi"
                        &&estado!="gane"&&estado!="empate") {
                    miGameView.put("Estado", "dispararival");
                } else if (gPRival.getSalvoes().size() >= origen.getSalvoes().size()
                        && estado !="perdi"
                        &&estado!="gane"&&estado!="empate") {
                    miGameView.put("Estado", "disparoyo");
                }

            }

/// estan  barcos rivales

            if (gPRival!=null &&gPRival.getShips().size()==0){
                miGameView.put("Estado", "nohaybarcorival");
            }

            ////
        return miGameView;}
        else{
            return new ResponseEntity<>("A dónde te creés que vas?", HttpStatus.UNAUTHORIZED);
                    //"a donde vas?";
            }

    };


    @RequestMapping("/leaderboard")
    public Object getLeaders() {
Map<String,Object> miLeaderboard= new LinkedHashMap<>();
        miLeaderboard.put("leader",gamePlayerRepo.findAll().stream().map(cada->cada.leaderboardDTO()).collect(Collectors.toList()) );
return miLeaderboard;
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }


}
