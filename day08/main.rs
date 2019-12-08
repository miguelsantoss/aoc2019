fn main() {
    let input = std::fs::read_to_string("input")
        .expect("Unable to read file");

    let size = 25*6;
    let chars: Vec<_> = input.trim().chars().collect();

    let min0 = chars.chunks(size)
        .min_by_key(|&w| w.iter().filter(|&s| *s == '0').count())
        .unwrap();

    let answer1 = min0.iter().filter(|&s| *s == '1').count()
        * min0.iter().filter(|&s| *s == '2').count();

    let decoded: Vec<_> = (0..size)
        .map(|x| chars.chunks(size).find(|&s| s[x] != '2').unwrap()[x])
        .map(|x| if x == '1' { "#" } else { " " })
        .collect();

    let answer2 = decoded.chunks(25)
        .map(|x| x.join(""))
        .collect::<Vec<String>>()
        .join("\n");

    println!("{}", answer1);
    println!("{}", answer2);
}
