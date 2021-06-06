const PI: f64 = 3.1416;

struct Circle {
    radius: f64
}

struct Rectangle {
    height: f64,
    width: f64
}

trait Shape {
    fn area(&self) -> f64;
}

impl Shape for Circle {
    fn area(&self) -> f64 {
        PI * self.radius * self.radius
    }
}

impl Shape for Rectangle {
    fn area(&self) -> f64 {
        self.height * self.width
    }
}

fn print_area<T: Shape> (shape: &T) {
    println!("{}", shape.area());
}

fn main() {
    let circle = Circle{radius: 2.0};
    let rectangle = Rectangle{height: 3.0, width: 5.0};

    print_area(&circle); // 12.5664
    print_area(&rectangle); // 15

    print_area_generic(&circle); // 12.5664
    print_area_generic(&rectangle); // 15
}
