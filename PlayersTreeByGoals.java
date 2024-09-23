public class PlayersTreeByGoals {
    private playerGoalNode root;
    playerGoalNode rightBoundNode;

    public PlayersTreeByGoals(){
        this.root = new playerGoalNode();
        playerGoalNode leftSentinel = new playerGoalNode(Integer.MIN_VALUE,new Player(Integer.MAX_VALUE, null), this.root);
        playerGoalNode rightSentinel = new playerGoalNode(Integer.MAX_VALUE,new Player(Integer.MIN_VALUE, null), this.root);
        this.root.setKey(Integer.MIN_VALUE,Integer.MAX_VALUE);
        this.root.setMaxNode(rightSentinel);
        this.root.setLeftChild(leftSentinel);
        this.root.setMiddleChild(rightSentinel);
        leftSentinel.setSuccessor(rightSentinel);
        rightSentinel.setPredecessor(leftSentinel);
        this.rightBoundNode=rightSentinel;
    }

    public playerGoalNode getRoot() {
        return this.root;
    }

    public playerGoalNode getRightBoundNode() {
        return rightBoundNode;
    }

    public void setRoot(playerGoalNode root) {
        this.root = root;
    }

    public playerGoalNode Search_Node(playerGoalNode Root, int id, int goals){
        if (Root.isLeaf()){
            if (Root.getGoals()==goals &&  Root.getKey()== id){
                return Root;
            }else{
                return null;
            }
        }

        if (goals<(Root).getLeftChild().getGoals() ||goals==(Root).getLeftChild().getGoals()&& id >= (Root).getLeftChild().getKey()){
            return Search_Node((Root).getLeftChild(),id,goals);
        } else if (goals<(Root).getMiddleChild().getGoals()||goals==(Root).getMiddleChild().getGoals()&& id >= (Root).getMiddleChild().getKey()){
            return Search_Node((Root).getMiddleChild(),id,goals);
        }else {
            return Search_Node((Root).getRightChild(),id,goals);
        }
    }

    public void UpdateKey(playerGoalNode X){
        X.setKey(X.getLeftChild().getKey(),X.getLeftChild().getGoals());
        X.setMaxNode(X.getLeftChild().getMaxNode());
        if (X.getMiddleChild()!=null){
            X.setKey(X.getMiddleChild().getKey(),X.getMiddleChild().getGoals());
            X.setMaxNode(X.getMiddleChild().getMaxNode());
        }
        if (X.getRightChild()!=null){
            X.setKey(X.getRightChild().getKey(),X.getRightChild().getGoals());
            X.setMaxNode(X.getRightChild().getMaxNode());
        }
    }
    public void SetChildren(playerGoalNode X, playerGoalNode L, playerGoalNode M, playerGoalNode R){
        X.setLeftChild(L);
        X.setMiddleChild(M);
        X.setRightChild(R);
        L.setParent(X);
        if (M!=null){
            M.setParent(X);
        }
        if(R!=null){
            R.setParent(X);
        }
        UpdateKey(X);
    }
    public playerGoalNode InsertAndSplit(playerGoalNode X, playerGoalNode Z){
        playerGoalNode L = X.getLeftChild();
        playerGoalNode M = X.getMiddleChild();
        playerGoalNode R = X.getRightChild();
        int goals = Z.getGoals();
        int id = Z.getKey();
        if (R==null) {
            if (goals<X.getLeftChild().getGoals()||goals==X.getLeftChild().getGoals()&& id > X.getLeftChild().getKey()) {
                SetChildren(X, Z, L, M);
            }
            else if (goals<X.getMiddleChild().getGoals()||goals==X.getMiddleChild().getGoals()&& id > X.getMiddleChild().getKey()){
                SetChildren(X, L, Z, M);
            }
            else {
                SetChildren(X, L, M, Z);
            }
            return null;
        }
        playerGoalNode Y = new playerGoalNode();
        if (goals<X.getLeftChild().getGoals()||goals==X.getLeftChild().getGoals()&& id > X.getLeftChild().getKey()){
            SetChildren(X, Z, L, null);
            SetChildren(Y, M, R, null);
        } else if (goals<X.getMiddleChild().getGoals()||goals==X.getMiddleChild().getGoals()&& id > X.getMiddleChild().getKey()) {
            SetChildren(X, L, Z, null);
            SetChildren(Y, M, R, null);
        } else if (goals<X.getRightChild().getGoals()||goals==X.getRightChild().getGoals()&& id > X.getRightChild().getKey()) {
            SetChildren(X, L, M, null);
            SetChildren(Y, Z, R, null);
        }else{
            SetChildren(X, L, M, null);
            SetChildren(Y, R, Z, null);
        }
        return Y;
    }
    public void Insert(playerGoalNode Z){
        playerGoalNode Y = this.root;
        int points = Z.getGoals();
        int id = Z.getKey();
        while (!(Y.isLeaf())){
            if (points<Y.getLeftChild().getGoals()||points==(Y).getLeftChild().getGoals()&& id > (Y).getLeftChild().getKey()){
                Y = Y.getLeftChild();
            } else if (points<(Y).getMiddleChild().getGoals()||points==(Y).getMiddleChild().getGoals()&&
                    id >Y.getMiddleChild().getKey()) {
                Y = (Y).getMiddleChild();
            }else {
                Y = (Y).getRightChild();
            }
        }
        playerGoalNode X = (Y).getParent();
        playerGoalNode newNode = InsertAndSplit(X, Z);
        while (X !=this.root){
            X= X.getParent();
            if (newNode!=null){
                newNode=InsertAndSplit(X,newNode);
            }else UpdateKey(X);
        }
        if (newNode!=null){
            playerGoalNode W = new playerGoalNode();
            SetChildren(W,X,newNode,null);
            this.root = W;
        }
        Z.setPredecessor(Predessesor(Z));
        Z.getPredecessor().setSuccessor(Z);
        Z.setSuccessor(Sucssesor(Z));
        Z.getSuccessor().setPredecessor(Z);

    }
    public playerGoalNode Borrow_or_Merge(playerGoalNode Y){
        playerGoalNode Z = Y.getParent();
        if (Y == Z.getLeftChild()){
            playerGoalNode X =Z.getMiddleChild();
            if(X.getRightChild()!=null){
                SetChildren(Y, Y.getLeftChild(), X.getLeftChild(), null);
                SetChildren(X, X.getMiddleChild(), X.getRightChild(), null);
            }else {
                SetChildren(X, Y.getLeftChild(), X.getLeftChild(), X.getMiddleChild());
                Y=null;
                SetChildren(Z, X, Z.getRightChild(), null);
            }
            return Z;
        }if (Y == Z.getMiddleChild()){
            playerGoalNode X =Z.getLeftChild();
            if (X.getRightChild() !=null){
                SetChildren(Y, X.getRightChild(), Y.getLeftChild(), null);
                SetChildren(X, X.getLeftChild(), X.getMiddleChild(), null);
            }else {
                SetChildren(X, X.getLeftChild(), X.getMiddleChild(), Y.getLeftChild());
                Y=null;
                SetChildren(Z, X, Z.getRightChild(), null);
            }
            return Z;
        }
        playerGoalNode X = Z.getMiddleChild();
        if(X.getRightChild()!=null){
            SetChildren(Y, X.getRightChild(), Y.getLeftChild(), null);
            SetChildren(X, X.getLeftChild(), X.getMiddleChild(), null);
        }else {
            SetChildren(X, X.getLeftChild(), X.getMiddleChild(), Y.getLeftChild());
            Y=null;
            SetChildren(Z,Z.getLeftChild(), X,null);
        }
        return Z;
    }

    public void Delete_2_3(playerGoalNode X){
        playerGoalNode Y = X.getParent();
        if (X== Y.getLeftChild()){
            (Y.getMiddleChild()).setPredecessor(X.getPredecessor());
            X.getPredecessor().setSuccessor(Y.getMiddleChild());
            SetChildren(Y,Y.getMiddleChild(),Y.getRightChild(),null);

        } else if (X==Y.getMiddleChild()) {
            (Y.getMiddleChild()).getSuccessor().setPredecessor(Y.getLeftChild());
            (Y.getLeftChild()).setSuccessor(Y.getMiddleChild().getSuccessor());
            SetChildren(Y,Y.getLeftChild(),Y.getRightChild(),null);

        }else{
            (Y.getMiddleChild()).setSuccessor(X.getSuccessor());
            X.getSuccessor().setPredecessor(Y.getMiddleChild());
            SetChildren(Y,Y.getLeftChild(),Y.getMiddleChild(),null);

        }
        X=null;
        while (Y!=null){
            if (Y.getMiddleChild() == null){
                if (Y != this.root){
                    Y = Borrow_or_Merge(Y);
                }else {
                    this.root = Y.getLeftChild();
                    Y.getLeftChild().setParent(null);
                    Y=null;
                    return;
                }
            }else {
                UpdateKey(Y);
                Y = Y.getParent();
            }
        }
    }


    public playerGoalNode Sucssesor(playerGoalNode leaf){

        playerGoalNode z = leaf.getParent();
        while (leaf==z.getRightChild() ||(z.getRightChild()==null&&leaf == z.getMiddleChild())){
            leaf = z;
            z = z.getParent();
        }
        playerGoalNode Y;
        if (leaf==z.getLeftChild()){
            Y = z.getMiddleChild();
        }else {
            Y = z.getRightChild();
        }
        while (!(Y.isLeaf())){
            Y = Y.getLeftChild();
        }
        if (Y.getGoals()<=Integer.MAX_VALUE){
            return Y;
        }
        else return null;
    }

    public playerGoalNode Predessesor(playerGoalNode leaf){

        playerGoalNode z = leaf.getParent();
        while (leaf==z.getLeftChild()){
            leaf = z;
            z = z.getParent();
        }
        playerGoalNode Y;
        if (leaf==z.getMiddleChild()){
            Y = z.getLeftChild();
        }else {
            Y = z.getMiddleChild();
        }
        while (!(Y.isLeaf())){
            if ((Y).getRightChild()==null){
                Y = Y.getMiddleChild();
            }else {
                Y = Y.getRightChild();
            }
        }
        if (Y.getGoals()>=Integer.MIN_VALUE){
            return Y;
        }else return null;
    }
}
