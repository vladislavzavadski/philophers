package by.training.philosopers.domain;

/**
 * Created by vladislav on 11.07.16.
 */
public class Fork {
    private int id;

    public Fork(){}

    public Fork(int id){
        this.id = id;
    }

    @Override
    public String toString() {
        return "Fork{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fork fork = (Fork) o;

        return id == fork.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
