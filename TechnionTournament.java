import java.util.ArrayList;

public class TechnionTournament implements Tournament{
    private PlayersTreeByGoals goalsTree;
    private TeamsTreeByPoints pointsTree;
    private TeamsTreeByFacultyId facultyIdTree;


    TechnionTournament(){};

    @Override
    public void init() { //O(1)
        this.goalsTree = new PlayersTreeByGoals();
        this.pointsTree = new TeamsTreeByPoints();
        this.facultyIdTree= new TeamsTreeByFacultyId();
    }

    @Override
    public void addFacultyToTournament(Faculty faculty) { //O(lgn)
        Faculty faculty1 = new Faculty(faculty.getId(), faculty.getName());
        facultyTeamNode teamNode = new facultyTeamNode(faculty1, null);
        teamPointsNode pointsNode= new teamPointsNode(0, faculty1, null);
        this.facultyIdTree.Insert(teamNode);
        this.pointsTree.Insert(pointsNode);

    }

    @Override
    public void removeFacultyFromTournament(int faculty_id){
        facultyTeamNode team = this.facultyIdTree.search_2_3(this.facultyIdTree.getRoot(), faculty_id); //O(lgn)
        teamPointsNode team2=this.pointsTree.Search_Node(this.pointsTree.getRoot(), faculty_id, team.getPoints());
        playerGoalNode[] players = team.getPlayers();
        //need to remove all players from the faculty
        for (int i=0; i<11; i++)
        {
            players[i] = null;
        }
        //remove faculty
        this.facultyIdTree.Delete_2_3(team);
        this.pointsTree.Delete_2_3(team2);
    }

    @Override
    public void addPlayerToFaculty(int faculty_id,Player player) {
        Player playerToAdd = new Player(player.getId(), player.getName());
        playerGoalNode playerNode = new playerGoalNode(0, playerToAdd, null);
        facultyTeamNode facultyToAdd = this.facultyIdTree.search_2_3(this.facultyIdTree.getRoot(), faculty_id);
        facultyToAdd.addPlayerToTeam(playerToAdd);
        this.goalsTree.Insert(playerNode);
    }

    @Override
    public void removePlayerFromFaculty(int faculty_id, int player_id) {
        facultyTeamNode playerTeam = this.facultyIdTree.search_2_3(this.facultyIdTree.getRoot(), faculty_id);
        playerTeam.removePlayerFromTeam(player_id);

    }

    @Override
    public void playGame(int faculty_id1, int faculty_id2, int winner,
                         ArrayList<Integer> faculty1_goals, ArrayList<Integer> faculty2_goals) {

        facultyTeamNode f1= this.facultyIdTree.search_2_3(this.facultyIdTree.getRoot(), faculty_id1);
        facultyTeamNode f2= this.facultyIdTree.search_2_3(this.facultyIdTree.getRoot(), faculty_id2);
        int f1Points= f1.getPoints();
        int f2Points = f2.getPoints();
        teamPointsNode team1 = this.pointsTree.Search_Node(this.pointsTree.getRoot(), faculty_id1, f1Points);
        teamPointsNode team2 = this.pointsTree.Search_Node(this.pointsTree.getRoot(), faculty_id2, f2Points);
        int team1Points = team1.getPoints();
        int team2Points = team2.getPoints();
        //update teams points

        if (winner == 1) //faculty 1 won
        {
            this.pointsTree.Delete_2_3(team1);
            team1.setPoints(team1Points+3);
            f1.setPoints(f1Points+3);
            this.pointsTree.Insert(team1);
        }
        else{
            if (winner == 2) //faculty 2 won
            {
                this.pointsTree.Delete_2_3(team2);
                team2.setPoints(team2Points+3);
                f2.setPoints(f2Points+3);
                this.pointsTree.Insert(team2);
            }
            else //draw
            {
                //faculty 1 update
                this.pointsTree.Delete_2_3(team1);
                team1.setPoints(team1Points+1);
                f1.setPoints(f1Points+1);
                this.pointsTree.Insert(team1);

                //faculty 2 update
                this.pointsTree.Delete_2_3(team2);
                team2.setPoints(team2Points+1);
                f2.setPoints(f2Points+1);
                this.pointsTree.Insert(team2);
            }
        }

        //goals update
        playerGoalNode playerInTeam;
        playerGoalNode playerInTree;

        for (int pID: faculty1_goals) {
            playerInTeam=f1.getPlayer(pID);
            playerInTree=this.goalsTree.Search_Node(this.goalsTree.getRoot(), playerInTeam.getPlayer().getId(), playerInTeam.getGoals());
            this.goalsTree.Delete_2_3(playerInTree);
            playerInTree.setGoals(playerInTree.getGoals()+1);
            playerInTeam.setGoals(playerInTeam.getGoals()+1);
            this.goalsTree.Insert(playerInTree);
        }

        for (int pID: faculty2_goals) {
            playerInTeam=f2.getPlayer(pID);
            playerInTree=this.goalsTree.Search_Node(this.goalsTree.getRoot(), playerInTeam.getPlayer().getId(), playerInTeam.getGoals());
            this.goalsTree.Delete_2_3(playerInTree);
            playerInTree.setGoals(playerInTree.getGoals()+1);
            playerInTeam.setGoals(playerInTeam.getGoals()+1);
            this.goalsTree.Insert(playerInTree);
        }





    }

    @Override
    public void getTopScorer(Player player) {
        playerGoalNode topScorer = this.goalsTree.getRightBoundNode().getPredecessor();
        player.setId(topScorer.getPlayer().getId());
        player.setName(topScorer.getPlayer().getName());
    }

    @Override
    public void getTopScorerInFaculty(int faculty_id, Player player) { //O(lgn)
        facultyTeamNode faculty = this.facultyIdTree.search_2_3(this.facultyIdTree.getRoot(), faculty_id);
        faculty.getTopScorerInTeam(player);
    }

    @Override
    public void getTopKFaculties(ArrayList<Faculty> faculties, int k, boolean ascending) {
        teamPointsNode idx = this.pointsTree.getRightBoundNode().getPredecessor();
        Faculty[] topFaculties = new Faculty[k];
        for(int i=0; i<k; i++)
        {
            topFaculties[i] = idx.getFaculty();
            idx=idx.getPredecessor();
        }
        if (ascending == false){
            for(int i=0; i<k; i++)
            {
                faculties.add(topFaculties[i]);
            }
        }
        else{
            for(int i=0; i<k; i++)
            {
                faculties.add(topFaculties[k-1-i]);
            }
        }
    }

    @Override
    public void getTopKScorers(ArrayList<Player> players, int k, boolean ascending) {
        playerGoalNode idx = this.goalsTree.getRightBoundNode().getPredecessor();
        Player[] scorers = new Player[k];
        for(int i=0; i<k; i++)
        {
            scorers[i] = idx.getPlayer();
            idx=idx.getPredecessor();
        }
        if (ascending == false){
            for(int i=0; i<k; i++)
            {
                players.add(scorers[i]);
            }
        }
        else{
            for(int i=k; i>0; i--)
            {
                players.add(scorers[i-1]);
            }
        }

    }

    @Override
    public void getTheWinner(Faculty faculty) {
        Faculty winner = this.pointsTree.rightBoundNode.getPredecessor().getFaculty();
        faculty.setId(winner.getId());
        faculty.setName(winner.getName());
    }

    ///TODO - add below your own variables and methods



}
