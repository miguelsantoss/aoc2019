fn execute(instructions: &mut Vec<usize>, current: usize) -> () {
    match instructions[current] {
        1 => handle_op(instructions, current, |a,b| a + b),
        2 => handle_op(instructions, current, |a,b| a * b),
        99 => return,
        _ => (),
    };

    execute(instructions, current + 4)
}

fn handle_op<F: Fn(usize, usize) -> usize>(instructions: &mut Vec<usize>, current: usize, op: F) {
    let first_arg = instructions[current + 1];
    let second_arg = instructions[current + 2];
    let result = instructions[current + 3];

    instructions[result] = op(instructions[first_arg], instructions[second_arg])
}

fn find_pair(instructions: &Vec<usize>, expected: usize) -> (usize, usize) {
    for i in 1..100 {
        for j in 1..100 {
            let mut to_run = instructions.to_vec();
            to_run[1] = i;
            to_run[2] = j;

            execute(&mut to_run, 0);

            if to_run[0] == expected {
                return (i, j);
            }
        }
    }

    (0, 0)
}

fn parse_instructions(input: &String) -> Vec<usize> {
    input.trim()
        .split(",")
        .map(|x| x.parse().unwrap())
        .collect()
}

fn main() {
    let input = std::fs::read_to_string("input")
        .expect("Unable to read file");

    let instructions = parse_instructions(&input);

    let mut to_run = instructions.to_vec();
    to_run[1] = 12;
    to_run[2] = 2;

    execute(&mut to_run, 0);
    println!("first_run: {}", to_run[0]);

    let pair = find_pair(&instructions, 19690720);
    let (noun, verb) = pair;
    let answer = 100 * noun + verb;

    println!("{}", answer);
}
