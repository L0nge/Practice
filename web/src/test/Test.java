import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

/**
 * Created by Longe on 2017.11.23.
 */
public class Test {
    public static void main(String[] args) {
        String []datas = new String[] {"penga","zhao","li"};
        Arrays.sort(datas);
        Stream.of(datas).forEach(param -> System.out.println(param));
    }
}
