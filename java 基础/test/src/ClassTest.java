public class ClassTest {
    private int id;
    public int num;
}
class SubClass extends ClassTest{
    public int getId(){
        // cannot return id;
        return num;
    }
}
