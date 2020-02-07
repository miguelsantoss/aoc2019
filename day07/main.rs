struct Amplifier {
    instructions: Vec<i32>,
    input: &Vec<i32>,
    output: &Vec<i32>,
    current: usize,
    halted: bool,
}

impl Amplifier {
    fn new(instructions: Vec<i32>, &input: Vec<i32>, output: &Vec<i32>) -> Amplifier {
        Amplifier {
            instructions: instructions,
            input: input,
            output: output,
            current: 0,
            halted: false,
        }
    }

    fn execute(&self) {
        let instructions = self.instructions;
        let current = self.current;

        let opcode = instructions[current] % 100;
        let mode1 = (instructions[current] / 100) % 10;
        let mode2 = (instructions[current] / 1000) % 10;

        let mut next_instruction = current;

        match opcode {
            1 => {
                next_instruction += 4;

                let arg1: usize =
                    if mode1 == 1 { current + 1 } else { instructions[current + 1] as usize };
                let arg2: usize =
                    if mode2 == 1 { current + 2 } else { instructions[current + 2] as usize };

                let res = instructions[current + 3] as usize;

                instructions[res] = instructions[arg1] + instructions[arg2]
            },
            2 => {
                next_instruction += 4;

                let arg1: usize =
                    if mode1 == 1 { current + 1 } else { instructions[current + 1] as usize };
                let arg2: usize =
                    if mode2 == 1 { current + 2 } else { instructions[current + 2] as usize };

                let res = instructions[current + 3] as usize;
                instructions[res] = instructions[arg1] * instructions[arg2]
            },
            3 => {
                next_instruction += 2;

                let arg1: usize =
                    if mode1 == 1 { current + 1 } else { instructions[current + 1] as usize };

                instructions[arg1] = self.input.remove(0)
            },
            4 => {
                next_instruction += 2;

                let arg1: usize =
                    if mode1 == 1 { current + 1 } else { instructions[current + 1] as usize };

                self.output.push(instructions[arg1])
            },
            5 => {
                let arg1: usize =
                    if mode1 == 1 { current + 1 } else { instructions[current + 1] as usize };
                let arg2: usize =
                    if mode2 == 1 { current + 2 } else { instructions[current + 2] as usize };

                next_instruction =
                    if instructions[arg1] != 0 {
                        instructions[arg2] as usize
                    } else {
                        next_instruction + 3
                    }
            },
            6 => {
                let arg1: usize =
                    if mode1 == 1 { current + 1 } else { instructions[current + 1] as usize };
                let arg2: usize =
                    if mode2 == 1 { current + 2 } else { instructions[current + 2] as usize };

                next_instruction =
                    if instructions[arg1] == 0 {
                        instructions[arg2] as usize
                    } else {
                        next_instruction + 3
                    }
            },
            7 => {
                next_instruction += 4;

                let arg1: usize =
                    if mode1 == 1 { current + 1 } else { instructions[current + 1] as usize };
                let arg2: usize =
                    if mode2 == 1 { current + 2 } else { instructions[current + 2] as usize };
                let res = instructions[current + 3] as usize;

                instructions[res] = (instructions[arg1] < instructions[arg2]) as i32
            },
            8 => {
                next_instruction += 4;

                let arg1: usize =
                    if mode1 == 1 { current + 1 } else { instructions[current + 1] as usize };
                let arg2: usize =
                    if mode2 == 1 { current + 2 } else { instructions[current + 2] as usize };
                let res = instructions[current + 3] as usize;

                instructions[res] = (instructions[arg1] == instructions[arg2]) as i32
            },
            99 => return,
            _ => (),
        };

        self.execute()
    }
}

fn parse_instructions(input: &String) -> Vec<i32> {
    input.trim()
        .split(",")
        .map(|x| x.parse::<i32>().unwrap())
        .collect()
}

fn main() {
    let input = std::fs::read_to_string("input").expect("Unable to read file");
    let instructions = parse_instructions(&input);

    let mut input1: Vec<i32> = vec![1];
    let mut output1: Vec<i32> = Vec::new();

    let amp = Amplifier::new(instructions.to_vec(), &input1, &output1);

    // let mut to_run1 = instructions.to_vec();
    // let mut input1: Vec<i32> = vec![1];
    // let mut output1: Vec<i32> = Vec::new();
    // execute(&mut to_run1, &mut input1, &mut output1, 0);
}
