import java.util.List;

public class OrderGenerator {
    public static Order getDefault() {
        List<String> color = List.of("BLACK,GREY");
        return new Order("liza","kirsanova","Nymphenburg 1","2","89203453434",6,"2023-02-23","жду",color);
    }

    public static Order getColor(List<String> color) {
        return new Order("liza","kirsanova","Nymphenburg 1","2","89203453434",6,"2023-02-23","жду",color);
    }
}
