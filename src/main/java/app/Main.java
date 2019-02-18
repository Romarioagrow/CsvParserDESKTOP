package app;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        String csvFilepath;
        boolean validCSV;

        // Получить путь к дирректории с CSV файлами
        Scanner in = new Scanner(System.in);
        System.out.println("Please enter full path to the directory with CSV files: ");
        String path = in.nextLine();

        //File folder = new File("C:\\Users\\Rescue\\Desktop\\csvfiles\\");

        File folder = new File(path);

        // Проверка и отсев файлов в дирректории
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.exists() && file.isFile()) { // Файл сушествует и является файлом
                csvFilepath = file.getPath();
                validCSV = csvFilepath.endsWith(".csv"); // Файл является CSV

                // Вызов потока с методом обработки для каждого файла
                if (validCSV) {
                    Collector collector = new Collector(csvFilepath);
                    collector.start();
                    collector.join();
                }
                else System.out.println("\n" + file.getName()+ " is not a CSV file");
            }
        }

        // Вывод итоговых данных в CSV файл
        Collector.dataOutputting(Collector.cheapestProducts);
    }
}
