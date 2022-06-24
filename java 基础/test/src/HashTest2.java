import java.util.HashSet;
import java.util.Objects;

/**
 * 重写了 equals 方法 和 hashCode 方法
 */
public class HashTest2 {

    public static void main(String[] args) {

        PersonWithHashCode p1 = new PersonWithHashCode(1,"a");
        PersonWithHashCode p2 = new PersonWithHashCode(1,"a");

        System.out.println(p1.equals(p2));

        HashSet<PersonWithHashCode> hashSet = new HashSet<>();
        hashSet.add(p1);
        hashSet.add(p2);

        System.out.println(hashSet.size());
        for (PersonWithHashCode p :
                hashSet) {
            System.out.println(p.toString());
        }


    }
}

class PersonWithHashCode{

    private int id;
    private String name;

    public PersonWithHashCode(int id, String name){
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonWithHashCode that = (PersonWithHashCode) o;
        return id == that.id &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "PersonWithHashCode{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
