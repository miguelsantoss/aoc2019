package main

import (
	"io/ioutil"
	"fmt"
	"strings"
	"strconv"
)

type op func(int, int) int
type Pair struct {
	Noun int
	Verb int
}

func parseInput(data string) []int {
	input := strings.Trim(data, "\t \n")
	lines := strings.Split(input, ",")

	modules := make([]int, 0)

	for _, i := range lines {
		j, err := strconv.Atoi(i)
		if err != nil {
			panic(err)
		}

		modules = append(modules, j)
	}

	return modules
}

func execute(instructions []int, current int) {
	for {
		switch instructions[current] {
		case 1:
			handleOp(instructions, current, add)
		case 2:
			handleOp(instructions, current, mul)
		case 99:
			return
		}
		current += 4
	}
}

func handleOp(instructions []int, current int, fn op) {
	firstArg := instructions[current + 1]
	secondArg := instructions[current + 2]
	result := instructions[current + 3]

	instructions[result] = fn(instructions[firstArg], instructions[secondArg])
}

func add(a int, b int) int {
	return a + b
}

func mul(a int, b int) int {
	return a * b
}

func findPair(instructions []int, expected int) Pair {
	for i := 1; i <= 100; i++ {
		for j := 1; j <= 100; j++ {
			toRun := make([]int, len(instructions))
			copy(toRun, instructions)

			toRun[1] = i
			toRun[2] = j

			execute(toRun, 0)

			if toRun[0] == expected {
				return Pair{Noun: i, Verb: j}
			}
		}
	}

	return Pair{Noun: 0, Verb: 0}
}

func main() {
	data, _ := ioutil.ReadFile("input")
	instructions := parseInput(string(data))

	instructions[1] = 12
	instructions[2] = 2

	toRun := make([]int, len(instructions))
	copy(toRun, instructions)

	execute(toRun, 0)
	fmt.Println("first_run:", toRun[0])

	solutionPair := findPair(instructions, 19690720)
	answer := 100 * solutionPair.Noun + solutionPair.Verb
	fmt.Println("final:", answer)
}
