package com.FSCom2.Salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

	@Bean
	public CommandLineRunner initData(ScoreRepository scoreRepo, SalvoRepository salvoRepo, ShipRepository shipRepo, PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepo) {
		return (args) -> {

			Player jack = playerRepository.save(new Player("j.bauer@ctu.gov", "Jack", "Bauer"));
			Player chloe = playerRepository.save(new Player("c.obrian@ctu.gov", "Chloe", "O'Brian"));
			Player kim = playerRepository.save(new Player("kim_bauer@gmail.com", "Kim", "Bauer"));
			Player tony = playerRepository.save(new Player("t.almeida@ctu.gov", "Tony", "Almeida"));
			Player pablo = playerRepository.save(new Player("pablo@ctu.gov", "Pablo", "Ducho"));

			jack.setPass(passwordEncoder().encode("24"));
			chloe.setPass(passwordEncoder().encode( "42"));
			kim.setPass(passwordEncoder().encode( "kb"));
			tony.setPass(passwordEncoder().encode( "mole"));
			pablo.setPass(passwordEncoder().encode( "123"));
			playerRepository.save(jack);
			playerRepository.save(chloe);
			playerRepository.save(kim);
			playerRepository.save(tony);
			playerRepository.save(pablo);

			Game primero=gameRepository.save(new Game(LocalDateTime.now()));
			Game seg=gameRepository.save(new Game(LocalDateTime.now().plusHours(1)));
			Game terc=gameRepository.save(new Game(LocalDateTime.now().plusHours(2)));
			Game cua=gameRepository.save(new Game(LocalDateTime.now().plusHours(3)));
			Game quin=gameRepository.save(new Game(LocalDateTime.now().plusHours(4)));
			Game sex=gameRepository.save(new Game(LocalDateTime.now().plusHours(5)));
			Game sept=gameRepository.save(new Game(LocalDateTime.now().plusHours(6)));
			Game oct=gameRepository.save(new Game(LocalDateTime.now().plusHours(7)));
			System.out.println(jack.saludar());

			GamePlayer gp1= gamePlayerRepo.save(new GamePlayer(LocalDateTime.now(),primero,jack));
			GamePlayer gp2= gamePlayerRepo.save(new GamePlayer(LocalDateTime.now().plusHours(1),primero,chloe));
			GamePlayer gp3= gamePlayerRepo.save(new GamePlayer(LocalDateTime.now().plusMinutes(23),seg,jack));
			GamePlayer gp4= gamePlayerRepo.save(new GamePlayer(LocalDateTime.now().plusMinutes(25), seg,chloe));
			GamePlayer gp5= gamePlayerRepo.save(new GamePlayer(LocalDateTime.now().plusMinutes(35), terc,chloe));
			GamePlayer gp6= gamePlayerRepo.save(new GamePlayer(LocalDateTime.now().plusMinutes(45), terc,tony));
			GamePlayer gp7= gamePlayerRepo.save(new GamePlayer(LocalDateTime.now(),cua,chloe));
			GamePlayer gp8= gamePlayerRepo.save(new GamePlayer(LocalDateTime.now().plusHours(1),cua,jack));
			GamePlayer gp9= gamePlayerRepo.save(new GamePlayer(LocalDateTime.now().plusMinutes(23),quin,tony));
			GamePlayer gp10= gamePlayerRepo.save(new GamePlayer(LocalDateTime.now().plusMinutes(25), quin,jack));
			GamePlayer gp11= gamePlayerRepo.save(new GamePlayer(LocalDateTime.now().plusMinutes(35), sex,kim));
			GamePlayer gp13= gamePlayerRepo.save(new GamePlayer(LocalDateTime.now(),sept,jack));
			GamePlayer gp15= gamePlayerRepo.save(new GamePlayer(LocalDateTime.now().plusMinutes(23),oct,kim));
			GamePlayer gp16= gamePlayerRepo.save(new GamePlayer(LocalDateTime.now().plusMinutes(25), oct,tony));
/*

			Salvo salvo1=salvoRepo.save(new Salvo(1,Arrays.asList("B5", "C5", "F1")));
			Salvo salvo2=salvoRepo.save(new Salvo(1,Arrays.asList("B4", "B5", "B6")));
			Salvo salvo3=salvoRepo.save(new Salvo(2,Arrays.asList("F2", "D5")));

*/

			Ship ship1= new Ship("destroyer", 3, Arrays.asList("H2","H3","H4"));
			Ship ship2= shipRepo.save(new Ship("submarine", 3, Arrays.asList("E1", "F1", "G1")));
			Ship ship3= shipRepo.save(new Ship("patrol_boat", 2, Arrays.asList("B4", "B5")));
			Ship ship4= shipRepo.save(new Ship("destroyer", 3, Arrays.asList("B5", "C5", "D5")));
			Ship ship5= shipRepo.save(new Ship("patrol_boat", 2, Arrays.asList("F1", "F2")));

			gp1.addShips(ship5);
			gp1.addShips(ship3);
			gp1.addShips(ship1);
			gp2.addShips(ship2);
			gp2.addShips(ship4);

			Ship ship6= shipRepo.save(new Ship("destroyer", 3, Arrays.asList("B5", "C5", "D5")));
			Ship ship7= shipRepo.save(new Ship("patrol_boat", 2, Arrays.asList("C6", "C7")));
			Ship ship8= shipRepo.save(new Ship("submarine", 3, Arrays.asList("A2", "A3", "A4")));
			Ship ship9= shipRepo.save(new Ship("patrol_boat", 2, Arrays.asList("G6", "H6")));

			gp3.addShips(ship6);
			gp3.addShips(ship7);
			gp4.addShips(ship8);
			gp4.addShips(ship9);

			Ship ship10= shipRepo.save(new Ship("destroyer", 3, Arrays.asList("B5", "C5", "D5")));
			Ship ship11= shipRepo.save(new Ship("patrol_boat", 2, Arrays.asList("C6", "C7")));
			Ship ship12= shipRepo.save(new Ship("submarine", 3, Arrays.asList("A2", "A3", "A4")));
			Ship ship13= shipRepo.save(new Ship("patrol_boat", 2, Arrays.asList("G6", "H6")));

			gp5.addShips(ship10);
			gp5.addShips(ship11);
			gp6.addShips(ship12);
			gp6.addShips(ship13);


			Ship ship14= shipRepo.save(new Ship("destroyer", 3, Arrays.asList("B5", "C5", "D5")));
			Ship ship15= shipRepo.save(new Ship("patrol_boat", 2, Arrays.asList("C6", "C7")));
			Ship ship16= shipRepo.save(new Ship("submarine", 3, Arrays.asList("A2", "A3", "A4")));
			Ship ship17= shipRepo.save(new Ship("patrol_boat", 2, Arrays.asList("G6", "H6")));
			gp7.addShips(ship14);
			gp7.addShips(ship15);
			gp8.addShips(ship16);
			gp8.addShips(ship17);
			Ship ship18= shipRepo.save(new Ship("destroyer", 3, Arrays.asList("B5", "C5", "D5")));
			Ship ship19= shipRepo.save(new Ship("patrol_boat", 2, Arrays.asList("C6", "C7")));
			Ship ship20= shipRepo.save(new Ship("submarine", 3, Arrays.asList("A2", "A3", "A4")));
			Ship ship21= shipRepo.save(new Ship("patrol_boat", 2, Arrays.asList("G6", "H6")));
			gp9.addShips(ship18);
			gp9.addShips(ship19);
			gp10.addShips(ship20);
			gp10.addShips(ship21);
			Ship ship22= shipRepo.save(new Ship("destroyer", 3, Arrays.asList("B5", "C5", "D5")));
			Ship ship23= shipRepo.save(new Ship("patrol_boat", 2, Arrays.asList("C6", "C7")));
			gp11.addShips(ship22);
			gp11.addShips(ship23);

			Ship ship24= shipRepo.save(new Ship("destroyer", 3, Arrays.asList("B5", "C5", "D5")));
			Ship ship25= shipRepo.save(new Ship("patrol_boat", 2, Arrays.asList("C6", "C7")));
			Ship ship26= shipRepo.save(new Ship("submarine", 3, Arrays.asList("A2", "A3", "A4")));
			Ship ship27= shipRepo.save(new Ship("patrol_boat", 2, Arrays.asList("G6", "H6")));
			gp15.addShips(ship24);
			gp15.addShips(ship25);
			gp16.addShips(ship26);
			gp16.addShips(ship27);



			Salvo salvo1=salvoRepo.save(new Salvo(1,Arrays.asList("B5", "C5", "F1")));
			Salvo salvo2=salvoRepo.save(new Salvo(1,Arrays.asList("B4", "B5", "B6")));
			Salvo salvo3=salvoRepo.save(new Salvo(2,Arrays.asList("F2", "D5")));
			Salvo salvo4=salvoRepo.save(new Salvo(2,Arrays.asList("E1", "H3", "A2")));

			Salvo salvo5=salvoRepo.save(new Salvo(1,Arrays.asList("A2", "A4", "G6")));
			Salvo salvo6=salvoRepo.save(new Salvo(1,Arrays.asList("B5", "D5", "C7")));
			Salvo salvo7=salvoRepo.save(new Salvo(2,Arrays.asList("A3", "H6")));
			Salvo salvo8=salvoRepo.save(new Salvo(2,Arrays.asList("C5", "C6")));

			Salvo salvo9=salvoRepo.save(new Salvo(1,Arrays.asList("B5", "C5", "F1")));
			Salvo salvo10=salvoRepo.save(new Salvo(1,Arrays.asList("B4", "B5", "B6")));
			Salvo salvo11=salvoRepo.save(new Salvo(2,Arrays.asList("F2", "D5")));
			Salvo salvo12=salvoRepo.save(new Salvo(2,Arrays.asList("E1", "H3", "A2")));

			Salvo salvo13=salvoRepo.save(new Salvo(1,Arrays.asList("A2", "A4", "G6")));
			Salvo salvo14=salvoRepo.save(new Salvo(1,Arrays.asList("B5", "D5", "C7")));
			Salvo salvo15=salvoRepo.save(new Salvo(2,Arrays.asList("A3", "H6")));
			Salvo salvo16=salvoRepo.save(new Salvo(2,Arrays.asList("C5", "C6")));

			Salvo salvo17=salvoRepo.save(new Salvo(1,Arrays.asList("A2", "A4", "G6")));
			Salvo salvo18=salvoRepo.save(new Salvo(1,Arrays.asList("B5", "D5", "C7")));
			Salvo salvo19=salvoRepo.save(new Salvo(2,Arrays.asList("A3", "H6")));
			Salvo salvo20=salvoRepo.save(new Salvo(2,Arrays.asList("C5", "C6")));
			Salvo salvo21=salvoRepo.save(new Salvo(3,Arrays.asList("F2", "D5")));

			gp1.addSalvoes(salvo1);
			gp1.addSalvoes(salvo3);
			gp2.addSalvoes(salvo2);
			gp2.addSalvoes(salvo4);
			gp3.addSalvoes(salvo5);
			gp4.addSalvoes(salvo6);
			gp3.addSalvoes(salvo7);
			gp4.addSalvoes(salvo8);

			gp5.addSalvoes(salvo9);
			gp6.addSalvoes(salvo10);
			gp5.addSalvoes(salvo11);
			gp6.addSalvoes(salvo12);

			gp7.addSalvoes(salvo13);
			gp8.addSalvoes(salvo14);
			gp7.addSalvoes(salvo15);
			gp8.addSalvoes(salvo16);

			gp9.addSalvoes(salvo17);
			gp10.addSalvoes(salvo18);
			gp9.addSalvoes(salvo19);
			gp10.addSalvoes(salvo20);
			gp10.addSalvoes(salvo21);

			gamePlayerRepo.save(gp4);
			gamePlayerRepo.save(gp3);
			gamePlayerRepo.save(gp2);
			gamePlayerRepo.save(gp1);
			gamePlayerRepo.save(gp5);
			gamePlayerRepo.save(gp6);
			gamePlayerRepo.save(gp7);
			gamePlayerRepo.save(gp8);
			gamePlayerRepo.save(gp9);
			gamePlayerRepo.save(gp10);
			gamePlayerRepo.save(gp11);
			gamePlayerRepo.save(gp13);
			gamePlayerRepo.save(gp15);
			gamePlayerRepo.save(gp16);

			Score sc01= scoreRepo.save(new Score(primero,jack, (float) 1.0, LocalDateTime.now()));
			Score sc02= scoreRepo.save(new Score(primero,chloe, (float) 0.0, LocalDateTime.now()));

			Score sc03= scoreRepo.save(new Score(seg,jack, (float) 0.5, LocalDateTime.now()));
			Score sc04= scoreRepo.save(new Score(seg,chloe, (float) 0.5, LocalDateTime.now()));

			Score sc05= scoreRepo.save(new Score(terc,chloe, (float) 1.0, LocalDateTime.now()));
			Score sc06= scoreRepo.save(new Score(terc,tony, (float) 0.0, LocalDateTime.now()));

			Score sc07= scoreRepo.save(new Score(cua,chloe, (float) 0.5, LocalDateTime.now()));
			Score sc08= scoreRepo.save(new Score(cua,jack, (float) 0.5, LocalDateTime.now()));

			Score sc09= scoreRepo.save(new Score(quin,tony,  LocalDateTime.now()));
			Score sc10= scoreRepo.save(new Score(quin,jack,  LocalDateTime.now()));

			Score sc11= scoreRepo.save(new Score(sex,chloe,  LocalDateTime.now()));

			Score sc13= scoreRepo.save(new Score(sex,tony,  LocalDateTime.now()));

			Score sc15= scoreRepo.save(new Score(sept,kim,  LocalDateTime.now()));
			Score sc16= scoreRepo.save(new Score(sept,tony,  LocalDateTime.now()));



			Player[] players = {jack,chloe,kim,tony};

			for(int i = 0; i < players.length; i++){
				System.out.println(players[i].saludar());
			}


			for(Player player : players){
				System.out.println(player.nombreCompl());
			}


			List<Player> playersList = new ArrayList<>();

			playersList.add(jack);
			playersList.add(chloe);
			playersList.add(kim);
			playersList.add(tony);
			playersList.add(jack);

			//List<Player> playersList = Arrays.asList(jack, chloe, kim, tony);

			List<Player> bauers = playersList.stream()
					.filter(player -> player.getLastName() == "Bauer")
					.collect(Collectors.toList());

			bauers.forEach(player -> System.out.println(player.saludar()));


			Set<Player> playersSet = new HashSet<>();

			playersSet.add(jack);
			playersSet.add(chloe);
			playersSet.add(kim);
			playersSet.add(tony);
			playersSet.add(jack);

			//Set<Player> playersSet = new HashSet<>(playersList);

			playersSet.forEach(player -> System.out.println(player.getEmail()));


			Player myPlayer = playerRepository.findByEmail("j.bauer@ctu.gov");

			if(myPlayer != null){
				System.out.println(myPlayer.saludar());
			}

			playerRepository.findByLastName("Bauer").forEach(player -> System.out.println(player.nombreCompl()));

			System.out.println(gp1.getPlayer().nombreCompl()+" "+ gp1.getGame().getGameDate());

		};
	}

}

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
	@Autowired
	PlayerRepository playerRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inputName-> {
            Player player = playerRepo.findByEmail(inputName);
            if (player != null) {
                return new User(player.getEmail(), player.getPass(),
                        AuthorityUtils.createAuthorityList("USER"));
            } else {
                throw new UsernameNotFoundException("Unknown user: " + inputName);
            }
        }).passwordEncoder(passwordEncoder);


    }

}


@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/rest/**").hasAuthority("ADMIN")
                .antMatchers("/api/game_view").hasAuthority("USER")
                .antMatchers("/api/games").permitAll()
                .antMatchers("/api/players").permitAll()
        .antMatchers("/api/game_view/**").hasAuthority("USER");
        http.formLogin()
                .usernameParameter("name")
                .passwordParameter("pwd")
                .loginPage("/api/login");

        http.logout().logoutUrl("/api/logout");

        ////////////////////////////////

        // turn off checking for CSRF tokens
        http.csrf().disable();

        // if user is not authenticated, just send an authentication failure response
        http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if login is successful, just clear the flags asking for authentication
        http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

        // if login fails, just send an authentication failure response
        http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

        // if logout is successful, just send a success response
        http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());
    }

    private void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }


    }
}