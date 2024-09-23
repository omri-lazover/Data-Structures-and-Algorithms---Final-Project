public class playerInTeam {
    private Player player;
    private int goals;

    public playerInTeam(Player player) {
        this.player=player;
        this.goals=0;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }
}
