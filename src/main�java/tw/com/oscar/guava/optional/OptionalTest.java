package tw.com.oscar.guava.optional;

import com.google.common.base.Optional;
import com.google.common.base.Strings;

import java.util.Set;

/**
 * Created by oscarwei168 on 2015/6/11.
 */
public class OptionalTest {

    public static void main(String[] args) {
        Optional<String> optional0 = Optional.absent();
        Optional<String> optional1 = Optional.of("Oscar");
        Optional<String> optional2 = Optional.fromNullable(null);
        System.out.println(optional0);
        System.out.println(optional2.isPresent());
        System.out.println(optional2.or(() -> "0")); // Supplier
        String defaultValue = optional2.or("Wei");
        System.out.println(defaultValue);
        System.out.println(optional1.get());
        System.out.println(optional2.orNull());
        Set<String> set = optional1.asSet();
        System.out.println(set.size());
        System.out.println(optional1.transform(s -> s.length())); // Function

        String s1 = Strings.emptyToNull(null);
        String s2 = Strings.nullToEmpty(null);
        System.out.println(Strings.isNullOrEmpty(s1));
        System.out.println(s2);
    }
}
