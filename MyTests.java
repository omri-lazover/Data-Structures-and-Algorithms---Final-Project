import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyTests {
    public static void main(String[] args) {

        System.out.println("\nInitialization starts");
        TechnionTournament tournament = new TechnionTournament();
        tournament.init();
        System.out.println("Initialization is over\n");

        Faculty faculty = new Faculty(0,"");
        int NUMBERofTEAMS = 300;
        int Number_OF_Players_in_Team=10;

        for (int i =0; i<NUMBERofTEAMS ; i++){
            faculty.setName(""+i);
            faculty.setId(i);
            tournament.addFacultyToTournament(faculty);
        }

        int playerIDCount = 0;

        Player player1 = new Player(0,"");
        for (int j = 0; j<NUMBERofTEAMS;j++){
            for (int i =0; i<10 ; i++){
                Player player = new Player(0,"");
                player.setId(playerIDCount);
                player.setName(playerIDCount +"-Faculty-"+j);
                tournament.addPlayerToFaculty(j,player);
                playerIDCount +=1;
            }
        }



        ArrayList<Integer> home_faculty_goals = new ArrayList<>();
        ArrayList<Integer> away_faculty_goals = new ArrayList<>();

        int NUMBER_of_games = 200; //pick number

        int[] teams = new int[NUMBERofTEAMS];
        int[] players = new int[NUMBERofTEAMS*NUMBERofTEAMS];

        for (int j = 0; j<NUMBER_of_games;j++) {
            System.out.println("GAME NO" + j);
            int HomeTeam = 30+j;
            int AwayTeam = 40 + j;

            int HomeGoals = 3;
            int AwayGoals = 2;

            int HomeTid = HomeTeam * 10;
            int AwayTid = AwayTeam * 10;

            home_faculty_goals.add(HomeTid);
            home_faculty_goals.add(HomeTid+1);
            home_faculty_goals.add(HomeTid);

            away_faculty_goals.add(AwayTid);
            away_faculty_goals.add(AwayTid+4);

            tournament.playGame(HomeTeam,AwayTeam,1, home_faculty_goals, away_faculty_goals);

            home_faculty_goals.clear();
            away_faculty_goals.clear();



        }
        Faculty winner = new Faculty(0, "");
        tournament.getTheWinner(winner);
        System.out.println(winner.getId());
        Player player= new Player(0, "");
        tournament.getTopScorer(player);
        System.out.println(player.getId());

    }
    public static void Assert(boolean expression){
        if (!expression){
            throw new AssertionError();
        }
    }
}
