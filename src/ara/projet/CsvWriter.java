package ara.projet;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CsvWriter {

  public StringBuilder sb;
  public String path;

  public CsvWriter(String path) {
    sb = new StringBuilder();
    this.path = path;
  }

  public void write(String s) {
    try (PrintWriter writer = new PrintWriter(new FileWriter(path, true))) {

      sb.append(s);
      writer.write(sb.toString());

      System.out.println("done!");

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

}
