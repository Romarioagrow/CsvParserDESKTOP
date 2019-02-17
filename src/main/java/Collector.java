import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.eclipse.collections.impl.block.factory.HashingStrategies;
import org.eclipse.collections.impl.utility.ListIterate;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("ALL")
public class Collector extends Thread {
    static List<Product> cheapestProducts = new ArrayList<Product>(); // Итоговая коллекция для обработанных данных
    private String csvFilepath = null; // Путь CSV файла
    private static int amount = 999; // Ограничитель количества строк в коллекциях

    Collector(String csvFilepath) {
        this.csvFilepath = csvFilepath;
    }

    @Override
    public void run() {
        synchronized (cheapestProducts) {
            try {
                dataCollecting(csvFilepath);
            }
            catch (NumberFormatException e) {
                System.out.println("The file contains invalid characters!");
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Собрать данные из файла в коллекцию products и обработать
    private static void dataCollecting(String csvFilepath) throws NumberFormatException, IOException {
        CSVReader reader = new CSVReader(new FileReader(csvFilepath), ';');
        List<Product> products = new ArrayList<Product>();
        String[] line = null;

        // Преобразовать CSV файл в коллекцию объектов Product
        while ((line = reader.readNext()) != null) { // Читать CSV файл построчно
            if (line[0].equals("ID")) { // Обозначить начало файла /// В ENUM
                showInfo(csvFilepath);
            }
            else {
                Product product = new Product();

                // Преобразовать ID из String в Integer
                Integer id = Integer.parseInt(line[0]);
                product.setId(id);

                product.setName(line[1]);
                product.setCondition(line[2]);
                product.setState(line[3]);

                // Преобразовать Price из String в Float с заменой некоректных символов
                String string = line[4];
                String string1 = string.replace(',', '.');
                Float price = Float.parseFloat(string1);
                product.setPrice(price);

                // Добавить заполненный Product в коллекцию
                products.add(product);
            }
        }

        // Отфильтровать по ID и отсортировать по Price
        dataProcessing(products);

        // Добавить обработанные данные в итоговую коллекцию cheapestProducts
        cheapestProducts.addAll(products);
    }

    // Обработать итоговую коллекцию с данными и вывести в файл
    static void dataOutputting(List<Product> cheapestProducts) throws IOException {
        final List<String[]> stringData = new ArrayList<String[]>();

        // Присвоить уникальное имя выходному файлу
        UUID randomName = UUID.randomUUID();
        String path = "C:\\Users\\Rescue\\Desktop\\Output_№"+randomName+".csv";
        CSVWriter csvWriter = new CSVWriter(new FileWriter(path), ',', '"');

        // Отсортировать по Price
        cheapestProducts.sort(Comparator.comparing(Product::getPrice));

        // Отфильтроваьть повторяющиеся ID в cheapestProducts
        cheapestProducts = ListIterate.distinct(cheapestProducts, HashingStrategies.fromIntFunction(Product::getId));

        // Вывести необходимое количество Product из коллекции в массив строк
        cheapestProducts.stream()
                .limit(getAmount())
                .forEach(product -> stringData.add(new String[] {
                        Integer.toString(product.getId()),
                        product.getName(),
                        product.getState(),
                        product.getCondition(),
                        Float.toString(product.getPrice())}));

        // Записать массив в файл и сохранить
        csvWriter.writeAll(stringData);
        csvWriter.close();

        System.out.println("\nYour CSV file is " + path);
    }

    // Промежуточная обработка данных
    private static void dataProcessing(List<Product> products) {
        // Отсортировать по Price
        products.sort(Comparator.comparing(Product::getPrice));

        // Отсеить повторяющиеся Product ID
        products = ListIterate.distinct(products, HashingStrategies.fromIntFunction(Product::getId));

        // Ограничить количество и вывести на экран
        showInfo(products);
        products.stream().limit(getAmount()).forEach(Product::showInfo);
    }

    // Вывести информацию и коллекциях с обработанными данными
    private static void showInfo(List<Product> products) {
        System.out.println("Current thread: " + Thread.currentThread().getId());

        LocalTime time = LocalTime.now();
        System.out.println("Start time: " + time);

        System.out.println(products.size() + " unique and sorted rows after processing");
        System.out.println(getAmount() + " rows limited");
    }

    // Вывести информацию о CSV файле
    private static void showInfo(String csvFilepath) {
        System.out.println("\nReading CSV file "+ csvFilepath.substring(csvFilepath.lastIndexOf("\\")+1));
    }

    private static int getAmount() {
        return amount;
    }
}
