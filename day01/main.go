package main

import (
	"io/ioutil"
	"fmt"
	"strings"
	"strconv"
)

func parseInput(data string) []int {
	input := strings.Trim(data, "\t \n")
	lines := strings.Split(input, "\n")

	modules := []int{}

	for _, i := range lines {
		j, err := strconv.Atoi(i)
		if err != nil {
			panic(err)
		}

		modules = append(modules, j)
	}

	return modules
}

func fuel_needed(mass int) int {
	fuel := (mass / 3) - 2

	if fuel <= 0 {
		return 0
	}

	return fuel
}

func initial_fuel_needed(modules []int) int {
	total := 0

	for _, mass := range modules {
		total += fuel_needed(mass)
	}

	return total
}

func total_fuel_needed(modules []int) int {
	total := 0

	for _, mass := range modules {
		fuel := fuel_needed(mass)

		for {
			if fuel <= 0 {
				break
			}
			total += fuel

			fuel = fuel_needed(fuel)
		}
	}

	return total
}

func main() {
	data, _ := ioutil.ReadFile("input")
	modules := parseInput(string(data))

	initial_fuel := initial_fuel_needed(modules)
	total_fuel := total_fuel_needed(modules)

	fmt.Println(initial_fuel)
	fmt.Println(total_fuel)
}
