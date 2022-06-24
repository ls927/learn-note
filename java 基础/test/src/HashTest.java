import java.util.HashSet;
import java.util.Objects;

/**
 * 重写了 equals 方法，没有重写 hashCode 方法
 */
public class HashTest {

    public static void main(String[] args) {


        Person p1 = new Person(1,"a");
        Person p2 = new Person(1,"a");

        System.out.println( p1.equals(p2));

        HashSet<Person> hashSet = new HashSet<>();
        hashSet.add(p1);
        hashSet.add(p2);

        System.out.println(hashSet.size());
        for (Person p :
                hashSet) {
            System.out.println(p.toString());
        }

    }
}


class Person {

    private int id;
    private String name;

    public Person(int id, String name){
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id &&
                Objects.equals(name, person.name);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
