fn incrementing_digits(guess: i32) -> bool {
    let mut num = guess;
    let mut curr = 10;

    while num != 0 {
        let next = num % 10;

        if next > curr {
            return false
        }

        curr = next;
        num = num / 10;
    }

    true
}

fn adjacent_digits(guess: i32) -> bool {
    let mut num = guess;
    let mut curr = 10;

    while num != 0 {
        let next = num % 10;

        if next == curr {
            return true
        }

        curr = next;
        num = num / 10;
    }

    false
}

fn digit_pair(guess: i32) -> bool {
    let mut num = guess;
    let mut curr = 10;
    let mut count = 1;

    while num != 0 {
        let next = num % 10;

        if next == curr {
            count += 1;
        } else {
            if count == 2 {
                return true
            }

            count = 1
        }

        curr = next;
        num = num / 10;
    }

    count == 2
}

fn main() {
    let min = 278384;
    let max = 824795;

    let part1 = |w: i32| incrementing_digits(w) && adjacent_digits(w);
    let part2 = |w: i32| incrementing_digits(w) && digit_pair(w);

    let answer1 = (min..max).filter(|&w| part1(w)).count();
    let answer2 = (min..max).filter(|&w| part2(w)).count();

    println!("{}", answer1);
    println!("{}", answer2);
}
