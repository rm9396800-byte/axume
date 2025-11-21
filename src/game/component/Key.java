package game.component;

public class Key {
    private boolean key_right;
    private boolean key_left;
    private boolean key_space;
    private boolean key_J;
    private boolean key_K;

    public boolean isKey_right() {  //the checker , checks if am holdin the button
                                    //if so move (get method, read
        return key_right;
    }

    public void setKey_right(boolean key_right) { //physically flipping it..action
        this.key_right = key_right;
    }     // write , set

    public boolean isKey_left() {
        return key_left;
    }

    public void setKey_left(boolean key_left) {
        this.key_left = key_left;
    }

    public boolean isKey_space() {
        return key_space;
    }

    public void setKey_space(boolean key_space) {
        this.key_space = key_space;
    }

    public boolean isKey_J() {
        return key_J;
    }

    public void setKey_J(boolean key_J) {
        this.key_J = key_J;
    }

    public boolean isKey_K() {
        return key_K;
    }

    public void setKey_K(boolean key_K) {
        this.key_K = key_K;
    }
}
