trait Monoid {
    fn identity() -> Self;
    fn op(&self, subject: Self) -> Self;
}

impl Monoid for i32 {
    fn identity() -> i32 { 0 }
    fn op(&self, b: i32) -> i32 { *self + b }
}

fn main() {
   println!("{}", i32::identity());
   println!("{}", 1.op(2).to_string());
}
