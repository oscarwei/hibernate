package tw.com.oscar.guava.ordering;

import com.google.common.base.Function;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by oscarwei168 on 2015/6/12.
 */
public class OrderingTest {

    private static final Comparator<Person> firstNameComparator = (p1, p2) -> p1.getFirstname().compareTo(p2
            .getFirstname());
    private static final Comparator<Person> lastNameComparator = (p1, p2) -> p1.getLastname().compareTo(p2
            .getLastname());
    private static final Comparator<Person> birthYearComparator = (p1, p2) -> p1.getBirthYear().compareTo(p2.getBirthYear());

    public static void main(String[] args) {

        List<Person> persons = Lists.newArrayList(new Person("Oscar", "Wei", 1970), new Person("Amy", "Hung", 1976),
                new Person("Sunny", "Wei", 2008));
        persons.add(null);
        persons.add(null);

        Ordering<Person> naturalOrdering = Ordering.natural();

        Ordering<Person> aComparator = Ordering.from(firstNameComparator);
        List<Person> sortedPersons = aComparator.nullsLast().sortedCopy(persons);
        System.out.println(sortedPersons);

        Ordering<Person> aComparator1 = Ordering.from(lastNameComparator).compound(birthYearComparator).reverse();
        System.out.println(aComparator1.nullsFirst().sortedCopy(persons));

        Ordering ordering = Ordering.natural().reverse().nullsFirst().onResultOf(new Function<Person, Integer>() {
            @Override
            public Integer apply(Person s) {
                return null != s ? s.getFirstname().length() : null;
            }
        });

        Collections.sort(persons, ordering);
        System.out.println(persons);

        List<Colors> colors = Lists.newArrayList(Colors.BLUE, Colors.WHITE, Colors.BLACK, Colors.YELLOW);
        Ordering<Colors> explicit = Ordering.explicit(Colors.BLACK, Colors.WHITE, Colors.YELLOW, Colors.BLUE);
        List<Colors> sortedColors = explicit.sortedCopy(colors);
        System.out.println(sortedColors);

        Ordering<Object> toStringOrdering = Ordering.usingToString();
        sortedPersons = toStringOrdering.nullsFirst().sortedCopy(persons);
        System.out.println(sortedPersons);

        System.out.println(explicit.binarySearch(sortedColors, Colors.WHITE));

        System.out.println(naturalOrdering.nullsFirst().max(persons));
        System.out.println(naturalOrdering.max(new Person("Oscar", "Wei", 1970), new Person("Amy",
                "Hung", 1976)));
        System.out.println(naturalOrdering.nullsFirst().min(persons));

        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);
        System.out.println(Ordering.natural().isOrdered(list));
        System.out.println(Ordering.natural().isStrictlyOrdered(list));

        List<Integer> list1 = Lists.newArrayList(1, 2, 3, 3, 4, 5, 6);
        System.out.println(Ordering.natural().isOrdered(list1));
        System.out.println(Ordering.natural().isStrictlyOrdered(list1));

    }

    private enum Colors {
        RED, WHITE, BLACK, ORANGE, BLUE, YELLOW
    }

    private static class Person implements Comparable<Person> {

        private String firstname;
        private String lastname;
        private Integer birthYear;

        public Person(@NotNull String firstname, @NotNull String lastname, Integer birthYear) {
            this.firstname = firstname;
            this.lastname = lastname;
            this.birthYear = birthYear;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }


        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public Integer getBirthYear() {
            return birthYear;
        }

        public void setBirthYear(Integer birthYear) {
            this.birthYear = birthYear;
        }

        @Override
        public int compareTo(Person other) {
            return ComparisonChain.start().
                    compare(this.firstname, other.getFirstname()).
                    compare(this.lastname, other.getLastname()).result();
        }

        @Override
        public String toString() {
            return "Person{" +
                    "firstname='" + firstname + '\'' +
                    ", lastname='" + lastname + '\'' +
                    ", birthYear=" + birthYear +
                    '}';
        }
    }
}
