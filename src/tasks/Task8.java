package tasks;

import common.Person;
import common.Task;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
А теперь о горьком
Всем придется читать код
А некоторым придется читать код, написанный мною
Сочувствую им
Спасите будущих жертв, и исправьте здесь все, что вам не по душе!
P.S. функции тут разные и рабочие (наверное), но вот их понятность и эффективность страдает (аж пришлось писать комменты)
P.P.S Здесь ваши правки желательно прокомментировать (можно на гитхабе в пулл реквесте)
 */
public class Task8 implements Task {
  // Не хотим выдывать апи нашу фальшивую персону, поэтому конвертим начиная со второй
  public List<String> getNames(List<Person> persons) {
    // Пропускаем фальшивую персону встроенным методом Stream API
    return persons.stream()
            .skip(1)
            .map(this::getName)
            .collect(Collectors.toList());
  }

  // Ну и различные имена тоже хочется
  public Set<String> getDifferentNames(List<Person> persons) {
    // Distinct здесь излишен, поэтому всю конструкцию можно избавить от stream
    return new HashSet<>(getNames(persons));
  }

  // Для фронтов выдадим полное имя, а то сами не могут
  public String getName(Person person) {
    return Stream.of(person.getSecondName(), person.getFirstName(), person.getMiddleName())
            .filter(Objects::nonNull)
            .collect(Collectors.joining(" "));
  }

  // Словарь id персоны -> ее имя
  public Map<Integer, String> getNameDictionary(Collection<Person> persons) {
    // Создаем словарь
    return persons.stream()
            .collect(Collectors.toMap(Person::getId, this::getName, (a, b) -> a));
  }

  // Есть ли совпадающие в двух коллекциях персоны?
  public boolean hasSamePersons(Collection<Person> comparing, Collection<Person> comparable) {
    // Инвертируем значение функции исключающей пересечение в объектах
    return !Collections.disjoint(comparing, comparable);
  }

  // Считаем четные числа в stream
  public long countEven(Stream<Integer> numbers) {
    // Нет необходимости в инкременте count
    return numbers.filter(num -> num % 2 == 0).count();
  }

  // Считаем четные числа вне stream
  public long countEven(Set<Integer> numbers) {
    // Смею предположить, что подсчет numbers вне stream мог бы быть не менее востребован
    return countEven(numbers.stream());
  }

  @Override
  public boolean check() {
    Instant now = Instant.now();
    Person firstPerson = new Person(1, "Petya", now);
    Person secondPerson = new Person(2, "Vasya", now.plusSeconds(1));
    Person thirdPerson = new Person(3, "Anya", now.plusSeconds(2));
    List<Person> withFake = List.of(firstPerson, firstPerson, secondPerson, thirdPerson);

    return getNames(withFake).equals(List.of("Petya", "Vasya", "Anya"))
            && getDifferentNames(withFake).equals(Set.of("Petya", "Vasya", "Anya"))
            && getName(thirdPerson).equals("Anya")
            && getNameDictionary(List.of(firstPerson, secondPerson, thirdPerson)).get(1).equals("Petya")
            && hasSamePersons(List.of(firstPerson, secondPerson), List.of(firstPerson, thirdPerson))
            && countEven(Set.of(1, 2, 3, 4, 5, 6)) == 3;
  }
}
