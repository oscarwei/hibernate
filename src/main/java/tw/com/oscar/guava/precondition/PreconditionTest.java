package tw.com.oscar.guava.precondition;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.google.common.base.Preconditions.*;

/**
 * Created by oscarwei168 on 2015/6/11.
 */
public class PreconditionTest {

    public static void main(String[] args) {

        try {
            int var = -3;
            checkArgument(var > 0, "Illegal argument passed: Negative value = [%s]", var);
        } catch (Exception e) {
            // throw IllegalArgumentException
            System.err.println(e.getMessage());
        }

        try {
            List<String> list = Arrays.asList("erer", "dfdf", "sdggr", "aff");
            checkState(list.size() >= 10, "Illegal argument passed: [%s] less than 10", list.size());
        } catch (Exception e) {
            // throw IllegalStateException
            System.err.println(e.getMessage());
        }

        try {
            checkNotNull(null, "Illegal argument passed: NULL");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        try {
            int[] index = {1, 2, 3, 4};
            checkElementIndex(5, index.length, "Illegal argument passed: Invalid index");
        } catch (Exception e) {
            // check weather first argument is in the index range
            System.err.println(e.getMessage());
        }

        try {
            int[] index = {1, 2, 3, 4};
            checkPositionIndexes(2, 5, index.length);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        Timestamp t = null;
        Date d = new Date(t.getTime());
        new SimpleDateFormat("").format(d);

    }
}
