
public class TeamsTreeByFacultyId {
    private facultyTeamNode root;

    public TeamsTreeByFacultyId(){
        this.root = new facultyTeamNode();
        facultyTeamNode leftSentinel = new facultyTeamNode(new Faculty(Integer.MIN_VALUE,null), this.root);
        facultyTeamNode rightSentinel = new facultyTeamNode(new Faculty(Integer.MAX_VALUE,null), this.root);
        this.root.setKey(Integer.MAX_VALUE);
        this.root.setLeftChild(leftSentinel);
        this.root.setMiddleChild(rightSentinel);
    }

    public facultyTeamNode getRoot() {
        return this.root;
    }
    public void setRoot(facultyTeamNode root) {
        this.root = root;
    }

    public facultyTeamNode search_2_3(facultyTeamNode Root, int Key){
        if (Root.isLeaf()){
            if (Root.getKey() == Key){
                return Root;
            }else{
                return null;
            }
        }

        if (Key<=(Root).getLeftChild().getKey()){
            return search_2_3((Root).getLeftChild(),Key);
        } else if (Key<=(Root).getMiddleChild().getKey()){
            return search_2_3((Root).getMiddleChild(),Key);
        }else {
            return search_2_3((Root).getRightChild(),Key);
        }
    }

    public void  UpdateKey(facultyTeamNode toUpdate){
        toUpdate.setKey(toUpdate.getLeftChild().getKey());
        if (toUpdate.getMiddleChild()!=null){
            toUpdate.setKey(toUpdate.getMiddleChild().getKey());
        }
        if (toUpdate.getRightChild()!=null){
            toUpdate.setKey(toUpdate.getRightChild().getKey());
        }
    }
    public void SetChildren(facultyTeamNode x, facultyTeamNode Left, facultyTeamNode Middle, facultyTeamNode Right){
        x.setLeftChild(Left);
        x.setMiddleChild(Middle);
        x.setRightChild(Right);
        Left.setParent(x);
        if (Middle!=null){
            Middle.setParent(x);
        }
        if(Right!=null){
            Right.setParent(x);
        }
        UpdateKey(x);
    }
    public facultyTeamNode InsertAndSplit(facultyTeamNode x, facultyTeamNode Z){
        facultyTeamNode L = x.getLeftChild();
        facultyTeamNode M = x.getMiddleChild();
        facultyTeamNode R = x.getRightChild();
        if (R==null) {
            if (Z.getKey() < L.getKey()) {
                SetChildren(x, Z, L, M);
            }
            else if (Z.getKey() < M.getKey()){
                SetChildren(x, L, Z, M);
            }
            else {
                SetChildren(x, L, M, Z);}
            return null;
        }
        facultyTeamNode Y = new facultyTeamNode();

        if (Z.getKey() < L.getKey()){
            SetChildren(x, Z, L, null);
            SetChildren(Y, M, R, null);
        } else if (Z.getKey() < M.getKey()) {
            SetChildren(x, L, Z, null);
            SetChildren(Y, M, R, null);//
        } else if (Z.getKey() < R.getKey()) {
            SetChildren(x, L, M, null);
            SetChildren(Y, Z, R, null);
        }else{
            SetChildren(x, L, M, null);
            SetChildren(Y, R, Z, null);
        }
        return Y;
    }
    public void Insert(facultyTeamNode Z){
        facultyTeamNode Y = this.root;
        while (!(Y.isLeaf())){
            if (Z.getKey() < ( Y).getLeftChild().getKey()){
                Y = (Y).getLeftChild();
            } else if (Z.getKey() < (Y).getMiddleChild().getKey()) {
                Y = (Y).getMiddleChild();
            }else {
                Y = (Y).getRightChild();
            }
        }
        facultyTeamNode X = (Y).getParent();
        facultyTeamNode newZ = InsertAndSplit(X,Z);
        while (X !=this.root){
            X= X.getParent();
            if (newZ!=null){
                newZ=InsertAndSplit(X,newZ);
            }else UpdateKey(X);
        }
        if (newZ!=null){
            facultyTeamNode W = new facultyTeamNode();
            SetChildren(W,X,newZ,null);
            this.root = W;
        }
    }

    public facultyTeamNode Borrow_or_Merge(facultyTeamNode Y){
        facultyTeamNode Z =Y.getParent();
        if (Y == Z.getLeftChild()){
            facultyTeamNode X =Z.getMiddleChild();
            if(X.getRightChild()!=null){
                SetChildren(Y, Y.getLeftChild(), X.getLeftChild(), null);
                SetChildren(X, X.getMiddleChild(), X.getRightChild(), null);
            }else {
                SetChildren(X, Y.getLeftChild(), X.getLeftChild(), X.getMiddleChild());
                Y=null;
                SetChildren(Z, X, Z.getRightChild(), null);
            }
            return Z;

        }
        if (Y == Z.getMiddleChild()){
            facultyTeamNode X =Z.getLeftChild();
            if (X.getRightChild() !=null){
                SetChildren(Y, X.getRightChild(), Y.getLeftChild(), null);
                SetChildren(X, X.getLeftChild(), X.getMiddleChild(), null);
            }else {
                SetChildren(X, X.getLeftChild(), X.getMiddleChild(), Y.getLeftChild());
                Y=null;
                SetChildren(Z, X, Z.getRightChild(), null);
                return Z;
            }
        }
        facultyTeamNode X = Z.getMiddleChild();
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

    public void Delete_2_3(facultyTeamNode X){
        facultyTeamNode Y = X.getParent();
        if (X== Y.getLeftChild()){
            SetChildren(Y,Y.getMiddleChild(),Y.getRightChild(),null);
        } else if (X==Y.getMiddleChild()) {
            SetChildren(Y,Y.getLeftChild(),Y.getRightChild(),null);
        }else{
            SetChildren(Y,Y.getLeftChild(),Y.getMiddleChild(),null);
        }
        X=null;
        while (Y!=null){
            if (Y.getMiddleChild() == null){
                if (Y != this.root){
                    Y = Borrow_or_Merge(Y);
                }else {
                    this.root =Y.getLeftChild();
                    Y.getLeftChild().setParent(null);
                    Y=null;
                    return;
                }
            }else {
                UpdateKey(Y);
                Y =Y.getParent();
            }
        }
    }
}
