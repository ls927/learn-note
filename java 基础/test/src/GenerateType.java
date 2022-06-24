import java.util.Date;

public class GenerateType<T> {
    T data;

    public void setData(T data){
        this.data = data;
    }

    public <E> void useThat(E a){
        System.out.println(a.toString());
    }

    public static void main(String[] args) {
        GenerateType<Integer> integerGenerateType = new GenerateType<>();
        integerGenerateType.useThat(new Date());
    }
}
