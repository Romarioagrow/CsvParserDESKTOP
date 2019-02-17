class Product {
    private Integer id;
    private String name;
    private String condition;
    private String state;
    private Float price;

    Product() {
    }

    static void showInfo(Product product) {
        System.out.println(
                "ID: " + product.getId() +
                        ", Name: " + product.getName() +
                        ", Condition: " + product.getCondition() +
                        ", State: " + product.getState() +
                        ", Price: " + product.getPrice()
        );
    }

    Integer getId() {
        return id;
    }
    void setId(Integer id) {
        this.id = id;
    }

    String getName() {
        return name;
    }
    void setName(String name) {
        this.name = name;
    }

    String getCondition() {
        return condition;
    }
    void setCondition(String condition) {
        this.condition = condition;
    }

    String getState() {
        return state;
    }
    void setState(String state) {
        this.state = state;
    }

    Float getPrice() {
        return price;
    }
    void setPrice(Float price) {
        this.price = price;
    }
}
