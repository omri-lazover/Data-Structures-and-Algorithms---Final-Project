public class TeamsTreeByPoints {
        private teamPointsNode root;
        teamPointsNode rightBoundNode;

        public TeamsTreeByPoints(){
            this.root = new teamPointsNode();
            teamPointsNode leftSentinel = new teamPointsNode(Integer.MIN_VALUE,new Faculty(Integer.MAX_VALUE,null), this.root);
            teamPointsNode rightSentinel = new teamPointsNode(Integer.MAX_VALUE,new Faculty(Integer.MIN_VALUE,null), this.root);
            this.root.setKey(Integer.MIN_VALUE,Integer.MAX_VALUE);
            this.root.setMaxNode(rightSentinel);
            this.root.setLeftChild(leftSentinel);
            this.root.setMiddleChild(rightSentinel);
            leftSentinel.setSuccessor(rightSentinel);
            rightSentinel.setPredecessor(leftSentinel);
            this.rightBoundNode=rightSentinel;

        }

        public teamPointsNode getRoot() {
            return this.root;
        }

    public teamPointsNode getRightBoundNode() {
        return rightBoundNode;
    }

    public void setRoot(teamPointsNode root) {

            this.root = root;
        }

        public teamPointsNode Search_Node(teamPointsNode Root, int id, int points){
            if (Root.isLeaf()){
                if (Root.getPoints()==points &&  Root.getKey()== id){
                    return Root;
                }else{
                    return null;
                }
            }

            if (points<(Root).getLeftChild().getPoints() ||points==(Root).getLeftChild().getPoints()&& id >= (Root).getLeftChild().getKey()){
                return Search_Node((Root).getLeftChild(),id,points);
            } else if (points<(Root).getMiddleChild().getPoints()||points==(Root).getMiddleChild().getPoints()&& id >= (Root).getMiddleChild().getKey()){
                return Search_Node((Root).getMiddleChild(),id,points);
            }else {
                return Search_Node((Root).getRightChild(),id,points);
            }
        }

        public void UpdateKey(teamPointsNode X){

            X.setKey(X.getLeftChild().getKey(),X.getLeftChild().getPoints());
            X.setMaxNode(X.getLeftChild().getMaxNode());
            if (X.getMiddleChild()!=null){
                X.setKey(X.getMiddleChild().getKey(),X.getMiddleChild().getPoints());
                X.setMaxNode(X.getMiddleChild().getMaxNode());
            }
            if (X.getRightChild()!=null){
                X.setKey(X.getRightChild().getKey(),X.getRightChild().getPoints());
                X.setMaxNode(X.getRightChild().getMaxNode());
            }
        }
        public void SetChildren(teamPointsNode X, teamPointsNode L, teamPointsNode M, teamPointsNode R){
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
        public teamPointsNode InsertAndSplit(teamPointsNode X, teamPointsNode Z){
            teamPointsNode L = X.getLeftChild();
            teamPointsNode M = X.getMiddleChild();
            teamPointsNode R = X.getRightChild();
            int points = Z.getPoints();
            int id = Z.getKey();
            if (R==null) {
                if (points<X.getLeftChild().getPoints()||points==X.getLeftChild().getPoints()&& id > X.getLeftChild().getKey()) {
                    SetChildren(X, Z, L, M);
                }
                else if (points<X.getMiddleChild().getPoints()||points==X.getMiddleChild().getPoints()&& id > X.getMiddleChild().getKey()){
                    SetChildren(X, L, Z, M);
                }
                else {
                    SetChildren(X, L, M, Z);
                }
                return null;
            }
            teamPointsNode Y = new teamPointsNode();
            if (points<X.getLeftChild().getPoints()||points==X.getLeftChild().getPoints()&& id > X.getLeftChild().getKey()){
                SetChildren(X, Z, L, null);
                SetChildren(Y, M, R, null);
            } else if (points<X.getMiddleChild().getPoints()||points==X.getMiddleChild().getPoints()&& id > X.getMiddleChild().getKey()) {
                SetChildren(X, L, Z, null);
                SetChildren(Y, M, R, null);//
            } else if (points<X.getRightChild().getPoints()||points==X.getRightChild().getPoints()&& id > X.getRightChild().getKey()) {
                SetChildren(X, L, M, null);
                SetChildren(Y, Z, R, null);
            }else{
                SetChildren(X, L, M, null);
                SetChildren(Y, R, Z, null);
            }
            return Y;
        }
        public void Insert(teamPointsNode Z){
            teamPointsNode Y = this.root;
            int points = Z.getPoints();
            int id = Z.getKey();
            while (!(Y.isLeaf())){
                if (points<Y.getLeftChild().getPoints()||points==(Y).getLeftChild().getPoints()&& id > (Y).getLeftChild().getKey()){
                    Y = Y.getLeftChild();
                } else if (points<(Y).getMiddleChild().getPoints()||points==(Y).getMiddleChild().getPoints()&&
                        id >Y.getMiddleChild().getKey()) {
                    Y = (Y).getMiddleChild();
                }else {
                    Y = (Y).getRightChild();
                }
            }
            teamPointsNode X = (Y).getParent();
            teamPointsNode newNode = InsertAndSplit(X, Z);
            while (X !=this.root){
                X= X.getParent();
                if (newNode!=null){
                    newNode=InsertAndSplit(X,newNode);
                }else UpdateKey(X);
            }
            if (newNode!=null){
                teamPointsNode W = new teamPointsNode();
                SetChildren(W,X,newNode,null);
                this.root = W;
            }
            Z.setPredecessor(Predessesor(Z));
            Z.getPredecessor().setSuccessor(Z);
            Z.setSuccessor(Sucssesor(Z));
            Z.getSuccessor().setPredecessor(Z);

        }
        public teamPointsNode Borrow_or_Merge(teamPointsNode Y){
            teamPointsNode Z = Y.getParent();
            if (Y == Z.getLeftChild()){
                teamPointsNode X =Z.getMiddleChild();
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
                teamPointsNode X =Z.getLeftChild();
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
            teamPointsNode X = Z.getMiddleChild();
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

        public void Delete_2_3(teamPointsNode X){
            teamPointsNode Y = X.getParent();
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


        public teamPointsNode Sucssesor(teamPointsNode leaf){

            teamPointsNode z = leaf.getParent();
            while (leaf==z.getRightChild() ||(z.getRightChild()==null&&leaf == z.getMiddleChild())){
                leaf = z;
                z = z.getParent();
            }
            teamPointsNode Y;
            if (leaf==z.getLeftChild()){
                 Y = z.getMiddleChild();
            }else {
                Y = z.getRightChild();
            }
            while (!(Y.isLeaf())){
                Y = Y.getLeftChild();
            }
            if (Y.getPoints()<=Integer.MAX_VALUE){
                return Y;
            }
            else return null;
        }

    public teamPointsNode Predessesor(teamPointsNode leaf){

        teamPointsNode z = leaf.getParent();
        while (leaf==z.getLeftChild()){
            leaf = z;
            z = z.getParent();
        }
        teamPointsNode Y;
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
        if (Y.getPoints()>=Integer.MIN_VALUE){
            return Y;
        }else return null;
    }
}
