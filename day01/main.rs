fn fuel_needed(module: u32) -> u32 {
    (module / 3).checked_sub(2).unwrap_or(0)
}

fn total_fuel_needed(module: u32) -> u32 {
    let value = fuel_needed(module);

    if value <= 0 {
        0
    } else {
        total_fuel_needed(value) + value
    }
}

fn initial_fuel(modules: &Vec<u32>) -> Vec<u32> {
    modules.iter().map(|&x| fuel_needed(x)).collect()
}

fn total_fuel(modules: &Vec<u32>) -> Vec<u32> {
    modules.iter().map(|&x| total_fuel_needed(x)).collect()
}

fn parse_modules(input: &String) -> Vec<u32> {
    input.lines().map(|x| x.parse::<u32>().unwrap()).collect()
}

fn main() {
    let input = std::fs::read_to_string("input")
        .expect("Unable to read file");

    let modules = parse_modules(&input);

    let initial_needed: u32 = initial_fuel(&modules).iter().sum();
    let total_needed: u32 = total_fuel(&modules).iter().sum();

    println!("{}", initial_needed);
    println!("{}", total_needed);
}
