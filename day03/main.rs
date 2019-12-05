use std::collections::HashSet;

struct Instruction {
    direction: String,
    steps: i32,
}

#[derive(Debug, Clone, PartialEq, Eq, Hash)]
struct Point {
    x: i32,
    y: i32,
}

fn navigate(directions: &Vec<Instruction>) -> Vec<Point> {
    let mut current = Point { x: 0, y: 0 };
    let mut positions = Vec::new();

    for dir in directions {
        match dir.direction.as_ref() {
            "U" => {
                for i in (current.y + 1)..(current.y + dir.steps + 1) {
                    positions.push(Point { x: current.x, y: i });
                }
                current.y = positions.last().unwrap().y
            }
            "D" => {
                for i in ((current.y - dir.steps)..(current.y)).rev() {
                    positions.push(Point { x: current.x, y: i });
                }
                current.y = positions.last().unwrap().y
            }
            "R" => {
                for i in (current.x + 1)..(current.x + dir.steps + 1) {
                    positions.push(Point { x: i, y: current.y });
                }
                current.x = positions.last().unwrap().x
            }
            "L" => {
                for i in ((current.x - dir.steps)..(current.x)).rev() {
                    positions.push(Point { x: i, y: current.y });
                }
                current.x = positions.last().unwrap().x
            }
            _ => ()
        }
    }

    positions
}

fn intersection(wire1: &Vec<Point>, wire2: &Vec<Point>) -> Vec<Point> {
    let mut intersections: Vec<Point> = Vec::new();
    let mut position_set = HashSet::new();

    for position in wire1 {
        position_set.insert(position.clone());
    }

    for position in wire2 {
        if position_set.contains(&position) {
            intersections.push(position.clone());
        }
    }

    intersections
}

fn minimum_distance(wire1: &Vec<Point>, wire2: &Vec<Point>) -> i32 {
    let mut lowest = 0;
    let initial = Point { x: 0, y: 0 };
    let intersections = intersection(&wire1, &wire2);

    for point in intersections {
        let distance = manhatan_distance(&initial, &point);

        if lowest == 0 || distance < lowest {
            lowest = distance
        }
    }

    lowest
}

fn manhatan_distance(p1: &Point, p2: &Point) -> i32 {
    (p1.x - p2.x).abs() + (p1.y - p2.y).abs()
}

fn minimum_steps(wire1: &Vec<Point>, wire2: &Vec<Point>) -> i32 {
    let mut lowest: i32 = 0;
    let intersections = intersection(&wire1, &wire2);

    for point in intersections {
        let steps1 = wire1.iter().position(|w| w == &point).unwrap() + 1;
        let steps2 = wire2.iter().position(|w| w == &point).unwrap() + 1;
        let steps = (steps1 + steps2) as i32;

        if lowest == 0 || steps < lowest {
            lowest = steps
        }
    }

    lowest
}

fn parse(wire: String) -> Vec<Instruction> {
    wire.split(",")
        .map(|w| {
            let arr: Vec<_> = w.chars().collect();

            let direction: String = arr[0].to_string();
            let steps: String = arr[1..arr.len()].to_vec().iter().collect();

            Instruction { direction: direction, steps: steps.parse().unwrap() }
        })
        .collect()
}

fn main() {
    // let wire1 = "R75,D30,R83,U83,L12,D49,R71,U7,L72".to_string();
    // let wire2 = "U62,R66,U55,R34,D71,R55,D58,R83".to_string();
    // let wire1 = "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51".to_string();
    // let wire2 = "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7".to_string();
    let input = std::fs::read_to_string("input")
        .expect("Unable to read file");

    let data: Vec<String> = input.trim()
        .split("\n")
        .map(|s| s.to_string())
        .collect();

    let wire1 = data[0].to_string();
    let wire2 = data[1].to_string();

    let wire1_instructions = parse(wire1);
    let wire2_instructions = parse(wire2);

    let wire1_positions = navigate(&wire1_instructions);
    let wire2_positions = navigate(&wire2_instructions);

    let answer1 = minimum_distance(&wire1_positions, &wire2_positions);
    let answer2 = minimum_steps(&wire1_positions, &wire2_positions);

    println!("{:?}", answer1);
    println!("{:?}", answer2);
}
