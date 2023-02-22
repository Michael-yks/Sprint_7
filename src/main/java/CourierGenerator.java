public class CourierGenerator {
    public static Courier getDefault() {
        return new Courier("elk","qwerty","oaks");
    }

    public static Courier getWithPasswordOnly() {
        return new Courier(null,"qwerty12",null);
    }

    public static Courier getWithLoginOnly() {
        return new Courier("goat","","");
    }

    public static Courier getWithNameOnly() {
        return new Courier("","","bear");
    }
}
