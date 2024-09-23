public class playerGoalNode {
    private int goals;
    private Player player;
    private playerGoalNode leftChild;
    private playerGoalNode middleChild;
    private playerGoalNode RightChild;
    private playerGoalNode Parent;
    private playerGoalNode MaxNode;
    private playerGoalNode Successor;
    public playerGoalNode Predecessor;
    boolean isLeaf=isLeaf();

    public playerGoalNode() {
    }

    public playerGoalNode(int goals, Player player, playerGoalNode parent) {
        this.goals = goals;
        this.player=player;
        Parent = parent;
    }

    public int getGoals() {
        return goals;
    }

    public void setGoals(int goals) {
        this.goals = goals;
    }

    public int getKey() {
        return this.player.getId();
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public playerGoalNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(playerGoalNode leftChild) {
        this.leftChild = leftChild;
    }

    public playerGoalNode getMiddleChild() {
        return middleChild;
    }

    public void setMiddleChild(playerGoalNode middleChild) {
        this.middleChild = middleChild;
    }

    public playerGoalNode getRightChild() {
        return RightChild;
    }

    public void setRightChild(playerGoalNode rightChild) {
        RightChild = rightChild;
    }

    public playerGoalNode getParent() {
        return Parent;
    }

    public void setParent(playerGoalNode parent) {
        Parent = parent;
    }

    public playerGoalNode getMaxNode() {
        return this.MaxNode;
    }

    public void setMaxNode(playerGoalNode maxNode) {
        this.MaxNode= maxNode;
    }

    public playerGoalNode getSuccessor() {
        return Successor;
    }

    public void setSuccessor(playerGoalNode successor) {
        Successor = successor;
    }

    public playerGoalNode getPredecessor() {
        return Predecessor;
    }

    public void setPredecessor(playerGoalNode predecessor) {
        Predecessor = predecessor;
    }

    public void setKey(int playerId, int goals) {
        if (this.player==null){
            this.player=new Player(playerId, null);
            this.goals=goals;
        }
        else {
            this.player.setId(playerId);
            this.goals=goals;
        }
    }

    public boolean isLeaf() {
        if(this.getLeftChild()==null){
            this.isLeaf = true;
            return true;
        }
        this.isLeaf = false;
        return false;
    }
}
